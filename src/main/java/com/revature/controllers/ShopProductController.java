package com.revature.controllers;

import com.revature.models.Product;
import com.revature.models.ShopProduct;
import com.revature.services.ShopProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class ShopProductController {
    @Autowired
    private ShopProductServiceImpl sps;

    @GetMapping("/shop_products/{id}")
    public ResponseEntity<ShopProduct> getShopProductByShopId(@PathVariable ("id") String id) {
        Optional<ShopProduct> sp = sps.getShopProductById(Integer.parseInt(id));
        return ResponseEntity.of(sp);

    }

    @GetMapping("/shop_products")
    public List<ShopProduct> getAllShopProducts() {

        return sps.getAllShopProducts();
    }

    @GetMapping("/shop_products/search")
    public List<Product> searchShopProducts(@RequestParam(name = "name", required = false) String name,
                                            @RequestParam(name = "category", required = false) String category){
        return sps.getByProductCategory(name, category);
    }
}
