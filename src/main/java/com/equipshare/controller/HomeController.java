package com.equipshare.controller;

import com.equipshare.model.Item;
import com.equipshare.model.User;
import com.equipshare.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
        return "cart"; // whatever html page name is written here would be the starting point of our app.
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
    public String viewTools(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(required = false) String keyword,
                            @RequestParam(required = false) String filter,
                            Model model) {

        int pageSize = 12;
        int pageGroupSize = 5;

        Page<Item> toolPage = itemService.searchAndFilterPaged(keyword, filter, page, pageSize);

        int currentPage = toolPage.getNumber();
        int totalPages = toolPage.getTotalPages();

        int startPage = (currentPage / pageGroupSize) * pageGroupSize;
        int endPage = Math.min(startPage + pageGroupSize - 1, totalPages - 1);

        model.addAttribute("tools", toolPage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "tools";
    }

    @GetMapping("/toggle-notifications")
    public String toggleNotifications(HttpServletRequest request, HttpSession session) {
        Boolean current = (Boolean) session.getAttribute("showNotifications");
        session.setAttribute("showNotifications", current == null ? true : !current);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @GetMapping("/notification-redirect")
    public String handleNotificationRedirect(@RequestParam("target") String target, HttpSession session) {
        session.setAttribute("showNotifications", false);
        return "redirect:" + target;
    }




}
