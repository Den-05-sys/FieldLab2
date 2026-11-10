package com.uac503.fieldlab.ui.auth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.uac503.fieldlab.R;

public class ForgotActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        EditText etEmail = findViewById(R.id.et_forgot_email);
        Button btnRecover = findViewById(R.id.btn_send_reset);

        btnRecover.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (!email.isEmpty()) {
                Toast.makeText(this, "Instrucciones enviadas a " + email, Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Ingresa tu correo electrónico", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
