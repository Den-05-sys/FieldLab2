package com.uac503.fieldlab.ui.theory;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.uac503.fieldlab.R;
import com.uac503.fieldlab.model.TheoryTopic;


import java.util.ArrayList;
import java.util.List;


public class TheoryFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_theory, container, false);


        RecyclerView rvTopics = root.findViewById(R.id.rv_theory_topics);
        rvTopics.setLayoutManager(new LinearLayoutManager(getContext()));


        List<TheoryTopic> topics = loadDocumentTopics();
        TheoryAdapter adapter = new TheoryAdapter(topics);
        rvTopics.setAdapter(adapter);


        return root;
    }


    private List<TheoryTopic> loadDocumentTopics() {
        List<TheoryTopic> list = new ArrayList<>();


        list.add(new TheoryTopic(
                1,
                "Carga eléctrica, cuantización y conservación",
                "Alcance (sec. 6) · Limitaciones (sec. 19)",
                "La carga eléctrica es una propiedad fundamental de la materia. Se divide en positiva y negativa. Cumple la regla de cuantización (q = n·e) y conservación de la carga.",
                "q = n · e\ne = 1.602 × 10⁻¹⁹ C\nSubmúltiplos: 1 µC = 10⁻⁶ C"
        ));


        list.add(new TheoryTopic(
                2,
                "Ley de Coulomb (Escalar y Vectorial)",
                "Intro · Fundamento 7.1 · Objetivos",
                "La fuerza electrostática entre dos cargas es directamente proporcional al producto de sus magnitudes e inversamente proporcional al cuadrado de la distancia r.",
                "F = k · |q₁·q₂| / r²\nk ≈ 8.9875 × 10⁹ N·m²/C²\nF₁₂ = k·q₁·q₂·(r₂ − r₁) / |r₂ − r₁|³"
        ));


        list.add(new TheoryTopic(
                3,
                "Campo eléctrico de una carga puntual",
                "Fundamento 7.2 · Algoritmo (sec. 10)",
                "El campo E en un punto es la fuerza por unidad de carga de prueba positiva. Es un campo vectorial con componentes cartesianas Ex, Ey, Ez.",
                "E = F / q₀  [N/C = V/m]\nE(P) = k · q · (P − r₀) / |P − r₀|³\n|E| = √(Ex² + Ey²)"
        ));


        list.add(new TheoryTopic(
                4,
                "Principio de superposición",
                "Fundamento 7.3 · Motor físico 8.2",
                "El campo eléctrico neto en un punto es la suma vectorial individual de los campos generados por cada carga presente.",
                "E_total(P) = Σ Eᵢ(P)\nEx_total = Σ Exᵢ,  Ey_total = Σ Eyᵢ"
        ));


        list.add(new TheoryTopic(
                5,
                "Álgebra vectorial en el simulador",
                "Algoritmo (sec. 10) · Código (sec. 22)",
                "Manejo de vectores de posición R = P - r₀, su módulo r = |R| y vectores unitarios r̂ = R / |R|. Construcción de mallas con meshgrid.",
                "R = (x - x₀, y - y₀)\nr = √((x - x₀)² + (y - y₀)²)\nθ = atan2(Ey, Ex)"
        ));


        list.add(new TheoryTopic(
                6,
                "Líneas de campo eléctrico",
                "Visualización 8.3 · Figura 2",
                "Líneas continuas tangentes al vector de campo E en cada punto. Salen de cargas + y entran en cargas -. Su densidad representa la intensidad.",
                "dr/ds = E(r) / |E(r)|\n2D: dy/dx = Ey / Ex"
        ));


        list.add(new TheoryTopic(
                7,
                "Dipolo eléctrico",
                "Figura 2 (+5 µC y −5 µC)",
                "Par de cargas de igual magnitud y signo contrario separadas una distancia d. El campo decae proporcionalmente a 1/r³.",
                "p = q · d\nE_eje ≈ 2·k·p / x³\nE_mediatriz ≈ k·p / y³"
        ));


        list.add(new TheoryTopic(
                8,
                "Potencial eléctrico y energía potencial",
                "Fundamento 7.4 · Panel de resultados",
                "El potencial V es la energía potencial por unidad de carga. Es una magnitud escalar sumable directamente sin considerar ángulos.",
                "U = k · q₁ · q₂ / r  [J]\nV = k · q / r  [V = J/C]\nV_total = k · Σ (qᵢ / rᵢ)"
        ));


        list.add(new TheoryTopic(
                9,
                "Superficies equipotenciales y relación E–V",
                "Mapas de nivel (7.4) · Mapas de intensidad",
                "Conjunto de puntos donde el potencial es constante. El campo eléctrico siempre es perpendicular a las equipotenciales.",
                "E = − ∇V\nEx ≈ −(V(x+h) − V(x−h)) / 2h"
        ));


        list.add(new TheoryTopic(
                10,
                "Dependencia E ∝ 1/r² (Inverso del cuadrado)",
                "Figura 3 · Estrategia de pruebas",
                "Al duplicar la distancia r respecto a la carga fuente, la intensidad del campo disminuye exactamente a la cuarta parte (25%).",
                "E(r₂)/E(r₁) = (r₁ / r₂)²"
        ));


        list.add(new TheoryTopic(
                11,
                "Singularidad y distribuciones continuas",
                "Motor físico 8.2 · Limitaciones",
                "Cuando r → 0 la fórmula produce una división por cero (infinito). El programa detecta esta singularidad para evitar valores NaN.",
                "Si r < 10⁻¹² m → Advertencia/Aviso\nE = k ∫ dq r̂ / r²"
        ));


        list.add(new TheoryTopic(
                12,
                "Simetría y cancelación de campos",
                "Estrategia de pruebas (sec. 14)",
                "En el punto medio entre dos cargas idénticas, los vectores de campo tienen magnitudes iguales en sentidos opuestos y se cancelan (|E| = 0).",
                "E_medio = k·q/r² − k·q/r² = 0\nV_medio = 2·k·q/r ≠ 0"
        ));


        list.add(new TheoryTopic(
                13,
                "Fuerza sobre una carga en un campo (F = qE)",
                "Caso de validación triangular (sec. 13)",
                "Una carga q colocada en un campo experimenta una fuerza vectorial proporcional a la intensidad del campo en esa coordenada.",
                "F = q · E\na = F / m = q · E / m"
        ));


        list.add(new TheoryTopic(
                14,
                "Análisis de error y precisión",
                "Análisis de error (sec. 15)",
                "Verificación analítica entre el valor numérico simulado y el valor teórico. Criterio de aceptación: error relativo menor al 1%.",
                "Error Relativo (%) = |sim - ref| / |ref| × 100"
        ));


        list.add(new TheoryTopic(
                15,
                "Caso de validación triangular resuelto",
                "Sec. 13 y Figura 4",
                "Ejemplo con tres cargas: q₁=-2µC en (0,0), q₂=+5µC en (0,0.1) y q₃=-3µC en (0.12,0). Campo calculado por superposición en posición q₃.",
                "E_total = (166570.83 ; −1179034.93) N/C\n|E_total| ≈ 1.19 × 10⁶ N/C\nθ ≈ −81.9°"
        ));


        list.add(new TheoryTopic(
                16,
                "Complemento: Ley de Gauss",
                "Apoyo teórico de la simulación",
                "El flujo eléctrico neto a través de cualquier superficie cerrada es igual a la carga encerrada dividida por la permitividad del vacío.",
                "Φ_E = ∮ E · dA = Q_enc / ε₀"
        ));


        return list;
    }
}