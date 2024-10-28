package com.paymybuddy.appli.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.appli.model.DTO.LoginDTO;
import com.paymybuddy.appli.model.DTO.UserDTO;
import com.paymybuddy.appli.service.UserService;



@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/login")
	public String getLogin(@RequestBody LoginDTO loginDTO) {
		return "login.html";
	}
	
	@PostMapping("/login")
	public boolean postLogin(@RequestBody LoginDTO loginDTO) {
		return userService.postLogin(loginDTO);
	}
	
	@GetMapping("/logout")
	public boolean getLogout(@RequestParam String email) {
		return userService.getLogout(email);
		
	}
	
	@PostMapping("/register")
	public boolean postRegister(@RequestBody UserDTO userDTO) {
		return userService.register(userDTO);
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
