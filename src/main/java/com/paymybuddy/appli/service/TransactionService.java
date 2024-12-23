package com.paymybuddy.appli.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.paymybuddy.appli.model.DBUser;
import com.paymybuddy.appli.model.Transaction;
import com.paymybuddy.appli.model.DTO.TransactionDTO;
import com.paymybuddy.appli.repository.TransactionRepository;
import com.paymybuddy.appli.repository.UserRepository;

/**
 * Service class for managing transactions.
 * 
 * This class handles the business logic for creating and saving transactions 
 * between users, including validating sender and receiver information and 
 * logging transaction details.
 */
@Service
public class TransactionService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	private static final Logger logger = LogManager.getLogger(TransactionService.class);
	
	/**
     * Creates and saves a new transaction.
     * 
     * This method retrieves the sender and receiver from the database, validates
     * their existence, sets the transaction details, and saves it to the repository.
     * 
     * @param userDetails      The authenticated user's details (sender).
     * @param transactionDTO   The transaction details submitted via a DTO.
     * @return The saved Transaction object.
     */
	public Transaction addTransaction(UserDetails userDetails, TransactionDTO transactionDTO) {
		logger.info("Initiating transaction for user: {}", userDetails.getUsername());
		Transaction transaction = new Transaction();
		
		DBUser byEmail = userRepository.findByEmail(userDetails.getUsername());
		if (byEmail == null) {
            logger.error("Sender not found for email: {}", userDetails.getUsername());
            throw new IllegalArgumentException("Sender not found.");
        }
		logger.debug("Sender fetched successfully: {}", byEmail.getEmail());
		
		DBUser byId = userRepository.findById(transactionDTO.getId()).orElse(null);
		if (byId == null) {
            logger.error("Receiver not found for ID: {}", transactionDTO.getId());
            throw new IllegalArgumentException("Receiver not found.");
        }
		logger.debug("Receiver fetched successfully: {}", byId.getEmail());
		
		transaction.setDescription(transactionDTO.getDescription());
		transaction.setAmount(transactionDTO.getMontant());
		transaction.setSender(byEmail);
		transaction.setReceiver(byId);
		
		logger.info("Transaction successfully saved. Sender: {}, Receiver: {}, Amount: {}", byEmail.getEmail(), byId.getEmail(), transactionDTO.getMontant());
		
		return transactionRepository.save(transaction);
	}
}
