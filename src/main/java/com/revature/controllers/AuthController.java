package com.revature.controllers;

import com.revature.models.ResetPassword;
import com.revature.models.UserLogin;
import com.revature.services.AuthService;
import com.revature.services.ResetPasswordService;
import com.revature.services.UserService;
import com.revature.utilities.JwtTokenUtil;
import com.revature.utilities.EmailServiceImpl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@CrossOrigin
public class AuthController {
	private final AuthenticationManager authenticationManager;
	private final JwtTokenUtil jwtTokenUtil;
	private final UserService userService;
	private final AuthService authService;
	private final ResetPasswordService resetPasswordService;
	private final EmailServiceImpl emailServiceImp;
	private final BCryptPasswordEncoder bCryptEncoder;

	private static final int minutesUntilResetPasswordEmailExpires = 15;
	
	@Autowired
	public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
						  UserService userService, AuthService authService, EmailServiceImpl emailServiceImp,
						  BCryptPasswordEncoder bCryptEncoder, ResetPasswordService resetPasswordService) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.userService = userService;
		this.authService = authService;
		this.emailServiceImp = emailServiceImp;
		this.bCryptEncoder = bCryptEncoder;
		this.resetPasswordService = resetPasswordService;
	}

	@PostMapping("/login")
	public ResponseEntity<com.revature.models.User> login(@RequestBody UserLogin request) {
		try {
			Authentication authenticate = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

			User user = (User) authenticate.getPrincipal();
			com.revature.models.User retUser = userService
					.getUserByUsername(user.getUsername().toLowerCase(Locale.ROOT));
			retUser.setPassword(null);

			return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwtTokenUtil.generateAccessToken(user))
					.body(retUser);
		} catch (BadCredentialsException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@GetMapping("/resetpass/{username}")
	public ResponseEntity<?> resetPassword(@PathVariable("username") String username) {
		com.revature.models.User user = userService.getUserByUsername(username);
		if(user == null) {
			return ResponseEntity.notFound().build();
		}
		
		Iterable<ResetPassword> userResetPassword = resetPasswordService.findAllByUserId(user.getId());
		
		if(userResetPassword != null) { // reset email(s) already in database, delete all found for user
			Iterator<ResetPassword> userRpIterator = userResetPassword.iterator();
			while( userRpIterator.hasNext() ) {
				resetPasswordService.deleteByID( userRpIterator.next().getEmailId() );
			}
		}
		
		// Caesar Cipher to obscure username in link
        StringBuilder cipheredUsername = new StringBuilder();
        int base = 96;
        int shift = 1;
        for(int i=0; i< username.length(); i++) {
            int charAsInt = (int) username.charAt(i);

            int shiftedLetter = charAsInt+shift;
            if( shiftedLetter > 122) {
            	shiftedLetter = (base + shiftedLetter % 122);
            }
            else if( shiftedLetter < 97) {
            	shiftedLetter = 123 - (97-shiftedLetter);
            }
            cipheredUsername.append( (char) shiftedLetter );            
        }
		
		String emailId = (UUID.randomUUID()).toString();
		
		String timestamp = Long.toString(System.currentTimeMillis());
		String minuteUntilEmailExpires = String.valueOf(minutesUntilResetPasswordEmailExpires);
		
		ResetPassword resetPassword = new ResetPassword(emailId, user, timestamp);
		
		resetPasswordService.createNewResetPassword(resetPassword);
		
		// email string is: username, email id, timestamp for email, minutes until email expires
		emailServiceImp.sendSimpleMessage(
				user.getEmail(), 
				"Password Reset Dart Cart", 
				"Hello "+user.getFirstName()+",\n"+ "Use the following link to reset your password:\n"
				+ "http://localhost:3000/resetpassword?data="
						+ cipheredUsername.toString()
						+ "&data2="+emailId+ "&data3="
						+String.valueOf(timestamp) + "&data4="
						+minuteUntilEmailExpires
				+"\nThe link will expire in 15 minutes.\nIf you did not request a passwrod reset, ignore this email.");

		return ResponseEntity.ok("Reset Email Sent");
	}
		
	@PatchMapping("/resetpassword")
	public ResponseEntity<?> updatePassword(@RequestBody String request) {
		
		//gets emailid and password from request body
		String[] userInfo = request.split(",");
		String[] emailIDArr = userInfo[0].split(":");
		String[] passwordArr = userInfo[1].split(":");
				
		int firstIndex = emailIDArr[1].indexOf("\"");
		int secondIndex = emailIDArr[1].lastIndexOf("\""); 
		String emailID = emailIDArr[1].substring(firstIndex+1, secondIndex);

		firstIndex = passwordArr[1].indexOf("\"");
		secondIndex = passwordArr[1].lastIndexOf("\""); 
		String password = passwordArr[1].substring(firstIndex+1, secondIndex);
		
		// get reset password object
		Optional<ResetPassword> resetPasswordOpt = resetPasswordService.findResetPasswordById(emailID);
		ResetPassword resetPassword = new ResetPassword();		
		// check if reset password data was returned from database
		if( resetPasswordOpt.isPresent() ) {
			resetPassword = resetPasswordOpt.get();
		}
		else {
			return ResponseEntity.notFound().build();
		}		
			
		// get user object
		com.revature.models.User existingUser = resetPassword.getUser();
		
		// if user does not exist
		if(existingUser == null) {
			return ResponseEntity.notFound().build();
		}
		
		// if email is expired
		long expireTime = Long.parseLong(resetPassword.getTimestamp()) 
				          + minutesUntilResetPasswordEmailExpires*60000;
		if(expireTime < System.currentTimeMillis()) {
			return ResponseEntity.notFound().build();
		}
		
		existingUser.setPassword(bCryptEncoder.encode(password));
		resetPasswordService.deleteByID(emailID);
		userService.updateUser(existingUser);
		
		return ResponseEntity.ok("Password updated");
	}
	

}
