package com.dam.armoniaskills.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.armoniaskills.NuevaSkillActivity;
import com.dam.armoniaskills.R;
import com.dam.armoniaskills.TopBarActivity;
import com.dam.armoniaskills.authentication.SharedPrefManager;
import com.dam.armoniaskills.model.Skill;
import com.dam.armoniaskills.network.RetrofitClient;
import com.dam.armoniaskills.recyclerutils.AdapterImagenes;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfiguracionFragment extends Fragment implements View.OnClickListener {

	private static final String ARG_SKILL = "SKILL";
	EditText etTitulo, etDescripcion, etPrecio;
	Button btnEditar, btnEliminar;
	RecyclerView rvImagenes;
	AdapterImagenes adapter;
	List<String> listaImagenes;
	List<Uri> listaUris;
	private Skill skill;

	public ConfiguracionFragment() {
		// Required empty public constructor
	}

	public static ConfiguracionFragment newInstance(Skill skill) {
		ConfiguracionFragment fragment = new ConfiguracionFragment();
		Bundle args = new Bundle();
		args.putParcelable(ARG_SKILL, skill);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			skill = getArguments().getParcelable(ARG_SKILL);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_configuracion, container, false);

		etTitulo = v.findViewById(R.id.etTituloConfig);
		etDescripcion = v.findViewById(R.id.etDescripcionConfig);
		etPrecio = v.findViewById(R.id.etPrecioConfig);
		rvImagenes = v.findViewById(R.id.rvImagenesConfig);
		btnEditar = v.findViewById(R.id.btnUpdateConfig);
		btnEliminar = v.findViewById(R.id.btnEliminar);

		etTitulo.setText(skill.getTitle());
		etDescripcion.setText(skill.getDescription());
		etPrecio.setText(skill.getPrice());

		listaImagenes = new ArrayList<>();
		listaImagenes = skill.getImageList();

		listaUris = new ArrayList<>();
		for (String imageUrl : listaImagenes) {
			Uri imageUri = Uri.parse(imageUrl);
			listaUris.add(imageUri);
		}

		int desiredSize = 10;
		while (listaUris.size() < desiredSize) {
			listaUris.add(null);
		}

		configurarRv();

		btnEliminar.setOnClickListener(this);
		btnEditar.setOnClickListener(this);

		return v;
	}

	private void configurarRv() {
        adapter = new AdapterImagenes(listaUris, this);
        rvImagenes.setLayoutManager(new GridLayoutManager(getContext(), 5));
        rvImagenes.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnEliminar) {
			mostrarDialogEliminar();
		} else if (v.getId() == R.id.btnUpdateConfig) {
			updateSkill();
		}
	}

	private void updateSkill() {
	}

	private void mostrarDialogEliminar() {
		MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getContext());

		materialAlertDialogBuilder
				.setCancelable(false)
				.setTitle(R.string.btn_eliminar)
				.setMessage(R.string.dialog_msg_eliminar)
				.setPositiveButton(R.string.btn_eliminar_d, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						borrarSkill();
					}
				})
				.setNegativeButton(R.string.btn_cancelar_d, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		materialAlertDialogBuilder.show();
	}

	private void borrarSkill() {

		SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());

		Call<ResponseBody> call = RetrofitClient
				.getInstance()
				.getApi()
				.deleteSkill(sharedPrefManager.fetchJwt(), skill.getId());

		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				Toast.makeText(getContext(), R.string.skill_eliminada, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getContext(), TopBarActivity.class);
				intent.putExtra("rellenar", "fragmentoPerfil");
				startActivity(intent);
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {
				Toast.makeText(getContext(), R.string.error_eliminar_skill, Toast.LENGTH_SHORT).show();

			}
		});

	}
}