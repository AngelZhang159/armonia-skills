package com.dam.armoniaskills.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dam.armoniaskills.R;

public class DepositarFragment extends Fragment implements View.OnClickListener {

    Button btnDepositar;
    EditText etCantidad;
    TextView tvCantidad;
    Double balanceCuenta;

    private String current;

    public DepositarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_depositar, container, false);

        btnDepositar = v.findViewById(R.id.btnDepositarDinero);
        etCantidad = v.findViewById(R.id.etCantidadDineroDepositar);

        tvCantidad = v.findViewById(R.id.tvDineroDisponibleDep);

        btnDepositar.setOnClickListener(this);

        current = "";

        etCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    // Enforce two decimal places without Euro symbol
                    int decimalIndex = s.toString().indexOf(".");
                    if (decimalIndex > 0) {
                        if (s.toString().length() - decimalIndex - 1 > 2) {
                            String newText = s.toString().substring(0, decimalIndex + 3);
                            current = newText;
                            etCantidad.setText(newText);
                            etCantidad.setSelection(newText.length());
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

        return v;
    }

    private void rellenarDinero() {
        //Recuperar balance y mostrarlo
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnDepositarDinero) {
            if (!current.isEmpty()) {

            } else {
                Toast.makeText(getContext(), R.string.cantidad_obligatoria, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void aniadirHistorial(double cantidadFormateada) {

    }
}