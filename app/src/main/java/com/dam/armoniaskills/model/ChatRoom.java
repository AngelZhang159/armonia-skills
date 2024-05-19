package com.dam.armoniaskills.model;

import java.util.UUID;

public class ChatRoom {

	private UUID id;
	private UUID chatId;
	private UUID senderId;
	private UUID receiverId;
	private UUID skillId;

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

	public UUID getSkillId() {
		return skillId;
	}

	public void setSkillId(UUID skillId) {
		this.skillId = skillId;
	}
}
