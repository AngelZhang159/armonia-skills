package com.dam.armoniaskills.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dam.armoniaskills.R;
import com.dam.armoniaskills.TopBarActivity;
import com.dam.armoniaskills.authentication.SharedPrefManager;
import com.dam.armoniaskills.model.ChatRoom;
import com.dam.armoniaskills.model.Review;
import com.dam.armoniaskills.model.Skill;
import com.dam.armoniaskills.model.User;
import com.dam.armoniaskills.network.ChatCallback;
import com.dam.armoniaskills.network.RetrofitClient;
import com.dam.armoniaskills.network.UserCallback;
import com.dam.armoniaskills.recyclerutils.AdapterReviews;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SkillFragment extends Fragment implements View.OnClickListener {

	private static final String ARG_SKILL = "skill";

	ImageSlider slider;
	TextView tvPrecio, tvTitulo, tvDescripcion, tvUsername, tvValoracion, tvTitVal;
	ImageView imvUser;
	RecyclerView rv;
	Button btnChat, btnContratar, btnAniadirValoracion;
	AdapterReviews adapter;
	RatingBar ratingBar;
	LinearLayout llUserDetalle;
	private Skill skill;


	public SkillFragment() {
	}

	public static SkillFragment newInstance(Skill skill) {
		SkillFragment fragment = new SkillFragment();
		Bundle args = new Bundle();
		args.putParcelable(ARG_SKILL, skill);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			skill = getArguments().getParcelable(ARG_SKILL);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_skill, container, false);

		slider = v.findViewById(R.id.slider);
		tvPrecio = v.findViewById(R.id.tvPrecioDetalle);
		tvTitulo = v.findViewById(R.id.tvTituloDetalle);
		tvDescripcion = v.findViewById(R.id.tvDescDetalle);
		tvUsername = v.findViewById(R.id.tvUser);
		tvValoracion = v.findViewById(R.id.tvValUser);
		tvTitVal = v.findViewById(R.id.tvValDetalle);
		imvUser = v.findViewById(R.id.imvUser);
		rv = v.findViewById(R.id.rvValDetalle);
		btnChat = v.findViewById(R.id.btnChatSkill);
		btnContratar = v.findViewById(R.id.btnContratar);
		btnAniadirValoracion = v.findViewById(R.id.btnAniadirValoracion);
		ratingBar = v.findViewById(R.id.ratingBarSkill);
		llUserDetalle = v.findViewById(R.id.llUserDetalle);

		btnAniadirValoracion.setOnClickListener(this);
		btnContratar.setOnClickListener(this);

		llUserDetalle.setOnClickListener(v1 -> {
			FragmentManager fragmentManager = getParentFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			UsuarioFragment usuarioFragment = UsuarioFragment.newInstance(skill.getUserID().toString());
			fragmentTransaction.replace(R.id.flTopBar, usuarioFragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		});

		btnChat.setOnClickListener(v1 -> {
			nuevoChat(new ChatCallback() {
				@Override
				public void onChatCreated(ChatRoom chatRoom) {
					Log.d("SkillFragment", "Chat creado " + chatRoom.getId() + chatRoom.getSkillId() + chatRoom.getSenderId() + chatRoom.getReceiverId());
					Intent i = new Intent(getContext(), TopBarActivity.class);
					i.putExtra("rellenar", "fragmentoChat");
					i.putExtra("chatId", chatRoom.getId().toString());
					i.putExtra("otroUsuarioId", chatRoom.getReceiverId().toString());
					startActivity(i);
				}

				@Override
				public void onError() {
					Toast.makeText(getContext(), R.string.error_crear_chat, Toast.LENGTH_SHORT).show();
				}
			});
		});

		cargarSkill();

		return v;
	}

	private void cargarReviews(User user) {
		List<Review> listaReviews = user.getReviewList();

		if (!listaReviews.isEmpty()) {
			double media = 0;
			for (Review review : listaReviews) {
				media += review.getStars();

			}
			media /= listaReviews.size();
			ratingBar.setRating((float) media);

			tvValoracion.setText(String.format(getString(R.string.tv_media_reviews), listaReviews.size()));

			adapter = new AdapterReviews(listaReviews);
			rv.setLayoutManager(new LinearLayoutManager(getContext()));
			rv.setAdapter(adapter);
		} else {
			ratingBar.setRating(0);
			tvValoracion.setText(R.string.no_reviews);
		}
	}

	private void nuevoChat(ChatCallback chatCallback) {
		SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
		String token = sharedPrefManager.fetchJwt();

		Call<ChatRoom> call = RetrofitClient
				.getInstance()
				.getApi()
				.createChat(token, skill.getUserID(), skill.getId());

		call.enqueue(new Callback<ChatRoom>() {
			@Override
			public void onResponse(@NonNull Call<ChatRoom> call, @NonNull Response<ChatRoom> response) {
				if (response.isSuccessful() && response.body() != null) {
					chatCallback.onChatCreated(response.body());
				} else {
					chatCallback.onError();
					Log.e("SkillFragment", "Error al crear el chat response " + response.message());
				}
			}

			@Override
			public void onFailure(@NonNull Call<ChatRoom> call, @NonNull Throwable t) {
				Toast.makeText(getContext(), R.string.error_crear_chat, Toast.LENGTH_SHORT).show();
				chatCallback.onError();
			}
		});
	}

	private void cargarSkill() {
		List<SlideModel> listaSlide = new ArrayList<>();
		String urlLocal = "http://10.0.2.2:8080";

		for (String url : skill.getImageList()) {
			url = urlLocal + url;
			listaSlide.add(new SlideModel(url, ScaleTypes.CENTER_INSIDE));
		}

		getUser(skill.getUserID(), new UserCallback() {
			@Override
			public void onUserLoaded(User user) {

				if (user.getImageURL() != null) {
					Glide.with(getContext()).load(urlLocal + user.getImageURL()).into(imvUser);
				} else {
					imvUser.setImageResource(R.drawable.user);
				}
				tvUsername.setText(user.getUsername());
				tvTitVal.setText(String.format(getString(R.string.tit_valoraciones), user.getFullName()));

				cargarReviews(user);
			}

			@Override
			public void onError() {
				Toast.makeText(getContext(), R.string.error_usuario, Toast.LENGTH_SHORT).show();
			}
		});

		slider.setImageList(listaSlide);

		tvPrecio.setText(String.format(getString(R.string.tv_precio_inicio), skill.getPrice()));
		tvTitulo.setText(skill.getTitle());
		tvDescripcion.setText(skill.getDescription());
	}

	private void getUser(UUID userID, final UserCallback callback) {
		Call<User> call = RetrofitClient
				.getInstance()
				.getApi()
				.getUserDataFromUUID(userID);

		call.enqueue(new Callback<User>() {
			@Override
			public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
				if (response.isSuccessful() && response.body() != null) {
					callback.onUserLoaded(response.body());
				} else {
					callback.onError();
					Log.e("AdapterSkills", "Error al cargar el usuario" + response.message());
				}
			}

			@Override
			public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
				Toast.makeText(getContext(), R.string.error_usuario, Toast.LENGTH_SHORT).show();
				callback.onError();
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnContratar) {

		} else if (v.getId() == R.id.btnAniadirValoracion) {
			Intent i = new Intent(getContext(), TopBarActivity.class);
			i.putExtra("rellenar", "fragmentoAniadirReview");
			i.putExtra("review", skill);
			startActivity(i);
		}
	}
}