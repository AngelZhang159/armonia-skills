package com.dam.armoniaskills;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dam.armoniaskills.listutils.CategoriaAdapter;
import com.dam.armoniaskills.model.Categoria;
import com.dam.armoniaskills.utils.CategoriaUtil;

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
		listaCategorias = CategoriaUtil.rellenarLista(this);
		listaCategorias.remove(0);

		adapter = new CategoriaAdapter(this, listaCategorias);
		lstCat.setAdapter(adapter);

		if (savedInstanceState != null) {
			etTitulo.setText(savedInstanceState.getString("titulo"));
		}

		lstCat.setOnItemClickListener(this);
	}

	@Override
	public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
		super.onSaveInstanceState(outState, outPersistentState);

		outState.putString("titulo", etTitulo.getText().toString());
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