package com.uac503.fieldlab.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.uac503.fieldlab.R;
import com.uac503.fieldlab.ui.main.MainActivity;

public class RegisterActivity extends AppCompatActivity {
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
                String pass = etPassword != null ? etPassword.getText().toString().trim() : "";

                if (!email.isEmpty() && !pass.isEmpty()) {
                    SharedPreferences prefs = getSharedPreferences("FieldLabPrefs", MODE_PRIVATE);
                    prefs.edit()
                            .putBoolean("is_logged_in", true)
                            .putString("user_email", email)
                            .putString("user_name", name.isEmpty() ? "Estudiante" : name)
                            .apply();

                    Toast.makeText(this, "¡Cuenta creada con éxito!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Por favor ingresa correo y contraseña", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
