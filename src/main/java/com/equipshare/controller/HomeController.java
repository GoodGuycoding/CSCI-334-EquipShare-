package com.equipshare.controller;

import com.equipshare.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.equipshare.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestMapping;

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
        return "login"; // whatever html page name is written here would be the starting point of our app.
    }
    @GetMapping("/cart")
    public String userCart(Model model) {
        return "cart";
    }

    //This method maps the /login URL to show the login.html page.
    //Spring Boot uses Thymeleaf to find and render that file from the templates folder.
    //It is done to keep the logic and HTML separate.

}
