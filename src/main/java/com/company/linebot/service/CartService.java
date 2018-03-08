package com.company.linebot.service;

import com.company.linebot.domain.Cart;
import com.company.linebot.dto.CartDto;

public interface CartService {

    void create(CartDto cartDto);

    Cart read(String id);

    void update(String id, CartDto cartDto);

    void delete(String id);

    void addItem(String id, String productId);

    void removeItem(String id, String productId);
}
