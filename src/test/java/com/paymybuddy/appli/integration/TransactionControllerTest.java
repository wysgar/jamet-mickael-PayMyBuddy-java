package com.paymybuddy.appli.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.appli.model.DTO.TransactionDTO;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {
	
	@Autowired
	public MockMvc mockMvc;
	
	@Test
	public void getTransactionTest() throws Exception {
		mockMvc.perform(get("/transaction").param("email", "test@test.com"))
		.andExpect(status().isOk());
	}
	
	@Test
	public void postTransactionTest() throws JsonProcessingException, Exception {
		TransactionDTO transactionDTO = new TransactionDTO();
//		transactionDTO.setUsername(null);
//		transactionDTO.setDescription(null);
//		transactionDTO.setMontant(0);
		
		mockMvc.perform(post("/transaction").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(transactionDTO))).andExpect(status().isOk());
	}
}
