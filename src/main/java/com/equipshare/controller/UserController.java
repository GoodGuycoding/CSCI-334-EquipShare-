package com.equipshare.controller;
import com.equipshare.model.User;
import com.equipshare.service.UserService;
import com.equipshare.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.equipshare.security.CustomUserDetails;


@Controller
public class UserController {
    private final UserService  userService;
    private final ItemService itemService;

    public UserController(UserService userService,  ItemService itemService) {
        this.userService = userService;
        this.itemService = itemService;
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "index";
    }

    @GetMapping("/borrower/dashboard")
    public String getBorrowerDashboard(Model model) {
//        // getting user details
//        User user = userDetails.getUser();
//
//        model.addAttribute("user", user);
//
//        // need to add bookings
//        // model.addAttribute("bookings", itemService.getBookingsByBorrower(user));

        return "borrowerDashboard";
    }

}
