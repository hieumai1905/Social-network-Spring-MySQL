package com.socialnetwork.socialnetworkjavaspring.services.notifications;

import com.socialnetwork.socialnetworkjavaspring.models.Notification;
import com.socialnetwork.socialnetworkjavaspring.services.IGeneralService;

import java.util.List;

public interface INotificationService
{
    List<Notification> findAllByUserId(String userId);
}
