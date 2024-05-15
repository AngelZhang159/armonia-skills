package com.dam.armoniaskills.network;

public interface UploadCallback {
	void onSuccess(String result);

	void onError(Throwable throwable);
}