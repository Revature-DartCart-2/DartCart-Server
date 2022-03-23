package com.revature.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.models.ShopProduct;
import com.revature.models.User;
import com.revature.models.WishList;
import com.revature.repositories.WishListDAO;
import com.revature.web.dto.WishListRequest;

@Service
public class WishListService {
	
	
	WishListDAO wishListDAO;
	
	@Autowired
	public WishListService(WishListDAO wishListDAO) {
		this.wishListDAO = wishListDAO;
	}
	
	
	public void addToWishList(WishList wishListItem) {
		wishListDAO.save(wishListItem);
	}
	
	public void removeFromWishList(WishList wishListItem) {
		WishList deleteThis = findByCustomerAndProduct(wishListItem);
		if(deleteThis != null) {
			wishListDAO.delete(deleteThis);
		}
	}
	
	public List<WishList> getMyWishList(User user) {
		return wishListDAO.findByCustomer(user);
	}
	
	public WishList findByCustomerAndProduct(WishList wishListItem) {
		return wishListDAO.findByCustomerAndProduct(wishListItem.getCustomer(), wishListItem.getProduct()).orElse(null);
	}
}
