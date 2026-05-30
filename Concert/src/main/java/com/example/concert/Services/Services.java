package com.example.concert.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.concert.DTO.userdto;
import com.example.concert.Model.UserModel;
import com.example.concert.UserRepository.userRepository;

@Service
public class Services {
   
    @Autowired
    private PasswordEncoder passwordEncoder;
   
    @Autowired
    private userRepository userRepository;

    public UserModel save(userdto userDto) {
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        // Determine role based on user count
        String role = userRepository.count() == 0 ? "ROLE_ADMIN" : "ROLE_USER";
        
        UserModel user = new UserModel(
            userDto.getEmail(),
            encodedPassword,
            userDto.getUsername(),
            userDto.getPhonenumber(),
            role // pass role to constructor
        );
        System.out.println("Saving user...");
        return userRepository.save(user);
        
      

    }
}