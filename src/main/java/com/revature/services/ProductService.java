package com.revature.services;

import com.revature.models.Product;
import com.revature.models.ShopProduct;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    public Optional<Product> getProductById(int productId);

    List<Product> getAllProducts();
}
