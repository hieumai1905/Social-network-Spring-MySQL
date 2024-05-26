package com.socialnetwork.socialnetworkjavaspring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static com.socialnetwork.socialnetworkjavaspring.utils.Constants.*;

@Controller
@RequestMapping("/settings")
public class SettingsController extends ApplicationController {

    @GetMapping
    public ModelAndView index(@RequestParam(value = "tab", required = false) String tab) {
        ModelAndView modelAndView = new ModelAndView("settings/index");
        if(tab == null){
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
}
