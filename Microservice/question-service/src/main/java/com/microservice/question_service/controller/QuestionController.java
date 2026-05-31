package com.microservice.question_service.controller;


import java.util.List;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.question_service.Model.QuestionWrapper;
import com.microservice.question_service.Model.Questions;
import com.microservice.question_service.Model.Response;
import com.microservice.question_service.Service.QuestionService;
import org.springframework.beans.factory.annotation.Value;




@RestController
@RequestMapping("question")
public class QuestionController {
  @Autowired
  QuestionService questionService;
  @Value("${server.port}")
  private String port;
	
   @GetMapping("/allQuestions")
  public ResponseEntity< List<Questions>> getAllQuestion() {
     
	   return  questionService.getAllQuestions();
   }
   @GetMapping("/category/{category}")
   public ResponseEntity< List<Questions>>  getQuestionByListCategory(@PathVariable String category){
	   return questionService.getQuestionByCategory(category); 
   }
   @PostMapping("/add")
   public ResponseEntity<String> addQuestion(@RequestBody Questions question) {
	   
	   return questionService.addQuestion(question);
   }
   
   @GetMapping("/generate")
   public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam String categoryName,@RequestParam Integer numQuestions){
	   System.out.println(categoryName);
	   System.out.println(numQuestions);
	
	   return questionService.getQuestionForQuiz(categoryName,numQuestions);
   }
   
 @PostMapping("/getQuestions")
   public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionsIds) {
       //TODO: process POST request
	  System.out.println(port);

	   System.out.println("get all questionsss");
       return questionService.getQuestionsFromId(questionsIds);
   }
   
  @PostMapping("/getScore")
  public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses) {
      //TODO: process POST request
      
      return questionService.getScore(responses);
  }
}
