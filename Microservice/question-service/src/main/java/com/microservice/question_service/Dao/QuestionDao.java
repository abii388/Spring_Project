package com.microservice.question_service.Dao;

import java.util.List;



import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.microservice.question_service.Model.Questions;



public interface QuestionDao extends JpaRepository<Questions, Integer>{

	List<Questions> findAll();
    List<Questions>findByCategory(String category);
    @Query(
    		  value = "SELECT q.id FROM questions q WHERE q.category = :categoryName ORDER BY RANDOM() LIMIT :numQuestions",
    		  nativeQuery = true
    		)    
    List<Integer> findRandomQuestionByCategory(@Param("categoryName") String categoryName, @Param("numQuestions")  int numQ);


}
