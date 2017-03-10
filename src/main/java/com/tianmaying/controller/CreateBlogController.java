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

import com.tianmaying.form.BlogCreateForm;
import com.tianmaying.model.Blog;
import com.tianmaying.model.User;
import com.tianmaying.service.BlogService;
import com.tianmaying.service.UserService;

@Controller
public class CreateBlogController {
	
	@Autowired
	BlogService bs;
	@Autowired
	UserService us;

	//render the create form page
    @GetMapping("/blogs/create")
    public String get(Model model, HttpSession session) {
    	User user=(User) session.getAttribute("CURRENT_USER");
    	model.addAttribute("blog", new Blog());
    	model.addAttribute("user", user);
        return "create";
    }
    
    //post the new blog to the database and redirected to the created blog
    @PostMapping("/blogs")
    public String post(@ModelAttribute("blog") @Valid BlogCreateForm form, BindingResult result, HttpSession session) {
    	if(result.hasErrors()){
    		return "create";
    	}
    	User user=(User) session.getAttribute("CURRENT_USER");
    	Blog blog=form.toBlog(user);
    	blog.setAuthor(user);
    	blog.setCreatedTime(new Date());
    	bs.createBlog(blog);
        return "redirect:/blogs/"+blog.getId();
    }

}
