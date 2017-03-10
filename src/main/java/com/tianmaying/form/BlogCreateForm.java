package com.tianmaying.form;

import java.util.Date;

import javax.validation.constraints.Size;

import com.tianmaying.model.Blog;
import com.tianmaying.model.User;

//form for checking the validation of blog
public class BlogCreateForm {
	@Size(min = 2, max = 30)
    private String title;

    @Size(min = 1)
    private String content;
    
    private long id;
    
    public long getId(){
    	return id;
    }
    
    public void setId(long id){
    	this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Blog toBlog(User author) {
        Blog blog = new Blog();
        blog.setTitle(this.title);
        blog.setId(this.id);
        blog.setContent(this.content);
        blog.setCreatedTime(new Date());
        blog.setAuthor(author);

        return blog;
    }
}
