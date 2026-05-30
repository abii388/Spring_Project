package com.example.quizzapp.Service;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.quizzapp.Dao.QuestionDao;
import com.example.quizzapp.Dao.QuizDao;
import com.example.quizzapp.Model.QuestionWrapper;
import com.example.quizzapp.Model.Questions;
import com.example.quizzapp.Model.Quiz;
import com.example.quizzapp.Model.Response;



@Service
public class QuizService {
	@Autowired
	QuizDao quizDao;
	
	@Autowired
	QuestionDao questionDao;

	public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
		List <Questions> questions=questionDao.findRandomQuestionByCategory(category,numQ);
		// TODO Auto-generated method stub
		Quiz quiz=new Quiz();
		quiz.setTitle(title);
		quiz.setQuestions(questions);
		quizDao.save(quiz);
		
		return new ResponseEntity<>("success" ,HttpStatus.CREATED);
	}

	public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
		// TODO Auto-generated method stub
		Optional<Quiz> quiz=quizDao.findById(id);
		List<Questions> questionsFromDB=quiz.get().getQuestions();
		List<QuestionWrapper> questionsForUser=new ArrayList<>();
		for (Questions  q:questionsFromDB ) {
			QuestionWrapper qw=new QuestionWrapper(q.getId(), q.getQuestion(), q.getOption1(), q.getOption2(),q.getOption3(), q.getOption4());
		    questionsForUser.add(qw);
		}
		return  new ResponseEntity<>(questionsForUser ,HttpStatus.OK);
	}

	public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
		// TODO Auto-generated method stub
		Quiz quiz=quizDao.findById(id).get();
		List<Questions>questions =quiz.getQuestions();
		int right=0;
		int i=0;
		for(Response response: responses) {
			if(response.getResponse().equals(questions.get(i).getAnswer())) {
				right++;
			}
			
				
			i++;
		}
		return new ResponseEntity<>(right,HttpStatus.OK);
	}

}
