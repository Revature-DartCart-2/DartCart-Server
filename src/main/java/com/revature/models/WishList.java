package com.revature.models;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="wish_lists")
public class WishList {
	
	
	//wishlist id
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wish_list_id", nullable = false)
	private Long wishListId;
	
	//user
	 @ManyToOne
	  @JoinColumn(name = "customer_id")
	  private User customer;
	 //shop product
	  @OneToOne
	  @JoinColumn(name = "product_id")
	  private Product product;
	
	public WishList(User customer, Product product) {
		this.customer = customer;
		this.product = product;
	}

}
