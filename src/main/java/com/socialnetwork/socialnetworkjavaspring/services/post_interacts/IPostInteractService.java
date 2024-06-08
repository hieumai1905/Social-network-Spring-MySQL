package com.socialnetwork.socialnetworkjavaspring.services.post_interacts;

import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.models.PostInteract;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.InteractType;

import java.util.List;

public interface IPostInteractService {
    String updatePostInteract(InteractType interactType, String postId, User user, String content);

    void deletePostInteract(InteractType interactType, String postId, User currentUser);
    PostInteract checkExistPostInteract(Post post, InteractType interactType, String userId);

    List<PostInteract> findAllPostInteractShareByUserId(String userId);
}
