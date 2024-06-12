package com.dam.armoniaskills.utils;

import android.content.Context;

import com.dam.armoniaskills.R;
import com.dam.armoniaskills.model.Categoria;

import java.util.ArrayList;

public class CategoriaUtil {

	public static ArrayList<Categoria> rellenarLista(Context context) {
		ArrayList<Categoria> listaCategorias = new ArrayList<>();

		String[] nombres = {context.getString(R.string.categoria0), context.getString(com.dam.armoniaskills.R.string.categoria1), context.getString(R.string.categoria2), context.getString(R.string.categoria3), context.getString(R.string.categoria4), context.getString(R.string.categoria5), context.getString(R.string.categoria6), context.getString(R.string.categoria7), context.getString(R.string.categoria8),
				context.getString(R.string.categoria9), context.getString(R.string.categoria10), context.getString(R.string.categoria11), context.getString(R.string.categoria12),
				context.getString(R.string.categoria13), context.getString(R.string.categoria14), context.getString(R.string.categoria15), context.getString(R.string.categoria16),
				context.getString(R.string.categoria17), context.getString(R.string.categoria18), context.getString(R.string.categoria19), context.getString(R.string.categoria20),
				context.getString(R.string.categoria21), context.getString(R.string.categoria22)};

		int[] imagenes = {R.drawable.redglobal, R.drawable.design, R.drawable.digitalmarketing, R.drawable.videoediting,
				R.drawable.musicalnote, R.drawable.webprogramming, R.drawable.cooperation,
				R.drawable.photography, R.drawable.helmet, R.drawable.house, R.drawable.redcarpet,
				R.drawable.transportation, R.drawable.skincare, R.drawable.carrepair, R.drawable.education,
				R.drawable.fastfood, R.drawable.financialadvisor, R.drawable.healthcare, R.drawable.pets,
				R.drawable.electriccar, R.drawable.dancer, R.drawable.travelandtourism, R.drawable.menu};

		for (int i = 0; i < nombres.length; i++) {
			Categoria categoria = new Categoria(imagenes[i], nombres[i]);
			listaCategorias.add(categoria);
		}

		return listaCategorias;
	}
}