package com.socialnetwork.socialnetworkjavaspring.repositories;

import com.socialnetwork.socialnetworkjavaspring.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findByEmailRequest(String email);

    @Query(value = "DELETE FROM requests WHERE TIMESTAMPDIFF(SECOND, requests.request_at, NOW()) > 60", nativeQuery = true)
    @Modifying
    void deleteAllByExpiredRequestCode();
}
