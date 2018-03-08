package com.company.linebot.domain.repository;

import com.company.linebot.domain.Cart;
import com.company.linebot.domain.CartItem;
import com.company.linebot.dto.CartDto;
import com.company.linebot.dto.CartItemDto;

public interface CartRepository {

    void create(CartDto cartDto);

    Cart read(String id);

    void update(String id, CartDto cartDto);

    void delete(String id);

    void addItem(String id, String productId);

    void removeItem(String id, String productId);
}
