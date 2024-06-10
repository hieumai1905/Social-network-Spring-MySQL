package com.socialnetwork.socialnetworkjavaspring.services.notifications;

import com.socialnetwork.socialnetworkjavaspring.models.Notification;

import java.util.List;
import java.util.Optional;

public interface INotificationService {
    List<Notification> findAllByUserId(String userId);

    void save(Notification notification);

    Optional<Notification> findById(Long notificationId);

    Boolean isDuplicateNotificationOneHour(Notification notification);
}
