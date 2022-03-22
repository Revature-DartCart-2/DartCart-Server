package com.revature.repositories;

import com.revature.models.CartItem;
import com.revature.models.ShopProduct;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepo extends CrudRepository<CartItem, Integer> {
  @Query(
    value = "SELECT * FROM Cart_Items WHERE customer_id=?",
    nativeQuery = true
  )
  List<CartItem> getAllCartItem(int id);

  @Query(
    value = "SELECT * FROM Cart_Items WHERE customer_id=?",
    nativeQuery = true
  )
  CartItem getCurrentCart(int userId);

  @Query(value = "FROM CartItem WHERE shopProduct.id=:ShopProductId and customer.id=:userId")
  CartItem getByShopProductId(int ShopProductId, int userId);
}
