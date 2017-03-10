package com.tianmaying.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tianmaying.form.UserRegisterForm;
import com.tianmaying.model.Blog;
import com.tianmaying.model.User;
import com.tianmaying.service.BlogService;
import com.tianmaying.service.UserService;

@Controller
@RequestMapping("/register")
public class RegisterController {
	
	@Autowired
	UserService us;
	@Autowired
	BlogService bs;
	
	//show the register page
    @GetMapping
    public String get(Model model) {
    	model.addAttribute("user", new User());
        return "register";
    }
    
    //check the register info, if valid, redirect to the user manipulate page and create a sample blog
    @PostMapping
    public String post(@ModelAttribute("user") @Valid UserRegisterForm form, BindingResult result,
    		final RedirectAttributes redirectAttr, HttpSession session) {

    	if(result.hasErrors()){
    		return "register";
    	}
    	User user=form.toUser();
    	us.register(user);
    	Blog blog=new Blog();
    	blog.setContent("Your first blog!");
    	blog.setTitle("Blog template");
    	blog.setCreatedTime(new Date());
    	blog.setAuthor(user);
    	bs.createBlog(blog);
    	redirectAttr.addFlashAttribute("message", "success");
    	session.setAttribute("CURRENT_USER", user);
    	
        return "redirect:/admin";
    }

}
