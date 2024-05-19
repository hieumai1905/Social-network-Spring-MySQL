package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.PostRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.PostResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.controllers.ApplicationController;
import com.socialnetwork.socialnetworkjavaspring.models.Post;
import com.socialnetwork.socialnetworkjavaspring.services.posts.IPostService;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController extends ApplicationController {

    @Autowired
    private IPostService postService;

    @PutMapping({"/{postId}"})
    @Transactional
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable String postId, @ModelAttribute PostRequestDTO postRequestDTO, @RequestParam("files") Optional<MultipartFile[]> files) {
        Optional<Post> post = postService.findById(postId);
        if (post.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        post.get().setPostContent(postRequestDTO.getPostContent());
        post.get().setAccess(postRequestDTO.getAccess());
        post.get().setPostType(postRequestDTO.getPostType());
        Optional<Post> postUpdate = postService.save(post.get());
        if (postUpdate.isPresent()) {
            boolean isDeletedMedia = mediaService.deleteAllByPost(postId);
            if (files.isPresent() && isDeletedMedia) {
                List<Map<String, String>> medias = mediaService.uploadFiles(files.get());
                for (Map<String, String> media : medias) {
                    Media mediaEntity = new Media();
                    mediaEntity.setPost(postUpdate.get());
                    mediaEntity.setUrl(media.get("url"));
                    String type = media.get("format");
                    int index = type.indexOf("/");
                    type = type.substring(0, index);
                    mediaEntity.setType(MediaType.valueOf(type));
                    mediaService.save(mediaEntity);
                }
            }
            PostResponseDTO postResponseDTO = ConvertUtils.convert(postUpdate.get(), PostResponseDTO.class);
            return ResponseEntity.ok(postResponseDTO);
        }
        return ResponseEntity.badRequest().build();
    }
}
