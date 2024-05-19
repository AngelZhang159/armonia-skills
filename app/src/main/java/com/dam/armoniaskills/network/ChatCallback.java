package com.dam.armoniaskills.network;

import com.dam.armoniaskills.model.ChatRoom;

public abstract class ChatCallback {

	public abstract void onChatCreated(ChatRoom chatRoom);

	public void onError() {
	}
}
