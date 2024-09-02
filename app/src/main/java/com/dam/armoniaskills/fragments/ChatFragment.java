package com.dam.armoniaskills.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.armoniaskills.R;
import com.dam.armoniaskills.authentication.SharedPrefManager;
import com.dam.armoniaskills.model.ChatMessage;
import com.dam.armoniaskills.network.RetrofitClient;
import com.dam.armoniaskills.recyclerutils.AdapterMensajes;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {

	LinearProgressIndicator progressBar;
	private EditText messageInput;
	private AdapterMensajes adapter;
	private ArrayList<ChatMessage> chatMessages;
	private RecyclerView rv;
	private WebSocket webSocket;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chat, container, false);

		Bundle args = getArguments();

		String chatId = args != null ? args.getString("chatId") : null;
		progressBar = view.findViewById(R.id.progressBarChat);

		messageInput = view.findViewById(R.id.message_input);
		Button sendButton = view.findViewById(R.id.send_button);
		rv = view.findViewById(R.id.rvMensajes);
		chatMessages = new ArrayList<>();


		cargarMensajes(chatId);

		configurarWebSocket();

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

		call.enqueue(new Callback<List<ChatMessage>>() {
			@Override
			public void onResponse(@NonNull Call<List<ChatMessage>> call, @NonNull Response<List<ChatMessage>> response) {
				if (response.isSuccessful()) {
					chatMessages.addAll(response.body());
					configurarRV();
					progressBar.hide();
				}
			}

			@Override
			public void onFailure(@NonNull Call<List<ChatMessage>> call, @NonNull Throwable t) {
				Toast.makeText(getContext(), R.string.error_chat, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void configurarRV() {
		if (getActivity() != null) {
			SharedPrefManager sharedPrefManager = new SharedPrefManager(getActivity());

			Call<UUID> call = RetrofitClient
					.getInstance()
					.getApi()
					.getUserId(sharedPrefManager.fetchJwt());

			call.enqueue(new Callback<UUID>() {
				@Override
				public void onResponse(@NonNull Call<UUID> call, @NonNull Response<UUID> response) {
					if (response.isSuccessful()) {
						adapter = new AdapterMensajes(chatMessages, response);

						LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
						layoutManager.setStackFromEnd(true);
						rv.setLayoutManager(layoutManager);
						rv.setAdapter(adapter);
						rv.setHasFixedSize(true);
					}
				}

				@Override
				public void onFailure(@NonNull Call<UUID> call, @NonNull Throwable t) {
					Toast.makeText(getContext(), R.string.error_mensajes, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	private void configurarWebSocket() {
		SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
		String token = sharedPrefManager.fetchJwt();

		OkHttpClient client = new OkHttpClient.Builder()
				.readTimeout(0, TimeUnit.MILLISECONDS)
				.build();

		Request request = new Request.Builder()
				.url("ws://13.39.104.96:8080/ws")
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

				Log.e("ONMESSAGE", "onMessage: " + text);

				try {
					JSONObject jsonObject = new JSONObject(text);

					String chatId = jsonObject.getString("chatId");
					String sender = jsonObject.getString("sender");
					String receiver = jsonObject.getString("receiver");
					String content = jsonObject.getString("content");
					long dateMillis = jsonObject.getLong("date");

					ChatMessage chatMessage = new ChatMessage();
					chatMessage.setChatId(UUID.fromString(chatId));
					chatMessage.setSender(UUID.fromString(sender));
					chatMessage.setReceiver(UUID.fromString(receiver));
					chatMessage.setContent(content);
					chatMessage.setDate(new Timestamp(dateMillis));

					chatMessages.add(chatMessage);

					// Notify the adapter that the data set has changed
					getActivity().runOnUiThread(() -> {
						adapter.notifyDataSetChanged();
						rv.smoothScrollToPosition(chatMessages.size() - 1);
					});
				} catch (Exception e) {
					e.printStackTrace();
				}

				// Add the new message to the list
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
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		webSocket.close(1000, "Goodbye!");
	}
}