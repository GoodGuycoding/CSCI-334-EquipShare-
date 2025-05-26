package com.equipshare.controller;
import com.equipshare.model.Item;
import com.equipshare.model.User;
import com.equipshare.security.CustomUserDetails;
import com.equipshare.service.ItemService;
import com.equipshare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.sql.Timestamp;

@Controller
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/tool/{id}")
    public String viewItemDetails(@PathVariable("id") String itemId,@RequestParam(required = false) String step,
                                  @RequestParam(required = false) String startDate, Model model) {
        Item item = itemService.getItemById(itemId);
        model.addAttribute("item", item);
        model.addAttribute("step", step); // "start" or "end"
        model.addAttribute("startDate", startDate);
        return "productPage"; // This matches product.html
    }

    @GetMapping("/payment")
    public String showPaymentPage(@RequestParam("itemId") String itemId, @RequestParam String startDate,
                                  @RequestParam(required = false) String success,
                                  @RequestParam String endDate, Model model) {
        Item item = itemService.getItemById(itemId);
        model.addAttribute("item", item);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        if ("true".equals(success)) {
            model.addAttribute("success", true); // ✅ pass flag to show modal
        }
        return "payment"; // payment.html
    }


    @GetMapping("/add-item")
    public String showAddItemForm() {
        return "add-item";
    }
    @Autowired
    private UserService userService;

    @PostMapping("/add-item")
    public String handleAddItem(@ModelAttribute Item item, Principal principal) {
        String email = principal.getName();
        User owner = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        item.setOwner(owner);
        item.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        item.setAvailable(true);

        itemService.addItem(item, owner);
        return "redirect:/ownerDashboard";
    }



}
