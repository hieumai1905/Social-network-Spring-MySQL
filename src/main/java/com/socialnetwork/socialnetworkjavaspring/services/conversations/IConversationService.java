package com.socialnetwork.socialnetworkjavaspring.services.conversations;

import com.socialnetwork.socialnetworkjavaspring.models.Conversation;
import com.socialnetwork.socialnetworkjavaspring.models.enums.ConversationType;
import com.socialnetwork.socialnetworkjavaspring.services.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface IConversationService extends IGeneralService<Conversation, Long> {
    List<Conversation> getConversationJoinedByUserId(String userId);

    Optional<Conversation> findById(Long key);

    Boolean isType(Long conversationId, ConversationType type);

    Conversation findByPersonalTypeAndUserIdAndUserTargetId(String userId, String userTargetId) throws Exception;
}
