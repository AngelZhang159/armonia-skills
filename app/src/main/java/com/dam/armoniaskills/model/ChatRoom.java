package com.dam.armoniaskills.model;

import java.util.UUID;

public class ChatRoom {

	private UUID id;
	private UUID chatId;
	private UUID senderId;
	private UUID receiverId;
	private String skillName;

	public ChatRoom(UUID id, UUID chatId, UUID senderId, UUID receiverId, String skillName) {
		this.id = id;
		this.chatId = chatId;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.skillName = skillName;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getChatId() {
		return chatId;
	}

	public void setChatId(UUID chatId) {
		this.chatId = chatId;
	}

	public UUID getSenderId() {
		return senderId;
	}

	public void setSenderId(UUID senderId) {
		this.senderId = senderId;
	}

	public UUID getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(UUID receiverId) {
		this.receiverId = receiverId;
	}

}
