package com.tianmaying.controller;

import com.tianmaying.exception.BlogNotFoundException;
import com.tianmaying.model.Blog;
import com.tianmaying.model.Comment;
import com.tianmaying.model.User;
import com.tianmaying.service.BlogService;
import com.tianmaying.service.CommentService;
import com.tianmaying.service.UserService;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/blogs/{id}")
public class BlogController {

    private final BlogService blogService;
    
    @Autowired
    private CommentService cs;
    
    @Autowired
    private UserService us;

    @Autowired
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }
    
    //get blog by id, display blog content page
    @GetMapping
    public String get(@PathVariable("id") long id, Model model) {
    	Blog blog=blogService.findBlog(id);
    	if(blog==null){
    		throw new BlogNotFoundException(null);
    	}
    	User user=blog.getAuthor();
    	model.addAttribute("blog", blog);
    	model.addAttribute("user", user);
    	model.addAttribute("comments", cs.getCommentOfBlog(blog));
    	Comment comment=new Comment();
    	comment.setBlog(blog);
    	model.addAttribute("comment", comment);
        return "item";
    }
    
    //deal with post requests of comments
    @PostMapping("/comments")
    public String addComment(@PathVariable("id") long id, Comment comment, HttpSession session, 
    		@RequestParam("next") Optional<String> nextUri){
    	comment.setCreatedTime(new Date());
    	User user=(User) session.getAttribute("CURRENT_USER");
    	//User user=us.findByName("tianmaying");
    	comment.setCommentor(user);
    	Blog blog=blogService.findBlog(id);
    	comment.setBlog(blog);
    	cs.createComment(comment);
    	return "redirect:/blogs/"+id;
    }
}
