package com.dam.armoniaskills.recyclerutils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.armoniaskills.R;
import com.dam.armoniaskills.model.ChatMessage;
import com.dam.armoniaskills.model.ChatRoom;
import com.dam.armoniaskills.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.ChatVH> implements View.OnClickListener {
	View.OnClickListener listener;
	ArrayList<ChatRoom> chatRooms;

	public AdapterChat(ArrayList<ChatRoom> chatRooms, View.OnClickListener listener) {
		this.listener = listener;
		this.chatRooms = chatRooms;
	}

	@NonNull
	@Override
	public AdapterChat.ChatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = View.inflate(parent.getContext(), R.layout.item_chat, null);

		v.setOnClickListener(this);

		return new ChatVH(v);
	}

	@Override
	public void onBindViewHolder(@NonNull AdapterChat.ChatVH holder, int position) {
		holder.bindChat(chatRooms.get(position));
	}

	@Override
	public int getItemCount() {
		return chatRooms.size();
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

		public void bindChat(ChatRoom chatRoom) {
			Call call = RetrofitClient
					.getInstance()
					.getApi()
					.getUserDataFromUUID(chatRoom.getReceiverId());

		}
	}
}
