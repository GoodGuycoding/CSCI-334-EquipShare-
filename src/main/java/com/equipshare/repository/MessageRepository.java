package com.equipshare.repository;

import com.equipshare.model.Booking;
import com.equipshare.model.Message;
import com.equipshare.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    // query to find all messages between the sender and recipient
    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender = :user1 AND m.recipient = :user2) OR " +
            "(m.sender = :user2 AND m.recipient = :user1) " +
            "ORDER BY m.timestamp ASC")
    List<Message> findConversation(@Param("user1") User user1, @Param("user2") User user2);

    // find messages from a certain sender
    List<Message> findBySender(User sender);

    // find messages from a certain recpient
    List<Message> findByRecipient(User recipient);

    //find most recent message
    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender = :user1 AND m.recipient = :user2) OR " +
            "(m.sender = :user2 AND m.recipient = :user1) " +
            "ORDER BY m.timestamp DESC LIMIT 1")
    Message findLatestMessageBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);

    // find specific users that messaged  current user
    @Query("SELECT DISTINCT CASE WHEN m.sender = :user THEN m.recipient ELSE m.sender END " +
            "FROM Message m WHERE m.sender = :user OR m.recipient = :user")
    List<User> findConversationPartners(@Param("user") User user);

    // count for unread
    @Query("SELECT COUNT(m) FROM Message m WHERE m.recipient = :user AND m.read = false")
    long countUnreadMessages(@Param("user") User user);

    // mark messages as read
    @Query("UPDATE Message m SET m.read = true WHERE m.recipient = :user AND m.sender = :sender AND m.read = false")
    void markMessagesAsRead(@Param("user") User user, @Param("sender") User sender);

    // find booking related messages
    @Query("SELECT m FROM Message m WHERE " +
            "((m.sender = :owner AND m.recipient = :borrower) OR " +
            "(m.sender = :borrower AND m.recipient = :owner)) " +
            "AND m.booking = :booking " +
            "ORDER BY m.timestamp ASC")
    List<Message> findMessagesByBooking(@Param("owner") User owner,
                                        @Param("borrower") User borrower,
                                        @Param("booking") Booking booking);
}