package com.example.concert1.SecurityConfig;



import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import com.example.concert1.Model.UserModel;
import com.example.concert1.UserRepository.userRepository;

@Service
public class TokenGenerator {
    @Autowired
    private userRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int TOKEN_LENGTH = 60;
    private static final SecureRandom RANDOM = new SecureRandom();

    // Method to generate a random alphanumeric string
    private String generateRandomString() {
        StringBuilder stringBuilder = new StringBuilder(TOKEN_LENGTH);

        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(randomIndex));
        }

        return stringBuilder.toString();
    }

    // Generate and save the token in the User model
    public String generateToken(String email, String password) {
        UserModel user = userRepository.findByEmail(email);
        
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            String token;
            do {
            	token = generateRandomString();
            } while (userRepository.existsByToken(token)); // Check if token exists in DB
            
            user.setToken(token);
            userRepository.save(user);
             return token;
        }
        return null;
    }

    // Validate the token
    public boolean validateToken(String token) {
        UserModel user = userRepository.findByToken(token);
        return user != null;
    }
}