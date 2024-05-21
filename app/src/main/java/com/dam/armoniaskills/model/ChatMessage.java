package com.dam.armoniaskills.model;

import java.sql.Date;
import java.util.UUID;

public class ChatMessage {
	private UUID chatId;
	private UUID sender;
	private UUID receiver;
	private String content;
	private Date date;

	public ChatMessage(String content, Date date) {
		this.content = content;
		this.date = date;
	}

	public UUID getChatId() {
		return chatId;
	}

	public void setChatId(UUID chatId) {
		this.chatId = chatId;
	}

	public UUID getSender() {
		return sender;
	}

	public void setSender(UUID sender) {
		this.sender = sender;
	}

	public UUID getReceiver() {
		return receiver;
	}

	public void setReceiver(UUID receiver) {
		this.receiver = receiver;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
