package com.tianmaying.controller;

import com.tianmaying.model.Blog;
import com.tianmaying.service.BlogService;
import com.tianmaying.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {
	@Autowired
	private BlogService bs;
	@Autowired
	private UserService us;
	
	//search api, could show both user and blog results
    @GetMapping("/search")
    public String search(@RequestParam("key") String key, @PageableDefault(value=15, sort={"id"}, direction=Sort.Direction.DESC) Pageable pageable,
            Model model) {
        // Your code goes here
    	model.addAttribute("user", us.findByName(key));
    	model.addAttribute("blogs", bs.findBlogsByKey(key, pageable));
    	
        return "searchres";
    }
}
