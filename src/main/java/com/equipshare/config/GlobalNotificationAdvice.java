package com.equipshare.config;

import com.equipshare.model.Notification;
import com.equipshare.model.User;
import com.equipshare.service.NotificationService;
import com.equipshare.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.List;

@ControllerAdvice
public class GlobalNotificationAdvice {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void getUserNotifications(Model model, Principal principal, HttpSession session) {
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName()).orElse(null);
            if (user != null) {
                model.addAttribute("userNotifications", notificationService.getUserNotifications(user.getId()));
            }
        }

        Boolean show = (Boolean) session.getAttribute("showNotifications");
        model.addAttribute("showNotifications", show != null ? show : false);
    }
}
