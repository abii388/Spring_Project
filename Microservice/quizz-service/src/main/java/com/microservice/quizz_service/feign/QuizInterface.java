package com.microservice.quizz_service.feign;

import java.util.List;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.microservice.quizz_service.model.QuestionWrapper;
import com.microservice.quizz_service.model.Response;

@FeignClient("QUESTION-SERVICE")
public interface QuizInterface {
	 @GetMapping("question/generate")
	   public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String categoryName,@RequestParam Integer numQuestions);
	   
	 @PostMapping("question/getQuestions")
	   public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionsIds) ;
	   
	  @PostMapping("question/getScore")
	  public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses);
}
