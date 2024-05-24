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
import java.util.Optional;

@Service
public class PostInteractService implements IPostInteractService {

    @Autowired
    private IPostInteractRepository postInteractRepository;
    @Autowired
    private IPostRepository postRepository;

    @Override
    public String updatePostInteract(InteractType interactType, String postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("Post not found!"));
        PostInteract existedPostInteract = checkExistPostInteract(post, interactType, user.getUserId());
        if(existedPostInteract == null){
            PostInteract postInteract = new PostInteract(post, user, interactType, new Date());
            postInteractRepository.save(postInteract);
            return interactType == InteractType.SAVED ? "Save post successfully!" : "Hide post successfully!";
        }
        return interactType == InteractType.SAVED ? "Post is already saved!" : "Post is already hidden!";
    }

    @Override
    public void deletePostInteract(InteractType interactType, String postId, User user) {
        String type = String.valueOf(interactType);
        Optional<PostInteract> postInteract = postInteractRepository.findPostInteractByTypeAndPostIdAndUserId(
                type, postId, user.getUserId());
        if(postInteract.isEmpty()){
            throw new NullPointerException("PostInteract is null");
        }
        postInteractRepository.delete(postInteract.get());
    }
    @Override
    public PostInteract checkExistPostInteract(Post post, InteractType interactType, String userId) {
        if(post.getPostInteracts() != null && post.getPostInteracts().size() > 0){
            for (PostInteract postInteract : post.getPostInteracts()) {
                if(postInteract.getType().equals(interactType) && userId.equals(postInteract.getUser().getUserId()))
                    return postInteract;
            }
        }
        return null;
    }
}
