package com.revature.controllers;

import com.revature.models.Invoice;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.services.UserServiceImpl;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class UserProfileController {

  private UserServiceImpl userService;
  
  @Autowired
  public UserProfileController(UserServiceImpl userService) {
	  this.userService = userService;
  }
  
  
  @GetMapping("/getProfile")
  public ResponseEntity<User> getUserByToken(Authentication auth) {
	  int id = userService.getUserByUsername(auth.getName()).getId();
    Optional<User> user = userService.getUserById(id);
    return user.isPresent()
      ? new ResponseEntity<User>(user.get(), HttpStatus.OK)
      : new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }
  
  
  
  
  // this is to make partial changes to the db for the user
  @PatchMapping("/updateProfile")
  public ResponseEntity<User> updateProfile(@RequestBody User user, Authentication auth) {
	  int id = userService.getUserByUsername(auth.getName()).getId();
	  Optional<User> oldUser = userService.getUserById(id);
	  if(user.getFirstName()!= null) {
		  oldUser.get().setFirstName(user.getFirstName());
	  }
	  if(user.getLastName()!= null) {
		  oldUser.get().setLastName(user.getLastName());
	  }
	  if(user.getEmail()!= null) {
		  oldUser.get().setEmail(user.getEmail());
	  }
	  if(user.getPhone()!= null) {
		  oldUser.get().setPhone(user.getPhone());
	  }
	  if(user.getLocation()!= null) {
		  oldUser.get().setLocation(user.getLocation());
	  }
	  userService.updateUser(oldUser.get());
	  return new ResponseEntity<User>(oldUser.get(), HttpStatus.OK);
	  

}
}
