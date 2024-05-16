package com.socialnetwork.socialnetworkjavaspring.repositories;

import com.socialnetwork.socialnetworkjavaspring.models.Participant;
import com.socialnetwork.socialnetworkjavaspring.models.key.ParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IParticipantRepository extends JpaRepository<Participant, ParticipantId> {
    @Query(value = "SELECT * FROM participants p where p.user_id != :userId and" +
            " p.conversation_id = :conversationId", nativeQuery = true)
    Optional<Participant> findMemberConversationPersonal(String userId, Long conversationId);
}
