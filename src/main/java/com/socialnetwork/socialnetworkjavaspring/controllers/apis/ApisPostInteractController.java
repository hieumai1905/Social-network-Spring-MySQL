package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.DTOs.common.StringDTO;
import com.socialnetwork.socialnetworkjavaspring.controllers.ApplicationController;
import com.socialnetwork.socialnetworkjavaspring.models.enums.InteractType;
import com.socialnetwork.socialnetworkjavaspring.services.post_interacts.IPostInteractService;
import com.socialnetwork.socialnetworkjavaspring.utils.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{post-id}/interact/{interact-type}")
public class ApisPostInteractController extends ApplicationController {

    @Autowired
    private IPostInteractService postInteractService;

    @PostMapping
    public ResponseEntity<ApiResponse> interactPost(@PathVariable("interact-type") String type,
                                                    @PathVariable("post-id") String postId, @RequestBody StringDTO stringDTO){
        try {
            InteractType interactType = InteractType.valueOf(type.toUpperCase());
            return responseApi(HttpStatus.OK,
                    postInteractService.updatePostInteract(interactType, postId, currentUser, stringDTO.getContent()));
        }catch (IllegalArgumentException e) {
            return responseApi(HttpStatus.BAD_REQUEST, "Invalid interact type!");
        }catch (Exception e) {
            return responseApi(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> delete(@PathVariable("interact-type") String type,
                          @PathVariable("post-id") String postId){
        try {
            InteractType interactType = InteractType.valueOf(type.toUpperCase());
            postInteractService.deletePostInteract(interactType, postId, currentUser);
            String message = interactType.equals(InteractType.SAVED) ? "UnSave post successfully!"
                    : "UnHidden post successfully!";
            return responseApi(HttpStatus.NO_CONTENT, message);
        }catch (IllegalArgumentException e) {
            return responseApi(HttpStatus.BAD_REQUEST, "Invalid interact type!");
        }catch (Exception e) {
            return responseApi(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
