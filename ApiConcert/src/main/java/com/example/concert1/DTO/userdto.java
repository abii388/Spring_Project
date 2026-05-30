package com.example.concert1.DTO;

public class userdto {

    private Long id;
    private String username;
    private String email;
    private String password;
    private Long phonenumber;
    private String role;
    private String confirmPassword;
    private String token;

    public userdto() {//This is just the default constructor. 
    	//It’s necessary for Spring to create a new instance of userdto 
    	//when rendering the form or when binding form data during a POST
    	super();
    }

    public userdto(String email, String password, String username, Long phonenumber, String role ,String confirmPassword,String token) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.phonenumber = phonenumber;
        this.role = role;
        this.confirmPassword=confirmPassword;
        this.token=token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {  // ✅ fixed
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getconfirmPassword() {
        return confirmPassword;
    }

    public void setconfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getPhonenumber() {  
        return phonenumber;
    }

    public void setPhonenumber(Long phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getToken() {
    	return token;
    }
    public void setToken(String token) {
    	this.token = token;
    }
}
