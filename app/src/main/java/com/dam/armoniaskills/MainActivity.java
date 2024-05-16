package com.dam.armoniaskills;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dam.armoniaskills.fragments.BalanceFragment;
import com.dam.armoniaskills.fragments.ChatFragment;
import com.dam.armoniaskills.fragments.InicioFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

	private int itemMenuSeleccionado = R.id.botNavVarHome;
	NavigationBarView navBarView;

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

		navBarView = findViewById(R.id.botNavVar);
		navBarView.setOnItemSelectedListener(this);

//		OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
//		dispatcher.addCallback(this, new OnBackPressedCallback(true) {
//			@Override
//			public void handleOnBackPressed() {
//			}
//		});
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
			Intent i = new Intent(this, ChatActivity.class);
			startActivity(i);
			//ChatFragment chatFragment = new ChatFragment();
			//transaction.replace(R.id.flPrincipal, chatFragment);
		} else if (menuItem.getItemId() == R.id.botNavVarBalance) {
			BalanceFragment balanceFragment = new BalanceFragment();
			transaction.replace(R.id.flPrincipal, balanceFragment);
		} else if (menuItem.getItemId() == R.id.botNavVarAdd) {
			Intent i = new Intent(this, SeleccionActivity.class);
			startActivity(i);
		} else if (menuItem.getItemId() == R.id.botNavVarPerfil) {
			Intent i = new Intent(this, MiPerfilActivity.class);
			startActivity(i);
		}
		transaction.commit();
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

		if(itemMenuSeleccionado == R.id.botNavVarPerfil || itemMenuSeleccionado == R.id.botNavVarAdd){
			navBarView.setSelectedItemId(R.id.botNavVarHome);
		} else {
			navBarView.setSelectedItemId(itemMenuSeleccionado);
		}
	}
}