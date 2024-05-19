package com.dam.armoniaskills.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.armoniaskills.ChatActivity;
import com.dam.armoniaskills.R;
import com.dam.armoniaskills.authentication.SharedPrefManager;
import com.dam.armoniaskills.model.ChatDTO;
import com.dam.armoniaskills.model.ChatRoom;
import com.dam.armoniaskills.network.RetrofitClient;
import com.dam.armoniaskills.recyclerutils.AdapterChat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment implements View.OnClickListener {

	RecyclerView rv;
	AdapterChat adapter;
	ArrayList<ChatDTO> chatDTOList;

	public static ChatFragment newInstance(String param1, String param2) {
		ChatFragment fragment = new ChatFragment();
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

		View v = inflater.inflate(R.layout.fragment_chat, container, false);

		rv = v.findViewById(R.id.rvChat);

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
					chatDTOList = new ArrayList<>(response.body());
					configurarRV();
				}
			}

			@Override
			public void onFailure(@NonNull Call<List<ChatDTO>> call, @NonNull Throwable t) {
				Log.e("CHAT", "Error al obtener los chats");
			}
		});


		return v;
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

		Intent i = new Intent(getContext(), ChatActivity.class);
		i.putExtra("chatId", chatDTO.getChatId());
		startActivity(i);
	}
}