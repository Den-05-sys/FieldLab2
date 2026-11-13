package com.uac503.fieldlab.ui.simulator;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.uac503.fieldlab.R;
import com.uac503.fieldlab.model.Charge;
import com.uac503.fieldlab.physics.PhysicsCalculator;


import java.util.Locale;


public class Simulator2DFragment extends Fragment {


    private SimulatorView simulatorView;
    private TextView tvChargeValue, tvProbePos, tvProbeComp, tvProbeMag, tvProbePot, tvSuperposition;
    private Slider sliderCharge;
    private Spinner spinnerUnits;


    // Unidades soportadas (micro, nano, pico, mili, Coulomb)
    private final String[] unitSymbols = {"µC", "nC", "pC", "mC", "C"};
    private final double[] unitFactors = {1e-6, 1e-9, 1e-12, 1e-3, 1.0};
    private int currentUnitIndex = 0; // Por defecto µC
    private boolean updatingFromCode = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_simulator_2d, container, false);


        // Vincular vistas
        simulatorView = root.findViewById(R.id.simulator_view_2d);
        tvChargeValue = root.findViewById(R.id.tv_charge_value);
        tvProbePos = root.findViewById(R.id.tv_probe_pos);
        tvProbeComp = root.findViewById(R.id.tv_probe_components);
        tvProbeMag = root.findViewById(R.id.tv_probe_magnitude);
        tvProbePot = root.findViewById(R.id.tv_probe_potential);
        tvSuperposition = root.findViewById(R.id.tv_superposition_list);
        sliderCharge = root.findViewById(R.id.slider_charge);
        spinnerUnits = root.findViewById(R.id.spinner_units);


        setupUnitSpinner();
        setupAllButtons(root);
        setupChargeSlider();


        // Escuchar actualizaciones en tiempo real desde el lienzo
        simulatorView.setOnSimulatorListener(new SimulatorView.OnSimulatorListener() {
            @Override
            public void onDataUpdated(PhysicsCalculator.DetailedFieldResult res, float xm, float ym) {
                updateTelemetryPanel(res, xm, ym);
            }


            @Override
            public void onChargeSelected(Charge charge, int index) {
                if (charge != null) {
                    updatingFromCode = true;
                    float clamped = Math.max(-10f, Math.min(10f, charge.getBaseValue()));
                    sliderCharge.setValue(clamped);


                    // Sincronizar el spinner con la unidad de la carga seleccionada
                    for (int i = 0; i < unitSymbols.length; i++) {
                        if (unitSymbols[i].equals(charge.getUnitSymbol())) {
                            currentUnitIndex = i;
                            spinnerUnits.setSelection(i);
                            break;
                        }
                    }


                    tvChargeValue.setText(String.format(Locale.US, "q = %+.1f %s", clamped, charge.getUnitSymbol()));
                    updatingFromCode = false;
                } else {
                    tvChargeValue.setText("Sin selección");
                }
            }
        });


        // Cargar un Dipolo por defecto al abrir el simulador
        simulatorView.post(() -> simulatorView.loadPreset("DIPOLE", unitFactors[currentUnitIndex], unitSymbols[currentUnitIndex]));


        return root;
    }


    /** Configura el desplegable de unidades (µC, nC, pC, mC, C) */
    private void setupUnitSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, unitSymbols) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.parseColor("#D0BCFF"));
                tv.setTextSize(13f);
                return tv;
            }


            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
                tv.setTextColor(Color.WHITE);
                tv.setBackgroundColor(Color.parseColor("#1D2230"));
                tv.setPadding(24, 16, 24, 16);
                return tv;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnits.setAdapter(adapter);


        spinnerUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentUnitIndex = position;
                if (updatingFromCode) return;


                float val = sliderCharge.getValue();
                tvChargeValue.setText(String.format(Locale.US, "q = %+.1f %s", val, unitSymbols[currentUnitIndex]));
                simulatorView.updateSelectedChargeValue(val, unitFactors[currentUnitIndex], unitSymbols[currentUnitIndex]);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }


    /** Configura el deslizador de magnitud de carga (-10.0 a +10.0) */
    private void setupChargeSlider() {
        sliderCharge.addOnChangeListener((slider, value, fromUser) -> {
            if (updatingFromCode) return;
            String sym = unitSymbols[currentUnitIndex];
            tvChargeValue.setText(String.format(Locale.US, "q = %+.1f %s", value, sym));
            simulatorView.updateSelectedChargeValue(value, unitFactors[currentUnitIndex], sym);
        });
    }


    /** Conecta todos los botones de acción, capas visuales y presets */
    private void setupAllButtons(View root) {
        // 1. Agregar Carga Positiva (+2.0)
        root.findViewById(R.id.btn_add_pos).setOnClickListener(v -> {
            float x = 220f + (float) (Math.random() * 400f);
            float y = 200f + (float) (Math.random() * 280f);
            simulatorView.addCharge(x, y, 2.0f, unitFactors[currentUnitIndex], unitSymbols[currentUnitIndex]);
        });


        // 2. Agregar Carga Negativa (-2.8)
        root.findViewById(R.id.btn_add_neg).setOnClickListener(v -> {
            float x = 220f + (float) (Math.random() * 400f);
            float y = 200f + (float) (Math.random() * 280f);
            simulatorView.addCharge(x, y, -2.8f, unitFactors[currentUnitIndex], unitSymbols[currentUnitIndex]);
        });


        // 3. Mostrar / Ocultar Sonda
        MaterialButton btnProbe = root.findViewById(R.id.btn_toggle_probe);
        btnProbe.setOnClickListener(v -> {
            boolean active = simulatorView.toggleProbe();
            btnProbe.setTextColor(active ? Color.parseColor("#FFD54F") : Color.parseColor("#9AA4B2"));
        });


        // 4. Iniciar / Pausar Partícula de Prueba Dinámica
        MaterialButton btnParticle = root.findViewById(R.id.btn_particle);
        btnParticle.setOnClickListener(v -> {
            boolean running = simulatorView.toggleTestParticle();
            btnParticle.setText(running ? "⏸ Pausar" : "▶ Prueba");
        });


        // 5. Capa: Vectores
        MaterialButton btnVectors = root.findViewById(R.id.btn_layer_vectors);
        btnVectors.setOnClickListener(v -> updateToggleStyle(btnVectors, simulatorView.toggleVectors()));


        // 6. Capa: Líneas de campo
        MaterialButton btnLines = root.findViewById(R.id.btn_layer_lines);
        btnLines.setOnClickListener(v -> updateToggleStyle(btnLines, simulatorView.toggleLines()));


        // 7. Capa: Mapa de Potencial
        MaterialButton btnPotential = root.findViewById(R.id.btn_layer_potential);
        btnPotential.setOnClickListener(v -> updateToggleStyle(btnPotential, simulatorView.togglePotential()));


        // 8. Preset: Dipolo (+ y -)
        root.findViewById(R.id.btn_preset_dipole).setOnClickListener(v ->
                simulatorView.loadPreset("DIPOLE", unitFactors[currentUnitIndex], unitSymbols[currentUnitIndex]));


        // 9. Preset: Cargas Iguales (+ y +)
        root.findViewById(R.id.btn_preset_equal).setOnClickListener(v ->
                simulatorView.loadPreset("EQUAL", unitFactors[currentUnitIndex], unitSymbols[currentUnitIndex]));


        // 10. Preset: Cuadrupolo (4 cargas alternadas)
        root.findViewById(R.id.btn_preset_quad).setOnClickListener(v ->
                simulatorView.loadPreset("QUADRUPOLE", unitFactors[currentUnitIndex], unitSymbols[currentUnitIndex]));


        // 11. Limpiar todo el lienzo
        root.findViewById(R.id.btn_clear).setOnClickListener(v -> simulatorView.clearAll());


        // 12. Borrar únicamente la carga seleccionada
        root.findViewById(R.id.btn_delete_selected).setOnClickListener(v -> simulatorView.removeSelectedCharge());
    }


    private void updateToggleStyle(MaterialButton btn, boolean isActive) {
        if (isActive) {
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#353148")));
            btn.setTextColor(Color.WHITE);
        } else {
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
            btn.setTextColor(Color.parseColor("#9AA4B2"));
        }
    }


    /** Actualiza el panel inferior con lecturas en metros y el desglose de superposición */
    private void updateTelemetryPanel(PhysicsCalculator.DetailedFieldResult res, float xm, float ym) {
        tvProbePos.setText(String.format(Locale.US, "Posición: (%.2f , %.2f) m", xm, ym));
        tvProbeComp.setText(String.format(Locale.US, "Ex = %.3e N/C    Ey = %.3e N/C", res.ex, res.ey));
        tvProbeMag.setText(String.format(Locale.US, "|E| = %.3e N/C   θ = %.1f°", res.eMagnitude, res.angleDegrees));
        tvProbePot.setText(String.format(Locale.US, "V  = %.3e V", res.potential));


        if (res.contributions.isEmpty()) {
            tvSuperposition.setText("Sin cargas activas en el lienzo");
            return;
        }


        SpannableStringBuilder ssb = new SpannableStringBuilder();
        for (int i = 0; i < res.contributions.size(); i++) {
            PhysicsCalculator.ChargeContribution c = res.contributions.get(i);
            String line = String.format(Locale.US, "q%d=%+.1f%s r=%.2fm |E|=%.2e",
                    c.index, c.baseValue, c.unitSymbol, c.distanceMeters, c.eContribution);
            if (i < res.contributions.size() - 1) line += "\n";


            int start = ssb.length();
            ssb.append(line);
            int color = c.baseValue >= 0 ? Color.parseColor("#FF8A80") : Color.parseColor("#82B1FF");
            ssb.setSpan(new ForegroundColorSpan(color), start, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tvSuperposition.setText(ssb);
    }
}