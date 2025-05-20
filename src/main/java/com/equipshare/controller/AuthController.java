package com.equipshare.controller;

import com.equipshare.model.User;
import com.equipshare.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLoginPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String email,
            Model model) {
        System.out.println("GET /login triggered");
        if (error != null) {
            model.addAttribute("error", "Invalid credentials or role mismatch");
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

        System.out.println("Login POST hit!");
        var user = userService.authenticateUser(email, password, role);

        if (user.isPresent()) {

            return "redirect:/" + ("borrower".equals(role) ? "borrowerDashboard" : "ownerDashboard");
        } else {
            model.addAttribute("error", "Invalid credentials or role mismatch");
            return "login";
        }
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
    @GetMapping("/borrowerDashboard")
    public String borrowerDashboard(Model model) {
        return "borrowerDashboard";
    }

    @GetMapping("/ownerDashboard")
    public String ownerDashboard(Model model) {
        return "ownerDashboard";
    }
}