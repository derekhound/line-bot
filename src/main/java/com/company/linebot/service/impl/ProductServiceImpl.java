package com.company.linebot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.linebot.domain.Product;
import com.company.linebot.domain.repository.ProductRepository;
import com.company.linebot.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    public List<Product> list() {
        return productRepository.list();
    }

    public Product read(String id) {
        return productRepository.read(id);
    }
}
