package com.socialnetwork.socialnetworkjavaspring.repositories;

import com.socialnetwork.socialnetworkjavaspring.models.Relation;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RelationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRelationRepository extends JpaRepository<Relation, Long> {
    @Query(value = "SELECT * FROM relations WHERE user_id = :userId AND user_target_id = :userTargetId", nativeQuery = true)
    List<Relation> findByUserUserIdAndAndUserTarget_UserId(String userId, String userTargetId);

    @Query(value = "DELETE FROM relations WHERE user_id = :userId AND user_target_id = :userTargetId", nativeQuery = true)
    @Modifying
    void removeAllByUser_UserIdAndAndUserTarget_UserId(String userId, String userTargetId);

    @Query(value = "SELECT * FROM relations WHERE user_id = :userId AND user_target_id = :userTargetId AND type = :type LIMIT 1", nativeQuery = true)
    Optional<Relation> findByUserIdAndUserTargetIdAndType(String userId, String userTargetId, String type);

    List<Relation> findAllByUserTarget_UserIdAndTypeOrderBySetAtDesc(String userTargetId, RelationType type);

    @Query(value = "SELECT COUNT(DISTINCT r1.user_target_id) " +
            "FROM relations r1 " +
            "JOIN relations r2 ON r1.user_target_id = r2.user_target_id " +
            "WHERE (r1.user_id = :userId AND r2.user_id = :userTargetId) " +
            "OR (r1.user_id = :userTargetId AND r2.user_id = :userId) " +
            "AND r1.type = 'FRIEND' AND r2.type = 'FRIEND'",
            nativeQuery = true)
    Long countMutualFriends(String userId, String userTargetId);
}
