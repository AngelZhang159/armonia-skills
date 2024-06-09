package com.dam.armoniaskills.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.armoniaskills.R;
import com.dam.armoniaskills.SeleccionActivity;
import com.dam.armoniaskills.TopBarActivity;
import com.dam.armoniaskills.listutils.AdapterPrecios;
import com.dam.armoniaskills.listutils.CategoriaAdapter;
import com.dam.armoniaskills.model.Categoria;
import com.dam.armoniaskills.model.Skill;
import com.dam.armoniaskills.network.RetrofitClient;
import com.dam.armoniaskills.recyclerutils.AdapterSkills;
import com.dam.armoniaskills.utils.CategoriaUtil;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class InicioFragment extends Fragment implements View.OnClickListener {

	public static final String SKILL = "SKILL";

	RecyclerView rv;
	AdapterSkills adapter;
	ArrayList<Skill> listaSkills;
	SearchView searchView;
	SearchBar searchBar;
	Spinner spinner, spinnerPrecio;
	LinearProgressIndicator progressBar;

	public InicioFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_inicio, container, false);

		rv = v.findViewById(R.id.rvSkills);
		searchView = v.findViewById(R.id.searchView);
		searchBar = v.findViewById(R.id.searchBar);
		spinner = v.findViewById(R.id.spinner);
		spinnerPrecio = v.findViewById(R.id.spinnerPrecio);
		progressBar = v.findViewById(R.id.progressBarInicio);

		ArrayList<Categoria> listaCategorias = CategoriaUtil.rellenarLista(getContext());
		CategoriaAdapter adapter = new CategoriaAdapter(getContext(), listaCategorias);
		spinner.setAdapter(adapter);

		String[] rangosPrecio = new String[]{"0-100", "100-200", "200-300", "300-400", "400-500", "500-600", "600-700", "700-800", "800-900", "900+"};
		AdapterPrecios adapterPrecio = new AdapterPrecios(getContext(), rangosPrecio);
		spinnerPrecio.setAdapter(adapterPrecio);

		searchView.getEditText().setOnEditorActionListener((view, actionId, event) -> {
			searchBar.setText(searchView.getText());
			if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {

				Categoria categoriaSeleccionada = (Categoria) spinner.getSelectedItem();
				String categoria = categoriaSeleccionada.getTitulo();

				if(searchView.getText().toString().isEmpty()){
					cargarSkillsPorCategoria(categoria);
				}else {
					Log.i("searchBar", "categoria: " + categoria + " query: " + searchView.getText());
					cargarSkills(categoria, searchView.getText().toString());
				}

				searchView.hide();
				return true;
			}
			return false;
		});


		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// Categoria seleccionada
				Categoria categoriaSeleccionada = (Categoria) parent.getItemAtPosition(position);

				// Título de la categoría seleccionada
				String titulo = categoriaSeleccionada.getTitulo();
				String query = searchBar.getText().toString();

				if(query.isEmpty()){
					cargarSkillsPorCategoria(titulo);
				} else{
					Log.i("spinner", "categoria: " + titulo + " query: " + query);

					cargarSkills(titulo, query);
				}




			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		listaSkills = new ArrayList<>();
		cargarSkills();

		return v;
	}

	private void cargarSkills() {
		//Recuperar skills backend

		Call<List<Skill>> call = RetrofitClient
				.getInstance()
				.getApi()
				.getSkills();

		call.enqueue(new retrofit2.Callback<List<Skill>>() {
			@Override
			public void onResponse(@NonNull Call<List<Skill>> call, @NonNull Response<List<Skill>> response) {
				if (response.isSuccessful()) {
					listaSkills.clear();
					listaSkills.addAll(response.body());
					configurarRV();
					progressBar.hide();
				}
			}

			@Override
			public void onFailure(@NonNull Call<List<Skill>> call, @NonNull Throwable t) {
				Toast.makeText(getContext(), R.string.error_skills, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void cargarSkills(String category, String query) {

		Log.i("alcachofas", "categoria: " + category + " query: " + query);
		Call<List<Skill>> call = RetrofitClient
				.getInstance()
				.getApi()
				.getSkills(category, query);

		call.enqueue(new retrofit2.Callback<List<Skill>>() {
			@Override
			public void onResponse(@NonNull Call<List<Skill>> call, @NonNull Response<List<Skill>> response) {
				if (response.isSuccessful()) {
					listaSkills.clear();
					listaSkills.addAll(response.body());
					if (adapter == null) {
						configurarRV();
					} else {
						adapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onFailure(@NonNull Call<List<Skill>> call, @NonNull Throwable t) {
				Toast.makeText(getContext(), R.string.error_skills, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void cargarSkillsPorCategoria(String titulo) {

		Call<List<Skill>> call = RetrofitClient
				.getInstance()
				.getApi()
				.getSkillsByCategory(titulo);

		call.enqueue(new retrofit2.Callback<List<Skill>>() {
			@Override
			public void onResponse(@NonNull Call<List<Skill>> call, @NonNull Response<List<Skill>> response) {
				if (response.isSuccessful()) {
					listaSkills.clear();
					listaSkills.addAll(response.body());
					if (adapter == null) {
						configurarRV();
					} else {
						adapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onFailure(@NonNull Call<List<Skill>> call, @NonNull Throwable t) {
				Toast.makeText(getContext(), R.string.error_skills, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onClick(View v) {
		int pos = rv.getChildAdapterPosition(v);
		Skill skill = listaSkills.get(pos);

		Intent i = new Intent(getContext(), TopBarActivity.class);
		i.putExtra(SKILL, skill);
		i.putExtra("rellenar", "fragmentoHome");
		startActivity(i);
	}

	private void configurarRV() {
		adapter = new AdapterSkills(listaSkills, this);
		rv.setLayoutManager(new LinearLayoutManager(getContext()));
		rv.setAdapter(adapter);
	}
}