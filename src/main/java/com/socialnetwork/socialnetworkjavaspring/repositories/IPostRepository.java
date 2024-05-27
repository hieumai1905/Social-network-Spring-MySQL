package com.socialnetwork.socialnetworkjavaspring.repositories;

import com.socialnetwork.socialnetworkjavaspring.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPostRepository extends JpaRepository<Post, String> {

    @Query(value = "SELECT p.* " +
            "FROM posts p " +
            "WHERE ( " +
            "        (p.user_id = :userId AND p.access IN ('PUBLIC', 'FRIEND')" +
            "            OR (p.user_id IN (SELECT r.user_target_id " +
            "                              FROM relations r " +
            "                              WHERE r.user_id = :userId " +
            "                                AND r.type = 'FRIEND') " +
            "                AND p.access IN ('PUBLIC', 'FRIEND')))" +
            "        OR p.post_id IN (SELECT ut.post_id " +
            "                         FROM user_tags ut " +
            "                         WHERE ut.user_id = :userId)" +
            "        OR (p.user_id IN (SELECT r.user_target_id " +
            "                          FROM relations r " +
            "                          WHERE r.user_id = :userId " +
            "                            AND r.type = 'FOLLOW') " +
            "        AND p.access = 'PUBLIC')" +
            "    ) " +
            "  AND NOT EXISTS (SELECT 1 " +
            "                  FROM post_interacts pi " +
            "                  WHERE pi.user_id = :userId " +
            "                    AND (pi.type = 'HIDDEN' OR pi.type = 'REPORT') " +
            "                    AND pi.post_id = p.post_id)" +
            " " +
            "UNION " +
            " " +
            "SELECT p.* " +
            "FROM posts p " +
            "WHERE p.post_id IN (SELECT s.post_id " +
            "                    FROM post_interacts s " +
            "                    WHERE s.user_id = :userId " +
            "                    AND s.type = 'SHARED' " +
            "                    )" +
            " " +
            "ORDER BY create_at DESC", nativeQuery = true)
    List<Post> findAllPostForNewsFeed(String userId);

    @Query(value = "SELECT p.* " +
            "FROM posts p " +
            "WHERE p.post_id IN (SELECT pi.post_id " +
            "                    FROM post_interacts pi " +
            "                    WHERE pi.type = :interactType " +
            "                    AND pi.user_id = :userId)", nativeQuery = true)
    List<Post> findPostByInteractType(String interactType, String userId);

    @Query(value = "SELECT p.* " +
            "FROM posts p " +
            "WHERE (p.user_id = :userId OR p.post_id IN " +
            "(SELECT ut.post_id FROM user_tags ut WHERE ut.user_id = :userId)) " +
            "  AND NOT EXISTS (SELECT 1 " +
            "                  FROM post_interacts pi " +
            "                  WHERE pi.user_id = :userId " +
            "                    AND (pi.type = 'HIDDEN') " +
            "                    AND pi.post_id = p.post_id) " +
            "UNION " +
            "SELECT p.* " +
            "FROM posts p " +
            "WHERE p.post_id IN (SELECT s.post_id " +
            "                    FROM post_interacts s " +
            "                    WHERE s.user_id = :userId " +
            "                    AND s.type = 'SHARED') " +
            "ORDER BY create_at DESC", nativeQuery = true)
    List<Post> findAllByUserCurrent(String userId);

    @Query(value = "SELECT p.* " +
            "FROM posts p " +
            "WHERE p.post_id = :postId " +
            "  AND NOT EXISTS (SELECT 1 " +
            "                  FROM relations r " +
            "                  WHERE r.user_id = :userId " +
            "                    AND r.user_target_id = p.user_id " +
            "                    AND r.type = 'BLOCK') " +
            "  AND NOT EXISTS (SELECT 1 " +
            "                  FROM post_interacts pi " +
            "                  WHERE pi.user_id = :userId " +
            "                    AND (pi.type = 'HIDDEN' OR pi.type = 'REPORT') " +
            "                    AND pi.post_id = p.post_id) " +
            "  AND (p.access = 'PUBLIC' " +
            "       OR (p.access = 'FRIEND' AND EXISTS (SELECT 1 " +
            "                                            FROM relations r " +
            "                                            WHERE r.user_id = :userId " +
            "                                              AND r.user_target_id = p.user_id " +
            "                                              AND r.type = 'FRIEND')))", nativeQuery = true)
    Optional<Post> findPostByIdAndNotHiddenOrReportedOrBlockedOrPrivate(@Param("postId") String postId, @Param("userId") String userId);

    @Query(value = "SELECT p.* " +
            "FROM posts p " +
            "WHERE (p.user_id = :userId OR p.post_id IN " +
            "(SELECT ut.post_id FROM user_tags ut WHERE ut.user_id = :userId)) " +
            "AND NOT EXISTS (SELECT 1 " +
            "               FROM post_interacts pi " +
            "               WHERE pi.user_id = :userId " +
            "               AND (pi.type = 'HIDDEN') " +
            "               AND pi.post_id = p.post_id) " +
            "AND (p.access = 'PUBLIC' OR p.access = 'FRIEND') " +
            "UNION " +
            "SELECT p.* " +
            "FROM posts p " +
            "WHERE p.post_id IN (SELECT s.post_id " +
            "                    FROM post_interacts s " +
            "                    WHERE s.user_id = :userId " +
            "                    AND s.type = 'SHARED') " +
            "ORDER BY create_at DESC", nativeQuery = true)
    List<Post> findAllByUserFriend(String userId);

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN p.postHashtags ph " +
            "LEFT JOIN ph.hashtag h " +
            "WHERE (:postContent IS NULL OR p.postContent LIKE CONCAT('%',:postContent,'%')) " +
            "AND (:#{#hashtags == null || #hashtags.isEmpty()} = true OR h.hashtag IN :hashtags) " +
            "AND p.user.userId NOT IN (SELECT r.userTarget.userId FROM Relation r " +
            "   WHERE r.user.userId = :userId AND r.type = 'BLOCK') " +
            "AND p.postId NOT IN (SELECT pi.post.postId FROM PostInteract pi " +
            "   WHERE pi.user.userId = :userId AND (pi.type = 'HIDDEN' OR pi.type = 'REPORT'))")
    Page<Post> findByContentAndHashtags(@Param("postContent") String postContent,
                                        @Param("hashtags") List<String> hashtags, @Param("userId") String userId,
                                        Pageable pageable);
}
