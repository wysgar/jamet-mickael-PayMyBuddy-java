package com.paymybuddy.appli.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.paymybuddy.appli.model.DBUser;
import com.paymybuddy.appli.model.Transaction;
import com.paymybuddy.appli.model.DTO.TransactionDTO;
import com.paymybuddy.appli.repository.TransactionRepository;
import com.paymybuddy.appli.repository.UserRepository;

@Service
public class TransactionService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TransactionRepository transactionRepository;
	
	public Transaction addTransaction(UserDetails userDetails, TransactionDTO transactionDTO) {
		Transaction transaction = new Transaction();
		DBUser byEmail = userRepository.findByEmail(userDetails.getUsername());
		DBUser byId = userRepository.findById(transactionDTO.getId()).orElse(null);
		
		if (transaction != null && byId != null) {
			transaction.setDescription(transactionDTO.getDescription());
			transaction.setAmount(transactionDTO.getMontant());
			transaction.setSender(byEmail);
			transaction.setReceiver(byId);
		}
		
		return transactionRepository.save(transaction);
	}
}
