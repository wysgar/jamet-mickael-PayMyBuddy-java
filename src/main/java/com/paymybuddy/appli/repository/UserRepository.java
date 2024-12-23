package com.paymybuddy.appli.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.paymybuddy.appli.model.DBUser;

/**
 * Repository interface for performing CRUD operations on {@link DBUser} entities.
 * <p>
 * This interface extends {@link CrudRepository}, providing built-in methods for basic database operations 
 * such as saving, updating, deleting, and finding users.
 * Additional query methods are defined to find users by their email or username.
 * </p>
 * 
 * @see CrudRepository
 * @see DBUser
 */
@Repository
public interface UserRepository extends CrudRepository<DBUser, Integer>  {
	
	/**
     * Finds a {@link DBUser} by their email.
     * 
     * @param email the email of the user
     * @return the user associated with the given email, or {@code null} if no user is found
     */
	DBUser findByEmail(String email);

	/**
     * Finds a {@link DBUser} by their username.
     * 
     * @param username the username of the user
     * @return the user associated with the given username, or {@code null} if no user is found
     */
	DBUser findByUsername(String username);
}
