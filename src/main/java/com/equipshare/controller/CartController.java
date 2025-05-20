package com.equipshare.controller;

import com.equipshare.model.CartItem;
import com.equipshare.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public void addToCart(@RequestParam String itemId, @RequestParam int rentalDays) {
        cartService.addToCart(itemId, rentalDays);
    }

    @GetMapping
    public List<CartItem> getCartItems() {
        return cartService.getItems();
    }

    @GetMapping("/total")
    public double getTotal() {
        return cartService.getTotal();
    }

    @PostMapping("/remove/{id}")
    public void removeItem(@PathVariable String id) {
        cartService.removeItem(id);
    }

    @PostMapping("/checkout")
    public String checkout() {
        cartService.clearCart();
        return "Cart checked out!";
    }
}
