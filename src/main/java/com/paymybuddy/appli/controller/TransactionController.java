package com.paymybuddy.appli.controller;

import com.paymybuddy.appli.model.DBUser;
import com.paymybuddy.appli.model.Transaction;
import com.paymybuddy.appli.model.DTO.TransactionDTO;
import com.paymybuddy.appli.repository.UserRepository;
import com.paymybuddy.appli.service.TransactionService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller class for managing transactions.
 * 
 * This class provides endpoints to display and process transactions for
 * authenticated users. It allows users to view their transaction history and
 * create new transactions with their connections.
 */
@Controller
public class TransactionController {
	
	private static final Logger logger = LogManager.getLogger(TransactionController.class);
	
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private UserRepository userRepository;

	/**
     * Displays the transactions page for the authenticated user.
     * 
     * This method retrieves the user's connections and sent transactions from
     * the database and populates the model with these details to be rendered in
     * the transactions view.
     * 
     * @param userDetails The authenticated user's details.
     * @param model       The model used to pass attributes to the view.
     * @return The name of the transactions view template ("transaction.html").
     */
	@GetMapping("/transaction")
	public String getTransaction(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		logger.info("Fetching transactions for user: {}", userDetails.getUsername());
		
		DBUser byEmail = userRepository.findByEmail(userDetails.getUsername());
		model.addAttribute("relations", byEmail.getConnections());
		model.addAttribute("transactions", byEmail.getSentTransactions());
		model.addAttribute("transactionDTO", new TransactionDTO());
		
		logger.debug("Loaded {} connections and {} transactions for user: {}", byEmail.getConnections().size(), byEmail.getSentTransactions().size(), userDetails.getUsername());
		
		return "transaction.html";
	}
	
	/**
     * Processes a new transaction for the authenticated user.
     * 
     * This method handles the submission of a new transaction form. It delegates
     * the transaction creation to the {@link TransactionService}, logs the
     * operation, and redirects the user back to the transactions page.
     * 
     * @param userDetails    The authenticated user's details.
     * @param transactionDTO The data transfer object containing the transaction details.
     * @return A redirect to the transactions page ("/transaction").
     */
	@PostMapping("/transaction")
	public String postTransaction(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute TransactionDTO transactionDTO, RedirectAttributes redirectAttributes) {
		logger.info("Processing new transaction for user: {}", userDetails.getUsername());
        logger.debug("Transaction details: Description={}, Amount={}, ReceiverId={}", transactionDTO.getDescription(), transactionDTO.getMontant(), transactionDTO.getId());
		
		Transaction result = new Transaction();
		try {
			result = transactionService.addTransaction(userDetails, transactionDTO);
			redirectAttributes.addFlashAttribute("error", "Transaction ajoutée");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		logger.debug("Transaction result: {}", result);
		
		return "redirect:/transaction";
	}
}
