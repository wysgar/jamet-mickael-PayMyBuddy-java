package com.paymybuddy.appli.unitaire;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.paymybuddy.appli.repository.UserRepository;
import com.paymybuddy.appli.service.UserService;

public class UserServiceTest {
	
	@Autowired
	private UserService userService;
	@Mock
	private UserRepository userRepository;
	
	@Test
	public void getLoginTest() {
		userService.getLogin(null);
	}
	
	@Test
	public void getLogoutTest() {
		userService.getLogout(null);
	}
	
	@Test
	public void registerTest() {
		userService.register(null);
	}
	
	@Test
	public void getProfileTest() {
		userService.getProfile(null);
	}
	
	@Test
	public void updateProfileTest() {
		userService.updateProfile(null);
	}
	
	@Test
	public void createRelationTest() {
		userService.createRelation(null);
	}
}
