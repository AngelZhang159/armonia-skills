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
		, getString(R.string.categoria5), getString(R.string.categoria6), getString(R.string.categoria7), getString(R.string.categoria8)};

		int[] imagenes = {R.drawable.design, R.drawable.digitalmarketing, R.drawable.videoediting,
				R.drawable.musicalnote, R.drawable.webprogramming, R.drawable.cooperation,
				R.drawable.photography, R.drawable.helmet};

		for (int i = 0; i < nombres.length; i++) {
			Categoria categoria = new Categoria(imagenes[i], nombres[i]);
			listaCategorias.add(categoria);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String titulo = etTitulo.getText().toString();

		if (titulo.isEmpty()) {
			Toast.makeText(this, "Debe introducir un título", Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent(this, NuevaSkillActivity.class);
			i.putExtra(CLAVE_CAT, listaCategorias.get(position));
			i.putExtra(CLAVE_TITULO, titulo);
			startActivity(i);
		}
	}
}