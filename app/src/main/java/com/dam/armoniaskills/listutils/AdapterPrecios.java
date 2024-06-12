package com.dam.armoniaskills.listutils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dam.armoniaskills.R;

public class AdapterPrecios extends ArrayAdapter<String> {

	private Context context;
	private String[] rangosPrecio;

	public AdapterPrecios(Context context, String[] rangosPrecio) {
		super(context, R.layout.item_precio, rangosPrecio);
		this.context = context;
		this.rangosPrecio = rangosPrecio;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_precio, parent, false);
		}

		TextView tvPrecio = convertView.findViewById(R.id.tvRangoPrecio);
		tvPrecio.setText(rangosPrecio[position]);

		return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
		}

		TextView label = convertView.findViewById(android.R.id.text1);
		label.setText(rangosPrecio[position]);

		return convertView;
	}
}
