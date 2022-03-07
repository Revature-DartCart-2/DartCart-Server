package com.revature.controllers;

import com.revature.models.Seller;
import com.revature.models.Shop;
import com.revature.services.ShopService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class ShopController {
    @Autowired
    ShopService shopService;

    @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Shop> newShop(@RequestBody Shop s) {
        try {
            Shop created = shopService.addShop(s);

            if (created.getId() != 0) {
                return new ResponseEntity<>(created, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/shops/{seller_id}")
    public ResponseEntity<Shop> getShopBySellerId(@PathVariable("seller_id") String id) {
        Optional<Shop> shop = shopService.getShopBySellerId(Integer.parseInt(id));
        return shop
            .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
