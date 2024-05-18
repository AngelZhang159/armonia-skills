package com.dam.armoniaskills.model;

import java.sql.Date;
import java.util.UUID;

public class ChatMessage {
	private UUID id;
	private UUID chatId;
	private UUID sender;
	private UUID receiver;
	private String content;
	private MessageType type;
	private Date date;
}
