package com.revature.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents an individual item in the cart or saved by a Customer.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Cart_Items")
public class CartItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cart_item_id")
  private int id;

  private int quantity;

  // if saved, set to wishlist, if not saved is in cart
  private boolean saved;

  @ManyToOne
//  @JoinColumn(name = "customer_id")
  private User customer;

  @OneToOne
  @JoinColumn(name = "shop_product_id")
  private ShopProduct shopProduct;

  public CartItem(
    int quantity,
    boolean saved,
    User customer,
    ShopProduct shopProduct
  ) {
    this.quantity = quantity;
    this.saved = saved;
    this.customer = customer;
    this.shopProduct = shopProduct;
  }
}
