package com.paymybuddy.appli.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.paymybuddy.appli.model.DBUser;
import com.paymybuddy.appli.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controller class for managing user-related actions.
 * 
 * This controller provides endpoints for user registration, login, profile management, 
 * relation management, and logout functionalities.
 */
@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	private static final Logger logger = LogManager.getLogger("UserController");
	
	/**
     * Displays the registration page.
     * 
     * @param model The model used to pass attributes to the view.
     * @return The name of the registration view template ("register.html").
     */
	@GetMapping("/register")
	public String getRegister(Model model) {
		logger.info("Rendering registration page");
		model.addAttribute("user", new DBUser());
		return "register.html";
	}
	
	/**
     * Processes the registration of a new user.
     * 
     * @param user  The user details submitted via the registration form.
     * @param model The model used to pass attributes to the view in case of an error.
     * @return A redirect to the login page if registration is successful, or the registration 
     *         page if an error occurs.
     */
	@PostMapping("/register")
	public String postRegister(@ModelAttribute DBUser user, Model model) {
		logger.info("Attempting to register a new user: {}", user.getEmail());
		try {
			userService.register(user);
			logger.info("Registration successful for user: {}", user.getEmail());
			return "redirect:/login";
		} catch (Exception e) {
			logger.error("Registration failed for user: {}. Reason: {}", user.getEmail(), e.getMessage(), e);
			model.addAttribute("error", e.getLocalizedMessage());
		}
		return getRegister(model);
	}
	
	/**
     * Displays the login page.
     * 
     * @param model The model used to pass attributes to the view.
     * @return The name of the login view template ("login.html").
     */
	@GetMapping("/login")
	public String getLogin(Model model) {
		logger.info("Rendering login page");
		model.addAttribute("user", new DBUser());
		return "login.html";
	}
	
	/**
     * Handles the login process.
     * 
     * @return A redirect to the profile page upon successful login.
     */
	@PostMapping("/login")
	public String postLogin() {
		logger.info("User logged in successfully");
		return "redirect:/profil";
	}
	
	/**
     * Displays the profile page for the authenticated user.
     * 
     * @param userDetails The authenticated user's details.
     * @param model       The model used to pass attributes to the view.
     * @return The name of the profile view template ("profil.html").
     */
	@GetMapping("/profil")
	public String getProfil(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		logger.info("Fetching profile for user: {}", userDetails.getUsername());
		
		DBUser user = userService.getProfil(userDetails.getUsername());
		model.addAttribute("user", user);
		
		logger.info("Profile loaded successfully for user: {}", userDetails.getUsername());
		
		return "profil.html";
	}
	
	/**
     * Updates the profile of the authenticated user.
     * 
     * @param userDetails The authenticated user's details.
     * @param user        The updated profile information.
     * @param redirectAttributes       The redirect attributes used to pass attributes to the view.
     * @return The profile page with updated details.
     */
	@PostMapping("/profil")
	public String updateProfile(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute DBUser user, RedirectAttributes redirectAttributes) {
		logger.info("Updating profile for user: {}", userDetails.getUsername());
		
		try {
			userService.updateProfil(userDetails, user);
			redirectAttributes.addFlashAttribute("error", "Updated");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		UserDetails updatedUserDetails = userService.loadUserByUsername(user.getEmail());
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				updatedUserDetails, 
				updatedUserDetails.getPassword(), 
				updatedUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		logger.info("Profile updated successfully for user: {}", userDetails.getUsername());
        
		return "redirect:/profil";
	}
	
	/**
     * Displays the relation management page.
     * 
     * @param model The model used to pass attributes to the view.
     * @return The name of the relation view template ("relation.html").
     */
	@GetMapping("/relation")
	public String getRelation(Model model) {
		logger.info("Rendering relation page");
		model.addAttribute("email", new String());
		return "relation.html";
	}
	
	/**
     * Adds a new relation for the authenticated user.
     * 
     * @param userDetails The authenticated user's details.
     * @param email       The email of the user to establish a relation with.
     * @return The newly created relation.
     */
	@PostMapping("/relation")
	public DBUser postRelation(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String email, Model model) {
		logger.info("Creating a new relation for user: {} with email: {}", userDetails.getUsername(), email);
		DBUser relation = new DBUser();
		try {
			relation = userService.createRelation(userDetails, email);
			model.addAttribute("error", "Relation ajoutée");
		} catch (Exception e) {
			model.addAttribute("error", e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		return relation;
	}
	
	/**
     * Logs out the currently authenticated user.
     * 
     * @param request  The HTTP request.
     * @param response The HTTP response.
     * @return A redirect to the login page after logout.
     */
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null) {
	    	logger.info("Logging out user: {}", auth.getName());
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
        logger.info("User logged out successfully");
	    return "redirect:/login";
	}
}
