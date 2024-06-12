package com.dam.armoniaskills.network;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.dam.armoniaskills.MainActivity;
import com.dam.armoniaskills.R;
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
		if (!remoteMessage.getData().isEmpty()) {
			// Handle the data payload here
			showNotification(remoteMessage);
		}
	}

	private void showNotification(RemoteMessage remoteMessage) {
		// Create an Intent for the activity you want to start
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Create a PendingIntent
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

		// Create a notification channel for Android O and above
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(
					CHANNEL_ID,
					"FCM Notifications",
					NotificationManager.IMPORTANCE_HIGH
			);
			channel.setDescription("Channel for FCM notifications");
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}

		// Build the notification
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
				.setSmallIcon(R.drawable.baseline_chat_24) // replace with your app's icon
				.setContentTitle(remoteMessage.getNotification().getTitle())
				.setContentText(remoteMessage.getNotification().getBody())
				.setAutoCancel(true)
				.setContentIntent(pendingIntent)
				.setPriority(NotificationCompat.PRIORITY_HIGH);

		// Get an instance of the NotificationManager service
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// Show the notification
		notificationManager.notify(0, notificationBuilder.build());
	}

}