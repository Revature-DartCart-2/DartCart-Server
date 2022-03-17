package com.revature.repositories;

import com.revature.models.ProductReview;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReviewRepo extends CrudRepository<ProductReview, Integer> {
    //using syntax from https://www.baeldung.com/spring-data-jpa-query 5.1 JPQL
    @Query("SELECT p FROM ProductReview p WHERE p.user_id = ?1")
    Iterable<ProductReview> findAllByUserId(int userId);
    
    @Query("SELECT p FROM ProductReview p WHERE p.product_id = ?1")
    Iterable<ProductReview> findAllByProductId(int productId);

}
