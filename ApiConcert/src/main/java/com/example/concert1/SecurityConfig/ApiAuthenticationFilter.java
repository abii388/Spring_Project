package com.example.concert1.SecurityConfig;



import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.concert1.Model.UserModel;
import com.example.concert1.UserRepository.userRepository;
import com.example.concert1.Services.CustomUserDetail;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private userRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // Check if the Authorization header is present and starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Extract the token
            if (tokenGenerator.validateToken(token)) {
                // Fetch the user associated with the token
                UserModel user = userRepository.findByToken(token);
                System.out.println("Authenticated user: " + user.getUserName());
                System.out.println("Role: " + user.getRole());
                System.out.println(token);
                if (user != null) {
                    // Load UserDetails from CustomUserDetailsService
                    CustomUserDetail userDetails = new CustomUserDetail(user);
                    
                    // Set authentication in SecurityContext
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    System.out.println("userdetailed user: " + userDetails.getUsername());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                } else {
//                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    response.getWriter().write("User not found for the token");
//                    return;
//                }
                }
                else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid or expired token");
                    return;
                } 
                } 
            } else if (request.getRequestURI().startsWith("/api/") 
                && !request.getRequestURI().equals("/api/register") 
                && !request.getRequestURI().equals("/api/login")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token required");
            return;
             }
         

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}