package com.socialnetwork.socialnetworkjavaspring.services.conversations;

import com.socialnetwork.socialnetworkjavaspring.models.Conversation;
import com.socialnetwork.socialnetworkjavaspring.models.enums.ConversationType;
import com.socialnetwork.socialnetworkjavaspring.models.enums.ParticipantStatus;
import com.socialnetwork.socialnetworkjavaspring.repositories.IConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ConversationService implements IConversationService {
    @Autowired
    private IConversationRepository conversationRepository;

    @Override
    public Optional<Conversation> save(Conversation object) {
        return Optional.of(conversationRepository.save(object));
    }

    @Override
    public Optional<Conversation> delete(Conversation object) {
        conversationRepository.delete(object);
        return Optional.of(object);
    }

    @Override
    public List<Conversation> getConversationJoinedByUserId(String userId) {
        return conversationRepository.findAllByUserId_AndStatusOrderByLatestMessageTime(userId, ParticipantStatus.JOINED.toString());
    }

    @Override
    public Optional<Conversation> findById(Long key) {
        return conversationRepository.findById(key);
    }

    @Override
    public Boolean isType(Long conversationId, ConversationType type) {
        Optional<Conversation> conversation = conversationRepository.findById(conversationId);
        boolean isType = false;
        if (conversation.isPresent()) {
            isType = conversation.get().getType().equals(type);
        }
        return isType;
    }

    @Override
    public Conversation findByPersonalTypeAndUserIdAndUserTargetId(String userId, String userTargetId) throws Exception {
        return conversationRepository.getPersonalConversation(userId, userTargetId)
                .orElseThrow(() -> new Exception("Conversation not found"));
    }
}
