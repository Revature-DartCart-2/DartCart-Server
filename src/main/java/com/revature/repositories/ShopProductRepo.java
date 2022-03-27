package com.revature.repositories;

import com.revature.models.Product;
import com.revature.models.ShopProduct;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopProductRepo extends CrudRepository<ShopProduct, Integer> {
  List<ShopProduct> findByProduct(Product product);

  @Query(
          value = "select *, cast(discount as float) / cast(price as float) * 100 as percentage from shop_products where discount > 0 order by percentage desc",
          nativeQuery = true
  )
  List<ShopProduct> orderProductByPercentage();
}
