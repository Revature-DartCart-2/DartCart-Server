package com.revature.controllers;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.Product;
import com.revature.models.ShopProduct;
import com.revature.models.User;
import com.revature.models.WishList;
import com.revature.services.ProductServiceImpl;
import com.revature.services.ShopProductServiceImpl;
import com.revature.services.UserServiceImpl;
import com.revature.services.WishListService;
import com.revature.web.dto.WishListRequest;

@CrossOrigin
@RestController
public class WishListServlet {
	
	
	WishListService wishListService;
	UserServiceImpl userService;
	ProductServiceImpl productService;
	
	@Autowired
	public WishListServlet(WishListService wishListService, UserServiceImpl userService, ProductServiceImpl productService) {
		this.wishListService = wishListService;
		this.userService = userService;
		this.productService = productService;
	}

	@GetMapping("/myWishList")
	public List<WishList> returnMyWishList(Authentication auth) {
		int id = userService.getUserByUsername(auth.getName()).getId();
	    User user = userService.getUserById(id).get();
	    return wishListService.getMyWishList(user);
	}
	
	@PostMapping("/addToWishList")
	public ResponseEntity<Void> addToWishList(Authentication auth, @RequestBody WishListRequest wishListRequest) {
		try{
			int id = userService.getUserByUsername(auth.getName()).getId();
			User user = userService.getUserById(id).get();
			Product product = productService.getProductById(wishListRequest.getProductId()).get();
			WishList addToWishList = new WishList(user, product);
			WishList checkWishList = wishListService.findByCustomerAndProduct(addToWishList);
			if(checkWishList != null) {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			}
			wishListService.addToWishList(addToWishList);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch(NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/removeFromWishList")
	public ResponseEntity<Void> removeFromWishList(Authentication auth, @RequestBody WishListRequest wishListRequest) {
		try{
			int id = userService.getUserByUsername(auth.getName()).getId();
			User user = userService.getUserById(id).get();
			Product product = productService.getProductById(wishListRequest.getProductId()).get();
			WishList removeFromWishList = new WishList(user, product);
			wishListService.removeFromWishList(removeFromWishList);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch(NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
