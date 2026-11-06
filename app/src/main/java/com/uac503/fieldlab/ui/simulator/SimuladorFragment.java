package com.uac503.fieldlab.ui.simulator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.uac503.fieldlab.R;
import com.uac503.fieldlab.data.db.AppDatabase;
import com.uac503.fieldlab.model.FieldResult;
import com.uac503.fieldlab.model.Scenario;
import com.uac503.fieldlab.physics.PhysicsCalculator;

import java.util.concurrent.Executors;

public class SimuladorFragment extends Fragment {

    private SimulatorView simulatorView;
    private TextView tvProbeResults;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_fragment_simulador, container, false);

        simulatorView = root.findViewById(R.id.simulator_view);
        tvProbeResults = root.findViewById(R.id.tv_probe_results);

        Button btnAddPos = root.findViewById(R.id.btn_add_pos);
        Button btnAddNeg = root.findViewById(R.id.btn_add_neg);
        Button btnSave = root.findViewById(R.id.btn_save);

        // Añadir carga positiva (+3 µC)
        btnAddPos.setOnClickListener(v -> {
            simulatorView.addCharge(400, 400, 3.0f);
            updateProbeData();
        });

        // Añadir carga negativa (-3 µC)
        btnAddNeg.setOnClickListener(v -> {
            simulatorView.addCharge(600, 400, -3.0f);
            updateProbeData();
        });

        // Guardar escenario en SQLite (Room)
        btnSave.setOnClickListener(v -> saveScenarioToDb());

        // Actualizar sonda en refresco de pantalla
        simulatorView.setOnTouchListener((v, event) -> {
            simulatorView.onTouchEvent(event);
            updateProbeData();
            return true;
        });

        return root;
    }

    private void updateProbeData() {
        float px = simulatorView.getProbeX();
        float py = simulatorView.getProbeY();

        FieldResult res = PhysicsCalculator.calculateAtPoint(px, py, simulatorView.getCharges());

        String info = String.format("Pos: (%.0f, %.0f)px | |E|: %.2e N/C | V: %.2e V",
                px, py, res.eMagnitude, res.potential);

        tvProbeResults.setText(info);
    }

    private void saveScenarioToDb() {
        if (simulatorView.getCharges().isEmpty()) {
            Toast.makeText(getContext(), "Agrega al menos una carga", Toast.LENGTH_SHORT).show();
            return;
        }

        Scenario scenario = new Scenario("Dipolo / Configuración", simulatorView.getCharges(), "2026-10-29");

        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase.getInstance(requireContext()).scenarioDao().insertScenario(scenario);
            requireActivity().runOnUiThread(() ->
                    Toast.makeText(getContext(), "¡Escenario guardado en SQLite!", Toast.LENGTH_SHORT).show()
            );
        });
    }
}
