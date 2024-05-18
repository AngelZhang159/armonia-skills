package com.dam.armoniaskills;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.armoniaskills.authentication.SharedPrefManager;
import com.dam.armoniaskills.model.Categoria;
import com.dam.armoniaskills.model.Skill;
import com.dam.armoniaskills.network.ImageUpload;
import com.dam.armoniaskills.network.RetrofitClient;
import com.dam.armoniaskills.network.UploadCallback;
import com.dam.armoniaskills.recyclerutils.AdapterImagenes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevaSkillActivity extends AppCompatActivity implements View.OnClickListener {

	RecyclerView rvImagenes;
	AdapterImagenes adapter;
	EditText etTitulo, etDescripcion, etPrecio, etCiudad;
	TextView tvCategoria;
	ImageView imvCategoria;
	Button btnConfirmar, btnCancelar;

	Uri imageUri;
	List<Uri> listaImagenes;
	int pos;
	String tituloI;
	Categoria categoria;
	boolean primera;

	ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia = registerForActivityResult(
			new ActivityResultContracts.PickMultipleVisualMedia(10), listaUris -> {
				if (listaUris != null) {
					for (int i = 0; i < listaUris.size(); i++) {
						listaImagenes.set(i, listaUris.get(i));
					}
					primera = false;
					adapter.notifyDataSetChanged();
				} else {
					Toast.makeText(this, "No ha seleccionado ninguna imagen", Toast.LENGTH_SHORT).show();
				}
			});

	ActivityResultLauncher<PickVisualMediaRequest> pickMedia = registerForActivityResult(
			new ActivityResultContracts.PickVisualMedia(), uri -> {
				if (uri != null) {
					imageUri = uri;
					listaImagenes.set(pos, imageUri);
					adapter.notifyDataSetChanged();
				} else {
					listaImagenes.set(pos, null);
					adapter.notifyDataSetChanged();
				}
			});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nueva_skill);

		primera = true;

		rvImagenes = findViewById(R.id.rvImagenes);
		etTitulo = findViewById(R.id.etTituloNuevaSkill);
		etDescripcion = findViewById(R.id.etDescNuevaSkill);
		etPrecio = findViewById(R.id.etPrecioNuevaSkill);
		etCiudad = findViewById(R.id.etCiudadNuevaSkill);
		tvCategoria = findViewById(R.id.tvCategoriaNuevaSkill);
		imvCategoria = findViewById(R.id.imgCategoriaNuevaSkill);
		btnConfirmar = findViewById(R.id.btnConfirmarNuevaSkill);
		btnCancelar = findViewById(R.id.btnCancelarNuevaSkill);

		listaImagenes = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			listaImagenes.add(null);
		}

		configurarRV();

		tituloI = getIntent().getStringExtra(SeleccionActivity.CLAVE_TITULO);
		categoria = getIntent().getParcelableExtra(SeleccionActivity.CLAVE_CAT);

		etTitulo.setText(tituloI);
		imvCategoria.setImageResource(categoria.getImagen());
		tvCategoria.setText(categoria.getTitulo());

		btnConfirmar.setOnClickListener(this);
		btnCancelar.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnConfirmarNuevaSkill) {
			String titulo = etTitulo.getText().toString();
			String descripcion = etDescripcion.getText().toString();
			String precio = etPrecio.getText().toString();
			String ciudad = etCiudad.getText().toString();

			if (listaImagenes.isEmpty()) {
				Toast.makeText(this, "Debe seleccionar al menos una imagen", Toast.LENGTH_SHORT).show();
			} else if (titulo.isEmpty()) {
				Toast.makeText(this, "Debe introducir un título", Toast.LENGTH_SHORT).show();
			} else if (descripcion.isEmpty()) {
				Toast.makeText(this, "Debe introducir una descripción", Toast.LENGTH_SHORT).show();
			} else if (precio.isEmpty()) {
				Toast.makeText(this, "Debe introducir un precio", Toast.LENGTH_SHORT).show();
			} else {


				List<String> imageList = new ArrayList<>();
				ImageUpload imageUpload = new ImageUpload();

				for (int i = listaImagenes.size() - 1; i >= 0; i--) {
					if (listaImagenes.get(i) == null) {
						listaImagenes.remove(i);
					}
				}

				CountDownLatch latch = new CountDownLatch(listaImagenes.size());


				for (Uri uri : listaImagenes) {
					imageUpload.subirImagen(uri, getContentResolver(), new UploadCallback() {
						@Override
						public void onSuccess(String result) {
							imageList.add(result);
							latch.countDown();

						}

						@Override
						public void onError(Throwable throwable) {
							Toast.makeText(NuevaSkillActivity.this, "Error al subir imagen", Toast.LENGTH_SHORT).show();
							latch.countDown();

						}
					});
				}

				// Crear Skill y guardarla en la BBDD
				new Thread(() -> {
					try {
						latch.await();
						runOnUiThread(() -> {
							Skill skill = new Skill(titulo, descripcion, categoria.getTitulo(), precio, ciudad, null, imageList);
							crearSkill(skill);
						});
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}).start();

			}
		} else if (v.getId() == R.id.btnCancelarNuevaSkill) {
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
		} else {
			if (primera) {
				pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
						.setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
						.build());
			} else {
				pos = rvImagenes.getChildAdapterPosition(v);

				pickMedia.launch(new PickVisualMediaRequest.Builder()
						.setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
						.build());
			}
		}
	}

	private void crearSkill(Skill skill) {

		SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());

		Call<ResponseBody> call = RetrofitClient
				.getInstance()
				.getApi()
				.postSkill(sharedPrefManager.fetchJwt(), skill);

		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				Toast.makeText(NuevaSkillActivity.this, R.string.skill_creada, Toast.LENGTH_SHORT).show();
				finish();
				finish();
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable throwable) {
				Toast.makeText(NuevaSkillActivity.this, R.string.error_creacion_skill, Toast.LENGTH_SHORT).show();

			}
		});


	}

	private void configurarRV() {
		adapter = new AdapterImagenes(listaImagenes, this);
		rvImagenes.setLayoutManager(new GridLayoutManager(this, 5));
		rvImagenes.setAdapter(adapter);
	}
}