package com.paymybuddy.appli.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "TRANSACTION")
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TRANSACTION_ID")
	private int transactionId;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "AMOUNT")
	private float amount;
	
	@ManyToOne(cascade = CascadeType.ALL,
			   fetch = FetchType.EAGER)
	@JoinColumn(name = "SENDER_ID")
	private DBUser sender;
	
	@ManyToOne(cascade = CascadeType.ALL,
			   fetch = FetchType.EAGER)
	@JoinColumn(name = "RECEIVER_ID")
	private DBUser receiver;

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public DBUser getSenderId() {
		return sender;
	}

	public void setSenderId(DBUser sender) {
		this.sender = sender;
	}

	public DBUser getReceiverId() {
		return receiver;
	}

	public void setReceiverId(DBUser receiver) {
		this.receiver = receiver;
	}
}
