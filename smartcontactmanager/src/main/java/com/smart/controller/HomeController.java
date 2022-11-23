package com.smart.controller;


import java.net.Authenticator.RequestorType;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;




@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title", "Home-SmartContactManager");
		return "home";
		
	}
	
	@RequestMapping("/about")
	public String about(Model model)
	{
		
		model.addAttribute("title", "About-SmartContactManager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signUp(Model model)
	{
		model.addAttribute("title", "Register-SmartContactManager");
		model.addAttribute("user", new User()); //
		return "signup";
	}
	
	//handler for registering User
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult res,
			@RequestParam(value="agreement", defaultValue="false") boolean agreement, Model model, HttpSession session)
	{
		try
		{
		
			if(res.hasErrors())
			{
				System.out.println(res);
				model.addAttribute("user", user);
				return "signup";
			}
			
			
		if(!agreement)
		{
		System.out.println("You have not accepted terms and condition ");
		throw new Exception("You have not accepted terms and condition");
		}
		user.setRole("ROLE_User");
		user.setEnabled(true);
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		System.out.println("Agreement"+agreement);
		System.out.println(user);
		
		User result=this.userRepository.save(user);
		model.addAttribute("user", new User());
		session.setAttribute("message", new Message("Successfully Registered!!","alert-success"));
		return "signup";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong!!"+e.getMessage(),"alert-danger"));
			return "signup";
		}
		
	}
	
	@RequestMapping(path="/signin")
	public String customLogin(Model model){
		model.addAttribute("title", "Login-SmartContactManager");
		
		return "login";
	}
}
