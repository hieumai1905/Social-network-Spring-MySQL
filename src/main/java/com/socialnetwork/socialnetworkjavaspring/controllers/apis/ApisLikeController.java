package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.controllers.ApplicationController;
import com.socialnetwork.socialnetworkjavaspring.models.Like;
import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.services.likes.ILikeService;
import com.socialnetwork.socialnetworkjavaspring.services.posts.IPostService;
import com.socialnetwork.socialnetworkjavaspring.utils.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/likes")
public class ApisLikeController extends ApplicationController {
    @Autowired
    private ILikeService likeService;

    @Autowired
    private IPostService postService;

    @PostMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse> likePost(@PathVariable("postId") String postId) {
        Optional<Like> likeExits = likeService.findByPostIdAndUserId(postId, currentUser.getUserId());
        if (likeExits.isPresent()) {
            likeService.delete(likeExits.get());
            return responseApi(HttpStatus.NO_CONTENT, "Unliked post successfully");
        }

        Optional<Post> post = postService.findById(postId);
        if (post.isEmpty()) {
            return responseApi(HttpStatus.NOT_FOUND, "Post not found");
        }

        Like like = new Like(currentUser, post.get());
        likeService.save(like);
        return responseApi(HttpStatus.CREATED, "Liked post successfully");
    }
}
