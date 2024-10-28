package com.paymybuddy.appli.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paymybuddy.appli.model.DTO.LoginDTO;
import com.paymybuddy.appli.model.DTO.UserDTO;
import com.paymybuddy.appli.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	public String getLogin(LoginDTO loginDTO) {
		return "";
	}

	public boolean getLogout(String email) {
		return false;
	}

	public boolean register(UserDTO userDTO) {
		return false;
	}

	public UserDTO getProfile(String email) {
		return null;
	}

	public boolean updateProfile(UserDTO userDTO) {
		return false;
	}

	public boolean createRelation(String email) {
		return false;
	}

	public boolean postLogin(LoginDTO loginDTO) {
		// TODO Auto-generated method stub
		return false;
	}
}
