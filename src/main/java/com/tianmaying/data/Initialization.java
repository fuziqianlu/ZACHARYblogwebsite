package com.tianmaying.data;

import com.tianmaying.model.Blog;
import com.tianmaying.model.User;
import com.tianmaying.service.BlogService;
import com.tianmaying.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Initialization implements CommandLineRunner {

    private final UserService userService;

    private final BlogService blogService;

    @Autowired
    public Initialization(UserService userService, BlogService blogService) {
        this.userService = userService;
        this.blogService = blogService;
    }

    @Override
    public void run(String... strings) throws Exception {
        User user = createUser("alice", "admin@tianmaying.com");

        createBlog("Bolstering security across Google Cloud", "San Francisco — Today at Google Cloud Next ‘17, we launched the following new features for Google Cloud Platform (GCP) and G Suite that are designed to help safeguard your company’s assets and prevent disruption to your business:", user);
        createBlog("Introducing new, enterprise-ready tools for Google Drive",
        		"Google Drive has always made it easy for individuals to safely store, sync and share files.", 
        		user);
        createBlog("The She Word: Googler Alexandrina Garcia-Verdin and her “tribe of women”", 
        		"In honor of Women’s History Month, we’re celebrating the powerful, dynamic and creative women of Google.", 
        		user);
        createBlog("Finally, live TV made for you", 
        		"There’s no question that people love TV, from live sports to breaking news to sitcoms and dramas. But the truth is, there are a lot of limitations in how to watch TV today. ", 
        		user);
        createBlog("Powering enterprise productivity and secure collaboration with major updates to G suite", 
        		"The promise of the cloud has always been to offer flexibility, access and security at a scale that’s unimaginable in legacy enterprise productivity solutions.",
        		user);
    }

    private User createUser(String username, String email) {
        User user = new User(username, email);
        user.setPassword("12345678ab");
        return userService.register(user);
    }

    private Blog createBlog(String title, String content, User author) {
        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setContent(content);
        blog.setAuthor(author);
        blog.setCreatedTime(new Date());

        return blogService.createBlog(blog);
    }

}
