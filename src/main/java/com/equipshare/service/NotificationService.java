package com.equipshare.service;

import com.equipshare.model.Notification;
import com.equipshare.model.NotificationType;
import com.equipshare.model.User;
import com.equipshare.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // Create and send a notification
    public void sendNotification(User user, String message, NotificationType type) {
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID().toString());
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setReadStatus(false);
        notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        notificationRepository.save(notification);
    }

    // Get all notifications for a user
    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // Count unread notifications
    public long countUnread(String userId) {
        return notificationRepository.countByUserIdAndReadStatusFalse(userId);
    }

    // Optional: Mark all as read
    public void markAllAsRead(String userId) {
        notificationRepository.markAllAsReadByUserId(userId); // Custom query
    }
}
