package com.socialnetwork.socialnetworkjavaspring.repositories;

import com.socialnetwork.socialnetworkjavaspring.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m " +
            "JOIN m.conversation c " +
            "JOIN c.participants p " +
            "WHERE c.conversationId = :conversationId " +
            "AND p.user.userId = :userId " +
            "AND (p.deletedAt IS NULL OR m.sendAt > p.deletedAt) " +
            "ORDER BY m.sendAt")
    List<Message> findMessagesAfterDeletionForUser(
            @Param("conversationId") Long conversationId,
            @Param("userId") String userId
    );
}
