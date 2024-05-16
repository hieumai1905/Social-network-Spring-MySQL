package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserParticipantDTO;
import com.socialnetwork.socialnetworkjavaspring.models.Conversation;
import com.socialnetwork.socialnetworkjavaspring.models.Participant;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.ConversationType;
import com.socialnetwork.socialnetworkjavaspring.services.conversations.IConversationService;
import com.socialnetwork.socialnetworkjavaspring.services.participants.IParticipantService;
import com.socialnetwork.socialnetworkjavaspring.services.sessions.SessionService;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    @Autowired
    private IParticipantService participantService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private IConversationService conversationService;

    @GetMapping("/{conversationId}/personal/users")
    public ResponseEntity<UserParticipantDTO> show(@PathVariable("conversationId") Long conversationId) {
        User userCurrent = sessionService.currentUser();
        Boolean isPersonal = conversationService.isType(conversationId, ConversationType.PERSONAL);
        if (isPersonal) {
            Optional<Participant> participant = participantService.findMemberConversationPersonal(
                    userCurrent.getUserId(), conversationId);
            if (participant.isPresent()) {
                UserParticipantDTO userParticipantDTO = ConvertUtils.convert(participant.get(), UserParticipantDTO.class);
                userParticipantDTO.setAvatar(participant.get().getUser().getAvatar());
                return ResponseEntity.ok(userParticipantDTO);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
