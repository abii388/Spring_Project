package com.example.concert.SecurityConfig;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.concert.Services.CustomUserDetailsService;
import com.example.concert.config.CustomSuccessHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
   
	 @Autowired
	    private CustomSuccessHandler customSuccessHandler;
    @Autowired
    CustomUserDetailsService customUserDetailsService;
   
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
   
   
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable())
           
            .authorizeHttpRequests(request -> request
                .requestMatchers("/register","/","/registration","/css/**", "/js/**").permitAll()
//                .requestMatchers("/admin/**").hasRole("ADMIN")  // Spring looks for ROLE_ADMIN
//                .requestMatchers("/user/**").hasRole("USER") 
               
                .anyRequest().authenticated())
            .formLogin(form -> form
                    .loginPage("/login")
                    .successHandler(customSuccessHandler)  // ✅ use custom handler
                    .permitAll()
                )
            .logout(form -> form
                .invalidateHttpSession(true).clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout"));
		        
           
        return http.build();
    }


    @Autowired
    public void configure (AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }     
}