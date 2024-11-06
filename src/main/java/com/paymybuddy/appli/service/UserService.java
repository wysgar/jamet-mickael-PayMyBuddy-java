package com.paymybuddy.appli.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.paymybuddy.appli.model.DBUser;
import com.paymybuddy.appli.model.DTO.UserDTO;
import com.paymybuddy.appli.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
//	private final BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder(16);

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		DBUser user = userRepository.findByEmail(email);
		
		if (user == null)
			throw new UsernameNotFoundException("User not found");
		return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
	}
	
	public DBUser register(DBUser user) {
		if (userRepository.findByEmail(user.getEmail()) != null) {
			throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà.");
		}
		return userRepository.save(user);
	}
	
//	public boolean login(DBUser user) {
//		DBUser byEmail = userRepository.findByEmail(user.getEmail());
//		if (passwordEncoder.matches(user.getPassword(), byEmail.getPassword())) {
//			return true;
//		}
//		return false;
//	}

	public UserDTO getProfile(String email) {
		return null;
	}

	public boolean updateProfile(UserDTO userDTO) {
		return false;
	}

	public boolean createRelation(String email) {
		return false;
	}
	
	public boolean logout(String email) {
		return false;
	}
}
