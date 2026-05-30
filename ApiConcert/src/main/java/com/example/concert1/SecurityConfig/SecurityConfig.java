package com.example.concert1.SecurityConfig;

import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.concert1.Services.CustomUserDetailsService;


//When is Spring Security enabled?
//Spring Security is automatically enabled if you include the Spring Security dependency in your project — either directly or transitively.



@Configuration
@EnableWebSecurity
public class SecurityConfig {
   

    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired 
    ApiAuthenticationFilter  apiAuthenticationFilter;
   
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
   
   
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable())
           
            .authorizeHttpRequests(request -> request
            		  .requestMatchers("/api/register", "/api/login").permitAll() // allow both
            		  .requestMatchers( "/api/admin/createconcert",
            				  "/api/admin/viewconcert",
            				  "/api/admin/status/{id}",
            				  "/api/admin/edit/{id}",
            				  "/api/admin/update/{id}",
            				  "/api/admin/delete/{id}",
            				  "/api/admin/manageuser",
            				  "/api/admin/userview/{id}",
            				  "/api/admin/usereditform/{id}",
            				  "/api/admin/userupdate/{id}",
            				  "/api/admin/deleteuser/{id}"
            				  
            				  
            				  ).hasAuthority("ROLE_ADMIN")
            		 
                      .anyRequest().authenticated()
                  )
                  .formLogin(form -> form.disable())
                    // Form login settings
            .logout(logout -> logout.disable()
                   ); // Logout settings
    http.addFilterBefore(apiAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    	
    return http.build();
    }


    @Autowired
    public void configure (AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }      
}