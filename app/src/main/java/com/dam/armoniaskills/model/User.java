package com.dam.armoniaskills.model;

import java.util.UUID;

public class User {

	private UUID id;
	private String fullName;
	private String username;
	private String email;
	private int phone;
	private String password;
	private String role;
	private String imageURL;

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public User(String fullName, String username, String email, int phone, String password, String imageURL) {
		this.fullName = fullName;
		this.username = username;
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.imageURL = imageURL;
	}

	public User() {
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
