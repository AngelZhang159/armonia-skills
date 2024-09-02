package com.dam.armoniaskills.recyclerutils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dam.armoniaskills.R;
import com.dam.armoniaskills.dto.ComprasVentasDTO;

import java.text.SimpleDateFormat;
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

		TextView tvNombreUsuario, tvNombreSkill, tvStatus, tvHora, tvPrecio;
		CircleImageView ivFotoPerfil;

		public ComprasVentasVH(@NonNull View itemView) {
			super(itemView);

			tvNombreUsuario = itemView.findViewById(R.id.tvUsuarioVenta);
			tvNombreSkill = itemView.findViewById(R.id.tvServicioVenta);
			tvStatus = itemView.findViewById(R.id.tvStatusVenta);
			tvHora = itemView.findViewById(R.id.tvFechaVenta);
			ivFotoPerfil = itemView.findViewById(R.id.ivFotoPerfilVenta);
			tvPrecio = itemView.findViewById(R.id.tvPrecioVenta);

		}

		public void bindCompraVenta(ComprasVentasDTO comprasVentasDTO) {
			tvNombreUsuario.setText(comprasVentasDTO.getUsername());
			tvNombreSkill.setText(comprasVentasDTO.getSkillName());

			String status = comprasVentasDTO.getStatus().toString();
			String statusTraducido = "";
			if (status.equals("PENDIENTE")){
				statusTraducido = itemView.getContext().getString(R.string.pendiente);
				tvStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.amarillo));
			} else if (status.equals("ACEPTADO")){
				statusTraducido = itemView.getContext().getString(R.string.aceptado);
				tvStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.verde));
			} else if (status.equals("RECHAZADO")){
				statusTraducido = itemView.getContext().getString(R.string.rechazado);
				tvStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.rojo));
			} else if (status.equals("ENVIADO")){
				statusTraducido = itemView.getContext().getString(R.string.enviado);
				tvStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.azul));
			} else if (status.equals("COMPLETADO")){
				statusTraducido = itemView.getContext().getString(R.string.completado);
				tvStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.verde));
			}

			tvStatus.setText(statusTraducido);
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", itemView.getResources().getConfiguration().getLocales().get(0));
			String formattedDate = formatter.format(comprasVentasDTO.getDate());
			tvHora.setText(formattedDate);
			tvPrecio.setText(String.format(itemView.getResources().getString(R.string.tv_precio_inicio), comprasVentasDTO.getPrice()));

			String url = "http://13.39.104.96:8080" + comprasVentasDTO.getImageURL();

			Glide.with(itemView.getContext())
					.load(url)
					.into(ivFotoPerfil);
		}
	}
}
