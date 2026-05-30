package com.example.concert1.Model;
import jakarta.persistence.Entity;




import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


	@Entity
	@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
	public class UserModel {
	    //@Id marks id as the primary key.
         //@GeneratedValue(strategy = GenerationType.AUTO) tells JPA (Java Persistence API) to automatically generate the value for
        //id when a new record is saved to the database
	    
	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Integer id;
	    private String username;
	    private String email;
	    private String password;
	    private Long phonenumber;
	    private String role;
	    private String token;

	    public UserModel() {
	        super();
	    }
//	    🔍 So why not include id in the constructor?
//	    		Because you don’t want to manually set the id when creating a new UserModel object. 
//	            You let the database generate it automatically.

	    public UserModel(String email, String password, String username ,Long phonenumber,String role) {
	        this.email = email;
	        this.password = password;
	        this.username = username;
	        this.phonenumber=phonenumber;
	        this.role=role;
	    }

	    // Getters and Setters
	    public Integer getId() {
	        return id;
	    }

	    public void setId(Integer id) {
	        this.id = id;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }

	   
	    public String getUserName() {
	        return username;
	    }

	    public void setUserName(String username) {
	        this.username = username;
	    }
	    
	    
	    public Long getPhoneNumber() {
	    	 return phonenumber;
	    }
	    
	    public void setPhoneNumber(Long phonenumber) {
	    	this.phonenumber=phonenumber;
	    }
	    
	    public String getRole() {
	    	return role;
	    }
	    
	    public void setRole(String role) {
	    	     this.role=role;
	    }
	    public String getToken() {
	    	return token;
	    }
	    public void setToken(String token) {
	    	this.token = token;
	    }
	    
	    
	}
	 

	
