package com.tianmaying.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tianmaying.model.User;
import com.tianmaying.service.UserService;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	UserService us;
	
	//show the login page
    @GetMapping
    public String get(@RequestParam("next") Optional<String> next) {
        return "login";
    }
    
    //login in Api
    @PostMapping
    public String post(@RequestParam(name="email") String email, @RequestParam(name="password") String password,
    		@RequestParam("next") Optional<String> next, HttpSession session) {
    	User user=us.login(email, password);
    	if(user==null){
    		return "login";
    	}
    	session.setAttribute("CURRENT_USER", user);
        return "redirect:".concat(next.orElse("/"+user.getName()));
    }
    
    //login out api
    @GetMapping("/out")
    public String loginout(HttpSession session, @RequestParam("next") Optional<String> next){
    	session.removeAttribute("CURRENT_USER");
    	return "redirect:".concat(next.orElse("/about"));
    }
}
