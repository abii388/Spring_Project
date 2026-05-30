package com.example.quizzapp.Service;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.weaver.patterns.TypePatternQuestions.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.quizzapp.Dao.QuestionDao;
import com.example.quizzapp.Model.Questions;

@Service
public class QuestionService {
     @Autowired
     QuestionDao questionDao;
	
	public ResponseEntity<List<Questions>> getAllQuestions() {
		// TODO Auto-generated method stub
		try {
			return new ResponseEntity<> ( questionDao.findAll(),HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<> (new ArrayList<>(),HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<List<Questions>> getQuestionByCategory(String category) {
		try {
			return new ResponseEntity<> (questionDao.findByCategory(category),HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<> (new ArrayList<>(),HttpStatus.BAD_REQUEST);
		// TODO Auto-generated method stub
		
	}

	public ResponseEntity<String> addQuestion(Questions question) {
		// TODO Auto-generated method stub
		questionDao.save(question);
		return new ResponseEntity<String>("success", HttpStatus.CREATED) ;
		
	}

}
