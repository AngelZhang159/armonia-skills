package com.dam.armoniaskills.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dam.armoniaskills.R;
import com.dam.armoniaskills.authentication.SharedPrefManager;
import com.dam.armoniaskills.model.Review;
import com.dam.armoniaskills.model.Skill;
import com.dam.armoniaskills.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReviewFragment extends Fragment {

	private static final String ARG_PARAM1 = "skill";

	RatingBar rating;
	EditText etContenido;

	Button btnConfirmarNuevaReview;
	int valoracion;
	private Skill skill;

	public ReviewFragment() {
	}

	public static ReviewFragment newInstance(Skill skill) {
		ReviewFragment fragment = new ReviewFragment();
		Bundle args = new Bundle();
		args.putParcelable(ARG_PARAM1, skill);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			skill = getArguments().getParcelable(ARG_PARAM1);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_review, container, false);

		rating = v.findViewById(R.id.ratingBar);
		etContenido = v.findViewById(R.id.etContenido);
		btnConfirmarNuevaReview = v.findViewById(R.id.btnConfirmarNuevaReview);

		valoracion = (int) rating.getRating();

		rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				valoracion = (int) rating;
			}
		});

		btnConfirmarNuevaReview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				guardarReview();
			}
		});

		return v;
	}

	private void guardarReview() {
		String content = String.valueOf(etContenido.getText());

		Review review = new Review(content, valoracion, null, skill.getUserID(), null, null, null);

		Log.i("cacaa", review.toString());
		SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
		String jwt = sharedPrefManager.fetchJwt();

		Call<ResponseBody> call = RetrofitClient
				.getInstance()
				.getApi()
				.addReview(jwt, review);

		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				if (response.isSuccessful() && response.body() != null) {
					Toast.makeText(getContext(), R.string.valoracion, Toast.LENGTH_SHORT).show();

					FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
					FragmentTransaction transaction = fragmentManager.beginTransaction();

					SkillFragment grupoFragment = SkillFragment.newInstance(skill);
					transaction.replace(R.id.flTopBar, grupoFragment);

					transaction.commit();

				} else {
					Log.e("Review", "Error al añadir review al usuario: " + skill.getUserID() + ". Mensaje de error: " + response.message());
				}
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable throwable) {
				Log.e("Review", "Error al añadir review al usuario: " + skill.getUserID() + ". Mensaje de error: " + throwable.getMessage());
			}
		});
	}
}