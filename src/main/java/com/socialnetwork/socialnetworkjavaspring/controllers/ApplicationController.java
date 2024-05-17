package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.services.sessions.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

@Component
public class ApplicationController {
    protected User currentUser;

    @Autowired
    private SessionService sessionService;

    @ModelAttribute
    public void getCurrentUser() {
        this.currentUser = sessionService.currentUser();
    }
}
