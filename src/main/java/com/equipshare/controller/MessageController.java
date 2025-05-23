package com.equipshare.controller;
import com.equipshare.model.Message;
import com.equipshare.repository.MessageRepository;
import com.equipshare.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.equipshare.security.CustomUserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageController(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

  //  @GetMapping("/conversations")
//    public <ConversationDTO> List<ConversationDTO> getConversations(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // Return list of conversations for the current user
 //       return getConversations();
//    }

  //  @GetMapping("/{conversationId}")
 //   public List<Message> getMessages(@PathVariable String conversationId,
 //                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        // Return messages for a specific conversation
//    }

 //   @PostMapping("/send")
 //   public Message sendMessage(@RequestBody MessageDTO messageDTO,
 //                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        // Save and send a new message
//    }
}