package com.equipshare.service;

import com.equipshare.model.CartItem;
import com.equipshare.model.Item;
import com.equipshare.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    private final Map<String, CartItem> cart = new HashMap<>();

    @Autowired
    private ItemRepository itemRepository;

    public void addToCart(String itemId, int rentalDays) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        CartItem cartItem = new CartItem();
        cartItem.setItemId(item.getId());
        cartItem.setTitle(item.getTitle());
        cartItem.setPricePerDay(item.getPricePerDay());
        cartItem.setRentalDays(rentalDays);

        cart.put(itemId, cartItem);
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(cart.values());
    }

    public double getTotal() {
        return cart.values().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    public void removeItem(String itemId) {
        cart.remove(itemId);
    }

    public void clearCart() {
        cart.clear();
    }
}
