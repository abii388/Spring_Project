package com.example.concert.Services;

import java.util.Collection;



import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.concert.Model.UserModel;
public class CustomUserDetail implements UserDetails {


    private UserModel user;


    public CustomUserDetail(UserModel user) {
        this.user = user;
    }
  

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole()));
    }
    
    public String getRole() {
        return user.getRole();
    }
    
    public Integer getUserId() {
        return user.getId();
    }

    public String getUserName() {
        return user.getUserName();
    }


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
	return user.getEmail();
}
}