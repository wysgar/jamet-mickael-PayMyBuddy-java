package com.paymybuddy.appli.controller;

import com.paymybuddy.appli.model.DBUser;
import com.paymybuddy.appli.model.DTO.TransactionDTO;
import com.paymybuddy.appli.repository.UserRepository;
import com.paymybuddy.appli.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/transaction")
	public String getTransaction(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		DBUser byEmail = userRepository.findByEmail(userDetails.getUsername());
		model.addAttribute("relations", byEmail.getConnections());
		model.addAttribute("transactions", byEmail.getSentTransactions());
		model.addAttribute("transactionDTO", new TransactionDTO());
		return "transaction.html";
	}
	
	@PostMapping("/transaction")
	public String postTransaction(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute TransactionDTO transactionDTO, Model model) {
		transactionService.addTransaction(userDetails, transactionDTO);
		
		return getTransaction(userDetails, model);
	}
}
