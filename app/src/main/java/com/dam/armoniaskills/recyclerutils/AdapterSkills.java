package com.dam.armoniaskills.recyclerutils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dam.armoniaskills.R;
import com.dam.armoniaskills.model.Skill;
import com.dam.armoniaskills.model.User;
import com.dam.armoniaskills.network.RetrofitClient;
import com.dam.armoniaskills.network.UserCallback;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterSkills extends RecyclerView.Adapter<AdapterSkills.SkillsVH> implements View.OnClickListener {

	ArrayList<Skill> listaSkills;
	View.OnClickListener listener;

	public AdapterSkills(ArrayList<Skill> listaSkills, View.OnClickListener listener) {
		this.listaSkills = listaSkills;
		this.listener = listener;
	}

	@NonNull
	@Override
	public SkillsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skill, parent, false);

		v.setOnClickListener(this);

		return new SkillsVH(v);
	}

	@Override
	public void onBindViewHolder(@NonNull SkillsVH holder, int position) {
		holder.bindSkill(listaSkills.get(position));
	}

	@Override
	public int getItemCount() {
		return listaSkills.size();
	}

	@Override
	public void onClick(View v) {
		listener.onClick(v);
	}

	public class SkillsVH extends RecyclerView.ViewHolder {

		ImageView ivUser;
		TextView tvPrecio;
		TextView tvUser;
		TextView tvTitulo;
		ImageSlider slider;


		public SkillsVH(@NonNull View itemView) {
			super(itemView);

			ivUser = itemView.findViewById(R.id.ivUser);
			tvPrecio = itemView.findViewById(R.id.tvPrecioSkill);
			tvTitulo = itemView.findViewById(R.id.tvTituloSkill);
			tvUser = itemView.findViewById(R.id.tvUser);
			slider = itemView.findViewById(R.id.sliderSkill);

		}

		public void bindSkill(Skill skill) {

			String urlLocal = "http://10.0.2.2:8080";

			getUser(skill.getUserID(), new UserCallback() {
				@Override
				public void onUserLoaded(User user) {
					if (user.getImageURL() != null) {
						Glide.with(itemView).load(urlLocal + user.getImageURL()).into(ivUser);
					} else {
						ivUser.setImageResource(R.drawable.user);
					}
					tvUser.setText(user.getUsername());
				}

				@Override
				public void onError() {
					Log.e("AdapterSkills", "Error al cargar el usuario");
				}
			});

			List<SlideModel> listaSlide = new ArrayList<>();

			for (String url : skill.getImageList()) {
				url = urlLocal + url;
				listaSlide.add(new SlideModel(url, ScaleTypes.CENTER_CROP));
			}

			slider.setImageList(listaSlide);

			tvPrecio.setText(String.format(itemView.getContext().getString(R.string.tv_precio_inicio), skill.getPrice()));
			tvTitulo.setText(skill.getTitle());
		}
	}

	private void getUser(UUID userID, final UserCallback callback) {
		Call<User> call = RetrofitClient
				.getInstance()
				.getApi()
				.getUserDataFromUUID(userID);

		call.enqueue(new Callback<User>() {
			@Override
			public void onResponse(Call<User> call, Response<User> response) {
				if (response.isSuccessful() && response.body() != null) {
					callback.onUserLoaded(response.body());
				} else {
					callback.onError();
					Log.e("AdapterSkills", "Error al cargar el usuario" + response.message());
				}
			}

			@Override
			public void onFailure(Call<User> call, Throwable t) {
				Log.e("AdapterSkills", "Error al cargar el usuario" + t.getMessage());
				callback.onError();
			}
		});
	}
}
