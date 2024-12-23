package com.paymybuddy.appli.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.paymybuddy.appli.service.UserService;

/**
 * Configuration class for Spring Security.
 * <p>
 * This configuration class sets up HTTP security, form-based login, and password encoding.
 * It defines the security rules, login pages, and authentication manager for the application.
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
	
	@Autowired
	private UserService userService;
	
	/**
     * Configures the security filter chain.
     * <p>
     * This method defines which URLs are publicly accessible and which require authentication.
     * It also sets up form-based login and logout.
     * </p>
     *
     * @param http the HttpSecurity object used to configure HTTP security
     * @return a configured SecurityFilterChain
     * @throws Exception if an error occurs during the configuration
     */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.authorizeHttpRequests(auth -> {
			auth.requestMatchers("/", "/style.css", "/register").permitAll();
			auth.anyRequest().authenticated();
		})
		.formLogin(form -> form
				.loginPage("/login")
				.usernameParameter("email")
                .defaultSuccessUrl("/profil", true)
				.permitAll())
		.logout((logout) -> 
				logout.permitAll()
				.logoutSuccessUrl("/login"));
		
		return http.build();
	}
	
	/**
     * Provides a BCryptPasswordEncoder bean for encoding passwords.
     *
     * @return a BCryptPasswordEncoder instance
     */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
     * Configures the authentication manager with a userDetailsService and password encoder.
     * <p>
     * This method creates an AuthenticationManager that uses the UserService for loading user details and the BCryptPasswordEncoder for password encoding.
     * </p>
     *
     * @param http the HttpSecurity object used to configure HTTP security
     * @param bCryptPasswordEncoder the password encoder to be used
     * @return a configured AuthenticationManager
     * @throws Exception if an error occurs during the configuration
     */
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
		return authenticationManagerBuilder.build();
	}
}
