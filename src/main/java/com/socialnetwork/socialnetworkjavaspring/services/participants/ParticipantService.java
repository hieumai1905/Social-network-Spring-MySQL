package com.socialnetwork.socialnetworkjavaspring.services.participants;

import com.socialnetwork.socialnetworkjavaspring.models.Participant;
import com.socialnetwork.socialnetworkjavaspring.repositories.IParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParticipantService implements IParticipantService {
    @Autowired
    private IParticipantRepository participantRepository;

    @Override
    public Optional<Participant> save(Participant object) {
        return Optional.of(participantRepository.save(object));
    }

    @Override
    public Optional<Participant> delete(Participant object) {
        return Optional.empty();
    }

    @Override
    public Optional<Participant> findMemberConversationPersonal(String userId, Long conversationId) {
        return participantRepository.findMemberConversationPersonal(userId, conversationId);
    }
}
