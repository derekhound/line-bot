package com.company.linebot.domain.repository;

import java.util.List;
import com.company.linebot.domain.Product;

public interface ProductRepository {

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    //void updateStock(String productId, long noOfUnit);
}
