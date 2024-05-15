package com.dam.armoniaskills;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dam.armoniaskills.authentication.LoginActivity;
import com.dam.armoniaskills.authentication.SharedPrefManager;
import com.dam.armoniaskills.model.User;
import com.dam.armoniaskills.network.RetrofitClient;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MiPerfilActivity extends AppCompatActivity implements View.OnClickListener {

	ImageView ivPerfil;
	EditText etNombre, etEmail, etUsername, etTlf, etPassword, etRepPassword;
	Button btnUpdate, btnCambiar, btnCerrarSesion;

	User user;
	Uri imageUri;

	ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(
			new ActivityResultContracts.PickVisualMedia(), uri -> {
				if (uri != null) {
					imageUri = uri;
					ivPerfil.setImageURI(uri);
				} else {
					Toast.makeText(MiPerfilActivity.this, getString(R.string.seleccionar_img), Toast.LENGTH_SHORT).show();
				}
			});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mi_perfil);

		etNombre = findViewById(R.id.etNombreP);
		etEmail = findViewById(R.id.etEmailP);
		etUsername = findViewById(R.id.etUserNameP);
		etTlf = findViewById(R.id.etTelefonoP);
		etPassword = findViewById(R.id.etPasswordP);
		etRepPassword = findViewById(R.id.etRepPasswordP);
		ivPerfil = findViewById(R.id.imvPerfilP);

		btnCambiar = findViewById(R.id.btnCambiar);
		btnUpdate = findViewById(R.id.btnUpdate);
		btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

		readUsuario();
		imageUri = null;

		btnUpdate.setOnClickListener(this);
		btnCambiar.setOnClickListener(this);
		btnCerrarSesion.setOnClickListener(this);
		ivPerfil.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnCambiar) {
			updatePassword();
		} else if (v.getId() == R.id.btnUpdate) {
			updateData();
		} else if (v.getId() == R.id.btnCerrarSesion) {
			mostrarDialog();
		} else if (v.getId() == R.id.imvPerfilP) {
			pickMedia.launch(new PickVisualMediaRequest.Builder()
					.setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
					.build());
		}
	}

	private void mostrarDialog() {
		MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);

		builder.setTitle(R.string.btn_cerrar_sesion)
				.setCancelable(false).
				setMessage(R.string.dialog_msg_perfil)
				.setPositiveButton(R.string.btn_aceptar_d, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//Cerrar sesion con Spring

						SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());

						sharedPrefManager.clearJwt();

						Intent i = new Intent(MiPerfilActivity.this, LoginActivity.class);
						startActivity(i);
					}
				})
				.setNegativeButton(R.string.btn_cancelar_d, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		builder.show();
	}

	private void updateData() {
		String nombre = etNombre.getText().toString();
		String username = etUsername.getText().toString();
		String tlf = etTlf.getText().toString();

		if (nombre.isEmpty() || username.isEmpty()) {
			Toast.makeText(MiPerfilActivity.this, getString(R.string.campos_obligatorios), Toast.LENGTH_SHORT).show();
		} else if (!tlf.isEmpty() && tlf.length() != 9) {
			Toast.makeText(MiPerfilActivity.this, getString(R.string.tlf_invalido), Toast.LENGTH_SHORT).show();
		} else {
			User user = new User();
			user.setFullName(nombre);
			user.setUsername(username);

			if (!tlf.isEmpty()) {
				user.setPhone(Integer.parseInt(tlf));
			}

			updateUsuario(user);


		}
	}

	private void updateUsuario(User user) {

		SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
		String jwt = sharedPrefManager.fetchJwt();

		Call<ResponseBody> call = RetrofitClient
				.getInstance()
				.getApi()
				.updateUser(jwt, user);

		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
				if (response.isSuccessful()) {
					Toast.makeText(MiPerfilActivity.this, getString(R.string.datos_actualizados), Toast.LENGTH_SHORT).show();
				} else {
					Log.e("Update User", "onResponse: " + response);
					Toast.makeText(MiPerfilActivity.this, getString(R.string.error_conexion), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
				Toast.makeText(MiPerfilActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void updatePassword() {
		String password = etPassword.getText().toString();
		String repPassword = etRepPassword.getText().toString();

		if (password.isEmpty()) {
			Toast.makeText(this, getString(R.string.contra_obligatoria), Toast.LENGTH_SHORT).show();
		} else if (repPassword.isEmpty()) {
			Toast.makeText(this, getString(R.string.rep_contra_obligatoria), Toast.LENGTH_SHORT).show();
		} else {
			if (password.equals(repPassword)) {

				User user = new User();
				user.setPassword(password);

				updateContraseniaUsuario(user);

			} else {
				Toast.makeText(this, getString(R.string.contra_no_coinciden), Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void updateContraseniaUsuario(User user) {

		SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
		String jwt = sharedPrefManager.fetchJwt();

		Call<ResponseBody> call = RetrofitClient
				.getInstance()
				.getApi()
				.updateUser(jwt, user);

		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
				if (response.isSuccessful()) {
					Toast.makeText(MiPerfilActivity.this, getString(R.string.contra_actualizada), Toast.LENGTH_SHORT).show();
					etPassword.setText("");
					etRepPassword.setText("");
				} else {
					Toast.makeText(MiPerfilActivity.this, getString(R.string.error_conexion), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
				Toast.makeText(MiPerfilActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});

	}

	private void readUsuario() {
		//Rellenar user con el usuario acutalmente logeado

		SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
		String jwt = sharedPrefManager.fetchJwt();

		Call<ResponseBody> call = RetrofitClient
				.getInstance()
				.getApi()
				.getUserData(jwt);

		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
				if (response.isSuccessful()) {
					try {
						String jsonData = response.body().string();
						JSONObject jsonObject = new JSONObject(jsonData);


						String username = jsonObject.getString("username");
						String email = jsonObject.getString("email");
						String fullName = jsonObject.getString("fullName");
						String phone = jsonObject.getString("phone");


						etUsername.setText(username);
						etEmail.setText(email);
						etNombre.setText(fullName);
						if (!phone.equals("0")) {
							etTlf.setText(phone);
						}

					} catch (IOException | JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(MiPerfilActivity.this, "Failed to get user data", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
				Toast.makeText(MiPerfilActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

}