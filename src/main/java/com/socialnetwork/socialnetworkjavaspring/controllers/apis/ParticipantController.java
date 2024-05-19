package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserParticipantDTO;
import com.socialnetwork.socialnetworkjavaspring.controllers.ApplicationController;
import com.socialnetwork.socialnetworkjavaspring.models.Conversation;
import com.socialnetwork.socialnetworkjavaspring.models.Participant;
import com.socialnetwork.socialnetworkjavaspring.models.enums.ConversationType;
import com.socialnetwork.socialnetworkjavaspring.models.enums.ParticipantStatus;
import com.socialnetwork.socialnetworkjavaspring.services.conversations.IConversationService;
import com.socialnetwork.socialnetworkjavaspring.services.participants.IParticipantService;
import com.socialnetwork.socialnetworkjavaspring.utils.Constants;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import com.socialnetwork.socialnetworkjavaspring.utils.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController extends ApplicationController {

    @Autowired
    private IParticipantService participantService;

    @Autowired
    private IConversationService conversationService;

    @GetMapping("/{conversationId}/personal/users")
    public ResponseEntity<UserParticipantDTO> conversationPersonal(@PathVariable("conversationId") Long conversationId) {
        Boolean isPersonal = conversationService.isType(conversationId, ConversationType.PERSONAL);
        if (isPersonal) {
            Optional<Participant> participant = participantService.findMemberConversationPersonal(
                    currentUser.getUserId(), conversationId);
            if (participant.isPresent()) {
                UserParticipantDTO userParticipantDTO = ConvertUtils.convert(participant.get(), UserParticipantDTO.class);
                userParticipantDTO.setAvatar(participant.get().getUser().getAvatar());
                return ResponseEntity.ok(userParticipantDTO);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/conversations/{conversationId}/leave")
    public ResponseEntity<ApiResponse> leaveConversationGroup(@PathVariable("conversationId") Long conversationId) {
        Optional<Conversation> conversation = conversationService.findById(conversationId);
        if (conversation.isEmpty()) {
            return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK.value(), "Conversation not found"));
        }
        if (conversation.get().getType() != ConversationType.GROUP) {
            return ResponseEntity.ok().body(new ApiResponse(HttpStatus.BAD_REQUEST.value(), "Cannot leave personal conversation"));
        }
        List<Participant> participants = getParticipantsByStatus(conversation, ParticipantStatus.JOINED);
        Optional<Participant> currentUserParticipant = getParticipantByUserIdAndConversationId(participants, currentUser.getUserId());
        return ResponseEntity.ok().body(handleLeaveConversation(currentUserParticipant, participants, conversation));
    }

    private ApiResponse handleLeaveConversation(Optional<Participant> currentUserParticipant, List<Participant> participants, Optional<Conversation> conversation) {
        if (currentUserParticipant.isPresent()) {
            if (participants.size() == Constants.NUMBER_ONE) {
                conversationService.delete(conversation.get());
            } else {
                currentUserParticipant.get().setStatus(ParticipantStatus.LEAVED);
                participantService.save(currentUserParticipant.get());
            }
            return new ApiResponse(HttpStatus.OK.value(), "Leave conversation successfully");
        }
        return new ApiResponse(HttpStatus.NOT_FOUND.value(), "User don't join conversation");
    }

    private List<Participant> getParticipantsByStatus(Optional<Conversation> conversation, ParticipantStatus status) {
        return conversation.get().getParticipants().stream()
                .filter(participant -> participant.getStatus() == status)
                .collect(Collectors.toList());
    }

    private Optional<Participant> getParticipantByUserIdAndConversationId(List<Participant> participants, String userId) {
        return participants.stream()
                .filter(participant -> participant.getUser().getUserId().equals(userId))
                .findFirst();
    }
}
