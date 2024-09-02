package com.dam.armoniaskills;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.dam.armoniaskills.authentication.SharedPrefManager;
import com.dam.armoniaskills.fragments.ChatFragment;
import com.dam.armoniaskills.fragments.ConfigPerfilFragment;
import com.dam.armoniaskills.fragments.ConfiguracionFragment;
import com.dam.armoniaskills.fragments.DepositarFragment;
import com.dam.armoniaskills.fragments.InicioFragment;
import com.dam.armoniaskills.fragments.MiPerfilFragment;
import com.dam.armoniaskills.fragments.RetirarFragment;
import com.dam.armoniaskills.fragments.ReviewFragment;
import com.dam.armoniaskills.fragments.SkillFragment;
import com.dam.armoniaskills.fragments.UsuarioFragment;
import com.dam.armoniaskills.model.Skill;
import com.dam.armoniaskills.network.RetrofitClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopBarActivity extends AppCompatActivity {

	MaterialToolbar toolbar;
	ImageView userImage;
	TextView userName;
	Skill skill;
	LinearLayout llTopBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top_bar);

		toolbar = findViewById(R.id.topAppBar);
		userImage = findViewById(R.id.ivUserChat);
		userName = findViewById(R.id.tvUserChat);
		llTopBar = findViewById(R.id.llTopBar);
		llTopBar.setClickable(false);
		llTopBar.setFocusable(false);

		userName.setVisibility(View.GONE);
		userImage.setVisibility(View.GONE);

		toolbar.setTitle("");

		setSupportActionBar(toolbar);

		toolbar.setNavigationOnClickListener(v -> {
			FragmentManager manager = getSupportFragmentManager();
			if (manager.getBackStackEntryCount() > 0) {
				String fragmentTag = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName();
				if (fragmentTag != null && fragmentTag.equals(ChatFragment.class.getSimpleName())) {
					llTopBar.setClickable(true);
					llTopBar.setFocusable(true);
				}
				getSupportFragmentManager().popBackStack();
			} else {
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
		} else if (i.equals("fragmentoPerfil")) {
			cargarPerfil();
		}
	}

	private void cargarPerfil() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		MiPerfilFragment perfilFragment = new MiPerfilFragment();
		fragmentTransaction.replace(R.id.flTopBar, perfilFragment);

		fragmentTransaction.commit();
		invalidateOptionsMenu();
	}

	private void cargarChat() {

		String nombre = getIntent().getStringExtra("userName");
		String foto = getIntent().getStringExtra("userImage");
		String otroUsuarioId = getIntent().getStringExtra("otroUsuarioId");

		if (foto != null) {
			String url = "http://13.39.104.96:8080" + foto;
			Glide.with(this).load(url).into(userImage);
		} else {
			userImage.setImageResource(R.drawable.user);
		}
		userName.setText(nombre);
		llTopBar.setClickable(true);
		llTopBar.setFocusable(true);
		llTopBar.setOnClickListener(v -> {

			llTopBar.setClickable(false);
			llTopBar.setFocusable(false);

			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			UsuarioFragment usuarioFragment = UsuarioFragment.newInstance(otroUsuarioId);
			fragmentTransaction.replace(R.id.flTopBar, usuarioFragment);
			fragmentTransaction.addToBackStack("ChatFragment");
			fragmentTransaction.commit();
		});

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
		String i = getIntent().getStringExtra("rellenar");

		if (i.equals("fragmentoHome")) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			ConfiguracionFragment configuracionFragment = ConfiguracionFragment.newInstance(skill);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.replace(R.id.flTopBar, configuracionFragment);

			fragmentTransaction.commit();
			invalidateOptionsMenu();
		} else if (i.equals("fragmentoPerfil")) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			ConfigPerfilFragment configFragment = new ConfigPerfilFragment();
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.replace(R.id.flTopBar, configFragment);

			fragmentTransaction.commit();
			invalidateOptionsMenu();
		}
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

		readUsuario(menu);

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

	private void readUsuario(Menu menu) {
		//Rellenar user con el usuario acutalmente logeado

		SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
		String jwt = sharedPrefManager.fetchJwt();

		Call<ResponseBody> call = RetrofitClient
				.getInstance()
				.getApi()
				.getUserData(jwt);

		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
				if (response.isSuccessful()) {
					try {
						String jsonData = response.body().string();
						JSONObject jsonObject = new JSONObject(jsonData);

						String id = jsonObject.getString("id");
						Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flTopBar);

						if (currentFragment instanceof SkillFragment) {
							Log.e("TOPBAR", "Es el mismo fragmento que skill");
							if (id.equals(skill.getUserID().toString())) {
								Log.e("TOPBAR", "Es el mismo usuario");
								getMenuInflater().inflate(R.menu.topbar_menu, menu);
							}
						} else if (currentFragment instanceof MiPerfilFragment) {
							getMenuInflater().inflate(R.menu.topbar_menu, menu);
						}
						Log.e("TOPBAR", "Sale if");

					} catch (IOException | JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(getApplicationContext(), R.string.error_usuario, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
				Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}
}