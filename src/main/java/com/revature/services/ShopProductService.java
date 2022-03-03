package com.revature.services;

import com.revature.models.ShopProduct;

import java.util.List;
import java.util.Optional;

public interface ShopProductService {
    /**
     * This method gets all Shop Products from the database
     * @return  returns a List of all Shop Products
     */
    List<ShopProduct> getAllShopProducts();


    /**
     * Retrieves a specific ShopProduct by parsed ID
     * @param id    ID of Shop Product to be retrieved from the database
     * @return      Returns the retrieved Shop Product by the ID
     */
    Optional<ShopProduct> getShopProductById(int id);

    /**
     * This function retrieves all Shop Products with the search string in the product
     * @param searchString The string to be searched
     * @return A list of all Sop Products that contain the search string in their name
     */
    List<ShopProduct> searchByProductName(String searchString);
}
