package com.equipshare.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

//    @ManyToOne
//    @JoinColumn(name = "booking_id")
//    private Booking booking;  // Optional, for messages related to a booking

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Timestamp timestamp;

    @Transient
    private String lastMessage;

    @Transient
    private int unreadCount;

    @Column(nullable = false)
    private boolean read = false;

    public Message() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }
    // Getters and Setters
    public String getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }
    public User getRecipient() {
        return recipient;
    }

//    public Booking getBooking() {
//        return booking;
//    }
    public String getContent() {
        return content;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public boolean isRead() {
        return read;
    }



    // Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

//    public void setBooking(Booking booking) {
//        this.booking = booking;
//    }

    public void setContent(String content) {
        this.content = content;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public void setRead(boolean read) {
        this.read = read;
    }
}