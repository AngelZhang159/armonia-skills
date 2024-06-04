package com.dam.armoniaskills.listutils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dam.armoniaskills.R;
import com.dam.armoniaskills.model.Categoria;

import java.util.ArrayList;

public class CategoriaAdapter extends ArrayAdapter<Categoria> {

	public CategoriaAdapter(@NonNull Context context, ArrayList<Categoria> listaCategorias) {
		super(context, R.layout.item_categoria, listaCategorias);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
		Categoria categoria = getItem(position);

		view = LayoutInflater.from(getContext()).inflate(R.layout.item_categoria, parent, false);

		ImageView img = view.findViewById(R.id.imgCategoria);
		TextView tv = view.findViewById(R.id.tvCategoria);

		img.setImageResource(categoria.getImagen());
		tv.setText(categoria.getTitulo());

		return view;
	}

	@Override
	public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		Categoria categoria = getItem(position);

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_categoria, parent, false);
		}

		ImageView img = convertView.findViewById(R.id.imgCategoria);
		TextView tv = convertView.findViewById(R.id.tvCategoria);

		if (categoria != null) {
			img.setImageResource(categoria.getImagen());
			tv.setText(categoria.getTitulo());
		}

		return convertView;
	}
}
