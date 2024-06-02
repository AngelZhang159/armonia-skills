package com.dam.armoniaskills.network;

import com.dam.armoniaskills.model.User;

public interface UserCallback {
	void onUserLoaded(User user);

	void onError();
}