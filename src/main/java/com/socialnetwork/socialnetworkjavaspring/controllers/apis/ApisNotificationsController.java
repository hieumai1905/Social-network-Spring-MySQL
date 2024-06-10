package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.models.Notification;
import com.socialnetwork.socialnetworkjavaspring.services.notifications.INotificationService;
import com.socialnetwork.socialnetworkjavaspring.utils.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
public class ApisNotificationsController {

    @Autowired
    private INotificationService notificationService;

    @PutMapping("/{notificationId}/mask-as-seen")
    public ApiResponse maskAsSeen(@PathVariable("notificationId") Long notificationId) {
        Optional<Notification> notification = notificationService.findById(notificationId);
        if (notification.isPresent()) {
            notification.get().setSeen(true);
            notificationService.save(notification.get());
            return new ApiResponse(HttpStatus.NO_CONTENT.value(), "Notification marked as seen");
        }
        return new ApiResponse(HttpStatus.NOT_FOUND.value(), "Notification not found");
    }
}
