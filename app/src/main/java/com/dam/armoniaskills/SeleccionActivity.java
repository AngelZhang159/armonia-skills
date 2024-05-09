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

	CategoriaAdapter adapter;
	ArrayList<Categoria> listaCategorias;
	Categoria categoria;

	EditText etTitulo;
	ListView lstCat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seleccion);

		etTitulo = findViewById(R.id.etTituloSkill);
		lstCat = findViewById(R.id.lvCategorias);

		listaCategorias = new ArrayList<>();
		categoria = new Categoria(R.drawable.baseline_add_24, "Hola");
		listaCategorias.add(categoria);

		adapter = new CategoriaAdapter(this, listaCategorias);
		lstCat.setAdapter(adapter);

		lstCat.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String titulo = etTitulo.getText().toString();

		if (titulo.isEmpty()) {
			Toast.makeText(this, "Debe introducir un título", Toast.LENGTH_SHORT).show();
		} else {
			Intent i = new Intent(this, NuevaSkillActivity.class);
			i.putExtra(CLAVE_CAT, listaCategorias.get(position));
			startActivity(i);
		}
	}
}