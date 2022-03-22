package com.revature.controllers;

import com.revature.models.UserLogin;
import com.revature.services.AuthService;
import com.revature.services.UserService;
import com.revature.utilities.JwtTokenUtil;
import com.revature.utilities.EmailServiceImpl;

import java.util.Locale;

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
	private final EmailServiceImpl emailServiceImp;
	private final BCryptPasswordEncoder bCryptEncoder;

	
	@Autowired
	public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil,
			UserService userService, AuthService authService, EmailServiceImpl emailServiceImp, BCryptPasswordEncoder bCryptEncoder) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.userService = userService;
		this.authService = authService;
		this.emailServiceImp = emailServiceImp;
		this.bCryptEncoder = bCryptEncoder;
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
		
		emailServiceImp.sendSimpleMessage(user.getEmail(), "password reset email test", "http://localhost:3000/resetpassword?data=" + cipheredUsername.toString());
		return ResponseEntity.ok("Reset Email Sent");
	}
		
	@PatchMapping("/resetpassword")
	public ResponseEntity<?> updatePassword(@RequestBody String request) {
		String[] userInfo = request.split(",");
		String[] usernameArr = userInfo[0].split(":");
		String[] passwordArr = userInfo[1].split(":");
				
		int firstIndex = usernameArr[1].indexOf("\"");
		int secondIndex = usernameArr[1].lastIndexOf("\""); 
		String username = usernameArr[1].substring(firstIndex+1, secondIndex);

		firstIndex = passwordArr[1].indexOf("\"");
		secondIndex = passwordArr[1].lastIndexOf("\""); 
		String password = passwordArr[1].substring(firstIndex+1, secondIndex);
		
		com.revature.models.User existingUser = userService.getUserByUsername(username);
		
		if(existingUser == null) {
			return ResponseEntity.notFound().build();
		}
		
		existingUser.setPassword(bCryptEncoder.encode(password));
		userService.updateUser(existingUser); 	
		
		return ResponseEntity.ok("Password updated");
	}
	

}
