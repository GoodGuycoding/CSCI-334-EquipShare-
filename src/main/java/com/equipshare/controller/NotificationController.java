package com.equipshare.controller;

import com.equipshare.model.Notification;
import com.equipshare.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import com.equipshare.model.User;
import com.equipshare.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    // Get notification dropdown fragment (or page)
    @GetMapping
    public String getNotifications(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userService.getUserByEmail(email).orElseThrow();
        List<Notification> notifications = notificationService.getUserNotifications(user.getId());
        model.addAttribute("notifications", notifications);
        return "fragments/notificationList"; // Can be used as AJAX fragment or full page
    }

    // Optional: mark all as read (e.g. after user opens dropdown)
    @PostMapping("/mark-all-read")
    @ResponseBody
    public String markAllAsRead(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userService.getUserByEmail(email).orElseThrow();
        notificationService.markAllAsRead(user.getId());
        return "OK";
    }

    // Optional: return unread count (for badge icon)
    @GetMapping("/unread-count")
    @ResponseBody
    public long getUnreadCount(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userService.getUserByEmail(email).orElseThrow();
        return notificationService.countUnread(user.getId());
    }
}
