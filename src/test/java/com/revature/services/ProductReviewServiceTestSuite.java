package com.revature.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.revature.models.Product;
import com.revature.models.ProductReview;
import com.revature.models.User;
import com.revature.repositories.ProductReviewRepo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductReviewServiceTestSuite {
    ProductReviewService sut;
    ProductReviewRepo mockProductReviewRepo;

    @BeforeEach
    void setUp() throws Exception {
        mockProductReviewRepo = mock(ProductReviewRepo.class);
        sut = new ProductReviewService(mockProductReviewRepo);
    }

    @Test
    void test_addProductReview_returnsProductReview_givenValidProductReview() {
        User user = new User();
        Product product = new Product();
        ProductReview newProductReview = new ProductReview(1, "valid", "valid", 1, user, product);
        when(mockProductReviewRepo.save(newProductReview)).thenReturn(newProductReview);

        ProductReview result = sut.addProductReview(newProductReview);

        Assertions.assertEquals(result, newProductReview);
        verify(mockProductReviewRepo, times(1)).save(newProductReview);
    }

    // @Test
    // void test_addProductReview_throwsException_givenUnauthorizedUser() {
    // ProductReview newProductReview = new ProductReview();
    // when(mockProductReviewRepo.save(newProductReview)).thenReturn(newProductReview);

    // ProductReview result = sut.addProductReview(newProductReview);

    // Assertions.assertEquals(result,newProductReview);
    // verify(mockProductReviewRepo, times(1)).save(newProductReview);
    // }

    @Test
    void test_addProductReview_throwsRuntimeException_givenNullProductReview() {
        ProductReview productReview = null;
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            sut.addProductReview(productReview);
        });
        Assertions.assertEquals("Invalid product review.", thrown.getMessage());
        verify(mockProductReviewRepo, times(0)).save(productReview);
    }

    @Test
    void test_addProductReview_throwsRuntimeException_givenInvalidProductReviewTitle() {
        User user = new User();
        Product product = new Product();
        ProductReview productReview = new ProductReview(1, null, "valid", 1, user, product);
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            sut.addProductReview(productReview);
        });
        Assertions.assertEquals("Invalid product review.", thrown.getMessage());
        verify(mockProductReviewRepo, times(0)).save(productReview);
    }

    @Test
    void test_addProductReview_throwsRuntimeException_givenInvalidProductReviewComment() {
        User user = new User();
        Product product = new Product();
        ProductReview productReview = new ProductReview(1, "valid", null, 1, user, product);
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            sut.addProductReview(productReview);
        });
        Assertions.assertEquals("Invalid product review.", thrown.getMessage());
        verify(mockProductReviewRepo, times(0)).save(productReview);
    }

    @Test
    void test_addProductReview_throwsRuntimeException_givenInvalidProductReviewRating() {
        User user = new User();
        Product product = new Product();
        ProductReview productReview = new ProductReview(1, "valid", "valid", 0, user, product);
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            sut.addProductReview(productReview);
        });
        Assertions.assertEquals("Invalid product review.", thrown.getMessage());
        verify(mockProductReviewRepo, times(0)).save(productReview);
    }

    @Test
    void test_findAllProductReviews_returnsProductReviews() {
        List<ProductReview> allProductReviews = new ArrayList<>();
        when(mockProductReviewRepo.findAll()).thenReturn(allProductReviews);

        List<ProductReview> result = sut.findAllProductReviews();

        Assertions.assertEquals(result, allProductReviews);
        verify(mockProductReviewRepo, times(1)).findAll();
    }

    // TODO findAll() test for no reviews returns null?

    @Test
    void test_findAllProductReviewsByUserId_returnsProductReviews_givenValidUserId() {
        User user = new User();
        List<ProductReview> allProductReviewsByUser = new ArrayList<>();
        // ProductReview productReview1 = new ProductReview()
        // ProductReview productReview2 = new ProductReview()
        when(mockProductReviewRepo.findAllByUser(user)).thenReturn(allProductReviewsByUser);

        List<ProductReview> result = sut.findAllProductReviewsByUser(user);

        Assertions.assertEquals(result, allProductReviewsByUser);
        verify(mockProductReviewRepo, times(1)).findAllByUser(user);
    }

    @Test
    void test_findAllProductReviewsByUserId_returnsNull_givenValidUserId_givenUserHasNoReviews() {
        User user = new User();

        when(mockProductReviewRepo.findAllByUser(user)).thenReturn(null);

        List<ProductReview> result = sut.findAllProductReviewsByUser(user);
        Assertions.assertNull(result);
        verify(mockProductReviewRepo, times(1)).findAllByUser(user);
    }

    // @Test
    // void
    // test_findAllProductReviewsByUserId_throwsRuntimeException_givenInvalidUserId()
    // {
    // int invalidUserId = 0;
    // RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, ()
    // -> {
    // sut.findAllProductReviewsByUserId(invalidUserId);
    // });
    // Assertions.assertEquals("message", thrown.getMessage());

    // verify(mockProductReviewRepo, times(0)).findAllByUserId(invalidUserId);
    // }

    @Test
    void test_findAllProductReviewsByProductId_returnsProductReviews_givenValidProductId() {
        Product product = new Product();
        List<ProductReview> allProductReviewsByProduct = new ArrayList<>();
        // ProductReview productReview1 = new ProductReview()
        // ProductReview productReview2 = new ProductReview()
        when(mockProductReviewRepo.findAllByProduct(product)).thenReturn(allProductReviewsByProduct);

        List<ProductReview> result = sut.findAllProductReviewsByProduct(product);

        Assertions.assertEquals(result, allProductReviewsByProduct);
        verify(mockProductReviewRepo, times(1)).findAllByProduct(product);
    }

    @Test
    void test_findAllProductReviewsByProductId_returnsNull_givenValidProductId_givenProductHasNoReviews() {
        Product product = new Product();
        when(mockProductReviewRepo.findAllByProduct(product)).thenReturn(null);

        List<ProductReview> result = sut.findAllProductReviewsByProduct(product);

        Assertions.assertNull(result);
        verify(mockProductReviewRepo, times(1)).findAllByProduct(product);
    }

    // @Test
    // void
    // test_findAllProductReviewsByProductId_throwsRuntimeException_givenInvalidProductId()
    // {
    // int invalidProductId = 0;
    // RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, ()
    // -> {
    // sut.findAllProductReviewsByProductId(invalidProductId);
    // });
    // Assertions.assertEquals("message", thrown.getMessage());

    // verify(mockProductReviewRepo, times(0)).findAllByProductId(invalidProductId);
    // }

    @Test
    void test_updateProductReview_returnsTrue_givenValidProductReview() {
        User user = new User();
        Product product = new Product();
        ProductReview productReview = new ProductReview(1, "valid", "valid", 1, user, product);
        when(mockProductReviewRepo.findById(productReview.getId())).thenReturn(Optional.of(productReview));
        when(mockProductReviewRepo.save(productReview)).thenReturn(productReview);

        boolean result = sut.updateProductReview(productReview);

        Assertions.assertTrue(result);
        verify(mockProductReviewRepo, times(1)).findById(productReview.getId());
        verify(mockProductReviewRepo, times(1)).save(productReview);
    }

    @Test
    void test_updateProductReview_throwsRuntimeException_givenInvalidProductReview() {
        User user = new User();
        Product product = new Product();
        ProductReview productReview = new ProductReview(1, "valid", "valid", 0, user, product);
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            sut.updateProductReview(productReview);
        });
        Assertions.assertEquals("Invalid product review.", thrown.getMessage());
        verify(mockProductReviewRepo, times(0)).findById(productReview.getId());
        verify(mockProductReviewRepo, times(0)).save(productReview);
    }

    @Test
    void test_updateProductReview_throwsRuntimeException_givenProductReviewDoesNotExist() {
        User user = new User();
        Product product = new Product();
        ProductReview productReview = new ProductReview(1, "valid", "valid", 1, user, product);
        when(mockProductReviewRepo.findById(productReview.getId())).thenReturn(null);
        // when(mockProductReviewRepo.save(productReview)).thenReturn(null);
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            sut.updateProductReview(productReview);
        });
        Assertions.assertEquals("Cannot update review--does not exist.", thrown.getMessage());
        verify(mockProductReviewRepo, times(1)).findById(productReview.getId());
        verify(mockProductReviewRepo, times(0)).save(productReview);
    }

    @Test
    void test_updateProductReview_returnsFalse_givenValidProductReview_givenUpdateNotPersisted() {
        User user = new User();
        Product product = new Product();
        ProductReview productReview1 = new ProductReview(1, "valid", "valid", 1, user, product);
        ProductReview productReview2 = new ProductReview(2, "also valid", "also valid", 1, user, product);
        int id = 1; // placeholder for getId implementation -> productReview.getId()
        when(mockProductReviewRepo.findById(id)).thenReturn(Optional.of(productReview2));
        when(mockProductReviewRepo.save(productReview1)).thenReturn(productReview2);

        boolean result = sut.updateProductReview(productReview1);

        Assertions.assertFalse(result);
        verify(mockProductReviewRepo, times(1)).findById(id);
        verify(mockProductReviewRepo, times(1)).save(productReview1);
    }

    @Test
    void test_deleteProductReview_returnsTrue_givenValidProductReview_givenProductReviewExists() {
        User user = new User();
        Product product = new Product();
        ProductReview productReview = new ProductReview(1, "valid", "valid", 1, user, product);
        int id = 1; // placeholder for getId implementation -> productReview.getId()
        when(mockProductReviewRepo.findById(id)).thenReturn(Optional.of(productReview), null);

        boolean result = sut.deleteProductReview(productReview);

        Assertions.assertTrue(result);
        verify(mockProductReviewRepo, times(2)).findById(id);
        verify(mockProductReviewRepo, times(1)).delete(productReview);
    }

    @Test
    void test_deleteProductReview_throwsRuntimeException_givenInvalidProductReview() {
        ProductReview productReview = null;
        int id = 1; // placeholder for getId implementation -> productReview.getId()
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            sut.deleteProductReview(productReview);
        });
        Assertions.assertEquals("Invalid product review.", thrown.getMessage());
        verify(mockProductReviewRepo, times(0)).findById(id);
        verify(mockProductReviewRepo, times(0)).delete(productReview);
    }

    @Test
    void test_deleteProductReview_throwsRuntimeException_givenValidProductReview_givenProductReviewDoesNotExist() {
        User user = new User();
        Product product = new Product();
        ProductReview productReview = new ProductReview(1, "valid", "valid", 1, user, product);
        when(mockProductReviewRepo.findById(productReview.getId())).thenReturn(null);

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            sut.deleteProductReview(productReview);
        });
        Assertions.assertEquals("Cannot delete review--does not exist.", thrown.getMessage());
        verify(mockProductReviewRepo, times(1)).findById(productReview.getId());
        verify(mockProductReviewRepo, times(0)).delete(productReview);
    }

    @Test
    void test_deleteProductReview_returnsFalse_givenValidProductReview_givenDeleteNotPersisted() {
        User user = new User();
        Product product = new Product();
        ProductReview productReview = new ProductReview(1, "valid", "valid", 1, user, product);
        when(mockProductReviewRepo.findById(productReview.getId())).thenReturn(Optional.of(productReview), Optional.of(productReview));

        boolean result = sut.deleteProductReview(productReview);

        Assertions.assertFalse(result);
        verify(mockProductReviewRepo, times(2)).findById(productReview.getId());
        verify(mockProductReviewRepo, times(1)).delete(productReview);
    }
}