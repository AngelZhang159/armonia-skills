package com.dam.armoniaskills.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.armoniaskills.R;
import com.dam.armoniaskills.TopBarActivity;
import com.dam.armoniaskills.model.HistorialBalance;
import com.dam.armoniaskills.recyclerutils.AdapterBalance;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;

public class BalanceFragment extends Fragment implements View.OnClickListener {

	Button btnDepositar, btnRetirar;
	TextView tvDinero;
	RecyclerView rv;
	ArrayList<HistorialBalance> listaHistorialBalance;
	AdapterBalance adapterBalance;
	MaterialButton btnElim;

	public BalanceFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_balance, container, false);

		tvDinero = v.findViewById(R.id.tvDinero);
		btnDepositar = v.findViewById(R.id.btnDepositarBalance);
		btnRetirar = v.findViewById(R.id.btnRetirarBalance);

		btnDepositar.setOnClickListener(this);
		btnRetirar.setOnClickListener(this);
		btnElim = v.findViewById(R.id.btnBorrarHistBal);
		btnElim.setOnClickListener(this);

		listaHistorialBalance = new ArrayList<>();
		rv = v.findViewById(R.id.rvBalance);

		rellenarDinero();
		rellenarHistorial();

		return v;
	}

	private void rellenarHistorial() {

	}

	private void rellenarDinero() {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnDepositarBalance) {
			Intent i = new Intent(getContext(), TopBarActivity.class);
			i.putExtra("rellenar", "fragmentoBalanceDepositar");
			startActivity(i);
		} else if (v.getId() == R.id.btnRetirarBalance) {
			Intent i = new Intent(getContext(), TopBarActivity.class);
			i.putExtra("rellenar", "fragmentoBalanceRetirar");
			startActivity(i);
		} else if (v.getId() == R.id.btnBorrarHistBal) {

		}
	}

	public void configurarRV() {
		adapterBalance = new AdapterBalance(listaHistorialBalance);

		rv.setLayoutManager(new LinearLayoutManager(getContext()));
		rv.setAdapter(adapterBalance);
		rv.setHasFixedSize(true);
	}
}