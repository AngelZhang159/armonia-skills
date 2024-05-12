package com.dam.armoniaskills;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dam.armoniaskills.authentication.LoginActivity;
import com.dam.armoniaskills.authentication.SharedPrefManager;
import com.dam.armoniaskills.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EdgeToEdge.enable(this);
		setContentView(R.layout.activity_splash);
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
			Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
			return insets;
		});

		SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
		String jwt = sharedPrefManager.fetchJwt();
		if (jwt != null) {
			loginJWT(jwt);
		} else {
			abrirLogin();
		}
	}

	private void abrirLogin() {
		Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	private void loginJWT(String jwt) {
		Call<ResponseBody> call = RetrofitClient
				.getInstance()
				.getApi()
				.loginJWT(jwt);

		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

				Log.e("Login JWT", "onResponse: " + response);

				if (response.isSuccessful()) {
					Toast.makeText(SplashActivity.this, R.string.ok_login, Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(SplashActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				} else {
					abrirLogin();
				}
			}

			@Override
			public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
				Log.e("Login JWT", "onFailure: " + t.getMessage());
				Toast.makeText(SplashActivity.this, R.string.err_servidor, Toast.LENGTH_SHORT).show();
				abrirLogin();
			}
		});
	}

}