package com.dam.armoniaskills.recyclerutils;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
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

	public class SkillsVH extends RecyclerView.ViewHolder {

		ImageView ivUser;
		TextView tvPrecio;
		TextView tvUser;
		TextView tvTitulo;
		ImageSlider slider;
		CardView cardView;


		public SkillsVH(@NonNull View itemView) {
			super(itemView);

			ivUser = itemView.findViewById(R.id.ivUser);
			tvPrecio = itemView.findViewById(R.id.tvPrecioSkill);
			tvTitulo = itemView.findViewById(R.id.tvTituloSkill);
			tvUser = itemView.findViewById(R.id.tvUser);
			slider = itemView.findViewById(R.id.sliderSkill);
			cardView = itemView.findViewById(R.id.cardViewSkill);

		}

		public void bindSkill(Skill skill) {

			String urlLocal = "http://13.39.104.96:8080";

			getUser(skill.getUserID(), new UserCallback() {
				@Override
				public void onUserLoaded(User user) {
					if (itemView.getContext() != null && !((Activity) itemView.getContext()).isDestroyed()) {
						if (user.getImageURL() != null) {
							Glide.with(itemView.getContext()).load(urlLocal + user.getImageURL()).into(ivUser);
						} else {
							ivUser.setImageResource(R.drawable.user);
						}
						tvUser.setText(user.getUsername());
					}
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

			tvPrecio.setText(String.format(itemView.getContext().getString(R.string.tv_precio_inicio), Double.parseDouble(skill.getPrice())));
			tvTitulo.setText(skill.getTitle());

			List<String> imageList = skill.getImageList();
			if (!imageList.isEmpty()) {
				String primaraFoto = urlLocal + imageList.get(0);

				Glide.with(itemView.getContext())
						.asBitmap()
						.load(primaraFoto)
						.into(new CustomTarget<Bitmap>() {
							@Override
							public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
								if (resource != null) {
									Palette.from(resource).generate((palette) -> {
										if (palette != null) {
											int color = itemView.getContext().getColor(R.color.md_theme_background);
											int currentNightMode = itemView.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
											if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
												// Get the dominant color and make it very dark
												color = palette.getDominantColor(itemView.getContext().getColor(R.color.md_theme_background));
												float[] hsl = new float[3];
												ColorUtils.colorToHSL(color, hsl);
												hsl[2] *= 0.2f; // make it very dark
												color = ColorUtils.HSLToColor(hsl);
											} else if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
												// Get the dominant color and make it pastel
												color = palette.getDominantColor(itemView.getContext().getColor(R.color.md_theme_background));
												float[] hsl = new float[3];
												ColorUtils.colorToHSL(color, hsl);
												hsl[1] *= 0.5f; // reduce saturation to make it pastel
												hsl[2] = 0.9f; // increase lightness to make it pastel
												color = ColorUtils.HSLToColor(hsl);
											}
											cardView.setCardBackgroundColor(color);
											int textColor;

											// Set the text color based on the background color
											if (ColorUtils.calculateLuminance(color) >= 0.5) {
												textColor = (itemView.getContext().getColor(R.color.md_theme_onSurface));

											} else {
												textColor = (itemView.getContext().getColor(R.color.md_theme_onSurface_dark));
											}

											tvPrecio.setTextColor(textColor);
											tvTitulo.setTextColor(textColor);
											tvUser.setTextColor(textColor);

										}
									});
								}
							}

							@Override
							public void onLoadCleared(Drawable placeholder) {
								// Handle placeholder if necessary
							}
						});
			}
		}

	}
}
