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
import com.dam.armoniaskills.authentication.SharedPrefManager;
import com.dam.armoniaskills.model.HistorialBalance;
import com.dam.armoniaskills.network.RetrofitClient;
import com.dam.armoniaskills.recyclerutils.AdapterBalance;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;

import retrofit2.Call;

public class BalanceFragment extends Fragment implements View.OnClickListener {

	Button btnDepositar, btnRetirar;
	TextView tvDinero;
	RecyclerView rv;
	ArrayList<HistorialBalance> listaHistorialBalance;
	AdapterBalance adapterBalance;
	MaterialButton btnElim;
	CircularProgressIndicator progressBar;

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
		progressBar = v.findViewById(R.id.progressBarBalance);

		listaHistorialBalance = new ArrayList<>();
		rv = v.findViewById(R.id.rvBalance);

		rellenarDinero();
		rellenarHistorial();

		return v;
	}

	private void rellenarHistorial() {

	}

	private void rellenarDinero() {
		SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
		String token = sharedPrefManager.fetchJwt();

		Call<Double> call = RetrofitClient
				.getInstance()
				.getApi()
				.getBalance(token);

		call.enqueue(new retrofit2.Callback<Double>() {
			@Override
			public void onResponse(@NonNull Call<Double> call, @NonNull retrofit2.Response<Double> response) {
				if (response.isSuccessful()) {
					progressBar.setVisibility(View.GONE);
					Double dinero = response.body();
					tvDinero.setText(String.format(getString(R.string.tv_precio_inicio), dinero));
				} else {
					Toast.makeText(getContext(), R.string.error_balance, Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(@NonNull Call<Double> call, @NonNull Throwable t) {
				Toast.makeText(getContext(), R.string.error_balance, Toast.LENGTH_SHORT).show();
			}
		});
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