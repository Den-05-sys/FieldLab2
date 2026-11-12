package com.uac503.fieldlab.physics;


import com.uac503.fieldlab.model.Charge;
import com.uac503.fieldlab.model.FieldResult;


import java.util.ArrayList;
import java.util.List;


public class PhysicsCalculator {
    public static final double K = 8.9875517923e9; // Constante de Coulomb (N·m²/C²)
    public static final float PIXELS_PER_METER = 150.0f;


    public static class ChargeContribution {
        public int index;
        public float baseValue;
        public String unitSymbol;
        public double distanceMeters;
        public double eContribution;
    }


    public static class DetailedFieldResult extends FieldResult {
        public double ex;
        public double ey;
        public List<ChargeContribution> contributions = new ArrayList<>();
    }


    public static DetailedFieldResult calculateDetailed(float px, float py, List<Charge> charges) {
        DetailedFieldResult result = new DetailedFieldResult();
        double exTotal = 0.0;
        double eyTotal = 0.0;
        double vTotal = 0.0;


        for (int i = 0; i < charges.size(); i++) {
            Charge c = charges.get(i);
            double dxMeters = (px - c.getX()) / PIXELS_PER_METER;
            double dyMeters = (c.getY() - py) / PIXELS_PER_METER; // Eje Y cartesiano hacia arriba
            double r2 = dxMeters * dxMeters + dyMeters * dyMeters;
            double r = Math.sqrt(r2);


            if (r < 0.02) r = 0.02; // Evitar singularidad (r -> 0)
            r2 = r * r;


            double qCoulombs = c.getChargeInCoulombs();
            double eMag = (K * Math.abs(qCoulombs)) / r2;
            double eSigned = (K * qCoulombs) / r2;


            exTotal += eSigned * (dxMeters / r);
            eyTotal += eSigned * (dyMeters / r);
            vTotal += (K * qCoulombs) / r;


            ChargeContribution contrib = new ChargeContribution();
            contrib.index = i + 1;
            contrib.baseValue = c.getBaseValue();
            contrib.unitSymbol = c.getUnitSymbol();
            contrib.distanceMeters = r;
            contrib.eContribution = eMag;
            result.contributions.add(contrib);
        }


        result.ex = exTotal;
        result.ey = eyTotal;
        result.eMagnitude = Math.hypot(exTotal, eyTotal);
        result.angleDegrees = Math.toDegrees(Math.atan2(eyTotal, exTotal));
        result.potential = vTotal;
        return result;
    }


    // Método de compatibilidad para otras vistas
    public static FieldResult calculateAtPoint(float px, float py, List<Charge> charges) {
        return calculateDetailed(px, py, charges);
    }
}