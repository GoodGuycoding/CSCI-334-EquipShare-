package com.equipshare.controller;

import com.equipshare.model.Item;
import com.equipshare.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/tool/{id}")
    public String viewItemDetails(@PathVariable("id") String itemId, Model model) {
        Item item = itemService.getItemById(itemId);
        model.addAttribute("item", item);
        return "productPage"; // This matches product.html
    }
}
