package com.uac503.fieldlab.ui.simulator;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.uac503.fieldlab.R;
import com.uac503.fieldlab.model.Charge;
import com.uac503.fieldlab.model.FieldResult;
import com.uac503.fieldlab.physics.PhysicsCalculator;


import java.util.ArrayList;
import java.util.List;


public class SimulatorCartesianFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_simulator_cartesian, container, false);


        CartesianView cartesianView = root.findViewById(R.id.cartesian_view);
        TextView tvBreakdown = root.findViewById(R.id.tv_cartesian_breakdown);


        // Caso de prueba cartesiano en metros
        List<Charge> charges = new ArrayList<>();
        charges.add(new Charge(0f, 0f, -2.0f));       // q1 en (0, 0) m
        charges.add(new Charge(0f, 100f, 5.0f));     // q2 en (0, 0.10) m


        float px = 0.12f; // Punto P en x = 0.12 m
        float py = 0.00f; // Punto P en y = 0.00 m


        cartesianView.updateCartesianData(charges, px, py);


        FieldResult res = PhysicsCalculator.calculateAtPoint(px, py, charges);


        String breakdown = String.format(
                "DESGLOSE VECTORIAL PASO A PASO:\n" +
                        "-------------------------------\n" +
                        "Punto de Evaluación P = (%.2f, %.2f) m\n" +
                        "Carga q1 = -2.00 µC en (0.00, 0.00) m\n" +
                        "Carga q2 = +5.00 µC en (0.00, 0.10) m\n\n" +
                        "E1x = -1.25e+06 N/C | E1y = 0.00 N/C\n" +
                        "E2x =  1.41e+06 N/C | E2y = -1.18e+06 N/C\n\n" +
                        "Ex_total = %.2e N/C\n" +
                        "Ey_total = %.2e N/C\n" +
                        "|E_total| = %.2e N/C\n" +
                        "Ángulo θ = %.1f°",
                px, py, res.eMagnitude, res.potential, res.eMagnitude, res.angleDegrees
        );


        tvBreakdown.setText(breakdown);


        return root;
    }
}