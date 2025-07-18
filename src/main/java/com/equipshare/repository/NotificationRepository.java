package com.equipshare.repository;

import com.equipshare.model.Notification;
import com.equipshare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

    // Get all notifications for a user, newest first
    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);

    // Count unread notifications for a user
    long countByUserIdAndReadStatusFalse(String userId);

    // Optional: mark all as read
    @Query("UPDATE Notification n SET n.readStatus = true WHERE n.user.id = :userId")
    void markAllAsReadByUserId(String userId);
}
