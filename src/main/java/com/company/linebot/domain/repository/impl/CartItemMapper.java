package com.company.linebot.domain.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.company.linebot.domain.CartItem;
import com.company.linebot.service.ProductService;

public class CartItemMapper implements RowMapper<CartItem> {
    private ProductService productService;

    public CartItemMapper(ProductService productService) {
        this.productService = productService;
    }

    public CartItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        CartItem cartItem = new CartItem(rs.getString("ID"));
        cartItem.setQuantity(rs.getInt("QUANTITY"));
        cartItem.setProduct(productService.read(rs.getString("PRODUCT_ID")));

        return cartItem;
    }
}
