package com.revature.repositories;

import com.revature.models.Shop;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepo extends CrudRepository<Shop, Integer> {
	@Query(value = "FROM Shop WHERE seller.user.id=:id")
	  List<Shop> getByUserId(int id);
}
