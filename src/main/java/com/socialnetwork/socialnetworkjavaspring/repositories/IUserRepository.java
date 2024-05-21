package com.socialnetwork.socialnetworkjavaspring.repositories;

import com.socialnetwork.socialnetworkjavaspring.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM users " +
            "WHERE CONVERT(LOWER(REPLACE(REPLACE(full_name, 'Đ', 'd'), 'Ă', 'a'))," +
            " CHAR) LIKE CONCAT('%', CONVERT(LOWER(REPLACE(REPLACE(:fullName, 'Đ', 'd'), 'Ă', 'a')), CHAR), '%') " +
            "and user_id <> :userId and user_role = 'ROLE_USER'"
            , nativeQuery = true)
    Page<User> findByFullNameLikeIgnoreCaseAndAccents(String fullName, String userId, Pageable pageable);
}
