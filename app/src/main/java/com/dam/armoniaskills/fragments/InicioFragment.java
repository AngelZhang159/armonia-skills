package com.dam.armoniaskills.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.armoniaskills.R;
import com.dam.armoniaskills.TopBarActivity;
import com.dam.armoniaskills.model.Skill;
import com.dam.armoniaskills.recyclerutils.AdapterImagenes;
import com.dam.armoniaskills.recyclerutils.AdapterSkills;

import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends Fragment implements View.OnClickListener {

	public static final String SKILL = "SKILL";

	RecyclerView rv;
	AdapterSkills adapter;
	ArrayList<Skill> listaSkills;

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

		listaSkills = new ArrayList<>();
		cargarSkills();

		return v;
	}

	private void cargarSkills() {
		//Recuperar skills backend
		List<String> lista = new ArrayList<>();
		lista.add("https://drive.google.com/file/d/16iffsZ_y3m-I8S-Ic-aF6AOJQ1-s8e3r/view?usp=sharing");
		listaSkills.add(new Skill("hola", "", "", "20", "", lista));
		listaSkills.add(new Skill("hola", "", "", "20", "", lista));
		configurarRV();
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
		rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
		rv.setAdapter(adapter);
	}
}