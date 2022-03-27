package com.revature.services;

import java.util.ArrayList;
import java.util.List;

import com.revature.models.Product;
import com.revature.models.ProductReview;
import com.revature.models.User;
import com.revature.repositories.ProductReviewRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductReviewService {
    private final ProductReviewRepo productReviewRepo;

    /**
     * Constructor
     * 
     * @param productReviewRepo
     */
    @Autowired
    public ProductReviewService(ProductReviewRepo productReviewRepo) {
        this.productReviewRepo = productReviewRepo;
    }

    /**
     * @param
     * @return Returns a ProductReview object after it has been added to the
     *         database
     * @throws RuntimeException when provided with invalid data
     */
    public ProductReview addProductReview(ProductReview newProductReview) {
        if (!isValidProductReview(newProductReview)) {
            throw new RuntimeException("Invalid product review.");
        }
        ProductReview savedProductReview = productReviewRepo.save(newProductReview);

        return savedProductReview;
    }

    /**
     * @return Returns a List<ProductReview> of all existing Product Reviews
     */
    public List<ProductReview> findAllProductReviews() {
        List<ProductReview> allProductReviews = new ArrayList<>();
        Iterable<ProductReview> prIterable = productReviewRepo.findAll();
        for (ProductReview pr : prIterable) {
            allProductReviews.add(pr);
        }
        return allProductReviews;
    }

    /**
     * @param
     * @return Returns a List of ProductReview objects by User Id, null if no
     *         results
     */
    public List<ProductReview> findAllProductReviewsByUser(User user) {
        List<ProductReview> reviewsByUser = new ArrayList<>();
        Iterable<ProductReview> results = productReviewRepo.findAllByUser(user);
        if (results != null) {
            for (ProductReview pr : results) {
                reviewsByUser.add(pr);
            }
            return reviewsByUser;
        }
        return null;
    }

    /**
     * @param
     * @return Returns a List of ProductReview objects by Product Id, null if no
     *         results
     */
    public List<ProductReview> findAllProductReviewsByProduct(Product product) {
        List<ProductReview> reviewsByProduct = new ArrayList<>();
        Iterable<ProductReview> results = productReviewRepo.findAllByProduct(product);
        if (results != null) {
            for (ProductReview pr : results) {
                reviewsByProduct.add(pr);
            }
            return reviewsByProduct;
        }
        return null;
    }

    /**
     * @param productReviewId
     * @return Returns a ProductReview object by its Id
     */
    public ProductReview findProductReviewById(int productReviewId) {

        ProductReview result = productReviewRepo.findById(productReviewId).get();
        return result;
    }

    /**
     * @param
     * @return Returns true if ProductReview updated, false if update unsuccesful
     * @throws RuntimeException when provided with invalid data or if product review
     *                          does not exist
     */
    public boolean updateProductReview(ProductReview productReview) {
        if (!isValidProductReview(productReview)) {
            throw new RuntimeException("Invalid product review.");
        }
        if (productReviewRepo.findById(productReview.getId()) == null) {
            throw new RuntimeException("Cannot update review--does not exist.");
        }
        if (productReviewRepo.save(productReview) == productReview) {
            return true;
        }
        return false;
    }

    /**
     * @param
     * @return Returns true if ProductReview deleted, false if delete unsuccesful
     * @throws RuntimeException when provided with invalid data or if product review
     *                          does not exist
     */
    public boolean deleteProductReview(ProductReview productReview) {
        if (!isValidProductReview(productReview)) {
            throw new RuntimeException("Invalid product review.");
        }
        if (productReviewRepo.findById(productReview.getId()) == null) {
            throw new RuntimeException("Cannot delete review--does not exist.");
        }
        productReviewRepo.delete(productReview);
        if (productReviewRepo.findById(productReview.getId()) == null) {
            return true;
        }
        return false;
    }

    /**
     * @param
     * @return Returns true if ProductReview contains all required fields and the
     *         fields are within a valid range of values
     */
    public boolean isValidProductReview(ProductReview productReview) {
        // TODO add more validation cases
        if (productReview == null) {
            return false;
        } else if (productReview.getTitle() == null ||
                productReview.getTitle().equals("")) {
            return false;
        } else if (productReview.getComment() == null ||
                productReview.getComment().equals("")) {
            return false;
        } else if (productReview.getRating() < 1 ||
                productReview.getRating() > 5) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param
     * @return Returns ProductReview matching user and product, else null
     */
    public ProductReview findProductReviewByUserAndProduct(User user, Product product) {
        ProductReview productReview = productReviewRepo.findByUserAndProduct(user, product);
        return productReview;
    }
}
