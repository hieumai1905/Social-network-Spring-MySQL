package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.services.new_feeds.NewsFeedService;
import com.socialnetwork.socialnetworkjavaspring.services.sessions.SessionService;
import com.socialnetwork.socialnetworkjavaspring.utils.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ApplicationController {
    protected User currentUser;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private NewsFeedService newsFeedService;
    @ModelAttribute
    public void getCurrentUser() {
        this.currentUser = sessionService.currentUser();
    }

    @GetMapping(value = {"/", "/index"})
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("posts", newsFeedService.getNewsFeed(currentUser.getUserId()));
        return setAuthor(modelAndView);
    }

    protected ModelAndView setAuthor(ModelAndView modelAndView){
        modelAndView.addObject("currentUser", currentUser);
        return modelAndView;
    }

    public ResponseEntity<ApiResponse> responseApi(HttpStatus code, String message) {
        return ResponseEntity.ok(new ApiResponse(code.value(), message, null));
    }

    public ResponseEntity<ApiResponse> responseApi(HttpStatus code, String message, Object data) {
        return ResponseEntity.ok(new ApiResponse(code.value(), message, data));
    }
}