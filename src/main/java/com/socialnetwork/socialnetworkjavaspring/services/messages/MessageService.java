package com.socialnetwork.socialnetworkjavaspring.services.messages;

import com.socialnetwork.socialnetworkjavaspring.models.Message;
import com.socialnetwork.socialnetworkjavaspring.repositories.IMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService implements IMessageService {

    @Autowired
    private IMessageRepository messageRepository;

    @Override
    public List<Message> findAllByConversationId(Long conversationId) {
        return messageRepository.findAllByConversation_ConversationIdOrderBySendAt(conversationId);
    }
}
