package com.socialnetwork.socialnetworkjavaspring.services.notifications;

import com.socialnetwork.socialnetworkjavaspring.models.Notification;
import com.socialnetwork.socialnetworkjavaspring.repositories.INotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationsService implements INotificationService {

    @Autowired
    private INotificationRepository notificationRepository;

    @Override
    public List<Notification> findAllByUserId(String userId) {
        return notificationRepository.findAllByUserTarget_UserIdOrderByNotificationAtDesc(userId);
    }

    @Override
    public void save(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public Optional<Notification> findById(Long notificationId) {
        return notificationRepository.findById(notificationId);
    }

    @Override
    public Boolean isDuplicateNotificationOneHour(Notification notification) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime oneHourAgo = currentTime.minusHours(1);
        String userId = notification.getUser().getUserId();
        String userTargetId = notification.getUserTarget().getUserId();
        Date notificationAtStart = java.sql.Timestamp.valueOf(oneHourAgo);
        Date notificationAtEnd = java.sql.Timestamp.valueOf(currentTime);

        List<Notification> existingNotifications =
                notificationRepository.findByNotificationAtBetweenAndUser_UserIdAndUserTarget_UserId(
                        notificationAtStart, notificationAtEnd, userId, userTargetId);
        for (Notification existingNotification : existingNotifications) {
            if (existingNotification.getContent().equals(notification.getContent()) &&
                    existingNotification.getUrlRedirect().equals(notification.getUrlRedirect())) {
                return true;
            }
        }
        return false;
    }
}
