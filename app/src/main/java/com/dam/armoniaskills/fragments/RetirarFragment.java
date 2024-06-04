package com.dam.armoniaskills.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dam.armoniaskills.R;
import com.dam.armoniaskills.authentication.SharedPrefManager;
import com.dam.armoniaskills.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class RetirarFragment extends Fragment implements View.OnClickListener {

	Button btnRetirar;
	TextView tvCantidad;
	EditText etCantidadRetirar;
	Double balanceCuenta;

	private String current;

	public RetirarFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_retirar, container, false);

		btnRetirar = v.findViewById(R.id.btnRetirarDinero);
		tvCantidad = v.findViewById(R.id.tvDineroDisponibleRet);
		etCantidadRetirar = v.findViewById(R.id.etCantidadDineroRetirar);

		etCantidadRetirar.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!s.toString().equals(current)) {
					// Enforce two decimal places
					int decimalIndex = s.toString().indexOf(".");
					if (decimalIndex > 0) {
						if (s.toString().length() - decimalIndex - 1 > 2) {
							String newText = s.toString().substring(0, decimalIndex + 3);
							current = newText;
							etCantidadRetirar.setText(newText);
							etCantidadRetirar.setSelection(newText.length());
						} else {
							current = s.toString();
						}
					} else {
						current = s.toString();
					}
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		rellenarDinero();

		btnRetirar.setOnClickListener(this);

		return v;
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
					Double dinero = response.body();
					tvCantidad.setText(String.format("%s€", String.valueOf(dinero)));
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
		if (v.getId() == R.id.btnRetirarDinero) {
			if (!current.isEmpty()) {
				double cantidadFormateada = Double.parseDouble(current);
				SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
				String token = sharedPrefManager.fetchJwt();

				Call<ResponseBody> call = RetrofitClient
						.getInstance()
						.getApi()
						.retirarDinero(token, cantidadFormateada);

				call.enqueue(new retrofit2.Callback<ResponseBody>() {
					@Override
					public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
						if (response.isSuccessful()) {
							Toast.makeText(getContext(), R.string.ok_retirar, Toast.LENGTH_SHORT).show();
							rellenarDinero();
							etCantidadRetirar.setText("");
							Log.e("Retirar", "onResponse: " + response);
							aniadirHistorial(cantidadFormateada);
						} else {
							Log.e("Retirar", "onResponse: " + response);
							Toast.makeText(getContext(), R.string.err_retirar, Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
						Toast.makeText(getContext(), R.string.err_retirar, Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				Toast.makeText(getContext(), R.string.cantidad_obligatoria, Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getContext(), R.string.cantidad_obligatoria, Toast.LENGTH_SHORT).show();
		}
	}

	private void aniadirHistorial(double cantidadFormateada) {
		//Añadir retiro al historial
	}
}