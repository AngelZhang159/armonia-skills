package com.dam.armoniaskills.webSocket;

import okhttp3.WebSocket;

public class WebSocketSingleton {
	private static WebSocketSingleton instance = null;
	private WebSocket webSocket;

	private WebSocketSingleton() {
	}

	public static WebSocketSingleton getInstance() {
		if (instance == null) {
			instance = new WebSocketSingleton();
		}
		return instance;
	}

	public WebSocket getWebSocket() {
		return this.webSocket;
	}

	public void setWebSocket(WebSocket webSocket) {
		this.webSocket = webSocket;
	}
}