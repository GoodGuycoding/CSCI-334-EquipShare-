package com.equipshare.controller;

import com.equipshare.model.Item;
import com.equipshare.model.User;
import com.equipshare.security.CustomUserDetails;
import com.equipshare.service.UserService;
import com.equipshare.service.ItemService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthController {
    private final UserService userService;
    private final ItemService itemService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, ItemService itemService) {
        this.userService = userService;
        this.itemService = itemService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLoginPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String email,
            Model model) {

        if (error != null) {
            switch (error) {
                case "auth_failed":
                    model.addAttribute("error", "Invalid email or password");
                    break;
                case "role_mismatch":
                    model.addAttribute("error", "Incorrect role selected.");
                    break;
                default:
                    model.addAttribute("error", "Login failed");
            }
        }
        if (email != null) {
            model.addAttribute("email", email);
        }
        return "login";
    }

    // made few changes here as well
    @PostMapping("/user-login")
    public String loginUser(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role,
            Model model) {

        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(
            @ModelAttribute User user,
            @RequestParam Boolean isOrganiser,
            Model model) {

        user.setIsOwner(isOrganiser);
        user.setIsBorrower(!isOrganiser);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        try {
            userService.registerUser(user);
            return "redirect:/login?signup=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            return "signup";
        }

    }


    @GetMapping("/ownerDashboard")
    public String ownerDashboard(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Assuming you have an itemService that can fetch items by owner
        List<Item> availableItems = itemService.getAvailableItemsByOwner(user);
        List<Item> rentedItems = itemService.getRentedItemsByOwner(user);

        model.addAttribute("user", user);
        model.addAttribute("owner", user);
        model.addAttribute("availableItems", availableItems);
        model.addAttribute("rentedItems", rentedItems);

        return "ownerDashboard";
    }

}