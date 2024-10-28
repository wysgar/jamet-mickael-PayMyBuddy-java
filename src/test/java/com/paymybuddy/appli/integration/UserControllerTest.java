package com.paymybuddy.appli.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.appli.model.DTO.LoginDTO;
import com.paymybuddy.appli.model.DTO.UserDTO;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
	
	@Autowired
	public MockMvc mockMvc;
	
	@Test
	public void getLoginTest() throws Exception {
		LoginDTO login = new LoginDTO();
		login.setMail("test@test.com");
		login.setPassword("test");
		
		mockMvc.perform(get("/login").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(login))).andExpect(status().isOk());
	}
	
	@Test
	public void getLogoutTest() throws Exception {
		mockMvc.perform(get("/logout").param("email", "test@test.com")).andExpect(status().isOk());
	}
	
	@Test
	public void postRegisterTest() throws JsonProcessingException, Exception {
		UserDTO user = new UserDTO();
		user.setUsername("Paul");
		user.setMail("paul@test.com");
		user.setPassword("paulTest");
		
		mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(user))).andExpect(status().isOk());
	}
	
	@Test
	public void getProfileTest() throws Exception {
		mockMvc.perform(get("/profile").param("email", "test@test.com"))
		.andExpect(status().isOk());
	}
	
	@Test
	public void putProfileTest() throws Exception {
		UserDTO user = new UserDTO();
		user.setUsername("Paul");
		user.setMail("paul@test.com");
		user.setPassword("paulTest");
		
		mockMvc.perform(put("/profile").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(user))).andExpect(status().isOk());
	}
	
	@Test
	public void postRelationTest() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/relation").param("email", "test@test.com")).andExpect(status().isOk());
	}
}
