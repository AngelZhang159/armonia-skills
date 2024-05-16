package com.dam.armoniaskills.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dam.armoniaskills.R;
import com.dam.armoniaskills.model.Skill;

public class SkillFragment extends Fragment {

    private static final String ARG_SKILL = "skill";

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_skill, container, false);
    }
}