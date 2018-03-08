package com.company.linebot.service;

import java.util.List;
import java.util.Map;

import com.company.linebot.domain.Product;

public interface ProductService {

    List<Product> list();

    Product read(String id);
}