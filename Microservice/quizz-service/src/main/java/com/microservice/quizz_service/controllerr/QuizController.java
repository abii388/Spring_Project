package com.microservice.quizz_service.controllerr;


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

import com.microservice.quizz_service.model.QuestionWrapper;
import com.microservice.quizz_service.model.QuizDto;
import com.microservice.quizz_service.model.Response;
import com.microservice.quizz_service.service.QuizService;



@RestController
@RequestMapping("quizz")
public class QuizController {
	@Autowired
	QuizService quizeService;
	@PostMapping("/create")
	public ResponseEntity<String> create(@RequestBody QuizDto quizDto ){
		return quizeService.createQuiz(quizDto.getCategoryName(),quizDto.getNumQuestions(),quizDto.getTitle());
		
	}
	@GetMapping("/get/{id}")
	public ResponseEntity<List<QuestionWrapper>> getQuestions(@PathVariable Integer id){
		
		return quizeService.getQuizQuestions(id);
	}
	@PostMapping("/submit/{id}")
	public ResponseEntity<Integer> submitQuiz(@PathVariable Integer id,@RequestBody List<Response> responses){
		return quizeService.calculateResult(id,responses);
	}
	

}
