package com.socialnetwork.socialnetworkjavaspring.repositories;

import com.socialnetwork.socialnetworkjavaspring.models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IConversationRepository extends JpaRepository<Conversation, Long> {

    @Query(value = "SELECT c.* " +
            "FROM conversations c " +
            "INNER JOIN participants p ON c.conversation_id = p.conversation_id " +
            "LEFT JOIN (SELECT conversation_id, MAX(send_at) AS latest_message_time " +
            "           FROM messages " +
            "           GROUP BY conversation_id) m ON c.conversation_id = m.conversation_id " +
            "WHERE p.user_id = :userId AND p.status = :status " +
            "AND (p.delete_at is null or p.delete_at < m.latest_message_time)" +
            "ORDER BY COALESCE(m.latest_message_time, c.created_at) DESC", nativeQuery = true)
    List<Conversation> findAllByUserId_AndStatusOrderByLatestMessageTime(String userId, String status);

    @Query(value = "SELECT c.* " +
            "FROM conversations c " +
            "INNER JOIN participants p1 ON c.conversation_id = p1.conversation_id AND p1.user_id = :userId1 " +
            "INNER JOIN participants p2 ON c.conversation_id = p2.conversation_id AND p2.user_id = :userId2 " +
            "WHERE c.type = 'PERSONAL' " +
            "GROUP BY c.conversation_id " +
            "HAVING COUNT(DISTINCT p1.user_id) = 1 AND COUNT(DISTINCT p2.user_id) = 1", nativeQuery = true)
    Optional<Conversation> getPersonalConversation(@Param("userId1") String userId1, @Param("userId2") String userId2);
}
