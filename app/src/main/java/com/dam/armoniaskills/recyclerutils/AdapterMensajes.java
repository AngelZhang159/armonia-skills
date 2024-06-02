package com.dam.armoniaskills.recyclerutils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.armoniaskills.R;
import com.dam.armoniaskills.model.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Response;

public class AdapterMensajes extends RecyclerView.Adapter<AdapterMensajes.MensajeVH> {

	ArrayList<ChatMessage> chatMessages;
	UUID userId;

	public AdapterMensajes(ArrayList<ChatMessage> chatMessages, Response<UUID> userId) {
		this.chatMessages = chatMessages;
		this.userId = userId.body();
	}

	@Override
	public int getItemViewType(int position) {
		if (chatMessages.get(position).getSender().equals(userId)) {
			return 1;
		} else {
			return 0;
		}
	}

	@NonNull
	@Override
	public MensajeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v;

		if (viewType == 0) {
			v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensaje_receiver, parent, false);
		} else {
			v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensaje_sender, parent, false);
		}

		return new MensajeVH(v);
	}

	@Override
	public void onBindViewHolder(MensajeVH holder, int position) {
		holder.bindChat(chatMessages.get(position));
	}

	@Override
	public int getItemCount() {
		return chatMessages.size();
	}

	public class MensajeVH extends RecyclerView.ViewHolder {

		TextView tvMensaje, tvFecha;

		public MensajeVH(@NonNull View itemView) {
			super(itemView);
			tvMensaje = itemView.findViewById(R.id.tvChatMensaje);
			tvFecha = itemView.findViewById(R.id.tvChatFecha);

		}

		public void bindChat(ChatMessage chatMessage) {
			tvMensaje.setText(chatMessage.getContent());
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
			String formattedDate = sdf.format(chatMessage.getDate());
			tvFecha.setText(formattedDate);

		}
	}
}
