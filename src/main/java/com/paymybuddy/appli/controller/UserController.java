package com.paymybuddy.appli.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.paymybuddy.appli.model.DBUser;
import com.paymybuddy.appli.model.DTO.UserDTO;
import com.paymybuddy.appli.service.UserService;



@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	private static final Logger logger = LogManager.getLogger("UserController");
	
	@GetMapping("/register")
	public String getRegister(Model model) {
		model.addAttribute("user", new DBUser());
		return "register.html";
	}
	
	@PostMapping("/register")
	public String postRegister(@ModelAttribute DBUser user, Model model) {
		try {
			userService.register(user);
			return "redirect:/login";
		} catch (Exception e) {
			logger.error("Utilisateur existe déjà", e);
		}
		return getRegister(model);
	}
	
	@GetMapping("/login")
	public String getLogin(Model model) {
		model.addAttribute("user", new DBUser());
		return "login.html";
	}
	
	@PostMapping("/login")
	public String postLogin() {
		return "register.html";
	}
	
	@GetMapping("/logout")
	public boolean getLogout(@RequestParam String email) {
		return userService.logout(email);
		
	}

	@GetMapping("/profile")
	public UserDTO getProfile(@RequestParam String email) {
		return userService.getProfile(email);
	}
	
	@PutMapping("/profile")
	public boolean putProfile(@RequestBody UserDTO userDTO) {
		return userService.updateProfile(userDTO);
	}
	
	@PostMapping("/relation")
	public boolean postRelation(@RequestParam String email) {
		return userService.createRelation(email);
	}
	
}
