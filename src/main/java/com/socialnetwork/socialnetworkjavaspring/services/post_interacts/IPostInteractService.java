package com.socialnetwork.socialnetworkjavaspring.services.post_interacts;

import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.InteractType;

public interface IPostInteractService {
    String updatePostInteract(InteractType interactType, String postId, User user);
}
