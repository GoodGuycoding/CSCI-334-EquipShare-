package com.equipshare.controller;

import com.equipshare.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.equipshare.repository.UserRepository;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "index"; // Thymeleaf will render templates/index.html
    }
}
