package com.uac503.fieldlab.ui.simulator;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.material.slider.Slider;
import com.uac503.fieldlab.R;
import com.uac503.fieldlab.model.Charge;
import com.uac503.fieldlab.physics.PhysicsCalculator;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class SimulatorCartesianFragment extends Fragment {


    private CartesianView cartesianView;
    private TextView tvPx, tvPy, tvBreakdown;
    private float pxMeters = 0.12f;
    private float pyMeters = 0.00f;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_simulator_cartesian, container, false);


        cartesianView = root.findViewById(R.id.cartesian_view);
        tvPx = root.findViewById(R.id.tv_px_val);
        tvPy = root.findViewById(R.id.tv_py_val);
        tvBreakdown = root.findViewById(R.id.tv_cartesian_breakdown);


        Slider sliderPx = root.findViewById(R.id.slider_px);
        Slider sliderPy = root.findViewById(R.id.slider_py);


        sliderPx.addOnChangeListener((slider, value, fromUser) -> {
            pxMeters = value;
            tvPx.setText(String.format(Locale.US, "Px = %.2f m", pxMeters));
            recalculateCartesian();
        });


        sliderPy.addOnChangeListener((slider, value, fromUser) -> {
            pyMeters = value;
            tvPy.setText(String.format(Locale.US, "Py = %.2f m", pyMeters));
            recalculateCartesian();
        });


        recalculateCartesian();
        return root;
    }


    private void recalculateCartesian() {
        // Cargas fijas en coordenadas cartesianas (en metros)
        double q1 = -2.0e-6; // -2.0 µC en (0, 0)
        double x1 = 0.0, y1 = 0.0;


        double q2 = 5.0e-6;  // +5.0 µC en (0, 0.10)
        double x2 = 0.0, y2 = 0.10;


        // Actualizar vista gráfica
        List<Charge> visualCharges = new ArrayList<>();
        visualCharges.add(new Charge((float) (x1 * 1000), (float) (y1 * 1000), -2.0f));
        visualCharges.add(new Charge((float) (x2 * 1000), (float) (y2 * 1000), 5.0f));
        cartesianView.updateCartesianData(visualCharges, pxMeters, pyMeters);


        // Cálculo exacto por componentes
        double dx1 = pxMeters - x1;
        double dy1 = pyMeters - y1;
        double r1 = Math.max(0.01, Math.hypot(dx1, dy1));
        double e1 = (PhysicsCalculator.K * q1) / (r1 * r1);
        double e1x = e1 * (dx1 / r1);
        double e1y = e1 * (dy1 / r1);


        double dx2 = pxMeters - x2;
        double dy2 = pyMeters - y2;
        double r2 = Math.max(0.01, Math.hypot(dx2, dy2));
        double e2 = (PhysicsCalculator.K * q2) / (r2 * r2);
        double e2x = e2 * (dx2 / r2);
        double e2y = e2 * (dy2 / r2);


        double exTotal = e1x + e2x;
        double eyTotal = e1y + e2y;
        double eTotal = Math.hypot(exTotal, eyTotal);
        double angle = Math.toDegrees(Math.atan2(eyTotal, exTotal));


        String report = String.format(Locale.US,
                "DESGLOSE CARTESIANO PASO A PASO:\n" +
                        "----------------------------------\n" +
                        "Punto P = (%.2f, %.2f) m\n" +
                        "q1 = -2.0 µC en (0.00, 0.00) m → r1 = %.3f m\n" +
                        "q2 = +5.0 µC en (0.00, 0.10) m → r2 = %.3f m\n\n" +
                        "Aporte q1: E1x = %.2e | E1y = %.2e N/C\n" +
                        "Aporte q2: E2x = %.2e | E2y = %.2e N/C\n\n" +
                        "RESULTANTE TOTAL:\n" +
                        "Ex_total = %.3e N/C\n" +
                        "Ey_total = %.3e N/C\n" +
                        "|E_total| = %.3e N/C\n" +
                        "Ángulo θ = %.1f°",
                pxMeters, pyMeters, r1, r2, e1x, e1y, e2x, e2y, exTotal, eyTotal, eTotal, angle
        );


        tvBreakdown.setText(report);
    }
}