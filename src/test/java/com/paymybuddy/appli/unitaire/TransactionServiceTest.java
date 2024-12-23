package com.paymybuddy.appli.unitaire;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import com.paymybuddy.appli.model.DBUser;
import com.paymybuddy.appli.model.Transaction;
import com.paymybuddy.appli.model.DTO.TransactionDTO;
import com.paymybuddy.appli.repository.TransactionRepository;
import com.paymybuddy.appli.repository.UserRepository;
import com.paymybuddy.appli.service.TransactionService;

@SpringBootTest
class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @Mock
    private UserDetails userDetails;
    
    private String senderEmail;
    private DBUser sender;
    private TransactionDTO transactionDTO;
    
    @BeforeEach
	public void setUp() {
    	senderEmail = "sender@example.com";
        sender = new DBUser();
        sender.setEmail(senderEmail);
        
        transactionDTO = new TransactionDTO();
        transactionDTO.setDescription("Test transaction");
        transactionDTO.setMontant(100.0);
	}

    @Test
    void addTransaction_Success() {
        DBUser receiver = new DBUser();

        Transaction savedTransaction = new Transaction();
        savedTransaction.setDescription(transactionDTO.getDescription());
        savedTransaction.setAmount(transactionDTO.getMontant());
        savedTransaction.setSender(sender);
        savedTransaction.setReceiver(receiver);

        when(userDetails.getUsername()).thenReturn(senderEmail);
        when(userRepository.findByEmail(senderEmail)).thenReturn(sender);
        when(userRepository.findById(transactionDTO.getId())).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        Transaction result = transactionService.addTransaction(userDetails, transactionDTO);

        assertNotNull(result);
        assertEquals(transactionDTO.getDescription(), result.getDescription());
        assertEquals(transactionDTO.getMontant(), result.getAmount());
        assertEquals(sender, result.getSender());
        assertEquals(receiver, result.getReceiver());

        verify(userRepository).findByEmail(senderEmail);
        verify(userRepository).findById(transactionDTO.getId());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void addTransaction_Fail_WhenReceiverNotFound() {
        when(userDetails.getUsername()).thenReturn(senderEmail);
        when(userRepository.findByEmail(senderEmail)).thenReturn(sender);
        when(userRepository.findById(transactionDTO.getId())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> transactionService.addTransaction(userDetails, transactionDTO));
        assertEquals("Receiver not found.", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(userDetails.getUsername());
        verify(userRepository, times(1)).findById(transactionDTO.getId());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void addTransaction_Fail_WhenTransactionIsNull() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> transactionService.addTransaction(userDetails, transactionDTO));
        assertEquals("Sender not found.", exception.getMessage());

        verify(userRepository, times(1)).findByEmail(userDetails.getUsername());
        verify(userRepository, never()).findById(transactionDTO.getId());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}

