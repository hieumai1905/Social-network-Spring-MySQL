package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.DTOs.posts.PostRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.controllers.ApplicationController;
import com.socialnetwork.socialnetworkjavaspring.services.posts.IPostService;
import com.socialnetwork.socialnetworkjavaspring.utils.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class ApisPostController extends ApplicationController {
    @Autowired
    private IPostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse> create(@ModelAttribute PostRequestDTO request,
                                                  @RequestPart(value = "files", required = false) List<MultipartFile> files){
        try{
            return responseApi(HttpStatus.CREATED, "Create post successfully!",
                    postService.save(request, files, currentUser));
        }catch (Exception ex){
            return responseApi(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
