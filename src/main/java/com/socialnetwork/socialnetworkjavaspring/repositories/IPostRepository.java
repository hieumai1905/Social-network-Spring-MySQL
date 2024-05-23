package com.socialnetwork.socialnetworkjavaspring.repositories;

import com.socialnetwork.socialnetworkjavaspring.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
