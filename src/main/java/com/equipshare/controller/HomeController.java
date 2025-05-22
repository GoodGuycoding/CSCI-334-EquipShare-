package com.equipshare.controller;

import com.equipshare.model.Item;
import com.equipshare.model.User;
import com.equipshare.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.equipshare.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {


    @Autowired
    private UserRepository userRepository;
    private final ItemService itemService;

    public HomeController(UserRepository userRepository, ItemService itemService) {
        this.userRepository = userRepository;
        this.itemService = itemService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "homepage"; // whatever html page name is written here would be the starting point of our app.
    }
    @GetMapping("/cart")
    public String userCart(Model model) {
        return "cart";
    }

    @GetMapping("/about")
    public String aboutUs(Model model) {
        return "about-us";
    }

    @GetMapping("/tools")
    public String viewToolsPage(@RequestParam(defaultValue = "0") int page,
                                Model model) {

        int pageSize = 12;
        Page<Item> toolPage = itemService.getItemsPaged(page, pageSize);

        model.addAttribute("tools", toolPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", toolPage.getTotalPages());

        return "tools"; // tools.html
    }

}
