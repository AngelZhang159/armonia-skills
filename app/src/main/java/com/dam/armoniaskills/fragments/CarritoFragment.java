package com.dam.armoniaskills.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.armoniaskills.R;
import com.dam.armoniaskills.authentication.SharedPrefManager;
import com.dam.armoniaskills.dto.ComprasVentasDTO;
import com.dam.armoniaskills.network.RetrofitClient;
import com.dam.armoniaskills.recyclerutils.ComprasVentasAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarritoFragment extends BottomSheetDialogFragment {

	TextView tvNoCompras, tvNoVentas;
	RecyclerView rvCompras, rvVentas;
	ArrayList<ComprasVentasDTO> compras, ventas;
	// Assume you have an adapter class named ComprasVentasAdapter
	ComprasVentasAdapter comprasAdapter, ventasAdapter;
	CircularProgressIndicator progressBar;
	MaterialDivider divider;

	public CarritoFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_carrito, container, false);

		// Initialize the RecyclerViews and their adapters
		rvCompras = view.findViewById(R.id.rvCompras);
		rvVentas = view.findViewById(R.id.rvVentas);
		tvNoCompras = view.findViewById(R.id.tvNoCompra);
		tvNoVentas = view.findViewById(R.id.tvNoVenta);
		progressBar = view.findViewById(R.id.progressBarCarrito);
		divider = view.findViewById(R.id.dividerCarrito);
		divider.setVisibility(View.GONE);

		// Initialize the ArrayLists and the adapters
		compras = new ArrayList<>();
		ventas = new ArrayList<>();


		// Set the adapters to the RecyclerViews
		rvVentas.setAdapter(ventasAdapter);

		// Get the arguments from the Bundle
		SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
		String token = sharedPrefManager.fetchJwt();

		Call<List<ComprasVentasDTO>> callCompras = RetrofitClient
				.getInstance()
				.getApi()
				.getCompras(token);

		callCompras.enqueue(new Callback<List<ComprasVentasDTO>>() {
			@Override
			public void onResponse(@NonNull Call<List<ComprasVentasDTO>> call, @NonNull Response<List<ComprasVentasDTO>> response) {
				if (response.isSuccessful()) {
					compras.clear();
					compras.addAll(response.body());
					configurarRV(rvCompras, compras);

					if (compras.isEmpty()) {
						tvNoCompras.setVisibility(View.VISIBLE);
					} else {
						tvNoCompras.setVisibility(View.GONE);
					}

					Call<List<ComprasVentasDTO>> callVentas = RetrofitClient
							.getInstance()
							.getApi()
							.getVentas(token);
					callVentas.enqueue(new Callback<List<ComprasVentasDTO>>() {
						@Override
						public void onResponse(Call<List<ComprasVentasDTO>> call, Response<List<ComprasVentasDTO>> response) {
							if (response.isSuccessful()) {
								progressBar.hide();
								divider.setVisibility(View.VISIBLE);

								ventas.clear();
								ventas.addAll(response.body());
								configurarRV(rvVentas, ventas);

								if (ventas.isEmpty()) {
									tvNoVentas.setVisibility(View.VISIBLE);
								} else {
									tvNoVentas.setVisibility(View.GONE);
								}
							}
						}

						@Override
						public void onFailure(Call<List<ComprasVentasDTO>> call, Throwable t) {
						}
					});
				}
			}

			@Override
			public void onFailure(Call<List<ComprasVentasDTO>> call, Throwable t) {
			}
		});

		return view;
	}

	private void configurarRV(RecyclerView rv, ArrayList<ComprasVentasDTO> listaCompraVenta) {
		ComprasVentasAdapter adapter = new ComprasVentasAdapter(listaCompraVenta);
		rv.setLayoutManager(new LinearLayoutManager(getContext()));
		rv.setAdapter(adapter);
	}
}