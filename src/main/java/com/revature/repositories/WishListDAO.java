package com.revature.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.revature.models.Product;
import com.revature.models.ShopProduct;
import com.revature.models.User;
import com.revature.models.WishList;

@Repository
public interface WishListDAO extends CrudRepository<WishList, Integer> {

	List<WishList> findByCustomer(User user);
	
	Optional<WishList> findByCustomerAndProduct(User user, Product product);

}
