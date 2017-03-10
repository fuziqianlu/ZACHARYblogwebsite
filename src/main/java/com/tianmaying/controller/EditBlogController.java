package com.tianmaying.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tianmaying.exception.RightLimitException;
import com.tianmaying.form.BlogCreateForm;
import com.tianmaying.form.BlogEditForm;
import com.tianmaying.model.Blog;
import com.tianmaying.model.User;
import com.tianmaying.service.BlogService;

@Controller
public class EditBlogController {
	
	@Autowired
	BlogService bs;
	
	
	//show the edit page
	@GetMapping("/blogs/{id}/edit")
	public String getEditPage(@PathVariable("id") long id, Model model, HttpSession session){
		Blog blog=bs.findBlog(id);
		User user=(User)session.getAttribute("CURRENT_USER");
		if(!blog.getAuthor().equals(user)){
			throw new RightLimitException("You cannot modify this blog");
		}
		model.addAttribute("blog", blog);
		model.addAttribute("isEdit", id);
		return "create";
	}
	
	//update the blog
	@PutMapping("/blogs/{id}")
	public String editPage(@PathVariable("id") long id, @ModelAttribute("blog") @Valid BlogCreateForm form, 
			BindingResult result, HttpSession session){
		if(result.hasErrors()){
			//return "redirect:/blogs/"+id+"/edit";
			return "create";
		}
		
		User user=(User) session.getAttribute("CURRENT_USER");
		if(!bs.findBlog(id).getAuthor().equals(user)){
			throw new RightLimitException("no right to modify blog");
		}
		Blog blog=form.toBlog(user);
		blog.setCreatedTime(new Date());
		bs.updateBlog(blog);
		return "redirect:/blogs/"+id;
	}
	
	
	//delete the blog by id
	@DeleteMapping("blogs/{id}")
	public String deleteBlog(@PathVariable("id") long id, final RedirectAttributes redirectAttr, HttpSession session){
		User user=(User) session.getAttribute("CURRENT_USER");
		if(!bs.findBlog(id).getAuthor().equals(user)){
			throw new RightLimitException("no right to modify blog");
		}
		
		bs.deleteBlog(bs.findBlog(id));
		redirectAttr.addFlashAttribute("message", "Delete Success");
		return "redirect:/admin";
	}
	
	@ResponseStatus(value=HttpStatus.FORBIDDEN, reason="Cannot modified blogs not belonging to you")
	@ExceptionHandler(RightLimitException.class)
	public void forbiddenError(){
		
	}
}
