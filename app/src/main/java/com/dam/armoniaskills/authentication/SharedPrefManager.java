package com.dam.armoniaskills.authentication;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

	private static final String PREFS_NAME = "MyPrefs";
	private static final String KEY_JWT = "jwt_token";

	private final SharedPreferences sharedPreferences;

	public SharedPrefManager(Context context) {
		sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}

	public void saveJwt(String jwt) {
		sharedPreferences.edit().putString(KEY_JWT, jwt).apply();
	}

	public String fetchJwt() {
		return sharedPreferences.getString(KEY_JWT, null);
	}

	public void clearJwt() {
		sharedPreferences.edit().remove(KEY_JWT).apply();
	}

	public void saveFCMToken(String token) {
		sharedPreferences.edit().putString("fcm_token", token).apply();
	}

	public String fetchFCMToken() {
		return sharedPreferences.getString("fcm_token", null);
	}

	public void clearFCMToken() {
		sharedPreferences.edit().remove("fcm_token").apply();
	}
}