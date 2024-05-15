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

import com.dam.armoniaskills.model.Categoria;
import com.dam.armoniaskills.model.Skill;
import com.dam.armoniaskills.recyclerutils.AdapterImagenes;

import java.util.ArrayList;
import java.util.List;

public class NuevaSkillActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rvImagenes;
    AdapterImagenes adapter;
    EditText etTitulo, etDescripcion, etPrecio, etCiudad;
    TextView tvCategoria;
    ImageView imvCategoria;
    Button btnConfirmar, btnCancelar;

    Uri uriVacio;
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
                    listaImagenes.set(pos, uriVacio);
                    adapter.notifyDataSetChanged();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_skill);

        uriVacio = null;
        primera = true;

        rvImagenes = findViewById(R.id.rvImagenes);
        etTitulo = findViewById(R.id.etTituloNuevaSkill);
        etDescripcion = findViewById(R.id.etDescNuevaSkill);
        etPrecio = findViewById(R.id.etPrecioNuevaSkill);
        tvCategoria = findViewById(R.id.tvCategoriaNuevaSkill);
        imvCategoria = findViewById(R.id.imgCategoriaNuevaSkill);
        btnConfirmar = findViewById(R.id.btnConfirmarNuevaSkill);
        btnCancelar = findViewById(R.id.btnCancelarNuevaSkill);

        listaImagenes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            listaImagenes.add(uriVacio);
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

            if(listaImagenes.isEmpty()) {
                Toast.makeText(this, "Debe seleccionar al menos una imagen", Toast.LENGTH_SHORT).show();
            } else if (titulo.isEmpty()) {
                Toast.makeText(this, "Debe introducir un título", Toast.LENGTH_SHORT).show();
            } else if (descripcion.isEmpty()) {
                Toast.makeText(this, "Debe introducir una descripción", Toast.LENGTH_SHORT).show();
            } else if (precio.isEmpty()) {
                Toast.makeText(this, "Debe introducir un precio", Toast.LENGTH_SHORT).show();
            } else {
                //TODO
                //Skill skill = new Skill(titulo, descripcion, categoria, precio, ciudad,  ,listaImagenes);
                //crearSkill(skill);
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

    }

    private void configurarRV() {
        adapter = new AdapterImagenes(listaImagenes, this);
        rvImagenes.setLayoutManager(new GridLayoutManager(this, 5));
        rvImagenes.setAdapter(adapter);
    }
}