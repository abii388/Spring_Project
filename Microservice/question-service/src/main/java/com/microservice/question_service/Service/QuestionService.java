package com.microservice.question_service.Service;

import java.util.ArrayList;

import java.util.List;

import org.aspectj.weaver.patterns.TypePatternQuestions.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


import com.microservice.question_service.Dao.QuestionDao;
import com.microservice.question_service.Model.QuestionWrapper;
import com.microservice.question_service.Model.Questions;
import com.microservice.question_service.Model.Response;



@Service
public class QuestionService {
     @Autowired
     QuestionDao questionDao;
	
	public ResponseEntity<List<Questions>> getAllQuestions() {
		  System.out.println("get all questions");
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

	//generate Questions
	public ResponseEntity<List<Integer>> getQuestionForQuiz(String categoryName, Integer numQuestions) {
		// TODO Auto-generated method stub
		
		List <Integer> questions=questionDao.findRandomQuestionByCategory(categoryName,numQuestions);
		// TODO Auto-generated method stub
		return new ResponseEntity<>(questions,HttpStatus.OK);
	}

	public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
		// TODO Auto-generated method stub
		List<QuestionWrapper> wrappers =new ArrayList<>();
		List<Questions> questions=new ArrayList<>();
		for(Integer id:questionIds) {
			questions.add(questionDao.findById(id).get());
		}
		for (Questions question:questions) {
			QuestionWrapper wrapper = new QuestionWrapper();
			wrapper.setId(question.getId());
			wrapper.setQuestion(question.getQuestion());
			wrapper.setOption1(question.getOption1());
			wrapper.setOption2(question.getOption2());
			wrapper.setOption3(question.getOption3());
			wrapper.setOption4(question.getOption4());
			wrappers.add(wrapper);
		}
		
	  return new ResponseEntity<>(wrappers,HttpStatus.OK);
	

}

	public ResponseEntity<Integer> getScore(List<Response> responses) {
		// TODO Auto-generated method stub
		
		int right=0;
		
		for(Response response: responses) {
			Questions questions = questionDao.findById(response.getId()).get()
;			if(response.getResponse().equals(questions.getAnswer())) {
				right++;
			}
			
				
	
		}
		return new ResponseEntity<>(right,HttpStatus.OK);
	}
	
}	
