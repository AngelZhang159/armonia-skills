package com.dam.armoniaskills;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.dam.armoniaskills.model.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MiPerfilActivity extends AppCompatActivity implements View.OnClickListener {

	ImageView ivPerfil;
	EditText etNombre, etEmail, etUsername,etTlf, etPassword, etRepPassword;
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
			//Actualizar los datos del usuario
		}
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
				//Actualizar contraseña del usuario
			} else {
				Toast.makeText(this, getString(R.string.contra_no_coinciden), Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void readUsuario() {
		//Rellenar user con el usuario acutalmente logeado
	}
}