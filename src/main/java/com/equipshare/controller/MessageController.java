package com.equipshare.controller;

import com.equipshare.model.Message;
import com.equipshare.model.User;
import com.equipshare.repository.MessageRepository;
import com.equipshare.repository.UserRepository;
import com.equipshare.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessageController {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageController(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    // Show all users you've chatted with
    @GetMapping
    public String getConversations(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String currentUserId = userDetails.getId();
        User currentUser = userRepository.findById(currentUserId).orElseThrow();

        // Get user IDs of chat partners
        List<String> userIds = messageRepository.findConversationPartnerIds(currentUserId);
        List<User> conversationUsers = userRepository.findAllById(userIds);

        // Enrich each user with last message and unread count
        for (User user : conversationUsers) {
            Message lastMessage = messageRepository.findLatestMessageBetweenUsers(currentUser, user)
                    .stream()
                    .findFirst()
                    .orElse(null);
            user.setLastMessage(lastMessage != null ? lastMessage.getContent() : "");

            long unread = messageRepository.countUnreadBetween(currentUserId, user.getId());
            user.setUnreadCount((int) unread);
        }

        // 🔍 ADD THESE DEBUG PRINTS HERE:
        System.out.println("Found user IDs in conversation: " + userIds);
        System.out.println("Conversation Users Count: " + conversationUsers.size());


        model.addAttribute("conversationUsers", conversationUsers);
        return "messages";
    }

    // Show messages with a specific user
    @GetMapping("/conversation/{otherUserId}")
    public String getConversation(@PathVariable String otherUserId,
                                  @RequestParam(value = "redirect", required = false) Boolean redirect,
                                  Model model,
                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        String currentUserId = userDetails.getId();

        System.out.print("conversation was initated");
        List<Message> messages = messageRepository.findMessagesBetweenUsers(currentUserId, otherUserId);

        // mark messages as read
        messageRepository.markMessagesAsRead(currentUserId, otherUserId);

        if (Boolean.TRUE.equals(redirect)) {
            return "redirect:/messages"; // go back to list
        }
        System.out.print("Problem is here");

        User currentUser = userRepository.findById(currentUserId).orElseThrow();
        User recipient = userRepository.findById(otherUserId).orElse(null);
        model.addAttribute("messages", messages);
        model.addAttribute("recipientId", otherUserId);
        model.addAttribute("recipientName", recipient != null ? recipient.getFirstName() + " " + recipient.getLastName() : "User");
        model.addAttribute("currentUserId", currentUserId);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("recipient", recipient);
        return "conversation";
    }

    // Send a message
    @PostMapping("/send/{recipientId}")
    public String sendMessage(@PathVariable String recipientId,
                              @RequestParam String content,
                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        // get user IDS
        User sender = userRepository.findById(userDetails.getId()).orElseThrow();
        User recipient = userRepository.findById(recipientId).orElseThrow();

        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(content);
        message.setIsRead(false);
        message.setTimestamp(new Timestamp(System.currentTimeMillis()));

        messageRepository.save(message);
        return "redirect:/messages/conversation/" + recipientId;
    }
}
