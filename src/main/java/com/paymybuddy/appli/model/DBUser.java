package com.paymybuddy.appli.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "USER")
public class DBUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private int userId;
	
	@Column(nullable = false, name = "USERNAME")
	private String username;
	
	
	@Column(nullable = false, unique = true, name = "EMAIL")
	private String email;
	
	@Column(nullable = false, name = "PASSWORD")
	private String password;
	
	@OneToMany
	private List<DBUser> friends;

	public List<DBUser> getFriends() {
		return friends;
	}

	public void setFriends(List<DBUser> friends) {
		this.friends = friends;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
