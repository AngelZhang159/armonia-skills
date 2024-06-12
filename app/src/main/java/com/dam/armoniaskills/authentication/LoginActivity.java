package com.dam.armoniaskills.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dam.armoniaskills.MainActivity;
import com.dam.armoniaskills.R;
import com.dam.armoniaskills.model.User;
import com.dam.armoniaskills.network.RetrofitClient;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.messaging.FirebaseMessaging;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

	EditText etEmail, etContra;
	Button btnLogin, btnRegistro;
	CircularProgressIndicator progressBar;
	View overlay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EdgeToEdge.enable(this);
		setContentView(R.layout.activity_login);
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
			Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
			return insets;
		});

		etEmail = findViewById(R.id.login_email);
		etContra = findViewById(R.id.login_password);
		btnLogin = findViewById(R.id.btnIniciarSesionLogin);
		btnRegistro = findViewById(R.id.btnRegistroLogin);
		progressBar = findViewById(R.id.progressBarLogin);
		overlay = findViewById(R.id.overlayLogin);

		if (savedInstanceState != null) {
			etEmail.setText(savedInstanceState.getString("email"));
			etContra.setText(savedInstanceState.getString("contra"));
		}

		btnLogin.setOnClickListener(this);
		btnRegistro.setOnClickListener(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putString("email", etEmail.getText().toString());
		outState.putString("contra", etContra.getText().toString());
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnIniciarSesionLogin) {
			String email = etEmail.getText().toString().trim();
			String contra = etContra.getText().toString().trim();

			hideKeyboard();

			if (email.isEmpty() || contra.isEmpty()) {
				Toast.makeText(this, getString(R.string.campos_obligatorios), Toast.LENGTH_SHORT).show();
			} else {
				User user = new User(email, contra);
				iniciarSesion(user);
				overlay.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.VISIBLE);
			}
		} else if (v.getId() == R.id.btnRegistroLogin) {
			Intent intent = new Intent(this, RegistroActivity.class);
			startActivity(intent);
		}
	}

	private void iniciarSesion(User user) {
		Call<ResponseBody> call = RetrofitClient
				.getInstance()
				.getApi()
				.loginUser(user);

		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

				Log.e("Login", "onResponse: " + response);

				if (response.isSuccessful()) {

					String jwtToken = response.headers().get("Authorization");

					SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
					sharedPrefManager.saveJwt(jwtToken);

					Log.e("JWT", "onResponse: " + jwtToken);

					FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
						if (!task.isSuccessful()) {
							Toast.makeText(LoginActivity.this, R.string.error_token, Toast.LENGTH_SHORT).show();
							Log.e("FCM Token", "Fetching FCM registration token failed", task.getException());

							progressBar.hide();
							overlay.setVisibility(View.GONE);

							return;
						}

						String token = task.getResult();
						sharedPrefManager.saveFCMToken(token);
						Log.i("FCM Token", "TOKEN REGISTRADO: " + token);
						Toast.makeText(LoginActivity.this, R.string.ok_login, Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(LoginActivity.this, MainActivity.class);
						startActivity(intent);

						progressBar.hide();
						overlay.setVisibility(View.GONE);

						finish();
					});

				} else {

					progressBar.hide();
					overlay.setVisibility(View.GONE);

					int statusCode = response.code();
					switch (statusCode) {
						case 401:
							Toast.makeText(LoginActivity.this, R.string.err_login, Toast.LENGTH_SHORT).show();
							break;
						case 404:
							Toast.makeText(LoginActivity.this, R.string.err_desconocido, Toast.LENGTH_SHORT).show();
							break;
						case 500:
							Toast.makeText(LoginActivity.this, R.string.err_servidor, Toast.LENGTH_SHORT).show();
							break;
					}
				}
			}

			@Override
			public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
				Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void hideKeyboard() {
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null) {
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
		}
	}
}
