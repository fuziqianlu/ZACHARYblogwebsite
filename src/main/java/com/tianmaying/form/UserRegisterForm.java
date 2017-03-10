package com.tianmaying.form;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.tianmaying.model.User;

public class UserRegisterForm {
	@Email(regexp="^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")
	private String email;
	
	@Size(min=6, max=30)
	private String name;
	
	@Size(min=6, max=30)
	private String password;
	
	public void setEmail(String email){
		this.email=email;
	}
	
	public String getEmail(){
		return email;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		return name;
	}
	
	public void setPassword(String password){
		this.password=password;
	}
	
	public String getPassword(){
		return password;
	}
	
	public User toUser(){
		User user=new User();
		user.setEmail(email);
		user.setName(name);
		user.setPassword(password);
		return user;
	}
	
}
