package com.dam.armoniaskills.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dam.armoniaskills.R;
import com.dam.armoniaskills.model.Skill;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class SkillFragment extends Fragment {

    private static final String ARG_SKILL = "skill";

    ImageSlider slider;
    TextView tvPrecio, tvTitulo, tvDescripcion, tvUsername, tvValoracion;
    ImageView imvUser;
    RecyclerView rv;
    Button btnChat, btnContratar;

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
        imvUser = v.findViewById(R.id.imvUser);
        rv = v.findViewById(R.id.rvValDetalle);
        btnChat = v.findViewById(R.id.btnChat);
        btnContratar = v.findViewById(R.id.btnContratar);

        cargarSkill();

        return v;
    }

    private void cargarSkill() {
        List<SlideModel> listaSlide = new ArrayList<>();

        for (String url : skill.getImageList()) {
            listaSlide.add(new SlideModel(url, ScaleTypes.CENTER_CROP));
        }

        slider.setImageList(listaSlide);

        tvPrecio.setText(skill.getPrice());
        tvTitulo.setText(skill.getTitle());
        tvDescripcion.setText(skill.getDescription());
    }
}