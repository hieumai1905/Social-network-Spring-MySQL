package com.socialnetwork.socialnetworkjavaspring.services.conversations;

import com.socialnetwork.socialnetworkjavaspring.DTOs.conversations.ConversationRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.conversations.ConversationResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.models.Conversation;
import com.socialnetwork.socialnetworkjavaspring.models.Participant;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.ConversationType;
import com.socialnetwork.socialnetworkjavaspring.models.enums.ParticipantStatus;
import com.socialnetwork.socialnetworkjavaspring.models.key.ParticipantId;
import com.socialnetwork.socialnetworkjavaspring.repositories.IConversationRepository;
import com.socialnetwork.socialnetworkjavaspring.repositories.IParticipantRepository;
import com.socialnetwork.socialnetworkjavaspring.repositories.IUserRepository;
import com.socialnetwork.socialnetworkjavaspring.utils.Constants;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import com.socialnetwork.socialnetworkjavaspring.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class ConversationService implements IConversationService {
    @Autowired
    private IConversationRepository conversationRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IParticipantRepository participantRepository;

    @Value("${root-path.dir}")
    private String CONTEXT_PATH_DIR;

    @Value("${upload.dir}")
    private String UPLOAD_DIR;

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

    @Override
    public void createConversation(ConversationRequestDTO request, MultipartFile file, User user) {
        String avatar = user.getAvatar();
        if(file != null){
            avatar = saveFile(file);
        }
        Conversation conversation = new Conversation();
        conversation.setName(request.getNameConversation());
        conversation.setAvatar(avatar);
        conversation.setUser(user);
        conversation.setType(ConversationType.GROUP);
        conversationRepository.save(conversation);
        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant(
                new ParticipantId(user.getUserId(), conversation.getConversationId()),
                ParticipantStatus.JOINED,
                null,
                new Date(),
                user.getFullName(),
                user,
                conversation
        ));
        for (String userId : request.getParticipantIds()) {
            User existingUser = userRepository.findById(userId).orElseThrow(
                    () -> new NullPointerException("User not found")
            );
            Participant participant = new Participant();
            participant.setParticipantId(new ParticipantId(existingUser.getUserId(), conversation.getConversationId()));
            participant.setNickname(existingUser.getFullName());
            participant.setStatus(ParticipantStatus.JOINED);
            participant.setJoinedAt(new Date());
            participant.setUser(existingUser);
            participant.setConversation(conversation);
            participants.add(participant);
        }
        participantRepository.saveAll(participants);
    }

    @Override
    public void deleteConversation(Long conversationId, User user) {
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(
                () -> new NullPointerException("Conversation not found!")
        );
        Participant participant = participantRepository.findById(new ParticipantId(user.getUserId(), conversationId))
                .orElseThrow(() -> new NullPointerException("Participant not found!"));
        participant.setDeletedAt(new Date());
        participantRepository.save(participant);
    }

    @Override
    public ConversationResponseDTO getConversationById(User userCurrent, Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new NullPointerException("Conversation not found!"));
        ConversationResponseDTO conversationResponseDTO = ConvertUtils.convert(conversation, ConversationResponseDTO.class);
        List<UserResponseDTO> userResponseDTOs = new ArrayList<>();
        for (Participant participant : conversation.getParticipants()) {
            userResponseDTOs.add(ConvertUtils.convert(participant.getUser(), UserResponseDTO.class));
        }
        conversationResponseDTO.setMembers(userResponseDTOs);
        return conversationResponseDTO;
    }

    @Override
    public void updateConversation(Long id, ConversationRequestDTO request, MultipartFile file, User user) {
        String avatar = user.getAvatar();
        if(file != null){
            avatar = saveFile(file);
        }
        Conversation conversation = conversationRepository.findById(id).orElseThrow(
                () -> new NullPointerException("Conversation not found!")
        );
        conversation.setName(request.getNameConversation());
        conversation.setAvatar(avatar);
        conversationRepository.save(conversation);
        for (Participant participant : conversation.getParticipants()) {
            if(!request.getParticipantIds().contains(participant.getParticipantId().getUserId())){
                participantRepository.deleteByUserIdAndConversationId(
                        participant.getParticipantId().getUserId(),
                        participant.getConversation().getConversationId()
                );
            }else{
                request.getParticipantIds().removeIf(item -> item.equals(participant.getParticipantId().getUserId()));
            }
        }
        List<Participant> participants = new ArrayList<>();
        for (String userId : request.getParticipantIds()) {
            User existingUser = userRepository.findById(userId).orElseThrow(
                    () -> new NullPointerException("User not found")
            );
            Participant participant = new Participant();
            participant.setParticipantId(new ParticipantId(existingUser.getUserId(), conversation.getConversationId()));
            participant.setNickname(existingUser.getFullName());
            participant.setStatus(ParticipantStatus.JOINED);
            participant.setJoinedAt(new Date());
            participant.setUser(existingUser);
            participant.setConversation(conversation);
            participants.add(participant);
        }
        participantRepository.saveAll(participants);
    }

    @Override
    public void updateManager(Long id, String managerId, User currentUser) {
        Conversation conversation = conversationRepository.findById(id).orElseThrow(
                () -> new NullPointerException("Conversation not found!")
        );
        User manager = userRepository.findById(managerId).orElseThrow(
                () -> new NullPointerException("Manager not found!")
        );
        conversation.setUser(manager);
        conversationRepository.save(conversation);
    }

    private String saveFile(MultipartFile file){
        try {
            String fileName = file.getOriginalFilename();
            File destFile = new File(CONTEXT_PATH_DIR + UPLOAD_DIR + File.separator + fileName);
            destFile.getParentFile().mkdirs();
            file.transferTo(destFile);
            return "/assets/files-upload" + Constants.FILE_SEPARATOR + fileName;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
