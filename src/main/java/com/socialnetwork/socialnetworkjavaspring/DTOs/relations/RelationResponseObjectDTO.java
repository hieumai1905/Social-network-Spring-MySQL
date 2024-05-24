package com.socialnetwork.socialnetworkjavaspring.DTOs.relations;

import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RelationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationResponseObjectDTO {
    private RelationType type;
    private Date setAt;
    private User user;
    private User userTarget;
    private Long mutualFriendCount;
}
