package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.DTOs.conversations.ConversationRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.conversations.ConversationResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.controllers.ApplicationController;
import com.socialnetwork.socialnetworkjavaspring.models.Conversation;
import com.socialnetwork.socialnetworkjavaspring.models.Participant;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.ConversationType;
import com.socialnetwork.socialnetworkjavaspring.models.key.ParticipantId;
import com.socialnetwork.socialnetworkjavaspring.services.conversations.IConversationService;
import com.socialnetwork.socialnetworkjavaspring.services.participants.IParticipantService;
import com.socialnetwork.socialnetworkjavaspring.services.sessions.SessionService;
import com.socialnetwork.socialnetworkjavaspring.services.users.IUserService;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import com.socialnetwork.socialnetworkjavaspring.utils.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/conversations")
public class ApisConversationController extends ApplicationController {

    @Autowired
    private IConversationService conversationService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IParticipantService participantService;

    @GetMapping
    public ResponseEntity<List<ConversationResponseDTO>> getAllConversation() {
        User userCurrent = sessionService.currentUser();
        List<Conversation> conversations = conversationService.getConversationJoinedByUserId(userCurrent.getUserId());
        return ResponseEntity.ok(ConvertUtils.convertList(conversations, ConversationResponseDTO.class));
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<ApiResponse> getConversationById(@PathVariable("conversationId") Long conversationId) {
        try{
            User userCurrent = sessionService.currentUser();
            return responseApi(HttpStatus.OK, "get conversation successfully!", conversationService.getConversationById(userCurrent, conversationId));
        }catch(Exception e){
            return responseApi(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/target/users")
    public ResponseEntity<ApiResponse> show(@RequestParam("id") String userId) {
        try {
            Conversation conversation = conversationService.findByPersonalTypeAndUserIdAndUserTargetId(currentUser.getUserId(), userId);
            ConversationResponseDTO conversationResponseDTO =
                    ConvertUtils.convert(conversation, ConversationResponseDTO.class);
            return responseApi(HttpStatus.OK, "Get conversation personal successfully!", conversationResponseDTO);
        } catch (Exception e) {
            return responseApi(HttpStatus.NOT_FOUND, "Conversation don't exist");
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@ModelAttribute ConversationRequestDTO requestDTO,
                                              @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            conversationService.createConversation(requestDTO, file, currentUser);
            return responseApi(HttpStatus.OK, "Create conversation group successfully!");
        } catch (Exception e) {
            return responseApi(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable("id") Long id, @ModelAttribute ConversationRequestDTO requestDTO,
                                              @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            conversationService.updateConversation(id, requestDTO, file, currentUser);
            return responseApi(HttpStatus.OK, "Create conversation group successfully!");
        } catch (Exception e) {
            return responseApi(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("{conversationId}")
    public ResponseEntity<ApiResponse> deleteConversation(@PathVariable Long conversationId) {
        try {
            conversationService.deleteConversation(conversationId, currentUser);
            return responseApi(HttpStatus.OK, "Delete conversation group successfully!");
        } catch (Exception e) {
            return responseApi(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Transactional
    @PostMapping("/target/users/{userTargetId}")
    public ResponseEntity<ApiResponse> create(@PathVariable String userTargetId) {
        try {
            Conversation conversation = handleSaveConversation().orElseThrow(
                    () -> new Exception("Create conversation personal failed!")
            );
            User userTarget = userService.findById(userTargetId);
            handleSaveParticipant(conversation, userTarget).orElseThrow(
                    () -> new Exception("Create participant failed!")
            );
            handleSaveParticipant(conversation, currentUser).orElseThrow(
                    () -> new Exception("Create participant failed!")
            );
            ConversationResponseDTO conversationResponseDTO =
                    ConvertUtils.convert(conversation, ConversationResponseDTO.class);
            return responseApi(HttpStatus.OK, "Create conversation personal successfully!", conversationResponseDTO);
        } catch (Exception e) {
            return responseApi(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private Optional<Conversation> handleSaveConversation() {
        Conversation conversation = new Conversation();
        conversation.setUser(currentUser);
        conversation.setType(ConversationType.PERSONAL);
        return conversationService.save(conversation);
    }

    private Optional<Participant> handleSaveParticipant(Conversation conversation, User userTarget) {
        Participant participant = new Participant();
        ParticipantId participantId = new ParticipantId(userTarget.getUserId(), conversation.getConversationId());
        participant.setParticipantId(participantId);
        participant.setNickname(userTarget.getFullName());
        participant.setConversation(conversation);
        participant.setUser(userTarget);
        return participantService.save(participant);
    }
}
