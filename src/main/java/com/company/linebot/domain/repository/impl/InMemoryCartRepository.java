package com.company.linebot.domain.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.company.linebot.domain.Cart;
import com.company.linebot.domain.Product;
import com.company.linebot.domain.repository.CartRepository;
import com.company.linebot.dto.CartDto;
import com.company.linebot.dto.CartItemDto;
import com.company.linebot.service.ProductService;

@Repository
public class InMemoryCartRepository implements CartRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private ProductService productService;

    public void create(CartDto cartDto) {
        String INSERT_CART_SQL = "INSERT INTO CART(ID) VALUES (:id)";

        Map<String, Object> cartParams = new HashMap<String, Object>();
        cartParams.put("id", cartDto.getId());

        jdbcTemplate.update(INSERT_CART_SQL, cartParams);

        for (CartItemDto cartItemDto : cartDto.getCartItems()) {
            Product product = productService.read(cartItemDto.getProductId());

            String INSERT_CART_ITEM_SQL = "INSERT INTO CART_ITEM(ID,PRODUCT_ID,CART_ID,QUANTITY) " +
                                          "VALUES (:id, :product_id, :cart_id, :quantity)";

            Map<String, Object> cartItemsParams = new HashMap<String, Object>();
            cartItemsParams.put("id", cartItemDto.getId());
            cartItemsParams.put("product_id", product.getProductId());
            cartItemsParams.put("cart_id", cartDto.getId());
            cartItemsParams.put("quantity", cartItemDto.getQuantity());

            jdbcTemplate.update(INSERT_CART_ITEM_SQL, cartItemsParams);
        }
    }

    public Cart read(String id) {
        String SQL = "SELECT * FROM CART WHERE ID = :id";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);

        return jdbcTemplate.queryForObject(SQL, params, new CartMapper(jdbcTemplate, productService));
    }

    public void update(String id, CartDto cartDto) {
        List<CartItemDto> cartItems = cartDto.getCartItems();
        for (CartItemDto cartItem: cartItems) {

            String SQL = "UPDATE CART_ITEM SET QUANTITY = :quantity,  PRODUCT_ID = :productId WHERE ID = :id AND CART_ID = :cartId";

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", cartItem.getId());
            params.put("quantity", cartItem.getQuantity());
            params.put("productId", cartItem.getProductId());
            params.put("cartId", id);

            jdbcTemplate.update(SQL, params);
        }
    }

    public void delete(String id) {
        String SQL_DELETE_CART_ITEM = "DELETE FROM CART_ITEM WHERE CART_ID = :id";
        String SQL_DELETE_CART = "DELETE FROM CART WHERE ID = :id";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);

        jdbcTemplate.update(SQL_DELETE_CART_ITEM, params);
        jdbcTemplate.update(SQL_DELETE_CART, params);
    }

    public void addItem(String id, String productId) {

    }

    public void removeItem(String id, String productId) {

    }

}
