package com.socialnetwork.socialnetworkjavaspring.services.conversations;

import com.socialnetwork.socialnetworkjavaspring.DTOs.conversations.ConversationRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.conversations.ConversationResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.models.Conversation;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.ConversationType;
import com.socialnetwork.socialnetworkjavaspring.services.IGeneralService;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.util.List;
import java.util.Optional;

public interface IConversationService extends IGeneralService<Conversation, Long> {
    List<Conversation> getConversationJoinedByUserId(String userId);

    Optional<Conversation> findById(Long key);

    Boolean isType(Long conversationId, ConversationType type);

    Conversation findByPersonalTypeAndUserIdAndUserTargetId(String userId, String userTargetId) throws Exception;

    void createConversation(ConversationRequestDTO request, MultipartFile file, User user);

    void deleteConversation(Long conversationId, User user);

    ConversationResponseDTO getConversationById(User userCurrent, Long conversationId);

    void updateConversation(Long id, ConversationRequestDTO requestDTO, MultipartFile file, User currentUser);

    void updateManager(Long id, String managerId, User currentUser);
}
