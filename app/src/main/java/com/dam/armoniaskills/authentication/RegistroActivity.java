package com.dam.armoniaskills.authentication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dam.armoniaskills.R;
import com.dam.armoniaskills.model.User;
import com.dam.armoniaskills.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etUsuario, etNombre, etApe, etEmail, etContra, etTelf;

    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etUsuario = findViewById(R.id.etUsuarioReg);
        etNombre = findViewById(R.id.etNomReg);
        etApe = findViewById(R.id.etApeReg);
        etEmail = findViewById(R.id.etEmailReg);
        etContra = findViewById(R.id.etContraReg);
        etTelf = findViewById(R.id.etTlfReg);

        btnRegistrar = findViewById(R.id.btnCrearCuentaReg);

        btnRegistrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCrearCuentaReg) {
            String username = etUsuario.getText().toString().trim();
            String name = etNombre.getText().toString().trim();
            String surname = etApe.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etContra.getText().toString().trim();
            int phone = 0;

            if (!etTelf.getText().toString().isEmpty()) {
                phone = Integer.parseInt(etTelf.getText().toString().trim());
            }

            if (username.isEmpty() || name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.campos_obligatorios), Toast.LENGTH_SHORT).show();
            } else {
                User user = new User(name + " " + surname, username, email, phone, password);
                registar(user);
            }
        }
    }

    private void registar(User user) {

         Call<ResponseBody> call = RetrofitClient
                 .getInstance()
                 .getApi()
                 .registerUser(user);
         call.enqueue(new Callback<ResponseBody>() {
             @Override
             public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                 Log.e("Register", "onResponse: " + response);

                 if (response.isSuccessful()) {
                     Toast.makeText(RegistroActivity.this, getString(R.string.registro_correcto), Toast.LENGTH_SHORT).show();
                     finish();
                 } else {
                     if (response.code() == 409) {
						 Toast.makeText(RegistroActivity.this, R.string.usuario_existente, Toast.LENGTH_SHORT).show();
                     } else {
                         Toast.makeText(RegistroActivity.this, "otro error no 409", Toast.LENGTH_SHORT).show();
                     }
                 }
             }
             @Override
             public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                 Toast.makeText(RegistroActivity.this, "err server", Toast.LENGTH_SHORT).show();
             }
         });
    }
}