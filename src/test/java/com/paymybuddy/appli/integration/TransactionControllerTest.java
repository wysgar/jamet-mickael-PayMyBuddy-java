package com.paymybuddy.appli.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.paymybuddy.appli.model.DBUser;
import com.paymybuddy.appli.model.Transaction;
import com.paymybuddy.appli.repository.TransactionRepository;
import com.paymybuddy.appli.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class TransactionControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;
    
    private DBUser sender;
    private DBUser receiver;
	private final BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();

    @BeforeEach
    public void setup() {
    	transactionRepository.deleteAll();
    	userRepository.deleteAll();
    	
        sender = new DBUser();
        sender.setEmail("sender@example.com");
        sender.setUsername("sender");
        sender.setPassword(passwordEncoder.encode("password"));

		userRepository.save(sender);
        
		receiver = new DBUser();
        receiver.setEmail("receiver@example.com");
        receiver.setUsername("receiver");
        receiver.setPassword(passwordEncoder.encode("password"));

        userRepository.save(receiver);
    }

    @Test
    @WithMockUser(username = "sender@example.com")
    public void testGetTransaction() throws Exception {
        mockMvc.perform(get("/transaction"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("relations"))
                .andExpect(model().attributeExists("transactions"))
                .andExpect(model().attributeExists("transactionDTO"))
                .andExpect(view().name("transaction.html"));
    }

    @Test
    @WithMockUser(username = "sender@example.com")
    public void testPostTransaction() throws Exception {
        mockMvc.perform(post("/transaction")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("description", "Test Transaction")
                .param("montant", "100.0")
                .param("id", String.valueOf(receiver.getUserId())))
		        .andExpect(status().is3xxRedirection())
		        .andExpect(redirectedUrl("/transaction"));

        Iterable<Transaction> transactions = transactionRepository.findAll();
        assert transactions.iterator().hasNext();
    }
}
