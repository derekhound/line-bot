package com.company.linebot.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.company.linebot.domain.Product;
import com.company.linebot.service.ProductService;

@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Product> list() {
        return productService.list();
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Product read(@PathVariable("productId") String productId) {
        Product iphone =  new Product("P1234", productId, new BigDecimal(500));
        return iphone;
    }
}
