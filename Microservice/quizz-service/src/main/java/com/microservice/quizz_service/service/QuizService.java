package com.microservice.quizz_service.service;


import java.util.ArrayList;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.microservice.quizz_service.dao.QuizDao;
import com.microservice.quizz_service.feign.QuizInterface;
import com.microservice.quizz_service.model.QuestionWrapper;

import com.microservice.quizz_service.model.Quiz;
import com.microservice.quizz_service.model.Response;




@Service
public class QuizService {
	@Autowired
	QuizDao quizDao;
	
 @Autowired
 QuizInterface quizInterface;

	public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Integer> questions=quizInterface.getQuestionsForQuiz(category, numQ).getBody();
        Quiz quiz=new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizDao.save(quiz);
        
		return new ResponseEntity<>("success" ,HttpStatus.CREATED);
	}

	public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
		Optional<Quiz> quizOpt=quizDao.findById(id);
		if(quizOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Quiz quiz=quizOpt.get();
		List<Integer> questionIds=quiz.getQuestionIds();
		
			ResponseEntity<List<QuestionWrapper>> questions=quizInterface.getQuestionsFromId(questionIds);
		
		
		
		return questions;

	}

	public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
		
		ResponseEntity<Integer> score =quizInterface.getScore(responses);	
		return score;
	}

}

