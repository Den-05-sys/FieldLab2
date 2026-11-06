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


public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        EditText etEmail = findViewById(R.id.et_email);
        EditText etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnRegister = findViewById(R.id.btn_go_to_register);


        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();


            if (!email.isEmpty() && !pass.isEmpty()) {
                SharedPreferences prefs = getSharedPreferences("FieldLabPrefs", MODE_PRIVATE);
                prefs.edit().putBoolean("is_logged_in", true).putString("user_email", email).apply();


                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Ingresa tus credenciales", Toast.LENGTH_SHORT).show();
            }
        });


        btnRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}