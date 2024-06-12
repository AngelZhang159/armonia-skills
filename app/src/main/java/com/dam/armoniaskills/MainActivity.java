package com.dam.armoniaskills;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dam.armoniaskills.authentication.SharedPrefManager;
import com.dam.armoniaskills.fragments.BalanceFragment;
import com.dam.armoniaskills.fragments.ChatListFragment;
import com.dam.armoniaskills.fragments.InicioFragment;
import com.dam.armoniaskills.network.RetrofitClient;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationBarView;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

	NavigationBarView navBarView;
	private int itemMenuSeleccionado = R.id.botNavVarHome;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		EdgeToEdge.enable(this);
		setContentView(R.layout.activity_main);
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
			Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
			return insets;
		});

		checkNotificationEnabled(this);

		navBarView = findViewById(R.id.botNavVar);
		navBarView.setOnItemSelectedListener(this);

		registrarTokenFCM();

	}

	private void registrarTokenFCM() {
		SharedPrefManager sharedPrefManager = new SharedPrefManager(this);

		Call<String> call = RetrofitClient
				.getInstance()
				.getApi()
				.registrarTokenFCM(sharedPrefManager.fetchJwt(), sharedPrefManager.fetchFCMToken());

		Log.e("Token FCM", "Intentando registrar: " + sharedPrefManager.fetchFCMToken());

		call.enqueue(new retrofit2.Callback<String>() {
			@Override
			public void onResponse(Call<String> call, retrofit2.Response<String> response) {
				if (!response.isSuccessful()) {
					Log.i("Error", "Error al registrar token FCM en el servidor");
					Log.e("Error", response.toString());
				} else {
					Log.i("Token FCM", "Token FCM registrado correctamente");
					Log.i("Token FCM", response.toString());
				}
			}

			@Override
			public void onFailure(Call<String> call, Throwable t) {
				t.printStackTrace();
			}
		});
	}

	public void checkNotificationEnabled(Context context) {
		if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
			new MaterialAlertDialogBuilder(context)
					.setTitle(R.string.notification_title)
					.setMessage(R.string.notification_message)
					.setPositiveButton(R.string.btn_aceptar_d, (dialog, which) -> {
						// Continue with operation
						Intent intent = new Intent();
						intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
						intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
						context.startActivity(intent);
					})
					.setNegativeButton(R.string.btn_cancelar_d, null)
					.setIcon(android.R.drawable.stat_notify_chat)
					.show();
		}
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
		itemMenuSeleccionado = menuItem.getItemId();

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		if (menuItem.getItemId() == R.id.botNavVarHome) {
			InicioFragment homeFragment = new InicioFragment();
			transaction.replace(R.id.flPrincipal, homeFragment);
		} else if (menuItem.getItemId() == R.id.botNavVarChat) {
			ChatListFragment chatListFragment = new ChatListFragment();
			transaction.replace(R.id.flPrincipal, chatListFragment);
		} else if (menuItem.getItemId() == R.id.botNavVarBalance) {
			BalanceFragment balanceFragment = new BalanceFragment();
			transaction.replace(R.id.flPrincipal, balanceFragment);
		} else if (menuItem.getItemId() == R.id.botNavVarAdd) {
			Intent i = new Intent(this, SeleccionActivity.class);
			startActivity(i);
		} else if (menuItem.getItemId() == R.id.botNavVarPerfil) {
			Intent i = new Intent(MainActivity.this, TopBarActivity.class);
			i.putExtra("rellenar", "fragmentoPerfil");
			startActivity(i);
		}
		transaction.commit();
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (itemMenuSeleccionado == R.id.botNavVarPerfil || itemMenuSeleccionado == R.id.botNavVarAdd) {
			navBarView.setSelectedItemId(R.id.botNavVarHome);
		} else {
			navBarView.setSelectedItemId(itemMenuSeleccionado);
		}
	}
}