package com.dam.armoniaskills;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.dam.armoniaskills.fragments.ChatFragment;
import com.dam.armoniaskills.fragments.ConfiguracionFragment;
import com.dam.armoniaskills.fragments.DepositarFragment;
import com.dam.armoniaskills.fragments.InicioFragment;
import com.dam.armoniaskills.fragments.RetirarFragment;
import com.dam.armoniaskills.fragments.ReviewFragment;
import com.dam.armoniaskills.fragments.SkillFragment;
import com.dam.armoniaskills.model.Skill;
import com.google.android.material.appbar.MaterialToolbar;

public class TopBarActivity extends AppCompatActivity {

	MaterialToolbar toolbar;
	ImageView userImage;
	TextView userName;
	Skill skill;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top_bar);

		toolbar = findViewById(R.id.topAppBar);
		userImage = findViewById(R.id.ivUserChat);
		userName = findViewById(R.id.tvUserChat);

		setSupportActionBar(toolbar);

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		String i = getIntent().getStringExtra("rellenar");

		if (i.equals("fragmentoHome")) {
			cargarSkill();
		} else if (i.equals("fragmentoBalanceDepositar")) {
			cargarDepositar();
		} else if (i.equals("fragmentoBalanceRetirar")) {
			cargarRetirar();
		} else if (i.equals("fragmentoAniadirReview")) {
			cargarAniadirReview();
		} else if (i.equals("fragmentoChat")) {
			cargarChat();
		}
	}

	private void cargarChat() {

		String nombre = getIntent().getStringExtra("userName");
		String foto = getIntent().getStringExtra("userImage");


		if (foto != null) {
			String url = "http://10.0.2.2:8080" + foto;
			Glide.with(this).load(url).into(userImage);
		} else {
			userImage.setImageResource(R.drawable.user);
		}
		userName.setText(nombre);

		userName.setVisibility(View.VISIBLE);
		userImage.setVisibility(View.VISIBLE);

		String chatId = getIntent().getStringExtra("chatId");

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		ChatFragment chatFragment = new ChatFragment();

		Bundle args = new Bundle();

		args.putString("chatId", chatId);

		chatFragment.setArguments(args);

		fragmentTransaction.replace(R.id.flTopBar, chatFragment);
		fragmentTransaction.commit();
		invalidateOptionsMenu();
	}

	private void cargarConfiguracionSkill() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		ConfiguracionFragment configuracionFragment = new ConfiguracionFragment();
		fragmentTransaction.replace(R.id.flTopBar, configuracionFragment);
		fragmentTransaction.commit();
	}

	private void cargarAniadirReview() {

		skill = getIntent().getParcelableExtra("review");

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		ReviewFragment grupoFragment = ReviewFragment.newInstance(skill);
		transaction.replace(R.id.flTopBar, grupoFragment);

		transaction.commit();

		invalidateOptionsMenu();
	}

	private void cargarSkill() {
		skill = getIntent().getParcelableExtra(InicioFragment.SKILL);

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		SkillFragment grupoFragment = SkillFragment.newInstance(skill);
		transaction.replace(R.id.flTopBar, grupoFragment);

		transaction.commit();
		invalidateOptionsMenu();
	}

	private void cargarRetirar() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		RetirarFragment retirarFragment = new RetirarFragment();
		fragmentTransaction.replace(R.id.flTopBar, retirarFragment);

		fragmentTransaction.commit();
		invalidateOptionsMenu();
	}

	private void cargarDepositar() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		DepositarFragment depositoFragment = new DepositarFragment();
		fragmentTransaction.replace(R.id.flTopBar, depositoFragment);

		fragmentTransaction.commit();
		invalidateOptionsMenu();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();

		Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flTopBar);

		if (currentFragment instanceof SkillFragment) {
			getMenuInflater().inflate(R.menu.topbar_menu, menu);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {

		if (item.getItemId() == R.id.item_config) {

			cargarConfiguracionSkill();


			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}