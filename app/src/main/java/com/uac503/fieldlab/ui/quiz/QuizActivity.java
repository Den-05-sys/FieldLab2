package com.uac503.fieldlab.ui.quiz;


import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.uac503.fieldlab.R;


public class QuizActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        TextView tvQuestion = findViewById(R.id.tv_quiz_question);
        RadioGroup rgOptions = findViewById(R.id.rg_quiz_options);
        Button btnSubmit = findViewById(R.id.btn_submit_answer);


        tvQuestion.setText("1. Si se duplica la distancia entre dos cargas puntuales, ¿qué le ocurre a la fuerza eléctrica entre ellas?");


        btnSubmit.setOnClickListener(v -> {
            int selectedId = rgOptions.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selected = findViewById(selectedId);
                if (selected.getText().toString().contains("1/4")) {
                    Toast.makeText(this, "¡Correcto! Ley del inverso del cuadrado.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Incorrecto. Revisa la Ley de Coulomb.", Toast.LENGTH_SHORT).show();
                }
                finish();
            } else {
                Toast.makeText(this, "Selecciona una respuesta", Toast.LENGTH_SHORT).show();
            }
        });
    }
}