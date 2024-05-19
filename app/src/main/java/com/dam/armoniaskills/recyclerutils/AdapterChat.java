package com.dam.armoniaskills.recyclerutils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dam.armoniaskills.R;
import com.dam.armoniaskills.model.ChatDTO;
import com.dam.armoniaskills.model.ChatRoom;

import java.util.ArrayList;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.ChatVH> implements View.OnClickListener {
	View.OnClickListener listener;
	ArrayList<ChatDTO> chatDTOList;

	public AdapterChat(ArrayList<ChatDTO> chatDTOList, View.OnClickListener listener) {
		this.listener = listener;
		this.chatDTOList = chatDTOList;
	}

	@NonNull
	@Override
	public AdapterChat.ChatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = View.inflate(parent.getContext(), R.layout.item_chat, null);

		v.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		v.setOnClickListener(this);

		return new ChatVH(v);
	}

	@Override
	public void onBindViewHolder(@NonNull AdapterChat.ChatVH holder, int position) {
		holder.bindChat(chatDTOList.get(position));

	}

	@Override
	public int getItemCount() {
		return chatDTOList.size();
	}

	@Override
	public void onClick(View v) {
		listener.onClick(v);
	}

	public class ChatVH extends RecyclerView.ViewHolder {

		ImageView iv;
		TextView tvUsuario, tvServicio, tvMensaje, tvFecha;

		public ChatVH(@NonNull View itemView) {
			super(itemView);
			iv = itemView.findViewById(R.id.ivFotoPerfilChat);
			tvUsuario = itemView.findViewById(R.id.tvUsuarioChat);
			tvServicio = itemView.findViewById(R.id.tvServicioChat);
			tvMensaje = itemView.findViewById(R.id.tvMensajeChat);
			tvFecha = itemView.findViewById(R.id.tvFechaChat);
		}

		public void bindChat(ChatDTO chatDTO) {
			tvUsuario.setText(chatDTO.getNombreUsuario());
			tvServicio.setText(chatDTO.getNombreSkill());


			if (chatDTO.getUltimoMensaje() != null) {
				tvMensaje.setText(chatDTO.getUltimoMensaje());
			}

			if (chatDTO.getUltimaHora() != null) {
				// Use a method to format the Date object to a String
				tvFecha.setText(chatDTO.getUltimaHora().getHours());
			}

			if (chatDTO.getFotoPerfil() != null) {
				String url = "http://10.0.2.2:8080" + chatDTO.getFotoPerfil();

				Glide.with(itemView).load(url).into(iv);
			}
		}
	}


}
