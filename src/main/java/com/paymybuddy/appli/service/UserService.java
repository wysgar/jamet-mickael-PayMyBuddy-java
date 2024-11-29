package com.paymybuddy.appli.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.paymybuddy.appli.model.DBUser;
import com.paymybuddy.appli.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();

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
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	public DBUser updateProfil(UserDetails userDetails, DBUser updateUser) {	
		DBUser user = getProfil(userDetails.getUsername());
		
		if (!user.getEmail().equals(updateUser.getEmail())) {
			if (getProfil(updateUser.getEmail()) == null) {
				user.setEmail(updateUser.getEmail());
			} else
				throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà.");
		}
		user.setUsername(updateUser.getUsername());
		
		if (!updateUser.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
		}
		
		return userRepository.save(user);
	}

	public DBUser getProfil(String email) {
		return userRepository.findByEmail(email);
	}

	public DBUser createRelation(UserDetails userDetails, String email) {
		DBUser user = getProfil(userDetails.getUsername());
		DBUser relation = getProfil(email);
		List<DBUser> relations = user.getConnections();
		
		boolean alreadyConnected = user.getConnections().stream()
                .anyMatch(connection -> connection == relation);

        if (alreadyConnected) {
            throw new RuntimeException("Cette connexion existe déjà.");
        }
		
		if (relation != null) {
			if (user != relation) {
				relations.add(relation);
				System.out.println(relations);
				user.setConnections(relations);
			}
		}
		
		return userRepository.save(user);
	}
	
//	public boolean logout(String email) {
//		return false;
//	}
}
