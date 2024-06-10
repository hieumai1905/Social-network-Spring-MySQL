package com.socialnetwork.socialnetworkjavaspring.DTOs.notifications;


import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserGenderResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDTO {
    private Long notificationId;
    private String content;
    private String type;
    private Boolean seen;
    private String image;
    private String urlRedirect;
    private String notificationAt;
    private UserGenderResponseDTO user;
    private UserGenderResponseDTO userTarget;
}
