package com.uac503.fieldlab.ui.auth;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.widget.FrameLayout frameLayout = new android.widget.FrameLayout(this);
        setContentView(frameLayout);
    }
}
