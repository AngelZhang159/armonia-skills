package com.dam.armoniaskills.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.armoniaskills.R;
import com.dam.armoniaskills.authentication.SharedPrefManager;
import com.dam.armoniaskills.model.ChatMessage;
import com.dam.armoniaskills.network.RetrofitClient;
import com.dam.armoniaskills.recyclerutils.AdapterMensajes;
import com.dam.armoniaskills.webSocket.WebSocketSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.WebSocket;
import retrofit2.Call;
import retrofit2.Response;

public class ChatFragment extends Fragment {

	private EditText messageInput;
	private AdapterMensajes adapter;
	private ArrayList<ChatMessage> chatMessages;
	private RecyclerView rv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chat, container, false);

		Bundle args = getArguments();

		String chatId = args != null ? args.getString("chatId") : null;

		messageInput = view.findViewById(R.id.message_input);
		Button sendButton = view.findViewById(R.id.send_button);
		rv = view.findViewById(R.id.rvMensajes);
		chatMessages = new ArrayList<>();

		cargarMensajes(chatId);

		WebSocket webSocket = WebSocketSingleton.getInstance().getWebSocket();

		sendButton.setOnClickListener(v -> {
			String message = messageInput.getText().toString();

			if (!message.isEmpty()) {
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("message", message);
					jsonObject.put("chatId", chatId);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				// Send the JSON object as a string
				webSocket.send(jsonObject.toString());

				messageInput.setText(""); // Clear the input field
			}
		});

		return view;
	}

	private void cargarMensajes(String chatId) {

		SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
		String token = sharedPrefManager.fetchJwt();

		Call<List<ChatMessage>> call = RetrofitClient
				.getInstance()
				.getApi()
				.getMessages(token, UUID.fromString(chatId));

		call.enqueue(new retrofit2.Callback<List<ChatMessage>>() {
			@Override
			public void onResponse(@NonNull Call<List<ChatMessage>> call, @NonNull Response<List<ChatMessage>> response) {
				if (response.isSuccessful()) {
					chatMessages.addAll(response.body());
					configurarRV();
				}
			}

			@Override
			public void onFailure(@NonNull Call<List<ChatMessage>> call, @NonNull Throwable t) {
				t.printStackTrace();
			}
		});

	}

	private void configurarRV() {
		SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());

		Call<UUID> call = RetrofitClient
				.getInstance()
				.getApi()
				.getUserId(sharedPrefManager.fetchJwt());

		call.enqueue(new retrofit2.Callback<UUID>() {
			@Override
			public void onResponse(@NonNull Call<UUID> call, @NonNull Response<UUID> response) {
				if (response.isSuccessful()) {
					adapter = new AdapterMensajes(chatMessages, response);

					rv.setLayoutManager(new LinearLayoutManager(getContext()));
					rv.setAdapter(adapter);
					rv.setHasFixedSize(true);
				}
			}

			@Override
			public void onFailure(@NonNull Call<UUID> call, @NonNull Throwable t) {
				t.printStackTrace();
			}
		});

	}


}