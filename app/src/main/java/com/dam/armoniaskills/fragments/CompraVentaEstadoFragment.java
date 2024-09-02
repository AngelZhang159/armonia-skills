package com.dam.armoniaskills.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.dam.armoniaskills.R;
import com.dam.armoniaskills.authentication.SharedPrefManager;
import com.dam.armoniaskills.dto.ComprasVentasDTO;
import com.dam.armoniaskills.model.StatusCompraEnum;
import com.dam.armoniaskills.network.RetrofitClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class CompraVentaEstadoFragment extends BottomSheetDialogFragment {

	CircleImageView ivFotoPerfil;
	TextView tvNombreUsuario, tvFechaCompra, tvPrecioCompra;
	TextView tvEstadoVenta1, tvEstadoVenta2, tvEstadoVenta3, tvEstadoVenta4, tvEstadoVenta5, tvEstadoVenta6, tvEstadoVenta7;
	Button btnAceptarVenta, btnRechazarVenta, btnEnviadoVenta, btnCompletarVenta;
	TextView tvCompraVentaTitulo;
	LinearProgressIndicator progressBar;
	CircularProgressIndicator progressBarVenta;
	Button btnInfoAceptarVenta, btnInfoRechazarVenta, btnInfoPreparandoVenta, btnInfoEnviadoVenta, btnInfoCompletarVenta;

	public CompraVentaEstadoFragment() {
	}

	public static CompraVentaEstadoFragment newInstance(ComprasVentasDTO compraVenta, boolean esVenta) {
		CompraVentaEstadoFragment fragment = new CompraVentaEstadoFragment();
		Bundle args = new Bundle();
		args.putParcelable("compraVenta", compraVenta);
		args.putBoolean("esVenta", esVenta);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_compra_venta_estado, container, false);

		tvEstadoVenta1 = v.findViewById(R.id.tvEstadoVenta1Contratado);
		tvEstadoVenta2 = v.findViewById(R.id.tvEstadoVenta2Pendiente);
		tvEstadoVenta3 = v.findViewById(R.id.tvEstadoVenta3Rechazado);
		tvEstadoVenta4 = v.findViewById(R.id.tvEstadoVenta4Contratado);
		tvEstadoVenta5 = v.findViewById(R.id.tvEstadoVenta5Preparando);
		tvEstadoVenta6 = v.findViewById(R.id.tvEstadoVenta6Enviado);
		tvEstadoVenta7 = v.findViewById(R.id.tvEstadoVenta7Completado);

		btnAceptarVenta = v.findViewById(R.id.btnAceptarVenta);
		btnRechazarVenta = v.findViewById(R.id.btnRechazarVenta);
		btnEnviadoVenta = v.findViewById(R.id.btnVentaEnviado);
		btnCompletarVenta = v.findViewById(R.id.btnVentaCompletado);

		tvPrecioCompra = v.findViewById(R.id.tvPrecioVentaDetalle);
		tvCompraVentaTitulo = v.findViewById(R.id.compraVentaTitulo);
		btnInfoAceptarVenta = v.findViewById(R.id.btnInfoAceptarVenta);
		btnInfoRechazarVenta = v.findViewById(R.id.btnInfoRechazarVenta);
		btnInfoPreparandoVenta = v.findViewById(R.id.btnInfoPreparandoVenta);
		btnInfoEnviadoVenta = v.findViewById(R.id.btnInfoEnviadoVenta);
		btnInfoCompletarVenta = v.findViewById(R.id.btnInfoCompletadoVenta);

		ivFotoPerfil = v.findViewById(R.id.civImagenUsuarioVenta);
		tvNombreUsuario = v.findViewById(R.id.tvNombreUsuarioVenta);
		tvFechaCompra = v.findViewById(R.id.tvHoraVenta);

		progressBar = v.findViewById(R.id.progressBarVenta);
		progressBarVenta = v.findViewById(R.id.circleProgressBarVenta);

		btnInfoAceptarVenta.setOnClickListener(v1 -> {
			MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
			builder.setTitle(R.string.aceptar_venta);
			builder.setMessage(R.string.aceptar_venta_info);
			builder.setPositiveButton(R.string.btn_aceptar_d, (dialog, which) -> {
			});
			builder.show();
		});

		btnInfoRechazarVenta.setOnClickListener(v1 -> {
			MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
			builder.setTitle(R.string.rechazar_venta);
			builder.setMessage(R.string.rechazar_venta_info);
			builder.setPositiveButton(R.string.btn_aceptar_d, (dialog, which) -> {
			});
			builder.show();
		});

		btnInfoPreparandoVenta.setOnClickListener(v1 -> {
			MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
			builder.setTitle(R.string.preparando_venta);
			builder.setMessage(R.string.preparando_venta_info);
			builder.setPositiveButton(R.string.btn_aceptar_d, (dialog, which) -> {
			});
			builder.show();
		});

		btnInfoEnviadoVenta.setOnClickListener(v1 -> {
			MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
			builder.setTitle(R.string.enviado_venta);
			builder.setMessage(R.string.enviado_venta_info);
			builder.setPositiveButton(R.string.aceptar, (dialog, which) -> {
			});
			builder.show();
		});

		btnInfoCompletarVenta.setOnClickListener(v1 -> {
			MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
			builder.setTitle(R.string.completar_venta);
			builder.setMessage(R.string.completar_venta_info);
			builder.setPositiveButton(R.string.btn_aceptar_d, (dialog, which) -> {
			});
			builder.show();
		});

		Bundle args = getArguments();
		if (args != null) {
			ComprasVentasDTO compraVenta = args.getParcelable("compraVenta");
			boolean esVenta = args.getBoolean("esVenta");

			if (esVenta) {
				tvCompraVentaTitulo.setText(R.string.tienes_una_nueva_venta);
			} else {
				tvCompraVentaTitulo.setText(R.string.tienes_una_nueva_compra);
			}

			if (compraVenta != null) {
				configurarDatos(compraVenta, esVenta);
			}
		}
		return v;
	}

	private void configurarDatos(ComprasVentasDTO compraVenta, boolean esVenta) {
		tvNombreUsuario.setText(compraVenta.getUsername());
		String url = "http://13.39.104.96:8080" + compraVenta.getImageURL();
		Glide.with(getContext()).load(url).into(ivFotoPerfil);
		Date date = compraVenta.getDate();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", getResources().getConfiguration().getLocales().get(0));
		String formattedDate = formatter.format(date);
		tvFechaCompra.setText(formattedDate);
		tvPrecioCompra.setText(String.format(getString(R.string.tv_precio_inicio), compraVenta.getPrice()));

		switch (compraVenta.getStatus()) {
			case PENDIENTE:
				progressBar.setProgress(25, true);
				tvEstadoVenta1.setVisibility(View.VISIBLE);
				tvEstadoVenta2.setVisibility(View.VISIBLE);
				btnInfoAceptarVenta.setVisibility(View.VISIBLE);
				tvEstadoVenta3.setVisibility(View.GONE);
				tvEstadoVenta4.setVisibility(View.GONE);
				tvEstadoVenta5.setVisibility(View.GONE);
				tvEstadoVenta6.setVisibility(View.GONE);
				tvEstadoVenta7.setVisibility(View.GONE);
				btnAceptarVenta.setVisibility(View.GONE);
				btnRechazarVenta.setVisibility(View.GONE);
				btnCompletarVenta.setVisibility(View.GONE);
				btnEnviadoVenta.setVisibility(View.GONE);

				if (esVenta) {
					btnAceptarVenta.setVisibility(View.VISIBLE);
					btnRechazarVenta.setVisibility(View.VISIBLE);

					btnAceptarVenta.setOnClickListener(v1 -> {
						aceptarVenta(compraVenta.getId());
						btnAceptarVenta.setEnabled(false);
						btnRechazarVenta.setEnabled(false);
					});
					btnRechazarVenta.setOnClickListener(v1 -> {
						rechazarVenta(compraVenta.getId());
						btnAceptarVenta.setEnabled(false);
						btnRechazarVenta.setEnabled(false);
					});
				}

				break;
			case ACEPTADO:
				progressBar.setProgress(50, true);
				tvEstadoVenta1.setVisibility(View.VISIBLE);
				tvEstadoVenta2.setVisibility(View.VISIBLE);
				btnInfoAceptarVenta.setVisibility(View.VISIBLE);
				tvEstadoVenta3.setVisibility(View.GONE);
				tvEstadoVenta4.setVisibility(View.VISIBLE);
				tvEstadoVenta5.setVisibility(View.VISIBLE);
				btnInfoPreparandoVenta.setVisibility(View.VISIBLE);
				tvEstadoVenta6.setVisibility(View.GONE);
				tvEstadoVenta7.setVisibility(View.GONE);
				btnAceptarVenta.setVisibility(View.GONE);
				btnRechazarVenta.setVisibility(View.GONE);
				btnEnviadoVenta.setVisibility(View.GONE);
				btnCompletarVenta.setVisibility(View.GONE);

				if (esVenta) {
					btnEnviadoVenta.setVisibility(View.VISIBLE);
					btnEnviadoVenta.setOnClickListener(v1 -> {
						enviarVenta(compraVenta.getId());
						btnEnviadoVenta.setEnabled(false);
					});
				}

				break;
			case RECHAZADO:
				progressBar.setProgress(0, true);
				tvEstadoVenta1.setVisibility(View.VISIBLE);
				tvEstadoVenta2.setVisibility(View.VISIBLE);
				btnInfoAceptarVenta.setVisibility(View.VISIBLE);
				tvEstadoVenta3.setVisibility(View.VISIBLE);
				btnInfoRechazarVenta.setVisibility(View.VISIBLE);
				tvEstadoVenta4.setVisibility(View.GONE);
				tvEstadoVenta5.setVisibility(View.GONE);
				tvEstadoVenta6.setVisibility(View.GONE);
				tvEstadoVenta7.setVisibility(View.GONE);
				btnAceptarVenta.setVisibility(View.GONE);
				btnRechazarVenta.setVisibility(View.GONE);
				btnCompletarVenta.setVisibility(View.GONE);
				btnEnviadoVenta.setVisibility(View.GONE);
				break;
			case ENVIADO:
				progressBar.setProgress(75, true);
				tvEstadoVenta1.setVisibility(View.VISIBLE);
				tvEstadoVenta2.setVisibility(View.VISIBLE);
				btnInfoAceptarVenta.setVisibility(View.VISIBLE);
				tvEstadoVenta3.setVisibility(View.GONE);
				tvEstadoVenta4.setVisibility(View.VISIBLE);
				tvEstadoVenta5.setVisibility(View.VISIBLE);
				btnInfoPreparandoVenta.setVisibility(View.VISIBLE);
				tvEstadoVenta6.setVisibility(View.VISIBLE);
				btnInfoEnviadoVenta.setVisibility(View.VISIBLE);
				tvEstadoVenta7.setVisibility(View.GONE);
				btnAceptarVenta.setVisibility(View.GONE);
				btnRechazarVenta.setVisibility(View.GONE);
				btnCompletarVenta.setVisibility(View.GONE);
				btnEnviadoVenta.setVisibility(View.GONE);

				if (!esVenta) {
					btnCompletarVenta.setVisibility(View.VISIBLE);
					btnCompletarVenta.setOnClickListener(v1 -> {
						completarVenta(compraVenta.getId());
						btnCompletarVenta.setEnabled(false);
					});
				}
				break;
			case COMPLETADO:
				progressBar.setProgress(100, true);
				tvEstadoVenta1.setVisibility(View.VISIBLE);
				tvEstadoVenta2.setVisibility(View.VISIBLE);
				btnInfoAceptarVenta.setVisibility(View.VISIBLE);
				tvEstadoVenta3.setVisibility(View.GONE);
				tvEstadoVenta4.setVisibility(View.VISIBLE);
				tvEstadoVenta5.setVisibility(View.VISIBLE);
				btnInfoPreparandoVenta.setVisibility(View.VISIBLE);
				tvEstadoVenta6.setVisibility(View.VISIBLE);
				btnInfoEnviadoVenta.setVisibility(View.VISIBLE);
				tvEstadoVenta7.setVisibility(View.VISIBLE);
				btnInfoCompletarVenta.setVisibility(View.VISIBLE);
				btnAceptarVenta.setVisibility(View.GONE);
				btnRechazarVenta.setVisibility(View.GONE);
				btnCompletarVenta.setVisibility(View.GONE);
				btnEnviadoVenta.setVisibility(View.GONE);
				break;
			default:
				break;
		}
	}

	private void enviarVenta(UUID id) {
		progressBarVenta.setVisibility(View.VISIBLE);
		progressBarVenta.show();
		SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
		String token = sharedPrefManager.fetchJwt();

		Call<ResponseBody> callCompletarVenta = RetrofitClient
				.getInstance()
				.getApi()
				.modificarVenta(token, id, StatusCompraEnum.ENVIADO);

		callCompletarVenta.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
				if (response.isSuccessful()) {
					recargarDatosVenta(token, id);
				}
			}

			@Override
			public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
			}
		});
	}

	private void completarVenta(UUID id) {
		progressBarVenta.setVisibility(View.VISIBLE);
		progressBarVenta.show();
		SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
		String token = sharedPrefManager.fetchJwt();

		Call<ResponseBody> callCompletarVenta = RetrofitClient
				.getInstance()
				.getApi()
				.modificarVenta(token, id, StatusCompraEnum.COMPLETADO);

		callCompletarVenta.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
				if (response.isSuccessful()) {
					recargarDatosVenta(token, id);
				}
			}

			@Override
			public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
			}
		});
	}

	private void rechazarVenta(UUID id) {
		progressBarVenta.setVisibility(View.VISIBLE);
		progressBarVenta.show();
		SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
		String token = sharedPrefManager.fetchJwt();

		Call<ResponseBody> callRechazarVenta = RetrofitClient
				.getInstance()
				.getApi()
				.modificarVenta(token, id, StatusCompraEnum.RECHAZADO);

		callRechazarVenta.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
				if (response.isSuccessful()) {
					recargarDatosVenta(token, id);
				}
			}

			@Override
			public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
			}
		});
	}

	private void aceptarVenta(UUID idVenta) {
		progressBarVenta.setVisibility(View.VISIBLE);
		progressBarVenta.show();

		SharedPrefManager sharedPrefManager = new SharedPrefManager(getContext());
		String token = sharedPrefManager.fetchJwt();

		Call<ResponseBody> callAceptarVenta = RetrofitClient
				.getInstance()
				.getApi()
				.modificarVenta(token, idVenta, StatusCompraEnum.ACEPTADO);

		callAceptarVenta.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
				if (response.isSuccessful()) {
					recargarDatosVenta(token, idVenta);
				}
			}

			@Override
			public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
			}
		});
	}

	private void recargarDatosVenta(String token, UUID idVenta) {

		Call<ComprasVentasDTO> callVenta = RetrofitClient
				.getInstance()
				.getApi()
				.getCompraVentaById(token, idVenta);

		callVenta.enqueue(new Callback<ComprasVentasDTO>() {
			@Override
			public void onResponse(@NonNull Call<ComprasVentasDTO> call, @NonNull retrofit2.Response<ComprasVentasDTO> response) {
				if (response.isSuccessful()) {
					ComprasVentasDTO compraVenta = response.body();
					if (compraVenta != null) {
						progressBarVenta.hide();
						configurarDatos(compraVenta, true);
					}
				}
			}

			@Override
			public void onFailure(@NonNull Call<ComprasVentasDTO> call, @NonNull Throwable t) {
			}
		});
	}
}