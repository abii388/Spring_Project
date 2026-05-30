package com.example.concert.UserRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.concert.Model.UserModel;

public interface userRepository extends JpaRepository<UserModel, Long> {
//    UserModel findByUserName(String username); if your using the username when loggin time use this
    UserModel findByEmail(String email);
  
    boolean existsByEmail(String email);
}
