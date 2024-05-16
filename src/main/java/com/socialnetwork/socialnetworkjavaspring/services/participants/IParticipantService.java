package com.socialnetwork.socialnetworkjavaspring.services.participants;


import com.socialnetwork.socialnetworkjavaspring.models.Participant;
import com.socialnetwork.socialnetworkjavaspring.services.IGeneralService;

import java.util.Optional;

public interface IParticipantService extends IGeneralService<Participant, Long> {
    Optional<Participant> findMemberConversationPersonal(String userId, Long conversationId);
}
