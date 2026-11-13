package com.uac503.fieldlab.ui.auth;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.uac503.fieldlab.R;

public class ForgotActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        Button btnReset = findViewById(R.id.btn_send_reset);
        btnReset.setOnClickListener(v -> finish());
    }
}
