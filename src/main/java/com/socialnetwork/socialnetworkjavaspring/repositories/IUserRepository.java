package com.socialnetwork.socialnetworkjavaspring.repositories;

import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RelationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM users " +
            "WHERE CONVERT(LOWER(REPLACE(REPLACE(full_name, 'Đ', 'd'), 'Ă', 'a'))," +
            " CHAR) LIKE CONCAT('%', CONVERT(LOWER(REPLACE(REPLACE(:fullName, 'Đ', 'd'), 'Ă', 'a')), CHAR), '%') " +
            "AND user_id <> :userId AND user_role = 'ROLE_USER' " +
            "AND user_id NOT IN (SELECT user_target_id FROM relations WHERE type = 'BLOCK' AND user_id = :userId)"
            , nativeQuery = true)
    Page<User> findByFullNameLikeIgnoreCaseAndAccents(String fullName, String userId, Pageable pageable);

    @Query(value = "SELECT u.* FROM users u INNER JOIN relations r ON u.user_id = r.user_id WHERE " +
            "r.type = :type AND r.user_target_id = :userId", nativeQuery = true)
    List<User> findUsersByRelationType(String userId, String type);

    @Query(value = "SELECT u.* FROM users u INNER JOIN relations r ON u.user_id = r.user_target_id WHERE " +
            "r.type = 'BLOCK' AND r.user_id = :userId", nativeQuery = true)
    List<User> findBlockedUsers(String userId);
}
