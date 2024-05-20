package com.dam.armoniaskills.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    private EditText etValoracion, etContenido;

    private Button btnConfirmarNuevaReview;

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

        etValoracion = v.findViewById(R.id.etValoracion);
        etContenido = v.findViewById(R.id.etContenido);
        btnConfirmarNuevaReview = v.findViewById(R.id.btnConfirmarNuevaReview);

        btnConfirmarNuevaReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etValoracion.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Debes añadir uan valoracion del 1 al 5", Toast.LENGTH_SHORT).show();
                } else if(Integer.parseInt(etValoracion.getText().toString()) > 5 || Integer.parseInt(etValoracion.getText().toString()) < 1){
                    Toast.makeText(getContext(), "La valoración debe estar entre 1 y 5", Toast.LENGTH_SHORT).show();
                } else {

                    guardarReview();
                }


            }
        });



        return v;
    }

    private void guardarReview() {


        String content = String.valueOf(etContenido.getText());
        int stars = Integer.parseInt(etValoracion.getText().toString());

        Review review = new Review(content, stars, null, skill.getUserID(), null, null, null);

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
                    Toast.makeText(getContext(), "¡Valoración Añadida con Exito!", Toast.LENGTH_SHORT).show();

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