package com.dam.armoniaskills.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

	EditText etEmail, etContra;
	Button btnLogin, btnRegistro;

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

		btnLogin.setOnClickListener(this);
		btnRegistro.setOnClickListener(this);

	}


	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btnIniciarSesionLogin) {
			String email = etEmail.getText().toString().trim();
			String contra = etContra.getText().toString().trim();

			if (email.isEmpty() || contra.isEmpty()) {
				Toast.makeText(this, getString(R.string.campos_obligatorios), Toast.LENGTH_SHORT).show();
			} else {
				User user = new User(email, contra);
				iniciarSesion(user);
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

					Toast.makeText(LoginActivity.this, R.string.ok_login, Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				} else {
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
}
