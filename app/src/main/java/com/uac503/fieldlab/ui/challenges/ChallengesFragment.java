package com.uac503.fieldlab.ui.challenges;


import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.uac503.fieldlab.R;
import com.uac503.fieldlab.ui.main.MainActivity;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ChallengesFragment extends Fragment {


    private static class ChallengeItem {
        String category;
        String title;
        String description;
        boolean isSliderMode;
        // Parámetros para modo Slider interactivo
        float minVal, maxVal, step, targetVal, tolerance;
        String unit;
        // Parámetros para modo Opción Múltiple
        String[] options;
        int correctOptionIndex;
        String explanation;


        // Constructor modo laboratorio (Slider)
        ChallengeItem(String cat, String title, String desc, float min, float max, float step, float target, float tol, String unit, String exp) {
            this.category = cat;
            this.title = title;
            this.description = desc;
            this.isSliderMode = true;
            this.minVal = min;
            this.maxVal = max;
            this.step = step;
            this.targetVal = target;
            this.tolerance = tol;
            this.unit = unit;
            this.explanation = exp;
        }


        // Constructor modo selección rápida
        ChallengeItem(String cat, String title, String desc, String[] opts, int correctIdx, String exp) {
            this.category = cat;
            this.title = title;
            this.description = desc;
            this.isSliderMode = false;
            this.options = opts;
            this.correctOptionIndex = correctIdx;
            this.explanation = exp;
        }
    }


    private final List<ChallengeItem> challenges = new ArrayList<>();
    private int currentIndex = 0;
    private int score = 0;
    private int streak = 0;
    private int lives = 3;
    private CountDownTimer timer;


    private TextView tvLives, tvScore, tvTimer, tvBadge, tvStreak, tvTitle, tvDesc, tvLiveFormula;
    private TextView tvFeedbackStatus, tvFeedbackExp;
    private ProgressBar progressBar;
    private LinearLayout layoutSlider, layoutOptions, layoutFeedback;
    private Slider sliderInput;
    private MaterialButton btnVerifySlider, btnOpt1, btnOpt2, btnOpt3, btnNext;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_challenges, container, false);


        bindViews(root);
        buildChallenges();
        loadChallenge(0);


        root.findViewById(R.id.btn_back_home).setOnClickListener(v -> {
            if (timer != null) timer.cancel();
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).switchFragment("HOME");
                BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_navigation);
                if (bottomNav != null) {
                    bottomNav.setSelectedItemId(R.id.nav_home);
                }
            }
        });


        return root;
    }


    private void bindViews(View root) {
        tvLives = root.findViewById(R.id.tv_lives);
        tvScore = root.findViewById(R.id.tv_score);
        tvTimer = root.findViewById(R.id.tv_timer);
        tvBadge = root.findViewById(R.id.tv_challenge_badge);
        tvStreak = root.findViewById(R.id.tv_streak);
        tvTitle = root.findViewById(R.id.tv_challenge_title);
        tvDesc = root.findViewById(R.id.tv_challenge_desc);
        tvLiveFormula = root.findViewById(R.id.tv_live_formula);
        progressBar = root.findViewById(R.id.progress_challenge);


        layoutSlider = root.findViewById(R.id.layout_slider_mode);
        layoutOptions = root.findViewById(R.id.layout_options_mode);
        layoutFeedback = root.findViewById(R.id.layout_feedback);


        sliderInput = root.findViewById(R.id.slider_challenge_input);
        btnVerifySlider = root.findViewById(R.id.btn_verify_slider);
        btnOpt1 = root.findViewById(R.id.btn_opt_1);
        btnOpt2 = root.findViewById(R.id.btn_opt_2);
        btnOpt3 = root.findViewById(R.id.btn_opt_3);


        tvFeedbackStatus = root.findViewById(R.id.tv_feedback_status);
        tvFeedbackExp = root.findViewById(R.id.tv_feedback_explanation);
        btnNext = root.findViewById(R.id.btn_next_challenge);


        sliderInput.addOnChangeListener((slider, value, fromUser) -> updateLiveCalculation(value));


        btnVerifySlider.setOnClickListener(v -> {
            ChallengeItem current = challenges.get(currentIndex);
            float userVal = sliderInput.getValue();
            boolean isCorrect = Math.abs(userVal - current.targetVal) <= current.tolerance;
            handleAnswer(isCorrect, current.explanation);
        });


        btnOpt1.setOnClickListener(v -> checkOption(0));
        btnOpt2.setOnClickListener(v -> checkOption(1));
        btnOpt3.setOnClickListener(v -> checkOption(2));


        btnNext.setOnClickListener(v -> {
            if (lives <= 0 || currentIndex + 1 >= challenges.size()) {
                // Reiniciar partida
                currentIndex = 0;
                lives = 3;
                score = 0;
                streak = 0;
                loadChallenge(0);
            } else {
                loadChallenge(currentIndex + 1);
            }
        });
    }


    private void buildChallenges() {
        challenges.clear();


        // Reto 1: Interactivo con cálculo en tiempo real (Punto nulo entre cargas)
        challenges.add(new ChallengeItem(
                "CAMPO ELÉCTRICO",
                "Punto de Equilibrio (|E| = 0)",
                "q₁ = +4.0 µC está en x = 0 m y q₂ = +1.0 µC está en x = 3.0 m. Mueve el deslizador para ubicar la sonda donde el campo neto se anula.",
                0.2f, 2.8f, 0.1f, 2.0f, 0.11f, "m",
                "Como q₁ es 4 veces mayor que q₂, la distancia debe ser el doble (r₁ = 2r₂). En x = 2.0 m: E₁ = k(4)/2² = k y E₂ = k(1)/1² = k en sentidos opuestos."
        ));


        // Reto 2: Interactivo de ajuste de carga para alcanzar un potencial objetivo
        challenges.add(new ChallengeItem(
                "POTENCIAL ELÉCTRICO",
                "Calibrador de Potencial",
                "Una sonda está situada a r = 1.0 m de una carga puntual. Ajusta la magnitud de la carga q para generar exactamente V = +4.50e+04 V.",
                1.0f, 10.0f, 0.5f, 5.0f, 0.25f, "µC",
                "V = k·q / r. Despejando q = (V·r)/k = (45,000 × 1.0) / (8.99×10⁹) ≈ 5.0 µC."
        ));


        // Reto 3: Selección rápida de Ley de Coulomb inversa al cuadrado
        challenges.add(new ChallengeItem(
                "LEY DE COULOMB",
                "Efecto de la Distancia",
                "Dos cargas se repelen con una fuerza F = 36 N. Si triplicas la distancia (3r) entre ellas sin cambiar las cargas, ¿cuál será la nueva fuerza?",
                new String[]{"A) 12 N (se divide por 3)", "B) 4 N (se divide por 9)", "C) 9 N (se divide por 4)"},
                1,
                "La fuerza electrostática es inversamente proporcional al cuadrado de la distancia (1/r²). Al triplicar r, F disminuye 3² = 9 veces: 36 / 9 = 4 N."
        ));


        // Reto 4: Interactivo de ángulo de superposición
        challenges.add(new ChallengeItem(
                "SUPERPOSICIÓN VECTORIAL",
                "Vector Resultante a 45°",
                "En el origen (0,0) hay una carga q₁ = +3.0 µC sobre el eje X. Ajusta q₂ sobre el eje Y (a igual distancia) para que el vector campo neto apunte a θ = 45.0°.",
                0.5f, 6.0f, 0.5f, 3.0f, 0.1f, "µC",
                "Para que el vector resultante forme un ángulo de 45°, ambas componentes deben ser iguales (Ex = Ey), lo que ocurre cuando q₂ = +3.0 µC."
        ));


        // Reto 5: Prefijos métricos (nano / micro)
        challenges.add(new ChallengeItem(
                "UNIDADES DE CARGA",
                "Conversión de Escala",
                "Si el simulador marca una carga de 2,500 nC (nanocoulombs), ¿a cuánto equivale exactamente en microcoulombs (µC)?",
                new String[]{"A) 0.25 µC", "B) 25.0 µC", "C) 2.5 µC"},
                2,
                "1 µC = 10⁻⁶ C y 1 nC = 10⁻⁹ C. Por lo tanto, 2,500 × 10⁻⁹ C = 2.5 × 10⁻⁶ C = 2.5 µC."
        ));
    }


    private void loadChallenge(int index) {
        currentIndex = index;
        ChallengeItem item = challenges.get(index);


        updateHeaderStats();
        layoutFeedback.setVisibility(View.GONE);


        tvBadge.setText(String.format(Locale.US, "RETO %d DE %d • %s", index + 1, challenges.size(), item.category));
        tvTitle.setText(item.title);
        tvDesc.setText(item.description);
        progressBar.setProgress(((index + 1) * 100) / challenges.size());


        if (item.isSliderMode) {
            layoutSlider.setVisibility(View.VISIBLE);
            layoutOptions.setVisibility(View.GONE);
            btnVerifySlider.setEnabled(true);


            sliderInput.setValueTo(item.maxVal);
            sliderInput.setValueFrom(item.minVal);
            sliderInput.setStepSize(item.step);
            float mid = (item.minVal + item.maxVal) / 2f;
            // Ajustar al step más cercano
            mid = Math.round(mid / item.step) * item.step;
            sliderInput.setValue(mid);
            updateLiveCalculation(mid);
        } else {
            layoutSlider.setVisibility(View.GONE);
            layoutOptions.setVisibility(View.VISIBLE);
            tvLiveFormula.setText("Selecciona la respuesta física correcta:");
            btnOpt1.setEnabled(true);
            btnOpt2.setEnabled(true);
            btnOpt3.setEnabled(true);
            btnOpt1.setText(item.options[0]);
            btnOpt2.setText(item.options[1]);
            btnOpt3.setText(item.options[2]);
        }


        startTimer();
    }


    private void updateLiveCalculation(float val) {
        double k = 8.99e9;
        if (currentIndex == 0) {
            // Cálculo real |E| neto entre q1=4uC en 0 y q2=1uC en 3m
            double e1 = (k * 4e-6) / (val * val);
            double r2 = 3.0 - val;
            double e2 = (k * 1e-6) / (r2 * r2);
            double eNet = Math.abs(e1 - e2);
            tvLiveFormula.setText(String.format(Locale.US, "Posición x = %.1f m  →  |E| neto = %.2e N/C", val, eNet));
        } else if (currentIndex == 1) {
            // V = k * q / 1.0
            double v = k * (val * 1e-6);
            tvLiveFormula.setText(String.format(Locale.US, "Carga q = %.1f µC  →  V = %.2e V", val, v));
        } else if (currentIndex == 3) {
            // Ángulo atan2(q2, 3.0)
            double angle = Math.toDegrees(Math.atan2(val, 3.0));
            tvLiveFormula.setText(String.format(Locale.US, "Carga q₂ = %.1f µC  →  Ángulo θ = %.1f°", val, angle));
        }
    }


    private void checkOption(int chosenIndex) {
        ChallengeItem current = challenges.get(currentIndex);
        handleAnswer(chosenIndex == current.correctOptionIndex, current.explanation);
    }


    private void handleAnswer(boolean isCorrect, String explanation) {
        if (timer != null) timer.cancel();
        btnVerifySlider.setEnabled(false);
        btnOpt1.setEnabled(false);
        btnOpt2.setEnabled(false);
        btnOpt3.setEnabled(false);


        layoutFeedback.setVisibility(View.VISIBLE);
        tvFeedbackExp.setText(explanation);


        if (isCorrect) {
            streak++;
            score += 100 + (streak * 20);
            tvFeedbackStatus.setText("✅ ¡Correcto! +" + (100 + streak * 20) + " pts");
            tvFeedbackStatus.setTextColor(Color.parseColor("#00E676"));
        } else {
            streak = 0;
            lives--;
            tvFeedbackStatus.setText("❌ Respuesta incorrecta");
            tvFeedbackStatus.setTextColor(Color.parseColor("#FF8A80"));
        }


        updateHeaderStats();


        if (lives <= 0) {
            tvFeedbackStatus.setText("💔 Te quedaste sin vidas • Puntuación final: " + score);
            btnNext.setText("Reintentar Retos");
        } else if (currentIndex + 1 >= challenges.size()) {
            btnNext.setText("🏆 Completado (" + score + " pts) • Jugar de nuevo");
        } else {
            btnNext.setText("Siguiente Reto →");
        }
    }


    private void updateHeaderStats() {
        StringBuilder hearts = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            hearts.append(i < lives ? "❤️" : "🖤");
        }
        tvLives.setText(hearts.toString());
        tvScore.setText("⚡ " + score + " pts");
        tvStreak.setText("🔥 Racha: " + streak);
    }


    private void startTimer() {
        if (timer != null) timer.cancel();
        timer = new CountDownTimer(45000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long sec = millisUntilFinished / 1000;
                tvTimer.setText(String.format(Locale.US, "00:%02d", sec));
                tvTimer.setTextColor(sec <= 10 ? Color.parseColor("#FF8A80") : Color.parseColor("#00E676"));
            }


            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
                handleAnswer(false, "Se agotó el tiempo. " + challenges.get(currentIndex).explanation);
            }
        }.start();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) timer.cancel();
    }
}