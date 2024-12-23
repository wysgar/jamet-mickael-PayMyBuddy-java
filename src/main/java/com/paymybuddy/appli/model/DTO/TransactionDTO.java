package com.paymybuddy.appli.model.DTO;

/**
 * Data Transfer Object (DTO) representing a transaction.
 * <p>
 * This class is used to transfer data related to a transaction, typically between the client and server.
 * It contains information about the transaction's ID, description, and amount.
 * </p>
 */
public class TransactionDTO {

	private int id;
	private String description;
	private double montant;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getMontant() {
		return montant;
	}
	public void setMontant(double d) {
		this.montant = d;
	}
}
