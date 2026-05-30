package com.example.concert1.config;

import jakarta.servlet.ServletException;




import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
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
//You configured .loginPage("/api/login") but didn’t supply a GET handler for that URL; 
//Spring Security expects the login page on GET and the credentials POST to the processing URL. 
//Without the GET handler, a GET to /api/login falls through to Spring expecting something else and triggers 
//HttpRequestMethodNotSupportedException because it's being treated as a submission endpoint that only accepts POST.