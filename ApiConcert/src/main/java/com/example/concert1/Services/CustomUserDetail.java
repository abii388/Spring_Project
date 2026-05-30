package com.example.concert1.Services;

import java.util.Collection;






import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.concert1.Model.UserModel;

public class CustomUserDetail implements UserDetails {


    private UserModel user;


    public CustomUserDetail(UserModel user) {
        this.user = user;
    }
  

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	  System.out.println("Authenticated user: " + user.getUserName());
          System.out.println("Role: " + user.getRole());
        return List.of(new SimpleGrantedAuthority(user.getRole()));
    }
    
    public String getRole() {
        return user.getRole();
    }
    
    public Integer getUserId() {
        return user.getId();
    }

//    public String getUserName() {
//        return user.getUserName();
//    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }


    public String getUserEmail() {
       
        return user.getEmail();
    }


    @Override
    public boolean isAccountNonExpired() {
   
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
   
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
   
        return true;
    }


   @Override
   public boolean isEnabled() {
	   return true;
   }


@Override
public String getUsername() {
	// TODO Auto-generated method stub
	 return user.getUserName();
}
//=======================import======================================

//This method is used internally by Spring Security to:
//
//Retrieve the logged-in user's username.
//
//Match the username during authentication.
//
//Track the authenticated user in the session.

//
//Are getRole() and getUserEmail() required by Spring Security?
//❌ No — they are not required by Spring Security.
//These are custom helper methods that you created to:
//
//Easily access additional user details (like role or email) from your UserModel.
//
//Use them in your own application code (e.g., in a controller or service) — not by Spring Security itself
}