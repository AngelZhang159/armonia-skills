package com.dam.armoniaskills;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private WebSocketClient webSocketClient;
    private ListView messageListView;
    private EditText messageInput;
    private Button sendButton;
    private List<String> messages;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageListView = findViewById(R.id.message_list);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);

        messages = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        messageListView.setAdapter(adapter);

//        try {
//            URI uri = new URI("ws://10.0.2.2:8080/ws");
//            webSocketClient = new WebSocketClient(uri) {
//                @Override
//                public void onOpen(ServerHandshake handshakedata) {
//                    runOnUiThread(() -> messages.add("Connected"));
//                }
//
//                @Override
//                public void onMessage(String message) {
//                    runOnUiThread(() -> {
//                        messages.add(message);
//                        adapter.notifyDataSetChanged();
//                    });
//                }
//
//                @Override
//                public void onClose(int code, String reason, boolean remote) {
//                    runOnUiThread(() -> messages.add("Disconnected: " + reason));
//                }
//
//                @Override
//                public void onError(Exception ex) {
//                    runOnUiThread(() -> messages.add("Error: " + ex.getMessage()));
//                }
//            };
//            webSocketClient.connect();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//
//        sendButton.setOnClickListener(v -> {
//            String message = messageInput.getText().toString();
//            if (!message.isEmpty()) {
//                webSocketClient.send(message);
//                messageInput.setText("");
//            }
//        });
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (webSocketClient != null) {
//            webSocketClient.close();
//        }
//    }
}