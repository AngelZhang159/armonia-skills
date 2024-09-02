package com.dam.armoniaskills.recyclerutils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dam.armoniaskills.R;
import com.dam.armoniaskills.model.Review;

import java.util.List;

public class AdapterReviews extends RecyclerView.Adapter<AdapterReviews.ReviewVH> {

	List<Review> listaReviews;

	public AdapterReviews(List<Review> listaReviews) {
		this.listaReviews = listaReviews;
	}

	@NonNull
	@Override
	public ReviewVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);

		return new ReviewVH(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ReviewVH holder, int position) {
		holder.bindReview(listaReviews.get(position));
	}

	@Override
	public int getItemCount() {
		return listaReviews.size();
	}

	public class ReviewVH extends RecyclerView.ViewHolder {

		ImageView ivUser;
		TextView tvUser, tvEstrellas, tvValoracion;

		public ReviewVH(@NonNull View itemView) {
			super(itemView);

			ivUser = itemView.findViewById(R.id.ivUserReview);
			tvUser = itemView.findViewById(R.id.tvUserReview);
			tvEstrellas = itemView.findViewById(R.id.tvEstrellas);
			tvValoracion = itemView.findViewById(R.id.tvValoracion);
		}

		public void bindReview(Review review) {
			String urlLocal = "http://13.39.104.96:8080";

			if (review.getImageUrl() == null) {
				ivUser.setImageResource(R.drawable.user);
			} else {
				Glide.with(itemView).load(urlLocal + review.getImageUrl()).into(ivUser);
			}

			tvUser.setText(review.getUsername());
			tvEstrellas.setText(String.format(itemView.getContext().getString(R.string.tv_estrellas), review.getStars()));

			if (review.getContent().isEmpty()) {
				tvValoracion.setVisibility(View.GONE);
			} else {
				tvValoracion.setText(review.getContent());
			}
		}
	}
}
