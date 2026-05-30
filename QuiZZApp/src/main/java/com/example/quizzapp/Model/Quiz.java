package com.example.quizzapp.Model;

import java.security.Identity;

import java.util.List;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;


@Entity
@Data

public class Quiz {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
  
	private Integer id ;
	private String  title;
	@ManyToMany
	private List <Questions> questions;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Questions> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Questions> quetsions) {
		this.questions = quetsions;
	}

	
}
