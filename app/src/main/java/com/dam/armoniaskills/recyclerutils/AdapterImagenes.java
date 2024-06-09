package com.dam.armoniaskills.recyclerutils;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dam.armoniaskills.R;

import java.util.List;

public class AdapterImagenes extends RecyclerView.Adapter<AdapterImagenes.ImagenesVH> implements View.OnClickListener {

	List<Uri> listaUris;
	View.OnClickListener listener;

	public AdapterImagenes(List<Uri> listaUris, View.OnClickListener listener) {
		this.listaUris = listaUris;
		this.listener = listener;
	}

	@NonNull
	@Override
	public ImagenesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imagen, parent, false);

		v.setOnClickListener(this);

		return new ImagenesVH(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ImagenesVH holder, int position) {
		holder.bindImagen(listaUris.get(position));
	}

	@Override
	public int getItemCount() {
		return listaUris.size();
	}

	@Override
	public void onClick(View v) {
		listener.onClick(v);
	}

	public class ImagenesVH extends RecyclerView.ViewHolder {

		ImageView imv;

		public ImagenesVH(@NonNull View itemView) {
			super(itemView);
			imv = itemView.findViewById(R.id.imvRv);
		}

		public void bindImagen(Uri uri) {
			if (uri == null) {
				imv.setImageResource(R.drawable.image);
			} else {
				Glide.with(itemView.getContext()).load(uri).into(imv);
			}
		}
	}
}
