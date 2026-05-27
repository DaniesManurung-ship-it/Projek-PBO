package com.kldel.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.kldel.model.User;
import com.kldel.repository.UserRepository;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    public CustomAuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        
        HttpSession session = request.getSession();
        session.setAttribute("loggedUser", user);
        
        if (user != null && "ADMIN".equals(user.getRole())) {
            response.sendRedirect("/admin");
        } else {
            response.sendRedirect("/home");
        }
    }
}
