package com.uac503.fieldlab.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uac503.fieldlab.R;
import com.uac503.fieldlab.ui.main.MainActivity;


public class HomeFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        // 1. Conexión directa por ID de Simulador
        View cardNewSim = root.findViewById(R.id.card_new_sim);
        if (cardNewSim != null) {
            cardNewSim.setOnClickListener(v -> openSimulator());
            enableChildClicks(cardNewSim, this::openSimulator);
        }


        // 2. Conexión directa por ID de Retos
        View cardChallenges = root.findViewById(R.id.card_challenges);
        if (cardChallenges != null) {
            cardChallenges.setOnClickListener(v -> openChallenges());
            // Asegura que si hay botones hijos dentro de la tarjeta también abran Retos
            enableChildClicks(cardChallenges, this::openChallenges);
        }


        // 3. Respaldo automático: busca cualquier tarjeta/texto que diga "Retos" si el ID en XML es distinto
        bindByTextFallback(root);


        return root;
    }


    private void openSimulator() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).switchFragment("SIM");
            BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) {
                bottomNav.setSelectedItemId(R.id.nav_simulator);
            }
        }
    }


    private void openChallenges() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).switchFragment("CHALLENGES");
            BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_navigation);
            if (bottomNav != null) {
                bottomNav.setSelectedItemId(R.id.nav_challenges);
            }
        }
    }


    private void enableChildClicks(View view, Runnable action) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                child.setOnClickListener(v -> action.run());
                enableChildClicks(child, action);
            }
        }
    }


    private void bindByTextFallback(View view) {
        if (view instanceof TextView) {
            String text = ((TextView) view).getText().toString().trim().toLowerCase();
            if (text.contains("reto")) {
                View parent = (View) view.getParent();
                view.setOnClickListener(v -> openChallenges());
                if (parent != null) {
                    parent.setOnClickListener(v -> openChallenges());
                }
            }
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                bindByTextFallback(group.getChildAt(i));
            }
        }
    }
}