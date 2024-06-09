package com.dam.armoniaskills.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.armoniaskills.R;
import com.dam.armoniaskills.TopBarActivity;
import com.dam.armoniaskills.authentication.SharedPrefManager;
import com.dam.armoniaskills.dto.ChatDTO;
import com.dam.armoniaskills.network.RetrofitClient;
import com.dam.armoniaskills.recyclerutils.AdapterChat;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatListFragment extends Fragment implements View.OnClickListener {

	RecyclerView rv;
	AdapterChat adapter;
	ArrayList<ChatDTO> chatDTOList;
	LinearProgressIndicator progressBar;
	Button btnCarrito;


	public static ChatListFragment newInstance(String param1, String param2) {
		ChatListFragment fragment = new ChatListFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_chat_list, container, false);

		rv = v.findViewById(R.id.rvChat);
		progressBar = v.findViewById(R.id.progressBarChatList);

		btnCarrito = v.findViewById(R.id.btnCarritoChatList);

		btnCarrito.setOnClickListener(btn -> {

			configurarCarrito();

		});

		chatDTOList = new ArrayList<>();
		configurarRV();

		mostrarChats();

		return v;
	}

	private void configurarCarrito() {
		CarritoFragment bottomSheetFragment = new CarritoFragment();
		bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
	}

	private void mostrarChats() {
		SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
		String token = sharedPrefManager.fetchJwt();

		Call<List<ChatDTO>> call = RetrofitClient
				.getInstance()
				.getApi()
				.getChats(token);

		call.enqueue(new Callback<List<ChatDTO>>() {
			@Override
			public void onResponse(@NonNull Call<List<ChatDTO>> call, @NonNull Response<List<ChatDTO>> response) {
				if (response.isSuccessful()) {
					chatDTOList.clear();
					chatDTOList.addAll(response.body());

					chatDTOList.removeIf(chat -> chat.getUltimaHora() == null);

					chatDTOList.sort((chat1, chat2) -> chat2.getUltimaHora().compareTo(chat1.getUltimaHora()));

					configurarRV();
					progressBar.hide();
				}
			}

			@Override
			public void onFailure(@NonNull Call<List<ChatDTO>> call, @NonNull Throwable t) {
				Toast.makeText(getContext(), R.string.error_chat, Toast.LENGTH_SHORT).show();
			}
		});
	}


	private void configurarRV() {
		adapter = new AdapterChat(chatDTOList, this);

		rv.setLayoutManager(new LinearLayoutManager(getContext()));
		rv.setAdapter(adapter);
		rv.setHasFixedSize(true);

	}

	@Override
	public void onClick(View v) {
		int pos = rv.getChildAdapterPosition(v);
		ChatDTO chatDTO = chatDTOList.get(pos);

		Intent i = new Intent(getContext(), TopBarActivity.class);
		i.putExtra("rellenar", "fragmentoChat");
		i.putExtra("userImage", chatDTO.getFotoPerfil());
		i.putExtra("userName", chatDTO.getNombreUsuario());
		i.putExtra("chatId", chatDTO.getChatId().toString());
		i.putExtra("otroUsuarioId", chatDTO.getOtroUsuarioId().toString());

		startActivity(i);
	}
}