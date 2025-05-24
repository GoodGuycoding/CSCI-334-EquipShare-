package com.equipshare.repository;

import com.equipshare.model.Booking;
import com.equipshare.model.Message;
import com.equipshare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    // Find all messages between two users
    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.id = :user1Id AND m.recipient.id = :user2Id) OR " +
            "(m.sender.id = :user2Id AND m.recipient.id = :user1Id) " +
            "ORDER BY m.timestamp ASC")
    List<Message> findMessagesBetweenUsers(@Param("user1Id") String user1Id,
                                           @Param("user2Id") String user2Id);

    // ✅ Native SQL: safer for Hibernate 6+
    @Query(value = """
        SELECT DISTINCT 
            CASE 
                WHEN sender_id = :userId THEN recipient_id 
                ELSE sender_id 
            END AS other_user_id
        FROM message
        WHERE sender_id = :userId OR recipient_id = :userId
    """, nativeQuery = true)
    List<String> findConversationPartnerIds(@Param("userId") String userId);

    // Count unread messages from any sender
    @Query("SELECT COUNT(m) FROM Message m WHERE m.recipient.id = :userId AND m.read = false")
    long countUnreadMessages(@Param("userId") String userId);

    // Count unread between two users
    @Query("SELECT COUNT(m) FROM Message m WHERE m.recipient.id = :recipientId AND m.sender.id = :senderId AND m.read = false")
    long countUnreadBetween(@Param("recipientId") String recipientId, @Param("senderId") String senderId);

    // Mark messages as read
    @Modifying
    @Transactional
    @Query(value = """
    UPDATE message 
    SET `read` = 1 
    WHERE recipient_id = :userId 
      AND sender_id = :senderId 
      AND `read` = 0
""", nativeQuery = true)
    void markMessagesAsRead(@Param("userId") String userId, @Param("senderId") String senderId);

    // Latest message between users (return top 1 by timestamp)
    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender = :user1 AND m.recipient = :user2) OR " +
            "(m.sender = :user2 AND m.recipient = :user1) " +
            "ORDER BY m.timestamp DESC")
    List<Message> findLatestMessageBetweenUsers(@Param("user1") User user1,
                                                @Param("user2") User user2);

    // Optional: messages related to a booking
//    @Query("SELECT m FROM Message m WHERE " +
//            "((m.sender.id = :ownerId AND m.recipient.id = :borrowerId) OR " +
//            "(m.sender.id = :borrowerId AND m.recipient.id = :ownerId)) " +
//            "AND m.booking = :booking " +
//            "ORDER BY m.timestamp ASC")
//    List<Message> findMessagesByBooking(@Param("ownerId") String ownerId,
//                                        @Param("borrowerId") String borrowerId,
//                                        @Param("booking") Booking booking);
}
