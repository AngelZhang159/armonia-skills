package com.dam.armoniaskills;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dam.armoniaskills.listutils.CategoriaAdapter;
import com.dam.armoniaskills.model.Categoria;

import java.util.ArrayList;

public class SeleccionActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

	public static final String CLAVE_CAT = "CATEGORIA";
	public static final String CLAVE_TITULO = "TITULO";

	CategoriaAdapter adapter;
	ArrayList<Categoria> listaCategorias;

	EditText etTitulo;
	ListView lstCat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seleccion);

		etTitulo = findViewById(R.id.etTituloSkill);
		lstCat = findViewById(R.id.lvCategorias);

		listaCategorias = new ArrayList<>();
		rellenarLista();

		adapter = new CategoriaAdapter(this, listaCategorias);
		lstCat.setAdapter(adapter);

		lstCat.setOnItemClickListener(this);
	}

	private void rellenarLista() {
		String[] nombres = {getString(R.string.categoria1), getString(R.string.categoria2), getString(R.string.categoria3), getString(R.string.categoria4)
				, getString(R.string.categoria5), getString(R.string.categoria6), getString(R.string.categoria7), getString(R.string.categoria8),
				getString(R.string.categoria9), getString(R.string.categoria10), getString(R.string.categoria11), getString(R.string.categoria12),
				getString(R.string.categoria13), getString(R.string.categoria14), getString(R.string.categoria15), getString(R.string.categoria16),
				getString(R.string.categoria17), getString(R.string.categoria18), getString(R.string.categoria19), getString(R.string.categoria20)};

		int[] imagenes = {R.drawable.design, R.drawable.digitalmarketing, R.drawable.videoediting,
				R.drawable.musicalnote, R.drawable.webprogramming, R.drawable.cooperation,
				R.drawable.photography, R.drawable.helmet, R.drawable.house, R.drawable.redcarpet,
				R.drawable.transportation, R.drawable.skincare, R.drawable.carrepair, R.drawable.education,
				R.drawable.fastfood, R.drawable.financialadvisor, R.drawable.healthcare, R.drawable.pets,
				R.drawable.electriccar, R.drawable.dancer, R.drawable.travelandtourism, R.drawable.menu};

		for (int i = 0; i < nombres.length; i++) {
			Categoria categoria = new Categoria(imagenes[i], nombres[i]);
			listaCategorias.add(categoria);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String titulo = etTitulo.getText().toString();

		if (titulo.isEmpty()) {
			Toast.makeText(this, R.string.titulo_obligatorio, Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent(this, NuevaSkillActivity.class);
			i.putExtra(CLAVE_CAT, listaCategorias.get(position));
			i.putExtra(CLAVE_TITULO, titulo);
			startActivity(i);
		}
	}
}