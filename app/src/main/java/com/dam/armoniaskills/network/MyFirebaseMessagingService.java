package com.dam.armoniaskills.network;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dam.armoniaskills.R;
import com.dam.armoniaskills.fragments.ChatFragment;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
	private static final String CHANNEL_ID = "com.dam.armoniaskills.MESSAGES";

	public MyFirebaseMessagingService() {
	}

	@Override
	public void onNewToken(@NonNull String token) {
		super.onNewToken(token);
	}

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);

		// Check if message contains a data payload.
		if (remoteMessage.getData().size() > 0) {
			// Handle the data payload here
			String message = remoteMessage.getData().get("message");
			String sender = remoteMessage.getData().get("sender");
			showNotification(message, sender);
		}
	}

	private void showNotification(String message, String sender) {
		// Create a notification and display it using NotificationManager
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			CharSequence name = "Messages";
			String description = "New messages";
			int importance = NotificationManager.IMPORTANCE_HIGH; // Set the importance to high
			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
			channel.setDescription(description);
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
				.setSmallIcon(R.drawable.baseline_chat_24)
				.setContentTitle(sender)
				.setContentText(message)
				.setPriority(NotificationCompat.PRIORITY_MAX)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(message))  // Use BigTextStyle for heads-up
				.setDefaults(NotificationCompat.DEFAULT_ALL)  // Ensure sound, vibration, and lights are used
				.setAutoCancel(true);  // Dismiss the notification when clicked

		// Ensure the heads-up notification by setting a full-screen intent
		Intent intent = new Intent(this, ChatFragment.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setFullScreenIntent(pendingIntent, true);

		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

		// notificationId is a unique int for each notification that you must define
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		notificationManager.notify(0, builder.build());
	}

}