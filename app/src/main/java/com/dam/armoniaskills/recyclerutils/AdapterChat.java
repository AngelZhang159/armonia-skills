package com.dam.armoniaskills.recyclerutils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dam.armoniaskills.R;
import com.dam.armoniaskills.dto.ChatDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);

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

	private String formatDate(Date date, Context context) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		Calendar today = Calendar.getInstance();
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DATE, -1);

		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
		SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());

		if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
				calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
			return timeFormat.format(date);
		} else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
				calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
			return context.getString(R.string.yesterday);
		} else if (today.get(Calendar.WEEK_OF_YEAR) - calendar.get(Calendar.WEEK_OF_YEAR) < 1) {
			return dayFormat.format(date);
		} else if (today.get(Calendar.MONTH) - calendar.get(Calendar.MONTH) < 1) {
			int weeksAgo = today.get(Calendar.WEEK_OF_YEAR) - calendar.get(Calendar.WEEK_OF_YEAR);
			return String.format(context.getString(R.string.weeks_ago), weeksAgo);
		} else {
			return dateFormat.format(date);
		}
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
				String formattedDate = formatDate(chatDTO.getUltimaHora(), itemView.getContext());
				tvFecha.setText(formattedDate);
			}

			if (chatDTO.getFotoPerfil() != null) {
				String url = "http://13.39.104.96:8080" + chatDTO.getFotoPerfil();

				Glide.with(itemView).load(url).into(iv);
			}
		}
	}

}
