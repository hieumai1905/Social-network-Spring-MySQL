package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/users")
public class UsersController extends ApplicationController{

    @Autowired
    private IUserRepository userRepository;

    @GetMapping(value = {"", "/index"})
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("admin/users");
        modelAndView.addObject("users", userRepository.findAll());
        return setAuthor(modelAndView);
    }
}
