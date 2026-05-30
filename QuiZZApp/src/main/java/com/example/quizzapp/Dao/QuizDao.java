package com.example.quizzapp.Dao;

import org.springframework.data.jpa.repository.query.Jpa21Utils;

import com.example.quizzapp.Model.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizDao extends JpaRepository<Quiz, Integer> {

}
