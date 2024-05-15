package com.dam.armoniaskills.authentication;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dam.armoniaskills.R;
import com.dam.armoniaskills.model.User;
import com.dam.armoniaskills.network.ImageUpload;
import com.dam.armoniaskills.network.RetrofitClient;
import com.dam.armoniaskills.network.UploadCallback;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

	private EditText etUsuario, etNombre, etApe, etEmail, etContra, etTelf;
	private CircleImageView imageView;
	private Button btnRegistrar;
	private Uri imageUri;
	private String imageURL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EdgeToEdge.enable(this);
		setContentView(R.layout.activity_registro);
		setupWindowInsets();
		initializeViews();
	}

	private void setupWindowInsets() {
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
			Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
			return insets;
		});
	}

	private void initializeViews() {
		etUsuario = findViewById(R.id.etUsuarioReg);
		etNombre = findViewById(R.id.etNomReg);
		etApe = findViewById(R.id.etApeReg);
		etEmail = findViewById(R.id.etEmailReg);
		etContra = findViewById(R.id.etContraReg);
		etTelf = findViewById(R.id.etTlfReg);

		imageView = findViewById(R.id.imvPerfil);
		btnRegistrar = findViewById(R.id.btnCrearCuentaReg);

		imageView.setOnClickListener(this);
		btnRegistrar.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnCrearCuentaReg) {
			createUser();
		} else if (v.getId() == R.id.imvPerfil) {
			pickMedia.launch(new PickVisualMediaRequest.Builder()
					.setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
					.build());
		}
	}

	private void createUser() {
		ImageUpload imageUpload = new ImageUpload();
		imageUpload.subirImagen(imageUri, getContentResolver(), new UploadCallback() {
			@Override
			public void onSuccess(String result) {
				imageURL = result;
				registerUser();
			}

			@Override
			public void onError(Throwable throwable) {
				imageURL = null;
				Log.e("Error imagen: ", throwable.toString());
				Toast.makeText(RegistroActivity.this, R.string.err_imagen_servidor, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void registerUser() {
		String username = etUsuario.getText().toString().trim();
		String name = etNombre.getText().toString().trim();
		String surname = etApe.getText().toString().trim();
		String email = etEmail.getText().toString().trim();
		String password = etContra.getText().toString().trim();

		int phone = 0;
		if (!etTelf.getText().toString().isEmpty()) {
			phone = Integer.parseInt(etTelf.getText().toString().trim());
		}

		if (username.isEmpty() || name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
			Toast.makeText(RegistroActivity.this, R.string.campos_obligatorios, Toast.LENGTH_SHORT).show();
		} else {
			User user = new User(name + " " + surname, username, email, phone, password, imageURL);
			register(user);
		}
	}

	private void register(User user) {
		Call<ResponseBody> call = RetrofitClient
				.getInstance()
				.getApi()
				.registerUser(user);
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
				if (response.isSuccessful()) {
					Toast.makeText(RegistroActivity.this, getString(R.string.registro_correcto), Toast.LENGTH_SHORT).show();
					finish();
				} else {
					handleRegistrationError(response);
				}
			}

			@Override
			public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
				Toast.makeText(RegistroActivity.this, "err server", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void handleRegistrationError(Response<ResponseBody> response) {
		if (response.code() == 409) {
			Toast.makeText(RegistroActivity.this, R.string.usuario_existente, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(RegistroActivity.this, "otro error no 409", Toast.LENGTH_SHORT).show();
		}
	}

	ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(
			new ActivityResultContracts.PickVisualMedia(), uri -> {
				if (uri != null) {
					imageUri = uri;
					imageView.setImageURI(imageUri);
				}
			});
}