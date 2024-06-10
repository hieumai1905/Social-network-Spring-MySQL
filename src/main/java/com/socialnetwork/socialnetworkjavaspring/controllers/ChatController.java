package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.DTOs.messages.ChatMessageDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.messages.MessageRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.notifications.NotificationResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.models.*;
import com.socialnetwork.socialnetworkjavaspring.services.conversations.IConversationService;
import com.socialnetwork.socialnetworkjavaspring.services.messages.IMessageService;
import com.socialnetwork.socialnetworkjavaspring.services.notifications.INotificationService;
import com.socialnetwork.socialnetworkjavaspring.services.posts.IPostService;
import com.socialnetwork.socialnetworkjavaspring.services.users.IUserService;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;
import java.util.Optional;

@Controller
@Slf4j
public class ChatController {

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IConversationService conversationService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IPostService postService;

    @Autowired
    private INotificationService notificationService;

    @MessageMapping("/chat.sendMessage/{conversationId}")
    @SendTo("/conversations/{conversationId}")
    public Object sendMessage(@Payload MessageRequestDTO messageRequestDTO,
                              @DestinationVariable("conversationId") Long conversationId) {
        messageRequestDTO.setConversationId(conversationId);
        try {
            if (messageRequestDTO.getMessageType().equals("NOTIFICATION"))
                return saveNotification(messageRequestDTO);
            else if (messageRequestDTO.getMessageType().equals("CHAT"))
                return saveMessage(messageRequestDTO);
            else {
                throw new Exception("Invalid message type");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private NotificationResponseDTO saveNotification(MessageRequestDTO messageRequestDTO) throws Exception {
        User user = userService.findById(messageRequestDTO.getSenderId());
        User userTarget = userService.findById(messageRequestDTO.getUserTargetId());
        String type = "";

        Notification notification = new Notification();
        notification.setContent(messageRequestDTO.getContent());
        notification.setUser(user);
        notification.setUserTarget(userTarget);

        if (messageRequestDTO.getTypeNotification().equals("POST")) {
            String postId = messageRequestDTO.getDataId();
            Optional<Post> post = postService.findById(postId);
            if (post.isEmpty()) {
                throw new Exception("Post not found");
            }
            notification.setUrlRedirect("/details/posts?id=" + postId);
            type = "NOTIFICATION";
            if (notificationService.isDuplicateNotificationOneHour(notification)) {
                throw new Exception("Duplicate notification");
            }
            notificationService.save(notification);
            NotificationResponseDTO responseDTO = ConvertUtils.convert(notification, NotificationResponseDTO.class);
            responseDTO.setType(type);
            return responseDTO;
        } else {
            throw new Exception("Invalid type notification");
        }
    }

    @MessageMapping("/chat.addUser/{conversationId}")
    @SendTo("/conversations/{conversationId}")
    public void addUser(@Payload MessageRequestDTO messageRequestDTO, SimpMessageHeaderAccessor headerAccessor,
                        @DestinationVariable("conversationId") String conversationId) {
        log.info("UserId: {} JOINED ConversationId: {}", messageRequestDTO.getSenderId(), conversationId);
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("senderId", messageRequestDTO.getSenderId());
        headerAccessor.getSessionAttributes().put("conversationId", conversationId);
    }

    private ChatMessageDTO saveMessage(MessageRequestDTO messageRequestDTO) throws Exception {
        Conversation conversation = conversationService.findById(messageRequestDTO.getConversationId()).get();
        User user = userService.findById(messageRequestDTO.getSenderId());
        Message message = new Message(messageRequestDTO.getContent(), conversation, user);
        messageService.save(message);
        return ConvertUtils.convert(message, ChatMessageDTO.class);
    }
}
