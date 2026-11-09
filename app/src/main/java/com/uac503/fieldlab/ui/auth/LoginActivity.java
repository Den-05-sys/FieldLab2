
package com.uac503.fieldlab.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uac503.fieldlab.R;
import com.uac503.fieldlab.data.db.AppDatabase;
import com.uac503.fieldlab.model.User;
import com.uac503.fieldlab.security.PasswordHasher;
import com.uac503.fieldlab.ui.main.MainActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etEmail = findViewById(R.id.et_email);
        EditText etPassword = findViewById(R.id.et_password);

        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_go_to_register);

        TextView tvForgot =
                findViewById(R.id.tv_forgot_password);

        if (btnLogin != null) {

            btnLogin.setOnClickListener(v -> {

                String email = etEmail.getText().toString().trim();
                String pass = etPassword.getText().toString();

                if (email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(this,
                            "Ingresa tus credenciales",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                btnLogin.setEnabled(false);

                executor.execute(() -> {

                    try {
                        // CONEXIÓN CON ROOM
                        AppDatabase db =
                                AppDatabase.getInstance(getApplicationContext());

                        User user = db.userDao().getUserByEmail(email);

                        boolean valid = user != null &&
                                PasswordHasher.verify(
                                        pass,
                                        user.getPasswordHash()
                                );

                        runOnUiThread(() -> {

                            btnLogin.setEnabled(true);

                            if (valid) {

                                SharedPreferences prefs =
                                        getSharedPreferences(
                                                "FieldLabPrefs",
                                                MODE_PRIVATE
                                        );

                                prefs.edit()
                                        .putBoolean("is_logged_in", true)
                                        .putString("user_email", user.getEmail())
                                        .putString("user_name", user.getName())
                                        .putInt("user_id", user.getId())
                                        .apply();

                                Toast.makeText(this,
                                        "¡Bienvenido a FieldLab!",
                                        Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(
                                        this, MainActivity.class));

                                finish();

                            } else {

                                Toast.makeText(this,
                                        "Correo o contraseña incorrectos",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (Exception e) {

                        runOnUiThread(() -> {
                            btnLogin.setEnabled(true);

                            Toast.makeText(this,
                                    "Error al iniciar sesión: "
                                            + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        });
                    }
                });
            });
        }

        if (btnRegister != null) {
            btnRegister.setOnClickListener(v ->
                    startActivity(new Intent(
                            this, RegisterActivity.class)));
        }

        if (tvForgot != null) {
            tvForgot.setOnClickListener(v ->
                    startActivity(new Intent(
                            this, ForgotActivity.class)));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}


