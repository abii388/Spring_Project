package com.microservice.quizz_service.dao;

import org.springframework.data.jpa.repository.query.Jpa21Utils;


import com.microservice.quizz_service.model.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizDao extends JpaRepository<Quiz, Integer> {

}