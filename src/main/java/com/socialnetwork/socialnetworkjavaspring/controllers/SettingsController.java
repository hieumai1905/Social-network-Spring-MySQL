package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserPasswordUpdateDTO;
import com.socialnetwork.socialnetworkjavaspring.services.users.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import static com.socialnetwork.socialnetworkjavaspring.utils.Constants.*;

@Controller
@RequestMapping("/settings")
public class SettingsController extends ApplicationController {

    @Autowired
    private IUserService userService;

    @GetMapping
    public ModelAndView index(@RequestParam(value = "tab", required = false) String tab) {
        ModelAndView modelAndView = new ModelAndView("settings/index");
        if (tab == null) {
            tab = "";
        }
        switch (tab) {
            case TAB_ACCOUNT:
                modelAndView.addObject("account", true);
                break;
            case TAB_EMAIL:
                modelAndView.addObject("email", true);
                break;
            case TAB_PASSWORD:
                modelAndView.addObject("password", true);
                break;
            default:
                modelAndView.addObject("setting", true);
        }
        return setAuthor(modelAndView);
    }

    @PostMapping("/update-password")
    public ModelAndView updatePassword(@ModelAttribute UserPasswordUpdateDTO userPasswordUpdateDTO) {
        ModelAndView modelAndView = new ModelAndView("settings/index");
        modelAndView.addObject("password", true);
        if (!currentUser.getPassword().equals(userPasswordUpdateDTO.getNewPassword())) {
            modelAndView.addObject("error", "Current password is incorrect!");
        } else if (userPasswordUpdateDTO.getNewPassword().equals(userPasswordUpdateDTO.getConfirmPassword())) {
            modelAndView.addObject("error", "Old password and new password are the same!");
        } else {
            currentUser.setPassword(userPasswordUpdateDTO.getConfirmPassword());
            userService.save(currentUser);
            modelAndView.addObject("error", "Update password successfully!");
        }
        return setAuthor(modelAndView);
    }
}
