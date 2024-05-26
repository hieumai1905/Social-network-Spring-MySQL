package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.DTOs.messages.MessageResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.controllers.ApplicationController;
import com.socialnetwork.socialnetworkjavaspring.models.Message;
import com.socialnetwork.socialnetworkjavaspring.services.messages.IMessageService;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class ApisMessageController extends ApplicationController {
    @Autowired
    private IMessageService messageService;

    @GetMapping("/conversations/{conversationId}")
    public ResponseEntity<?> getAllMessageByConversationId(@PathVariable("conversationId") Long conversationId) {
        List<Message> messages = messageService.findAllByConversationId(conversationId);
        List<MessageResponseDTO> messageResponseDTOs = ConvertUtils.convertList(messages, MessageResponseDTO.class);
        for (MessageResponseDTO messageResponseDTO : messageResponseDTOs) {
            if (messageResponseDTO.getUserSender().getUserId().equals(currentUser.getUserId())) {
                messageResponseDTO.setUserCurrent(true);
            }
        }
        return responseApi(HttpStatus.OK, String.format("Get messages for conversation %s", conversationId),
                messageResponseDTOs);
    }
}
