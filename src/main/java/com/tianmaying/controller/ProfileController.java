package com.tianmaying.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tianmaying.form.UserRegisterForm;
import com.tianmaying.model.User;
import com.tianmaying.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class ProfileController {

    //这里你需要根据自己的文件目录位置进行修改
	@Value("${file.dir}")
    private String ROOT;
	
	@Autowired
	UserService us;

    private final ResourceLoader resourceLoader;

    @Autowired
    public ProfileController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping(value = "/avatar/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename) {
        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(ROOT, filename).toString()));
        } catch (Exception e) {
            //return ResponseEntity.notFound().build();
        	return ResponseEntity.ok(resourceLoader.getResource(Paths.get("/img/catty.jpeg").toString()));
        }
    }
    
    //show the profile edit page of current user
    @GetMapping("/profile")
    public String getProfile(Model model, HttpSession session){
    	User user=(User) session.getAttribute("CURRENT_USER");
    	model.addAttribute("user", user);
    	return "profile";
    }
    
    @GetMapping("/profile/{username}")
    @ResponseBody
    public ResponseEntity<?> getImg(@PathVariable("username") String username){
    	User user=us.findByName(username);
    	String filename=""+user.getId()+".jpg";
    	try {
    		if(Paths.get(ROOT, filename).toFile().exists()){
    			return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(ROOT, filename).toString()));
            }
    		else{
    			return ResponseEntity.ok(resourceLoader.getResource("classpath:/static/img/mac.jpg"));
    		}
        } catch (Exception e) {
            //return ResponseEntity.notFound().build();
        	return ResponseEntity.ok(resourceLoader.getResource("classpath:/static/img/mac.jpg"));
        }
    }
    
    //api for updating personal profile
    @PostMapping("/profile")
    public String updateInf(@RequestParam(name="file", required=false) MultipartFile file, 
    		@ModelAttribute("user") @Valid UserRegisterForm form, BindingResult result,
    		HttpSession session,
     		final RedirectAttributes redirectAttr) throws IOException{
    	
    	if(result.hasErrors()){
    		redirectAttr.addFlashAttribute("message", "Update Failed");
    		return "redirect:/profile";
    	}
    	User user=(User) session.getAttribute("CURRENT_USER");
    	User updatedUser=form.toUser();
    	String filename=user.getId()+".jpg";
    	if(!file.isEmpty()){
    		Files.copy(file.getInputStream(), Paths.get(ROOT, filename), StandardCopyOption.REPLACE_EXISTING);
    	}
    	if(updatedUser.getName()!=user.getName()){
    		user.setName(updatedUser.getName());
    	}
    	if(updatedUser.getEmail()!=user.getEmail()){
    		user.setEmail(updatedUser.getEmail());
    	}
    	if(updatedUser.getPassword()!=user.getPassword()&&updatedUser.getPassword()!=null){
    		user.setPassword(updatedUser.getPassword());
    	}
    	us.update(user);
    	session.setAttribute("CURRENT_USER", user);
    	redirectAttr.addFlashAttribute("message", "Update Success");
    	return "redirect:/profile";
    }

    @PostMapping("/avatar")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        //这里要对currentUser进行替换
    	User currentUser=(User) session.getAttribute("CURRENT_USER");
        String filename = currentUser.getId() + ".jpg";
        // 保存图片
        Files.copy(file.getInputStream(), Paths.get(ROOT, filename));
        return "redirect:/profile";
    }
}