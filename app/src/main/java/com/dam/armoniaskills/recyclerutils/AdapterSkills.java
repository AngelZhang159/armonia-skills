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
import com.dam.armoniaskills.model.Skill;

import java.util.ArrayList;

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

        return new SkillsVH(v) ;
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

        ImageView iv;
        TextView tvPrecio;
        TextView tvTitulo;

        public SkillsVH(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.ivSkill);
            tvPrecio = itemView.findViewById(R.id.tvPrecioSkill);
            tvTitulo = itemView.findViewById(R.id.tvTituloSkill);
        }

        public void bindSkill(Skill skill) {
            Glide.with(itemView).load(skill.getImageList().get(0)).into(iv);
            tvPrecio.setText(skill.getPrice());
            tvTitulo.setText(skill.getTitle());
        }
    }
}
