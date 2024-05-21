package com.dam.armoniaskills;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.dam.armoniaskills.model.ChatMessage;
import com.dam.armoniaskills.webSocket.WebSocketSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import okhttp3.WebSocket;

public class ChatActivity extends AppCompatActivity {

    private ListView messageListView;
    private EditText messageInput;
    private Button sendButton;
    private List<String> messages;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String chatId = getIntent().getStringExtra("chatId");

        messageListView = findViewById(R.id.message_list);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);

        messages = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        messageListView.setAdapter(adapter);

        WebSocket webSocket = WebSocketSingleton.getInstance().getWebSocket();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

    }



}