package com.socialnetwork.socialnetworkjavaspring.security.jwt;

import com.socialnetwork.socialnetworkjavaspring.services.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String COOKIE_NAME = "access_token";

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> jwt = getJwtFromRequest(request);
            jwt.flatMap(this::authenticateUser)
                    .or(() -> getJwtFromCookie(request).flatMap(this::authenticateUser))
                    .ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));
        } catch (Exception ex) {
            log.error("Failed to set user authentication", ex);
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return Optional.of(bearerToken.substring(BEARER_PREFIX.length()));
        }
        return Optional.empty();
    }

    private Optional<String> getJwtFromCookie(HttpServletRequest request) {
        return Arrays.stream(request.getCookies() != null ? request.getCookies() : new Cookie[0])
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    private Optional<UsernamePasswordAuthenticationToken> authenticateUser(String jwt) {
        if (tokenProvider.validateJwtToken(jwt)) {
            String userId = tokenProvider.getUserIdFromJWT(jwt);
            UserDetails userDetails = userService.loadUserById(userId);
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                return Optional.of(authentication);
            }
        }
        return Optional.empty();
    }
}
