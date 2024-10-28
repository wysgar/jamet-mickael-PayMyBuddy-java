package com.paymybuddy.appli.controller;

import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.appli.model.DTO.TransactionDTO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class TransactionController {

	@GetMapping("/transaction")
	public void getTransaction(@RequestParam String email) {
		
	}
	
	@PostMapping("/transaction")
	public void postTransaction(@RequestBody TransactionDTO transactionDTO) {
		
	}
}
