package com.company.linebot.controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import com.company.linebot.domain.Product;

@Controller
@RequestMapping("rest/product")
public class HelloController {

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public Product read(@PathVariable("productId") String productId) {
        Product iphone =  new Product("P1234", productId, new BigDecimal(500));
        return iphone;
    }
}
