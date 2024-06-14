package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.FriendResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.services.friends.IFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/friends")
public class FriendController extends ApplicationController {

    @Autowired
    private IFriendService friendService;

    @GetMapping
    public ModelAndView showFriends(@RequestParam(value = "user-id", required = false) String userId) {
        ModelAndView modelAndView = new ModelAndView("/friends/index");
        try {
            String userIdToProcess = userId != null ? userId : currentUser.getUserId();
            List<FriendResponseDTO> friendResponseDTOs = friendService.getUserFriends(userIdToProcess, currentUser);
            modelAndView.addObject("friends", friendResponseDTOs);
        } catch (Exception e) {
            return handleException(modelAndView);
        }
        return setAuthor(modelAndView);
    }

    private ModelAndView handleException(ModelAndView modelAndView) {
        modelAndView.setViewName("errors/server-error");
        return modelAndView;
    }
}
