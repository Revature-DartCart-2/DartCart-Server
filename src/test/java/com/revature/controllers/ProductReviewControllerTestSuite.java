package com.revature.controllers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

import com.revature.driver.DartCartApplication;
import com.revature.models.Product;
import com.revature.models.ProductReview;
import com.revature.models.User;
import com.revature.services.ProductReviewService;
import com.revature.services.ProductServiceImpl;
import com.revature.services.UserServiceImpl;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = DartCartApplication.class)
public class ProductReviewControllerTestSuite {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    ProductReviewController sut;

    @MockBean
    ProductReviewService mockProductReviewService;
    @MockBean
    UserServiceImpl mockUserService;
    @MockBean
    ProductServiceImpl mockProductService;

    // @BeforeEach
    // void setup() {
    //     mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    // }

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        mockUserService = mock(UserServiceImpl.class);
        // mockProductReviewService = new
        // ProductReviewService(mockProductReviewRepo);
        mockProductReviewService = mock(ProductReviewService.class);
        mockProductService = mock(ProductServiceImpl.class);
        sut = new ProductReviewController(mockProductReviewService, mockUserService,
                mockProductService);
    }

    @Test
    public void test_newProductReview_returns200_givenValidRequest() throws Exception {
        User user = new User();
        Product product = new Product();
        user.setId(1);
        product.setId(1);
        ProductReview pr = new ProductReview(1, "valid", "valid", 3, user, product);
        // // MockHttpServletRequest request = new MockHttpServletRequest();
        // // RequestContextHolder.setRequestAttributes(new
        // // ServletRequestAttributes(request));

        // when(mockProductReviewService.addProductReview(pr)).thenReturn(pr);

        // ResponseEntity<ProductReview> responseEntity = sut.newProductReview(pr);

        // Assertions.assertEquals(200, responseEntity.getStatusCode());

        when(mockProductReviewService.addProductReview(pr))
                .thenReturn(pr);
        mockMvc
                .perform(MockMvcRequestBuilders.post("/invoices/customer/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void test_newProductReview_returns400_givenInvalidRequest() {

    }

    @Test
    public void test_newProductReview_returns500_onServerError() {

    }

}
