package com.company.linebot.domain.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.company.linebot.domain.Cart;
import com.company.linebot.domain.CartItem;
import com.company.linebot.service.ProductService;

public class CartMapper implements RowMapper<Cart> {

    private NamedParameterJdbcTemplate jdbcTemplate;
    private CartItemMapper cartItemMapper;

    public CartMapper(NamedParameterJdbcTemplate jdbcTemplate, ProductService productService) {
        this.jdbcTemplate = jdbcTemplate;
        cartItemMapper = new CartItemMapper(productService);
    }

    public Cart mapRow(ResultSet rs, int rowNum) throws SQLException {
        String id = rs.getString("ID");
        Cart cart = new Cart(id);

        String SQL = String.format("SELECT * FROM CART_ITEM WHERE CART_ID = '%s'", id);
        List<CartItem> carItems = jdbcTemplate.query(SQL, cartItemMapper);
        cart.setCartItems(carItems);

        return cart;
    }
}
