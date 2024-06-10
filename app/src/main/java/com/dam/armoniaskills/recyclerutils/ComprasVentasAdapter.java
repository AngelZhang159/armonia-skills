package com.dam.armoniaskills.recyclerutils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dam.armoniaskills.R;
import com.dam.armoniaskills.dto.ComprasVentasDTO;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ComprasVentasAdapter extends RecyclerView.Adapter<ComprasVentasAdapter.ComprasVentasVH> implements View.OnClickListener {

	private final ArrayList<ComprasVentasDTO> comprasVentas;
	private View.OnClickListener listener;

	public ComprasVentasAdapter(ArrayList<ComprasVentasDTO> comprasVentas, View.OnClickListener listener) {
		this.comprasVentas = comprasVentas;
		this.listener = listener;
	}

	@NonNull
	@Override
	public ComprasVentasVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venta, parent, false);

		v.setOnClickListener(this);

		return new ComprasVentasVH(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ComprasVentasVH holder, int position) {
		holder.bindCompraVenta(comprasVentas.get(position));
	}

	@Override
	public int getItemCount() {
		return comprasVentas.size();
	}

	@Override
	public void onClick(View v) {
		if (listener != null) {
			listener.onClick(v);
		}
	}

	public class ComprasVentasVH extends RecyclerView.ViewHolder {

		TextView tvNombreUsuario, tvNombreSkill, tvStatus, tvHora;
		CircleImageView ivFotoPerfil;

		public ComprasVentasVH(@NonNull View itemView) {
			super(itemView);

			tvNombreUsuario = itemView.findViewById(R.id.tvUsuarioVenta);
			tvNombreSkill = itemView.findViewById(R.id.tvServicioVenta);
			tvStatus = itemView.findViewById(R.id.tvStatusVenta);
			tvHora = itemView.findViewById(R.id.tvFechaVenta);
			ivFotoPerfil = itemView.findViewById(R.id.ivFotoPerfilVenta);

		}

		public void bindCompraVenta(ComprasVentasDTO comprasVentasDTO) {
			tvNombreUsuario.setText(comprasVentasDTO.getUsername());
			tvNombreSkill.setText(comprasVentasDTO.getSkillName());
			tvStatus.setText(comprasVentasDTO.getStatus().toString());
			tvHora.setText(comprasVentasDTO.getDate().toString());

			String url = "http://10.0.2.2:8080" + comprasVentasDTO.getImageURL();

			Glide.with(itemView.getContext())
					.load(url)
					.into(ivFotoPerfil);
		}
	}
}
