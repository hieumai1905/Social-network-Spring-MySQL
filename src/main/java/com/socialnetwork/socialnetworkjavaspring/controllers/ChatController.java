package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.DTOs.messages.ChatMessageDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.messages.MessageRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.models.Conversation;
import com.socialnetwork.socialnetworkjavaspring.models.Message;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.services.conversations.IConversationService;
import com.socialnetwork.socialnetworkjavaspring.services.messages.IMessageService;
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

@Controller
@Slf4j
public class ChatController {

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IConversationService conversationService;

    @Autowired
    private IUserService userService;

    @MessageMapping("/chat.sendMessage/{conversationId}")
    @SendTo("/conversations/{conversationId}")
    public ChatMessageDTO sendMessage(@Payload MessageRequestDTO messageRequestDTO,
                                      @DestinationVariable("conversationId") Long conversationId) {
        messageRequestDTO.setConversationId(conversationId);
        try {
            return saveMessage(messageRequestDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
