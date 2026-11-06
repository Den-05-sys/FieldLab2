package com.uac503.fieldlab.physics;

import com.uac503.fieldlab.model.Charge;
import com.uac503.fieldlab.model.FieldResult;
import java.util.List;

public class PhysicsCalculator {
    public static final double K = 8.98755e9; // Constante de Coulomb (N·m²/C²)

    public static FieldResult calculateAtPoint(float px, float py, List<Charge> charges) {
        double ex = 0;
        double ey = 0;
        double potential = 0;

        for (Charge c : charges) {
            double dx = px - c.getX();
            double dy = py - c.getY();
            double r2 = dx * dx + dy * dy;
            double r = Math.sqrt(r2);

            if (r < 1e-3) continue; // Evitar división por cero cerca de la carga

            double qCoulombs = c.getCharge() * 1e-6; // Convertir µC a C

            // Magnitud del campo E = k * |q| / r²
            double eMag = K * Math.abs(qCoulombs) / r2;

            // Dirección
            double unitX = dx / r;
            double unitY = dy / r;

            // Si la carga es positiva repele, si es negativa atrae
            if (c.getCharge() < 0) {
                eMag = -eMag;
            }

            ex += eMag * unitX;
            ey += eMag * unitY;

            // Potencial escalar V = k * q / r
            potential += K * qCoulombs / r;
        }

        double totalE = Math.sqrt(ex * ex + ey * ey);
        double angle = Math.toDegrees(Math.atan2(ey, ex));

        return new FieldResult(ex, ey, totalE, angle, potential);
    }
}
