package com.revature.services;

import com.revature.models.Seller;
import com.revature.models.Shop;
import com.revature.repositories.ShopRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl implements ShopService {
  @Autowired
  ShopRepo shopRepo;

  @Override
  public Shop addShop(Shop shop) {
    return shopRepo.save(shop);
  }

  @Override
  public Optional<Shop> getShopById(int id) {
    return shopRepo.findById(id);
  }

  @Override
  public List<Shop> getAllShops() {
    return (List<Shop>) shopRepo.findAll();
  }

  @Override
  public void updateShop(Shop change) {
    shopRepo.save(change);
  }

  @Override
  public boolean deleteShop(int id) {
    try {
      shopRepo.deleteById(id);
      return true;
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      return false;
    }
  }
  
  public List<Shop> getShopsByUserId(int id) {
	  return shopRepo.getByUserId(id);
  }
}
