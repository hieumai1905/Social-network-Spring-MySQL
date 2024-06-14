package com.socialnetwork.socialnetworkjavaspring.controllers;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserLoginDTO;
import com.socialnetwork.socialnetworkjavaspring.models.Request;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RequestType;
import com.socialnetwork.socialnetworkjavaspring.models.enums.UserStatus;
import com.socialnetwork.socialnetworkjavaspring.services.requests.IRequestService;
import com.socialnetwork.socialnetworkjavaspring.services.sessions.SessionService;
import com.socialnetwork.socialnetworkjavaspring.services.users.IUserService;
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
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class SessionController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRequestService requestService;

    @Autowired
    private HttpSession session;

    @GetMapping("/login")
    public String loginForm() {
        return "sessions/new";
    }

    @PostMapping("/login")
    public ModelAndView create(@ModelAttribute UserLoginDTO userLoginDTO) {
        ModelAndView modelAndView = new ModelAndView("/sessions/new");
        try {
            User userLogin = userService.findUserByEmailAndPassword(userLoginDTO.getEmail(), userLoginDTO.getPassword());
            switch (userLogin.getStatus()) {
                case ACTIVE:
                    handleStatusActive(userLogin);
                    break;
                case LOCKED:
                    return new ModelAndView("pages/locked");
                case INACTIVE:
                    return handleStatusInactive(userLogin, modelAndView);
                default:
                    return new ModelAndView("errors/server-error");
            }
        } catch (Exception e) {
            modelAndView.addObject("error", "Username or password is incorrect");
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    private void handleStatusActive(User userLogin) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLogin.getEmail(),
                        userLogin.getPassword()
                )
        );
        sessionService.login(authentication);
    }

    private ModelAndView handleStatusInactive(User userLogin, ModelAndView modelAndView) {
        Optional<Request> request = requestService.findByEmailRequest(userLogin.getEmail());
        if (request.isPresent()) {
            request.get().setRequestType(RequestType.ACTIVATE);
        } else {
            request = Optional.of(new Request(userLogin.getEmail(), RequestType.ACTIVATE));
        }
        session.setAttribute("email", userLogin.getEmail());
        session.setAttribute(userLogin.getEmail(), UserStatus.INACTIVE);
        Optional<Request> requestAdded = requestService.save(request.get());
        if(requestAdded.isEmpty()){
            return new ModelAndView("errors/server-error");
        }
        boolean sendCodeSuccess = requestService.sendCodeToEmail(userLogin.getEmail(), "CONFIRM REGISTER", request.get().getRequestCode());
        if (!sendCodeSuccess) {
            return new ModelAndView("errors/server-error");
        }
        modelAndView.setViewName("accounts/activate");
        modelAndView.addObject("title", "Activate account");
        modelAndView.addObject("email", userLogin.getEmail());
        modelAndView.addObject("urlTarget", "/activate");
        return modelAndView;
    }

    @GetMapping("/logout")
    public String destroy(HttpServletResponse response) {
        sessionService.logout();
        return "redirect:/login";
    }
}
