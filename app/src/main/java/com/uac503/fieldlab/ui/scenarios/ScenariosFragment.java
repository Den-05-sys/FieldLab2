package com.uac503.fieldlab.ui.scenarios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.uac503.fieldlab.R;
import com.uac503.fieldlab.model.Charge;
import com.uac503.fieldlab.model.Scenario;
import com.uac503.fieldlab.ui.simulator.SimulatorViewModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScenariosFragment extends Fragment {

    private ScenarioAdapter adapter;
    private SimulatorViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scenarios, container, false);

        RecyclerView rvScenarios = root.findViewById(R.id.rv_scenarios);
        if (rvScenarios != null) {
            rvScenarios.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new ScenarioAdapter();
            rvScenarios.setAdapter(adapter);
        }

        viewModel = new ViewModelProvider(this).get(SimulatorViewModel.class);
        viewModel.getAllScenarios().observe(getViewLifecycleOwner(), scenarios -> {
            if (scenarios == null || scenarios.isEmpty()) {
                adapter.setScenarios(getDefaultPresets());
            } else {
                adapter.setScenarios(scenarios);
            }
        });

        return root;
    }

    private List<Scenario> getDefaultPresets() {
        List<Scenario> presets = new ArrayList<>();
        presets.add(new Scenario("Dipolo Eléctrico", Arrays.asList(
                new Charge(300, 400, 3.0f),
                new Charge(500, 400, -3.0f)
        ), "Preset"));

        presets.add(new Scenario("Cargas Iguales Positivas", Arrays.asList(
                new Charge(300, 400, 5.0f),
                new Charge(500, 400, 5.0f)
        ), "Preset"));

        presets.add(new Scenario("Cuadrupolo Eléctrico", Arrays.asList(
                new Charge(300, 300, 2.0f),
                new Charge(500, 300, -2.0f),
                new Charge(300, 500, -2.0f),
                new Charge(500, 500, 2.0f)
        ), "Preset"));

        return presets;
    }
}
