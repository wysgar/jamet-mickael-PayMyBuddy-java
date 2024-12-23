package com.paymybuddy.appli.service;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.paymybuddy.appli.model.DBUser;
import com.paymybuddy.appli.repository.UserRepository;

import jakarta.transaction.Transactional;

/**
 * Service class responsible for user management operations, including user registration, profile updates, and relation creation.
 * It interacts with the UserRepository to persist and retrieve user data and manages user authentication details through the {@link UserDetailsService} interface.
 * 
 * This class also logs the different operations for traceability and debugging purposes.
 */
@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
	private static final Logger logger = LogManager.getLogger(UserService.class);
	
	/**
     * Loads the user by their email.
     * 
     * @param email The email of the user to be loaded.
     * @return A UserDetails object containing the user's email and password.
     * @throws UsernameNotFoundException If no user with the provided email is found.
     */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		logger.info("Attempting to load user by email: {}", email);
		DBUser user = userRepository.findByEmail(email);
		
		if (user == null) {
			logger.error("User not found with email: {}", email);
			throw new UsernameNotFoundException("User not found");	
		}
		
		logger.info("User loaded successfully: {}", email);
		return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
	}
	
	/**
     * Registers a new user with the given information.
     * 
     * @param user The DBUser object containing user details.
     * @return The registered user.
     * @throws IllegalArgumentException If a user with the same email already exists.
     */
	public DBUser register(DBUser user) {
		logger.info("Attempting to register user with email: {}", user.getEmail());
		
		if (!isValidEmail(user.getEmail())) {
			logger.error("Email invalid", user.getEmail());
			throw new IllegalArgumentException("Email invalid.");
		}
		
		if (userRepository.findByEmail(user.getEmail()) != null) {
			logger.error("Registration failed: User with email {} already exists", user.getEmail());
			throw new IllegalArgumentException("User with this email already exists.");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		logger.info("User registered successfully: {}", user.getEmail());
		return userRepository.save(user);
	}

	/**
     * Updates the profile of an existing user.
     * 
     * @param userDetails The details of the currently authenticated user.
     * @param updateUser The updated user details.
     * @return The updated user.
     * @throws IllegalArgumentException If the email already exists or the user tries to update their email to one already in use.
     */
	public DBUser updateProfil(UserDetails userDetails, DBUser updateUser) {
		logger.info("Attempting to update profile for user: {}", userDetails.getUsername());
		DBUser user = getProfil(userDetails.getUsername());
		
		if (!isValidEmail(updateUser.getEmail())) {
			logger.error("Email invalid : {}", updateUser.getEmail());
			throw new IllegalArgumentException("Email invalid.");
		}
		
		if (!user.getEmail().equals(updateUser.getEmail())) {
			if (getProfil(updateUser.getEmail()) == null) {
				user.setEmail(updateUser.getEmail());
				logger.info("Email updated successfully for user: {}", userDetails.getUsername());
			} else {
				logger.error("Update failed: User with email {} already exists", updateUser.getEmail());
				throw new IllegalArgumentException("User with this email already exists.");
			}
		}
		user.setUsername(updateUser.getUsername());
		logger.debug("Username updated to: {}", updateUser.getUsername());
		
		if (!updateUser.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
			logger.debug("Password updated for user: {}", userDetails.getUsername());
		}
		
		logger.info("Profile updated successfully for user: {}", userDetails.getUsername());
		return userRepository.save(user);
	}

	/**
     * Fetches the profile for a user by email.
     * 
     * @param email The email of the user whose profile is to be fetched.
     * @return The DBUser object corresponding to the email.
     */
	public DBUser getProfil(String email) {
		logger.info("Fetching profile for email: {}", email);
		return userRepository.findByEmail(email);
	}
	
	/**
     * Creates a relation (connection) between two users.
     * 
     * @param userDetails The authenticated user's details (creator of the relation).
     * @param email The email of the user to be added as a relation.
     * @return The updated DBUser object with the new relation added.
     * @throws IllegalArgumentException If the relation already exists, or if the user attempts to relate to themselves, or if the email is not found.
     */
	@Transactional
	public DBUser createRelation(UserDetails userDetails, String email) {
		logger.info("Attempting to create a relation for user: {} with email: {}", userDetails.getUsername(), email);
	    DBUser user = getProfil(userDetails.getUsername());
	    DBUser relation = getProfil(email);

	    if (relation == null) {
	    	logger.error("Relation creation failed: User with email {} not found", email);
	        throw new IllegalArgumentException("Relation not found.");
	    }
	    
	    if (relation == user) {
	    	logger.error("Relation creation failed: User tried to create a relation with themselves");
	        throw new IllegalArgumentException("Relation with self.");
	    }

	    if (user.getConnections().contains(relation)) {
	    	logger.error("Relation creation failed: User already has a connection with email {}", email);
	        throw new IllegalArgumentException("This connection already exists.");
	    }

	    user.getConnections().add(relation);
	    logger.info("Relation created successfully between user: {} and email: {}", userDetails.getUsername(), email);

	    return userRepository.save(user);
	}
	
	/**
	 * Validates whether a given string is a valid email address.
	 *
	 * A valid email address must meet the following criteria:
	 * <ul>
	 *   <li>Contains text before the "@" symbol, consisting of alphanumeric characters,
	 *       and optionally the characters "+", "_", ".", or "-".</li>
	 *   <li>The domain (after "@") contains alphanumeric characters, "." or "-".</li>
	 *   <li>Ends with a valid top-level domain (at least two letters).</li>
	 * </ul>
	 *
	 * @param email the string to validate as an email address
	 * @return {@code true} if the email is valid, {@code false} otherwise
	 * @throws NullPointerException if the email is {@code null}
	 */
	public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        return email.matches(emailRegex);
    }
}
