package com.socialnetwork.socialnetworkjavaspring.services.messages;

import com.socialnetwork.socialnetworkjavaspring.models.Message;
import com.socialnetwork.socialnetworkjavaspring.repositories.IMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService implements IMessageService {

    @Autowired
    private IMessageRepository messageRepository;

    @Override
    public List<Message> findAllByConversationId(Long conversationId, String userId) {
        return messageRepository.findMessagesAfterDeletionForUser(conversationId, userId);
    }

    @Override
    public Optional<Message> save(Message object) {
        return Optional.of(messageRepository.save(object));
    }

    @Override
    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }
}
