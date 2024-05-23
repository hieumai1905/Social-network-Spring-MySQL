package com.socialnetwork.socialnetworkjavaspring.services.post_interacts;

import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.models.PostInteract;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.InteractType;
import com.socialnetwork.socialnetworkjavaspring.repositories.IPostInteractRepository;
import com.socialnetwork.socialnetworkjavaspring.repositories.IPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PostInteractService implements IPostInteractService {

    @Autowired
    private IPostInteractRepository postInteractRepository;
    @Autowired
    private IPostRepository postRepository;

    @Override
    public String updatePostInteract(InteractType interactType, String postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("Post not found!"));
        Boolean isNeedUpdate = checkExistPostInteract(post, interactType);
        if(isNeedUpdate){
            PostInteract postInteract = new PostInteract(post, user, interactType, new Date());
            postInteractRepository.save(postInteract);
            return interactType == InteractType.SAVED ? "Save post successfully!" : "Hide post successfully!";
        }
        return interactType == InteractType.SAVED ? "Post is already saved!" : "Post is already hidden!";
    }

    private Boolean checkExistPostInteract(Post post, InteractType interactType) {
        if(post.getPostInteracts() != null && post.getPostInteracts().size() > 0){
            for (PostInteract postInteract : post.getPostInteracts()) {
                if(postInteract.getType().equals(interactType))
                    return false;
            }
        }
        return true;
    }
}
