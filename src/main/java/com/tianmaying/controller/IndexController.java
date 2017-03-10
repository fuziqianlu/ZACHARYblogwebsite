package com.tianmaying.controller;

import com.tianmaying.model.User;
import com.tianmaying.service.BlogService;
import com.tianmaying.service.UserService;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    private final BlogService blogService;
    @Autowired UserService us;

    @Autowired
    public IndexController(BlogService blogService) {
        this.blogService = blogService;
    }
    
    
    //get the personal main page by username
    @GetMapping("/{username}")
    public String getByPage(@PathVariable("username") String username,
                                @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                @PageableDefault(value=15, sort={"id"}, direction=Sort.Direction.DESC) Pageable pageable,
                                Model model) {
        
    	User user=us.findByName(username);
    	model.addAttribute("blogs", blogService.findBlogs(user, pageable));
    	model.addAttribute("user", user);
        return "list";
    }
    
    
    //show the main page of the website
    @GetMapping("/about")
    public String about(HttpSession session, Model model) {
    	if(session.getAttribute("CURRENT_USER")==null){
    		model.addAttribute("user", null);
    	}
    	else{
    		model.addAttribute("user", session.getAttribute("CURRENT_USER"));
    	}
        return "about";
    }

}
