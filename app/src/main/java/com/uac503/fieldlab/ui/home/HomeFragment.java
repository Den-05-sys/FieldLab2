package com.uac503.fieldlab.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uac503.fieldlab.R;
import com.uac503.fieldlab.ui.challenges.ChallengesFragment;
import com.uac503.fieldlab.ui.main.MainActivity;


public class HomeFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        View cardNewSim = root.findViewById(R.id.card_new_sim);
        if (cardNewSim != null) {
            cardNewSim.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).switchFragment("SIM");
                    BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_navigation);
                    if (bottomNav != null) {
                        bottomNav.setSelectedItemId(R.id.nav_simulator);
                    }
                }
            });
        }


        View cardChallenges = root.findViewById(R.id.card_challenges);
        if (cardChallenges != null) {
            cardChallenges.setClickable(true);
            cardChallenges.setFocusable(true);
            cardChallenges.setOnClickListener(v -> abrirRetos());
        } else {
            Toast.makeText(getContext(), "Aviso: No se encontró R.id.card_challenges en fragment_home.xml", Toast.LENGTH_LONG).show();
        }


        return root;
    }


    private void abrirRetos() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).switchFragment("CHALLENGES");
        } else {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ChallengesFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }
}