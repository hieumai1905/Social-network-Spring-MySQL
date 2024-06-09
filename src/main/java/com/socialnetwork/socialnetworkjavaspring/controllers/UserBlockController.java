package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.services.users.IUserService;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/users-block")
public class UserBlockController extends ApplicationController {
    @Autowired
    private IUserService userService;

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/users/block");
        List<User> blockedUsers = userService.findAllUserBlocked(currentUser.getUserId());
        List<UserResponseDTO> userResponseDTOs = ConvertUtils.convertList(blockedUsers, UserResponseDTO.class);
        modelAndView.addObject("blockedUsers", userResponseDTOs);
        return setAuthor(modelAndView);
    }
}
