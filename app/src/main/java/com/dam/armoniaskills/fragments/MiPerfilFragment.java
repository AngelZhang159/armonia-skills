package com.dam.armoniaskills.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dam.armoniaskills.R;
import com.dam.armoniaskills.TopBarActivity;
import com.dam.armoniaskills.authentication.SharedPrefManager;
import com.dam.armoniaskills.dto.PerfilDTO;
import com.dam.armoniaskills.model.Review;
import com.dam.armoniaskills.model.Skill;
import com.dam.armoniaskills.network.RetrofitClient;
import com.dam.armoniaskills.recyclerutils.AdapterReviews;
import com.dam.armoniaskills.recyclerutils.AdapterSkills;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MiPerfilFragment extends Fragment implements View.OnClickListener {

	public static final String SKILL = "SKILL";

	ImageView imvUser;
	TextView tvUsername, tvNumReviews;
	RatingBar ratingBar;
	RecyclerView rv;
	PerfilDTO perfilDTO;
	TabLayout tabLayout;
	String id;
	LinearProgressIndicator progressBar;


	public MiPerfilFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_usuario, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		imvUser = view.findViewById(R.id.imvUsuarioPerfil);
		tvUsername = view.findViewById(R.id.tvNombreUsuarioPerfil);
		ratingBar = view.findViewById(R.id.ratingBarUsuarioPerfil);
		rv = view.findViewById(R.id.rvUsuarioPerfil);
		tvNumReviews = view.findViewById(R.id.tvNumReviews);
		progressBar = view.findViewById(R.id.progressBarUsuarioPerfil);

		tabLayout = view.findViewById(R.id.tlUsuarioPerfil);
		tabLayout.setEnabled(false);

		readUsuario();
	}

	private void encenderTablayout() {
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				if (tab.getPosition() == 0) {
					configurarRVSkills(perfilDTO.getSkills());
				} else {
					configurarRVReviews(perfilDTO.getReviews());
				}
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {
			}
		});
	}

	private void configurarRVReviews(List<Review> reviews) {
		AdapterReviews adapter = new AdapterReviews(new ArrayList<>(reviews));
		rv.setLayoutManager(new LinearLayoutManager(getContext()));
		rv.setAdapter(adapter);
	}

	private void configurarRVSkills(List<Skill> skills) {
		AdapterSkills adapter = new AdapterSkills(new ArrayList<>(skills), this);
		rv.setLayoutManager(new LinearLayoutManager(getContext()));
		rv.setAdapter(adapter);
	}

	private void readUsuario() {
		//Rellenar user con el usuario acutalmente logeado

		SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
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

						id = jsonObject.getString("id");

						getPerfil(id);

					} catch (IOException | JSONException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(getContext(), R.string.error_usuario, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
				Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void getPerfil(String id) {
		Call<PerfilDTO> call = RetrofitClient
				.getInstance()
				.getApi()
				.getPerfil(id);

		call.enqueue(new retrofit2.Callback<PerfilDTO>() {
			@Override
			public void onResponse(Call<PerfilDTO> call, Response<PerfilDTO> response) {
				if (isAdded() && response.isSuccessful()) {
					perfilDTO = response.body();
					Log.e("PerfilDTO", perfilDTO.toString());

					if (getActivity() != null) {
						if (perfilDTO.getFoto() == null) {
							Glide.with(getActivity()).load(R.drawable.user).into(imvUser);
						} else {
							String url = "http://13.39.104.96:8080" + perfilDTO.getFoto();
							Glide.with(getActivity()).load(url).into(imvUser);
						}
					}

					tvUsername.setText(perfilDTO.getNombre());
					List<Review> listaReviews = perfilDTO.getReviews();
					if (!listaReviews.isEmpty()) {
						double media = 0;
						for (Review review : listaReviews) {
							media += review.getStars();

						}
						media /= listaReviews.size();
						ratingBar.setRating((float) media);
						tvNumReviews.setText(String.format(getString(R.string.tv_media_reviews), listaReviews.size()));
					} else {
						ratingBar.setRating(0);
						tvNumReviews.setText(R.string.no_reviews);
					}

					configurarRVSkills(perfilDTO.getSkills());
					tabLayout.setEnabled(true);

					progressBar.hide();

					encenderTablayout();
				}
			}

			@Override
			public void onFailure(Call<PerfilDTO> call, Throwable t) {
				// Handle the error here
				progressBar.hide();
			}
		});
	}

	@Override
	public void onClick(View v) {
		int pos = rv.getChildAdapterPosition(v);
		Skill skill = perfilDTO.getSkills().get(pos);

		Intent i = new Intent(getContext(), TopBarActivity.class);
		i.putExtra(SKILL, skill);
		i.putExtra("rellenar", "fragmentoHome");
		startActivity(i);
	}
}