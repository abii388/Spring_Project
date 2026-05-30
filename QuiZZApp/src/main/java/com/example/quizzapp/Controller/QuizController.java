package com.example.quizzapp.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quizzapp.Model.QuestionWrapper;
import com.example.quizzapp.Model.Questions;
import com.example.quizzapp.Model.Response;
import com.example.quizzapp.Service.QuizService;

@RestController
@RequestMapping("/quizz")
public class QuizController {
	@Autowired
	QuizService quizeService;
	@PostMapping("create")
	public ResponseEntity<String> create(@RequestParam String category ,@RequestParam int numQ,@RequestParam String title ){
		return quizeService.createQuiz(category,numQ,title);
		
	}
	@GetMapping("get/{id}")
	public ResponseEntity<List<QuestionWrapper>> getQuestions(@PathVariable Integer id){
		
		return quizeService.getQuizQuestions(id);
	}
	@PostMapping("submit/{id}")
	public ResponseEntity<Integer> submitQuiz(@PathVariable Integer id,@RequestBody List<Response> responses){
		return quizeService.calculateResult(id,responses);
	}
	

}
