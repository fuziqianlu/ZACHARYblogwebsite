package com.tianmaying.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.tianmaying.model.User;
import com.tianmaying.service.BlogService;
import com.tianmaying.service.UserService;

@Controller
public class AdminController {
	
	@Autowired
	private UserService us;
	@Autowired
	private BlogService bs;
	
	//show manipulate page of current user
	@GetMapping("/admin")
	public String admin(@PageableDefault(value=10, sort={"id"}, direction=Sort.Direction.DESC) Pageable pageable,
			Model model, @ModelAttribute("message") String message, HttpSession session){
		User user=(User) session.getAttribute("CURRENT_USER");
		model.addAttribute("blogs", bs.findBlogs(user, pageable));
		model.addAttribute("user", user);
		
		return "admin";
	}
}
