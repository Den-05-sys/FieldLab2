package com.uac503.fieldlab.ui.simulator;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.InputType;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import com.uac503.fieldlab.R;
import com.uac503.fieldlab.data.db.AppDatabase;
import com.uac503.fieldlab.model.Calculation;
import com.uac503.fieldlab.model.Charge;
import com.uac503.fieldlab.model.FieldResult;
import com.uac503.fieldlab.model.Scenario;
import com.uac503.fieldlab.physics.PhysicsCalculator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;

public class SimuladorFragment extends Fragment {

    private SimulatorView simulatorView;
    private TextView tvProbeResults;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_simulador, container, false);

        simulatorView = root.findViewById(R.id.simulator_view);
        tvProbeResults = root.findViewById(R.id.tv_probe_results);

        Button btnAddPos = root.findViewById(R.id.btn_add_pos);
        Button btnAddNeg = root.findViewById(R.id.btn_add_neg);
        Button btnSave = root.findViewById(R.id.btn_save_scenario);

        // Añadir carga positiva (+3 µC)
        if (btnAddPos != null) {
            btnAddPos.setOnClickListener(v -> {
                if (simulatorView != null) {
                    simulatorView.addCharge(400, 400, 3.0f);
                    updateProbeData();
                }
            });
        }

        // Añadir carga negativa (-3 µC)
        if (btnAddNeg != null) {
            btnAddNeg.setOnClickListener(v -> {
                if (simulatorView != null) {
                    simulatorView.addCharge(600, 400, -3.0f);
                    updateProbeData();
                }
            });
        }

        // Guardar cálculos y escenario en SQLite (Room)
        if (btnSave != null) {
            btnSave.setOnClickListener(v -> showSaveProjectDialog());
        }

        // Actualizar datos de la sonda al tocar o arrastrar
        if (simulatorView != null) {
            simulatorView.setOnTouchListener((v, event) -> {
                simulatorView.onTouchEvent(event);
                updateProbeData();
                return true;
            });
        }

        return root;
    }

    private void updateProbeData() {
        if (simulatorView == null || tvProbeResults == null) return;
        float px = simulatorView.getProbeX();
        float py = simulatorView.getProbeY();

        FieldResult res = PhysicsCalculator.calculateAtPoint(px, py, simulatorView.getCharges());

        String info = String.format(Locale.getDefault(),
                "Pos: (%.0f, %.0f)px | |E|: %.2e N/C | V: %.2e V",
                px, py, res.eMagnitude, res.potential);

        tvProbeResults.setText(info);
    }

    private void showSaveProjectDialog() {
        if (simulatorView == null || simulatorView.getCharges().isEmpty()) {
            Toast.makeText(getContext(), "Agrega al menos una carga antes de guardar", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText nameInput = new EditText(requireContext());
        nameInput.setSingleLine(true);
        nameInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        nameInput.setHint("Ej. Dipolo de prueba");
        nameInput.setTextColor(Color.WHITE);
        nameInput.setHintTextColor(Color.GRAY);
        nameInput.setPadding(48, 32, 48, 32);

        androidx.appcompat.app.AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Nombra tu emulación")
                .setMessage("Elige un nombre para encontrar fácilmente este campo después.")
                .setView(nameInput)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Guardar", null)
                .create();

        dialog.setOnShowListener(ignored -> dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(button -> {
                    String projectName = nameInput.getText().toString().trim();
                    if (projectName.isEmpty()) {
                        nameInput.setError("Escribe un nombre para continuar");
                        return;
                    }
                    dialog.dismiss();
                    saveCalculationToDb(projectName);
                }));
        dialog.show();
    }

    private void saveCalculationToDb(String projectName) {
        if (simulatorView == null || simulatorView.getCharges().isEmpty()) {
            Toast.makeText(getContext(), "Agrega al menos una carga antes de guardar", Toast.LENGTH_SHORT).show();
            return;
        }

        Context context = getContext();
        if (context == null) return;

        SharedPreferences preferences =
                context.getSharedPreferences("FieldLabPrefs", Context.MODE_PRIVATE);

        int userId = preferences.getInt("user_id", -1);

        float px = simulatorView.getProbeX();
        float py = simulatorView.getProbeY();

        FieldResult res = PhysicsCalculator.calculateAtPoint(px, py, simulatorView.getCharges());

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        double calculatedTotalCharge = 0;
        double calculatedMinDistance = Double.MAX_VALUE;

        for (Charge charge : simulatorView.getCharges()) {
            calculatedTotalCharge += charge.getCharge();
            double dx = px - charge.getX();
            double dy = py - charge.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance < calculatedMinDistance) {
                calculatedMinDistance = distance;
            }
        }

        final double finalTotalCharge = calculatedTotalCharge;
        final double finalMinDistance = calculatedMinDistance;

        Scenario scenario = new Scenario(projectName, new java.util.ArrayList<>(simulatorView.getCharges()), date);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(context.getApplicationContext());
                db.scenarioDao().insertScenario(scenario);

                if (userId != -1) {
                    Calculation fieldCalculation = new Calculation(
                            userId, "Campo eléctrico", finalTotalCharge, finalMinDistance, res.eMagnitude, "N/C", date
                    );
                    Calculation potentialCalculation = new Calculation(
                            userId, "Potencial eléctrico", finalTotalCharge, finalMinDistance, res.potential, "V", date
                    );
                    db.calculationDao().insertCalculation(fieldCalculation);
                    db.calculationDao().insertCalculation(potentialCalculation);
                }

                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(context, "Emulación «" + projectName + "» guardada", Toast.LENGTH_SHORT).show()
                    );
                }
            } catch (Exception e) {
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(context, "No se pudo guardar la emulación", Toast.LENGTH_LONG).show()
                    );
                }
            }
        });
    }
}
