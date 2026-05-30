package com.example.concert1.UserRepository;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.concert1.Model.UserModel;

public interface userRepository extends JpaRepository<UserModel, Long> {
    
    UserModel findByEmail(String email);
    UserModel findByToken(String token);
    boolean existsByEmail(String email);
    boolean existsByToken(String token);
}
