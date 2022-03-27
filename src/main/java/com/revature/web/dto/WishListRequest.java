package com.revature.web.dto;

import com.revature.models.ShopProduct;
import com.revature.models.User;
import com.revature.models.WishList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishListRequest {
	
	private int productId;
}
