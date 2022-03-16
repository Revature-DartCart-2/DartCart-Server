package com.revature.models;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.sun.istack.NotNull;

public class ProductReview {
	@Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  @Column(name = "product_review_id")
	  private int id;

	  @NotNull
	  @Column(unique = true)
	  private String title;

	  @NotNull
	  private String comment;

	  @NotNull
	  private Integer rating;

}
