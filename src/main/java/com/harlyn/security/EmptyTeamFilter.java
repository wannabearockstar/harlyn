package com.harlyn.security;

import com.harlyn.domain.User;
import com.harlyn.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wannabe on 29.11.15.
 */
public class EmptyTeamFilter implements Filter {
    private final UserService userService;
    private final Set<String> allowedUrls;
    private final Set<String> internalUrls;

    public EmptyTeamFilter(UserService userService, Set<String> allowedUrls) {
        this.userService = userService;
        this.allowedUrls = allowedUrls;
        internalUrls = new HashSet<>(Arrays.asList(
                "/team",
                "/users",
                "/admin",
                "/static"
        ));
    }


    private void handleEmptyTeam(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/users/me");
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object userInMemory = authentication.getPrincipal();
        if (userInMemory == null || !(userInMemory instanceof User)) {
            return null;
        }
        return userService.getById(((User) userInMemory).getId());
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!isFilterResponsible((HttpServletRequest) request)) {
            chain.doFilter(request, response);
        } else {
            User user = getCurrentUser();
            if (user == null || user.getTeam() != null) {
                chain.doFilter(request, response);
            } else {
                handleEmptyTeam(request, response);
            }
        }
    }

    private void handleEmptyTeam(ServletRequest request, ServletResponse response) throws IOException {
        ((HttpServletResponse) response).sendRedirect("/users/me");
    }

    @Override
    public void destroy() {

    }

    private boolean isFilterResponsible(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (allowedUrls.contains(uri)) {
            return false;
        }
        return internalUrls.stream()
                .noneMatch(s -> uri.startsWith(s));
    }
}
