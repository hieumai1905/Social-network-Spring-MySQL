package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserLoginDTO;
import com.socialnetwork.socialnetworkjavaspring.services.sessions.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

@Controller
public class SessionController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private SessionService sessionService;

    @GetMapping("/login")
    public String loginForm() {
        return "sessions/new";
    }

    @PostMapping ("/login")
    public ModelAndView create(@ModelAttribute UserLoginDTO userLoginDTO, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDTO.getEmail(),
                        userLoginDTO.getPassword()
                )
        );
        sessionService.login(authentication);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/logout")
    public String destroy() {
        sessionService.logout();
        return "redirect:/login";
    }
}
