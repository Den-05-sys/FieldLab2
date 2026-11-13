package com.uac503.fieldlab.ui.scenarios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.uac503.fieldlab.R;
import com.uac503.fieldlab.model.Charge;
import com.uac503.fieldlab.model.Scenario;
import com.uac503.fieldlab.ui.simulator.SimulatorViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScenariosFragment extends Fragment {

    private ScenarioAdapter adapter;
    private List<Scenario> savedScenarios = new ArrayList<>();
    private TextView tvProjectCount;
    private TextView tvEmptyTitle;
    private LinearLayout emptyProjects;
    private RecyclerView rvScenarios;
    private TabLayout tabScenarios;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scenarios, container, false);

        rvScenarios = root.findViewById(R.id.rv_scenarios);
        emptyProjects = root.findViewById(R.id.layout_empty_projects);
        tvEmptyTitle = root.findViewById(R.id.tv_empty_projects_title);
        tvProjectCount = root.findViewById(R.id.tv_project_count);
        tabScenarios = root.findViewById(R.id.tab_scenarios);

        adapter = new ScenarioAdapter();
        rvScenarios.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvScenarios.setAdapter(adapter);

        tabScenarios.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showCurrentTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                rvScenarios.smoothScrollToPosition(0);
            }
        });

        showCurrentTab(tabScenarios.getSelectedTabPosition());

        SimulatorViewModel viewModel =
                new ViewModelProvider(this).get(SimulatorViewModel.class);
        viewModel.getAllScenarios().observe(getViewLifecycleOwner(), scenarios -> {
            savedScenarios = scenarios != null ? scenarios : new ArrayList<>();
            showCurrentTab(tabScenarios.getSelectedTabPosition());
        });

        return root;
    }

    private void showCurrentTab(int selectedTab) {
        if (adapter == null) return;

        if (selectedTab == 0) {
            List<Scenario> templates = getDefaultPresets();
            adapter.setScenarios(templates);
            tvProjectCount.setText(templates.size() + " plantillas para explorar");
            emptyProjects.setVisibility(View.GONE);
            rvScenarios.setVisibility(View.VISIBLE);
            return;
        }

        tvProjectCount.setText(savedScenarios.size()
                + (savedScenarios.size() == 1 ? " emulación guardada" : " emulaciones guardadas"));

        if (savedScenarios.isEmpty()) {
            adapter.setScenarios(new ArrayList<>());
            rvScenarios.setVisibility(View.GONE);
            emptyProjects.setVisibility(View.VISIBLE);
            tvEmptyTitle.setText("Aún no hay emulaciones");
        } else {
            emptyProjects.setVisibility(View.GONE);
            rvScenarios.setVisibility(View.VISIBLE);
            adapter.setScenarios(savedScenarios);
        }
    }

    private List<Scenario> getDefaultPresets() {
        List<Scenario> presets = new ArrayList<>();
        presets.add(new Scenario("Dipolo eléctrico", Arrays.asList(
                new Charge(300, 400, 3.0f),
                new Charge(500, 400, -3.0f)
        ), "Preset"));

        presets.add(new Scenario("Cargas iguales positivas", Arrays.asList(
                new Charge(300, 400, 5.0f),
                new Charge(500, 400, 5.0f)
        ), "Preset"));

        presets.add(new Scenario("Cuadrupolo eléctrico", Arrays.asList(
                new Charge(300, 300, 2.0f),
                new Charge(500, 300, -2.0f),
                new Charge(300, 500, -2.0f),
                new Charge(500, 500, 2.0f)
        ), "Preset"));

        return presets;
    }
}
