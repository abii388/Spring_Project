package com.example.concert.config;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
//  
        for (GrantedAuthority auth : authentication.getAuthorities()) {
          	System.out.println("Granted authority: " + auth.getAuthority());
            if ("ROLE_ADMIN".equals(auth.getAuthority())) {
            	
                response.sendRedirect("/adminhome");  
                return;
            }
        }
        response.sendRedirect("/userhome");  
    }
}
