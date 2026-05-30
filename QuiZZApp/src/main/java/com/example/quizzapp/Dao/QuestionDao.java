package com.example.quizzapp.Dao;

import java.util.List;


import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quizzapp.Model.Questions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface QuestionDao extends JpaRepository<Questions, Integer>{

	List<Questions> findAll();
    List<Questions>findByCategory(String category);
    @Query(
    		  value = "SELECT * FROM questions WHERE category = :category ORDER BY RAND()  ",
    		  nativeQuery = true
    		)    
    List<Questions> findRandomQuestionByCategory(@Param("category") String category, @Param("numQ")  int numQ);


}
