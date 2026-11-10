package com.uac503.fieldlab.ui.simulator;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.uac503.fieldlab.R;
import com.uac503.fieldlab.model.FieldResult;
import com.uac503.fieldlab.physics.PhysicsCalculator;


public class Simulator2DFragment extends Fragment {


    private SimulatorView simulatorView;
    private TextView tvResults;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_simulator_2d, container, false);


        simulatorView = root.findViewById(R.id.simulator_view_2d);
        tvResults = root.findViewById(R.id.tv_probe_results);


        Button btnPos = root.findViewById(R.id.btn_add_pos);
        Button btnNeg = root.findViewById(R.id.btn_add_neg);
        Button btnClear = root.findViewById(R.id.btn_clear);


        btnPos.setOnClickListener(v -> simulatorView.addCharge(300, 400, 3.0f));
        btnNeg.setOnClickListener(v -> simulatorView.addCharge(600, 400, -3.0f));
        btnClear.setOnClickListener(v -> simulatorView.clearCharges());


        simulatorView.setOnProbeChangeListener((x, y) -> {
            FieldResult res = PhysicsCalculator.calculateAtPoint(x, y, simulatorView.getCharges());
            String info = String.format("▲ LECTURA DE SONDA EN TIEMPO REAL\nPosición: (%.0f, %.0f) px\n|E| = %.2e N/C\nV = %.2e V",
                    x, y, res.eMagnitude, res.potential);
            tvResults.setText(info);
        });


        return root;
    }
}