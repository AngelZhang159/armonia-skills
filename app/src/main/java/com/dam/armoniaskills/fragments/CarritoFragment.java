package com.dam.armoniaskills.fragments;

import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarritoFragment extends BottomSheetDialogFragment implements View.OnClickListener {

	TextView tvNoCompras, tvNoVentas;
	RecyclerView rvCompras, rvVentas;
	ArrayList<ComprasVentasDTO> compras, ventas;
	ComprasVentasAdapter ventasAdapter;
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
								configurarRV(rvCompras, compras);
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
		ComprasVentasAdapter adapter = new ComprasVentasAdapter(listaCompraVenta, this);
		rv.setLayoutManager(new LinearLayoutManager(getContext()));
		rv.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		RecyclerView parentRecyclerView = (RecyclerView) v.getParent();
		int pos;
		CompraVentaEstadoFragment newFragment = null;

		if (parentRecyclerView == rvCompras) {
			pos = rvCompras.getChildAdapterPosition(v);
			newFragment = CompraVentaEstadoFragment.newInstance(compras.get(pos), false);
		} else if (parentRecyclerView == rvVentas) {
			pos = rvVentas.getChildAdapterPosition(v);
			newFragment = CompraVentaEstadoFragment.newInstance(ventas.get(pos), true);
		}

		if (newFragment != null) {
			dismiss();
			newFragment.show(getParentFragmentManager(), newFragment.getTag());
		} else {
			Log.e("CarritoFragment", "Error: clicked view does not belong to any expected RecyclerView");
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
		if (dialog != null) {
			View bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
			if (bottomSheet != null) {
				BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
				behavior.setDraggable(false);
			}
		}
	}
}