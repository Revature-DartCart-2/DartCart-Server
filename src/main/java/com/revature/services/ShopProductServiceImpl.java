package com.revature.services;

import com.revature.models.*;
import com.revature.repositories.ProductRepo;
import com.revature.repositories.ShopProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShopProductServiceImpl implements ShopProductService {
    @Autowired
    ShopProductRepo shopProductRepository;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ProductRepo pr;

    @Override
    public List<ShopProduct> getAllShopProducts() {
        return (List<ShopProduct>) shopProductRepository.findAll();
    }

    @Override
    public Optional<ShopProduct> getShopProductById(int id) {

        return shopProductRepository.findById(id);
    @Override
    public List<ShopProduct> searchByProductName(String searchString) {
        List<ShopProduct> shopProductList = (List<ShopProduct>) shopProductRepo.findAll();
        return shopProductList.stream().filter(shopProduct ->
                shopProduct.getProduct().getName().toLowerCase().contains(searchString.toLowerCase())).collect(Collectors.toList());
    }

    @Override
    public List<Product> getByProductCategory(String name, String category) {
        List<Product> productList = (List<Product>) productRepo.findAll();
        if(name != null){
            productList = productList.stream().filter(product ->
                    product.getName().toLowerCase().contains(name.toLowerCase())
                            ||  product.getDescription().toLowerCase().contains(name.toLowerCase())
            ).collect(Collectors.toList());

        }
        if(category != null){
          productList = productList.stream().filter(product -> {
              List<Category> categories = product.getCategories();
              for (Category category1: categories){
                  if(category1.getName().equalsIgnoreCase(category))
                      return true;
              }
              return false;
          }).collect(Collectors.toList());
        }
        return productList;
    }

    @Override
    public List<ShopProductResponse> getSellersForProduct(int id) {
        List<ShopProduct> allListings =  shopProductRepo.findByProduct(shopProductRepo.findById(id).get().getProduct());

        ArrayList<ShopProductResponse> shopProducts = new ArrayList<>();

        for(ShopProduct s:allListings)
        {
            shopProducts.add(new ShopProductResponse(s.getId()
                    ,s.getShop().getId()
                    ,s.getProduct()
                    ,s.getPrice()
                    ,s.getShop().getLocation()
                    ,s.getDiscount()
                    ,s.getQuantity()
                    ,s.getShop().getSeller().getDescription()));
        }
        return shopProducts;
    }
}
