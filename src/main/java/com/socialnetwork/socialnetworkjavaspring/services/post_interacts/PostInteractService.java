package com.socialnetwork.socialnetworkjavaspring.services.post_interacts;

import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.models.PostInteract;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.InteractType;
import com.socialnetwork.socialnetworkjavaspring.repositories.IPostInteractRepository;
import com.socialnetwork.socialnetworkjavaspring.repositories.IPostRepository;
import com.socialnetwork.socialnetworkjavaspring.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostInteractService implements IPostInteractService {

    @Autowired
    private IPostInteractRepository postInteractRepository;
    @Autowired
    private IPostRepository postRepository;

    @Override
    public String updatePostInteract(InteractType interactType, String postId, User user, String content) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NullPointerException("Post not found!"));
        PostInteract existedPostInteract = checkExistPostInteract(post, interactType, user.getUserId());
        if(existedPostInteract == null || interactType.equals(InteractType.SHARED)
                || interactType.equals(InteractType.REPORT)){
            PostInteract postInteract = new PostInteract(post, user, interactType, new Date(), content);
            postInteractRepository.save(postInteract);
            return getInteractionMessage(interactType, false);
        }
        return getInteractionMessage(interactType, true);
    }

    private String getInteractionMessage(InteractType interactType, boolean isAlreadyDone) {
        if (isAlreadyDone) {
            if (interactType.equals(InteractType.SAVED))
                return "Post is already saved!";
            else if (interactType.equals(InteractType.HIDDEN))
                return "Post is already hidden!";
        } else {
            if (interactType.equals(InteractType.SAVED))
                return "Save post successfully!";
            else if (interactType.equals(InteractType.HIDDEN))
                return "Hide post successfully!";
            else if (interactType.equals(InteractType.SHARED))
                return "Share post successfully!";
            else if (interactType.equals(InteractType.REPORT))
                return "Report post successfully!";
        }
        return Constants.EMPTY_STRING;
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
        if (post.getPostInteracts() != null && post.getPostInteracts().size() > 0) {
            for (PostInteract postInteract : post.getPostInteracts()) {
                if (postInteract.getType().equals(interactType) && userId.equals(postInteract.getUser().getUserId()))
                    return postInteract;
            }
        }
        return null;
    }

    @Override
    public List<PostInteract> findAllPostInteractShareByUserId(String userId) {
        return postInteractRepository.findAllSharedPostInteractsForUserAndFollowing(userId);
    }

    @Override
    public List<PostInteract> findAllPostInteractSharedByUserIdProfile(String userId) {
        return postInteractRepository.findAllSharedPostInteractsForUser(userId);
    }
}
