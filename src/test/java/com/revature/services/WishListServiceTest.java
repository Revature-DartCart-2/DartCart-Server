package com.revature.services;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.revature.driver.DartCartApplication;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.models.WishList;
import com.revature.repositories.WishListDAO;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = DartCartApplication.class)

public class WishListServiceTest {
	
	private final WishList mockWishList = new WishList(1L, new User(), new Product());
	//injecting dependency
	@MockBean
	private WishListDAO wishListDao;
	
	//getting wish list services
	@Autowired
	private WishListService wishListService;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@BeforeEach
	void setUp() {
		MockMvc mvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.build();	
		
	}
	
	@Test
	void addToWishList_valid() {
		WishList addList = mockWishList;
		addList.setWishListId(mockWishList.getWishListId());
		Mockito
			.when(wishListDao.save(mockWishList))
			.thenReturn(addList);
		
		//Assertions.assertEquals(addList, wishListService.addToWishList(mockWishList));
	}

}
