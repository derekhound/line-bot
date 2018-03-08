package com.company.linebot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.linebot.domain.Cart;
import com.company.linebot.domain.repository.CartRepository;
import com.company.linebot.dto.CartDto;
import com.company.linebot.service.CartService;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;

    public void create(CartDto cartDto) {
        cartRepository.create(cartDto);
    }

    public Cart read(String id) {
        return cartRepository.read(id);
    }

    public void update(String id, CartDto cartDto) {
        cartRepository.update(id, cartDto);
    }

    public void delete(String id) {
        cartRepository.delete(id);
    }

    public void addItem(String id, String productId) {
        cartRepository.addItem(id, productId);
    }

    public void removeItem(String id, String productId) {
        cartRepository.removeItem(id, productId);
    }
}