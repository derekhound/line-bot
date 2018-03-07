package com.company.linebot.service;

import java.util.List;
import java.util.Map;

import com.company.linebot.domain.Product;

public interface ProductService {

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    /*
    List<Product> getProductsByFilter(Map<String, List<String>> filterParams);

    Product getProductById(String productID);

    void addProduct(Product product);
    */
}