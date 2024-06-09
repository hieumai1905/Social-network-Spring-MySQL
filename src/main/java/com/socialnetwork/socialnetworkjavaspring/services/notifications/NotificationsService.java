package com.socialnetwork.socialnetworkjavaspring.services.notifications;

import com.socialnetwork.socialnetworkjavaspring.models.Notification;
import com.socialnetwork.socialnetworkjavaspring.repositories.INotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationsService implements INotificationService{

    @Autowired
    private INotificationRepository notificationRepository;
    @Override
    public List<Notification> findAllByUserId(String userId) {
        return notificationRepository.findAllByUserTarget_UserId(userId);
    }
}
