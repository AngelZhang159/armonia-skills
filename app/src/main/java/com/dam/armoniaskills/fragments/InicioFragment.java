package com.dam.armoniaskills.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.armoniaskills.R;
import com.dam.armoniaskills.TopBarActivity;
import com.dam.armoniaskills.model.Skill;
import com.dam.armoniaskills.network.RetrofitClient;
import com.dam.armoniaskills.recyclerutils.AdapterSkills;
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

		searchView.getEditText().setOnEditorActionListener((view, actionId, event) -> {
			searchBar.setText(searchView.getText());
			if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
				if (searchView.getText().toString().isEmpty()){
					cargarSkills();

				} else {
					cargarSkills(searchView.getText().toString());
				}
				searchView.hide();
				return true;
			}
			return false;
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
				}
			}

			@Override
			public void onFailure(@NonNull Call<List<Skill>> call, @NonNull Throwable t) {
				Toast.makeText(getContext(), R.string.error_skills, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void cargarSkills(String query) {
		Log.e("RETROFIT", "Buscando skills con query: " + query);

		Call<List<Skill>> call = RetrofitClient
				.getInstance()
				.getApi()
				.getSkills(query);

		call.enqueue(new retrofit2.Callback<List<Skill>>() {
			@Override
			public void onResponse(@NonNull Call<List<Skill>> call, @NonNull Response<List<Skill>> response) {
				if (response.isSuccessful()) {
					listaSkills.clear();
					listaSkills.addAll(response.body());
					adapter.notifyDataSetChanged();
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