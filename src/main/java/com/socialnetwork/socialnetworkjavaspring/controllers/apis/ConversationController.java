package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.DTOs.conversations.ConversationResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.models.Conversation;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.services.conversations.IConversationService;
import com.socialnetwork.socialnetworkjavaspring.services.sessions.SessionService;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    @Autowired
    private IConversationService conversationService;

    @Autowired
    private SessionService sessionService;

    @GetMapping
    public ResponseEntity<List<ConversationResponseDTO>> index() {
        User userCurrent = sessionService.currentUser();
        List<Conversation> conversations = conversationService.getConversationJoinedByUserId(userCurrent.getUserId());
        return ResponseEntity.ok(ConvertUtils.convertList(conversations, ConversationResponseDTO.class));
    }
}
