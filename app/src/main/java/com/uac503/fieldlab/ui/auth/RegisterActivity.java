package com.uac503.fieldlab.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.uac503.fieldlab.R;
import com.uac503.fieldlab.data.db.AppDatabase;
import com.uac503.fieldlab.model.User;
import com.uac503.fieldlab.security.PasswordHasher;
import com.uac503.fieldlab.ui.main.MainActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText etName = findViewById(R.id.et_reg_name);
        EditText etEmail = findViewById(R.id.et_reg_email);
        EditText etPassword = findViewById(R.id.et_reg_password);
        Button btnRegister = findViewById(R.id.btn_register);

        if (btnRegister != null) {
            btnRegister.setOnClickListener(v -> {
                String name = etName != null ? etName.getText().toString().trim() : "";
                String email = etEmail != null ? etEmail.getText().toString().trim() : "";
                String pass = etPassword != null ? etPassword.getText().toString() : "";

                if (name.isEmpty()) {
                    etName.setError("Ingresa tu nombre");
                    etName.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Ingresa un correo válido");
                    etEmail.requestFocus();
                    return;
                }
                if (pass.length() < 8) {
                    etPassword.setError("Usa al menos 8 caracteres");
                    etPassword.requestFocus();
                    return;
                }
                final String normalizedEmail = email.toLowerCase(java.util.Locale.ROOT);

                btnRegister.setEnabled(false);

                executor.execute(() -> {
                    try {
                        AppDatabase db = AppDatabase.getInstance(getApplicationContext());

                        if (db.userDao().emailExists(normalizedEmail) > 0) {
                            runOnUiThread(() -> {
                                btnRegister.setEnabled(true);
                                Toast.makeText(this, "El correo electrónico ya está registrado", Toast.LENGTH_SHORT).show();
                            });
                            return;
                        }

                        String passwordHash = PasswordHasher.hash(pass);
                        String userName = name.isEmpty() ? "Estudiante" : name;

                        User user = new User(userName, normalizedEmail, passwordHash);
                        long id = db.userDao().insertUser(user);

                        runOnUiThread(() -> {
                            btnRegister.setEnabled(true);
                            if (id > 0) {
                                SharedPreferences prefs = getSharedPreferences("FieldLabPrefs", MODE_PRIVATE);
                                prefs.edit()
                                        .putBoolean("is_logged_in", true)
                                        .putString("user_email", normalizedEmail)
                                        .putString("user_name", userName)
                                        .putInt("user_id", (int) id)
                                        .apply();

                                Toast.makeText(this, "¡Cuenta creada correctamente!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(this, "Error al guardar el usuario en la base de datos", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            btnRegister.setEnabled(true);
                            Toast.makeText(this, "No se pudo registrar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                    }
                });
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
