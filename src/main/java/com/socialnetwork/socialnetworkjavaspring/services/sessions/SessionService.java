package com.socialnetwork.socialnetworkjavaspring.services.sessions;

import com.socialnetwork.socialnetworkjavaspring.models.CustomUserDetails;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Service
public class SessionService {
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private HttpServletRequest request;

    public void login(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        Cookie cookie = new Cookie("access_token", jwt);
        response.addCookie(cookie);
    }

    public User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getUser();
        }
        return null;
    }

    public void logout() {
        Cookie cookie = new Cookie("access_token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        HttpSession session = request.getSession();
        session.invalidate();
        SecurityContextHolder.clearContext();
    }
}
