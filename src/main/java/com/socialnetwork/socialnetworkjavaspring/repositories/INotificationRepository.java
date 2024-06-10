package com.socialnetwork.socialnetworkjavaspring.repositories;

import com.socialnetwork.socialnetworkjavaspring.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface INotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserTarget_UserIdOrderByNotificationAtDesc(String userId);

    List<Notification> findByNotificationAtBetweenAndUser_UserIdAndUserTarget_UserId(
            Date notificationAtStart, Date notificationAtEnd, String userId, String userTargetId);
}
