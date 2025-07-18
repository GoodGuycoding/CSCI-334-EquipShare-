package com.equipshare.controller;
import com.equipshare.model.User;
import com.equipshare.security.CustomUserDetails;
import com.equipshare.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/borrower")
public class BorrowerProfileController {

    private final UserService userService;

    public BorrowerProfileController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/update-profile")
    public String updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String contact,
            @RequestParam String address,
            @RequestParam(required = false) String password) {

        User user = userDetails.getUser();
        user.setFirstName(firstName);
        user.setLastName(lastName);
       // user.setContact(contact);
      //  user.setAddress(address);

        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }

        userService.updateUser(user);

        return "redirect:/borrowerDashboard";
    }
}