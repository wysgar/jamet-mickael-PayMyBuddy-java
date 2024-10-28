package com.paymybuddy.appli.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "USER")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private int userId;
	
	@Column(name = "USERNAME")
	private String username;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "PASSWORD")
	private String password;
	
	@OneToMany
	private List<User> friends;
	
//	@ManyToMany(
//			fetch = FetchType.LAZY, 
//			cascade = {CascadeType.PERSIST, CascadeType.MERGE}
//	)
//	@JoinTable(
//			name = "USER_CONNECTIONS", 
//			joinColumns = @JoinColumn(name = "USER_Id"), 
//			inverseJoinColumns = @JoinColumn(name = "USER_TO")
//	)
//	private List<User> usersTo = new ArrayList<>();
//	
//	@ManyToMany(
//			mappedBy = "usersTo",
//			cascade = CascadeType.ALL
//	)
//	private List<User> users = new ArrayList<>();
	
//	public void addUsersTo(User userTo) {
//		usersTo.add(userTo);
//		userTo.getUsersTo().add(this);
//	}
// 
//	public void removeUsersTo(User userTo) {
//		usersTo.remove(userTo);
//		userTo.getUsersTo().remove(this);
//	}

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

//	public List<User> getUsersTo() {
//		return usersTo;
//	}
//
//	public void setUsersTo(List<User> usersTo) {
//		this.usersTo = usersTo;
//	}
//
//	public List<User> getUsers() {
//		return users;
//	}
//
//	public void setUsers(List<User> users) {
//		this.users = users;
//	}
}
