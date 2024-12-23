package com.paymybuddy.appli.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.appli.model.Transaction;

/**
 * Repository interface for performing CRUD operations on {@link Transaction} entities.
 * <p>
 * This interface extends {@link CrudRepository}, providing built-in methods for basic database operations 
 * such as saving, updating, deleting, and finding transactions.
 * It can be customized with additional query methods if needed.
 * </p>
 * 
 * @see CrudRepository
 * @see Transaction
 */
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

}
