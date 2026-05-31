package com.microservice.question_service.Model;

public class Response {
	private Integer id;
	 public Response(Integer id, String response) {
		super();
		this.id = id;
		this.response = response;
	}
	private String response;
	
	 public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}


}
