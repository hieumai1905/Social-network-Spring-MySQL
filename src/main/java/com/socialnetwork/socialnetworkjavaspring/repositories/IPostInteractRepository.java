package com.socialnetwork.socialnetworkjavaspring.repositories;

import com.socialnetwork.socialnetworkjavaspring.models.PostInteract;
import com.socialnetwork.socialnetworkjavaspring.models.enums.InteractType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPostInteractRepository  extends JpaRepository<PostInteract, Long> {

    @Query(value = "SELECT * FROM post_interacts pi WHERE pi.type = :type " +
            "AND pi.user_id = :userId AND pi.post_id = :postId",
            nativeQuery = true)
    Optional<PostInteract> findPostInteractByTypeAndPostIdAndUserId(@Param("type") String type,
                                                                    @Param("postId") String postId,
                                                                    @Param("userId") String userId);
    @Query("SELECT pi FROM PostInteract pi WHERE pi.type = :type")
    List<PostInteract> findAllByType(InteractType type);

    @Query("SELECT pi "
            + "FROM PostInteract pi "
            + "WHERE pi.type = 'SHARED' "
            + "AND (pi.user.userId = :userId "
            + "     OR pi.user.userId IN (SELECT r.userTarget.userId FROM Relation r WHERE r.user.userId = :userId AND r.type = 'FOLLOW')) "
            + "AND NOT EXISTS ("
            + "    SELECT 1 "
            + "    FROM Relation r "
            + "    WHERE (r.user.userId = :userId AND r.userTarget.userId = pi.user.userId AND r.type = 'BLOCK') "
            + "       OR (r.user.userId = pi.user.userId AND r.userTarget.userId = :userId AND r.type = 'BLOCK')"
            + ") "
            + "ORDER BY pi.interactAt DESC")
    List<PostInteract> findAllSharedPostInteractsForUserAndFollowing(String userId);

    @Query("SELECT pi "
            + "FROM PostInteract pi "
            + "WHERE pi.type = 'SHARED' "
            + "AND pi.user.userId = :userId "
            + "AND NOT EXISTS ("
            + "    SELECT 1 "
            + "    FROM Relation r "
            + "    WHERE (r.user.userId = :userId AND r.userTarget.userId = pi.user.userId AND r.type = 'BLOCK') "
            + "       OR (r.user.userId = pi.user.userId AND r.userTarget.userId = :userId AND r.type = 'BLOCK')"
            + ") "
            + "ORDER BY pi.interactAt DESC")
    List<PostInteract> findAllSharedPostInteractsForUser(String userId);
}
