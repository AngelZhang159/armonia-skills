package com.dam.armoniaskills.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.armoniaskills.ChatActivity;
import com.dam.armoniaskills.R;
import com.dam.armoniaskills.authentication.SharedPrefManager;
import com.dam.armoniaskills.model.ChatDTO;
import com.dam.armoniaskills.network.RetrofitClient;
import com.dam.armoniaskills.recyclerutils.AdapterChat;
import com.dam.armoniaskills.webSocket.WebSocketSingleton;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment implements View.OnClickListener {

	RecyclerView rv;
	AdapterChat adapter;
	ArrayList<ChatDTO> chatDTOList;
	private WebSocket webSocket;

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

		chatDTOList = new ArrayList<>();
		configurarRV();

		mostrarChats();

		configurarWebSocket();


		return v;
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
					configurarRV();
				}
			}

			@Override
			public void onFailure(@NonNull Call<List<ChatDTO>> call, @NonNull Throwable t) {
				Log.e("CHAT", "Error al obtener los chats");
			}
		});
	}

	private void configurarWebSocket() {

		SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
		String token = sharedPrefManager.fetchJwt();

		OkHttpClient client = new OkHttpClient.Builder()
				.readTimeout(0,  TimeUnit.MILLISECONDS)
				.build();

		Request request = new Request.Builder()
				.url("ws://10.0.2.2:8080/ws")
				.addHeader("Authorization", token)
				.build();

		webSocket = client.newWebSocket(request, new WebSocketListener() {
			@Override
			public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
				super.onClosed(webSocket, code, reason);
			}

			@Override
			public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
				super.onClosing(webSocket, code, reason);
			}

			@Override
			public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable okhttp3.Response response) {
				super.onFailure(webSocket, t, response);
			}

			@Override
			public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
				super.onMessage(webSocket, text);
			}

			@Override
			public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
				super.onMessage(webSocket, bytes);
			}

			@Override
			public void onOpen(@NonNull WebSocket webSocket, @NonNull okhttp3.Response response) {
				super.onOpen(webSocket, response);
			}
		});

		WebSocketSingleton.getInstance().setWebSocket(webSocket);
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
		i.putExtra("chatId", chatDTO.getChatId().toString());
		startActivity(i);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (webSocket != null) {
			webSocket.close(1000, "Chat View destroyed");
		}
	}
}