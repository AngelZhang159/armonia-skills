package com.dam.armoniaskills;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.armoniaskills.model.Categoria;
import com.dam.armoniaskills.recyclerutils.AdapterImagenes;

import java.util.ArrayList;
import java.util.List;

public class NuevaSkillActivity extends AppCompatActivity implements View.OnClickListener {

    Categoria categoria;
    RecyclerView rvImagenes;
    AdapterImagenes adapter;
    Uri imageUri;
    List<Uri> listaImagenes;
    int pos;

    ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia = registerForActivityResult(
            new ActivityResultContracts.PickMultipleVisualMedia(10), listaUris -> {
                if (listaUris != null) {
                    listaImagenes = listaUris;
                    configurarRV();
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
                    listaImagenes.remove(pos);
                    adapter.notifyDataSetChanged();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_skill);

        rvImagenes = findViewById(R.id.rvImagenes);

        listaImagenes = new ArrayList<>();

        pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());

        categoria = getIntent().getParcelableExtra(SeleccionActivity.CLAVE_CAT);
    }

    @Override
    public void onClick(View v) {
        pos = rvImagenes.getChildAdapterPosition(v);

        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    private void configurarRV() {
        adapter = new AdapterImagenes(listaImagenes, this);
        rvImagenes.setLayoutManager(new GridLayoutManager(this, 5));
        rvImagenes.setAdapter(adapter);
    }
}