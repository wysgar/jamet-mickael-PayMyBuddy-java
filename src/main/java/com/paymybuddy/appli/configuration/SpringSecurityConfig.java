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

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
	
	@Autowired
	private UserService userService;
	
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
		
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
		return authenticationManagerBuilder.build();
	}
}
