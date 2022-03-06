package com.revature.services;

import com.revature.models.Product;
import com.revature.repositories.ProductRepo;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepo productRepo;

    @Override
    public Optional<Product> getProductById(int productId) {
        return productRepo.findById(productId);
    }

    @Override
    public List<Product> getAllProducts() { return (List<Product>) productRepo.findAll();}
}
