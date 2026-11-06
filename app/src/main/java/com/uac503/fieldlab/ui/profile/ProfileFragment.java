package com.uac503.fieldlab.ui.profile;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.uac503.fieldlab.R;
import com.uac503.fieldlab.ui.auth.LoginActivity;


public class ProfileFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);


        TextView tvEmail = root.findViewById(R.id.tv_profile_email);
        Button btnLogout = root.findViewById(R.id.btn_logout);


        SharedPreferences prefs = requireActivity().getSharedPreferences("FieldLabPrefs", Context.MODE_PRIVATE);
        String email = prefs.getString("user_email", "estudiante@fieldlab.edu");
        if (tvEmail != null) tvEmail.setText(email);


        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                prefs.edit().clear().apply();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
        }


        return root;
    }
}