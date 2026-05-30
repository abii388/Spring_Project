package com.example.concert1.Services;

import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.concert1.Model.UserModel;
import com.example.concert1.UserRepository.userRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private userRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//    	If you want to use actual usernames (not emails), just rename your method
//    	userRepository.findByUserName(username);

        UserModel user = userRepository.findByEmail(username);
        System.out.printf("the user",user);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        
        return new CustomUserDetail(user);
    }
}