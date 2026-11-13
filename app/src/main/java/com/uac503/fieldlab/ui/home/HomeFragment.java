package com.uac503.fieldlab.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.uac503.fieldlab.R;
import com.uac503.fieldlab.ui.quiz.QuizActivity;
import com.uac503.fieldlab.ui.simulator.SimuladorFragment;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        View cardNewSim = root.findViewById(R.id.card_new_sim);
        if (cardNewSim != null) {
            cardNewSim.setOnClickListener(v -> {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SimuladorFragment())
                        .commit();
            });
        }

        View cardChallenges = root.findViewById(R.id.card_challenges);
        if (cardChallenges != null) {
            cardChallenges.setOnClickListener(v -> {
                startActivity(new Intent(requireContext(), QuizActivity.class));
            });
        }

        return root;
    }
}
