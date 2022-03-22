package com.revature.controllers;

import com.revature.models.Product;
import com.revature.models.Seller;
import com.revature.models.Shop;
import com.revature.models.ShopProduct;
import com.revature.models.ShopProductResponse;
import com.revature.services.ProductServiceImpl;
import com.revature.services.ShopProductServiceImpl;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class ShopProductController {
	@Autowired
	private ShopProductServiceImpl sps;
	
	@Autowired
	private ProductServiceImpl ps;

	@GetMapping("/shop_products/{id}")
	public ResponseEntity<ShopProduct> getShopProductByShopId(@PathVariable("id") String id) {
		Optional<ShopProduct> sp = sps.getShopProductById(Integer.parseInt(id));
		return ResponseEntity.of(sp);
	}

	@GetMapping("/shop_products")
	public List<ShopProduct> getAllShopProducts() {
		return sps.getAllShopProducts();
	}

  @GetMapping("/featured_products")
  public List<ShopProduct> getOrderedProductsByPercentage() { return sps.getOrderedProductsByPercentage(); }

  @GetMapping("/shop_products/search")
  public List<Product> searchShopProducts(
    @RequestParam(name = "name", required = false) String name,
    @RequestParam(name = "category", required = false) String category
  ) {
    return sps.getByProductCategory(name, category);
  }

	@GetMapping("/sellers/{ProductId}")
	public ResponseEntity<List<ShopProductResponse>> getAllSellersForProduct(@PathVariable("ProductId") int id) {
		try {
			Product p = ps.getProductById(id).get();
			int spid = sps.getShopProductsByProduct(p).get(0).getId();
			List<ShopProductResponse> shops = sps.getSellersForProduct(spid);
			return new ResponseEntity<>(shops, HttpStatus.OK);
		}
		catch (Exception e) {
			return null;
		}
	}
}
