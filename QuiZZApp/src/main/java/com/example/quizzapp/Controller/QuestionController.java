package com.example.quizzapp.Controller;


import java.util.List;


import org.aspectj.weaver.patterns.TypePatternQuestions.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quizzapp.Model.Questions;
import com.example.quizzapp.Service.QuestionService;

@RestController
@RequestMapping("/quiz")
public class QuestionController {
  @Autowired
  QuestionService questionService;
	
   @GetMapping("/question")
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
   
}
