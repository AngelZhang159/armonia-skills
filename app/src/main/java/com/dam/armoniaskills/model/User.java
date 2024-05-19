package com.dam.armoniaskills.model;

import java.util.List;
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
	private List<ChatRoom> chatRooms;
	private List<Review> reviewList;
	private double balance;

	public User() {
	}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public User(UUID id, String fullName, String username, String email, int phone, String password, String role, String imageURL, List<ChatRoom> chatRooms, List<Review> reviewList, double balance) {
		this.id = id;
		this.fullName = fullName;
		this.username = username;
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.role = role;
		this.imageURL = imageURL;
		this.chatRooms = chatRooms;
		this.reviewList = reviewList;
		this.balance = balance;
	}

	public User(String fullName, String username, String email, int phone, String password, String imageURL, Double balance) {
		this.fullName = fullName;
		this.username = username;
		this.email = email;
		this.phone = phone;
		this.password = password;
		this.imageURL = imageURL;
		this.balance = balance;
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

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public List<ChatRoom> getChatRooms() {
		return chatRooms;
	}

	public void setChatRooms(List<ChatRoom> chatRooms) {
		this.chatRooms = chatRooms;
	}

	public List<Review> getReviewList() {
		return reviewList;
	}

	public void setReviewList(List<Review> reviewList) {
		this.reviewList = reviewList;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}
