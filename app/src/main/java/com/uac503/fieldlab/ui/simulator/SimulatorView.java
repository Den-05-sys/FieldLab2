package com.uac503.fieldlab.ui.simulator;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.uac503.fieldlab.model.Charge;
import com.uac503.fieldlab.physics.PhysicsCalculator;


import java.util.ArrayList;
import java.util.List;


public class SimulatorView extends View {


    private final List<Charge> charges = new ArrayList<>();
    private final Paint paintPos = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintNeg = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintSelectedRing = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintVector = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintFieldLine = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintPotential = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintProbe = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintProbeArrow = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintTestParticle = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);


    // Capas activables (como los botones de la imagen)
    private boolean showVectors = true;
    private boolean showFieldLines = true;
    private boolean showPotentialMap = false;
    private boolean showProbe = true;


    // Estado de selección y arrastre
    private int selectedChargeIndex = -1;
    private boolean draggingProbe = false;
    private boolean isDragging = false;
    private float probeX = 250f, probeY = 220f;


    // Innovación extra: Partícula de prueba animada (F = qE)
    private boolean particleRunning = false;
    private float partX = -1f, partY = -1f, partVx = 0f, partVy = 0f;


    public interface OnSimulatorListener {
        void onDataUpdated(PhysicsCalculator.DetailedFieldResult probeResult, float probeXm, float probeYm);
        void onChargeSelected(Charge charge, int index);
    }


    private OnSimulatorListener listener;


    public SimulatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        paintPos.setColor(Color.parseColor("#E53935"));
        paintNeg.setColor(Color.parseColor("#1E88E5"));


        paintSelectedRing.setColor(Color.WHITE);
        paintSelectedRing.setStyle(Paint.Style.STROKE);
        paintSelectedRing.setStrokeWidth(5f);


        paintVector.setStrokeWidth(2.8f);
        paintVector.setStrokeCap(Paint.Cap.ROUND);


        paintFieldLine.setColor(Color.parseColor("#78909C"));
        paintFieldLine.setStyle(Paint.Style.STROKE);
        paintFieldLine.setStrokeWidth(2.2f);


        paintProbe.setColor(Color.parseColor("#FFD54F"));
        paintProbe.setStyle(Paint.Style.STROKE);
        paintProbe.setStrokeWidth(4f);


        paintProbeArrow.setColor(Color.WHITE);
        paintProbeArrow.setStrokeWidth(6f);
        paintProbeArrow.setStrokeCap(Paint.Cap.ROUND);


        paintTestParticle.setColor(Color.parseColor("#00E676"));


        paintText.setColor(Color.WHITE);
        paintText.setTextSize(34f);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setFakeBoldText(true);
    }


    public void setOnSimulatorListener(OnSimulatorListener listener) {
        this.listener = listener;
    }


    public void addCharge(float x, float y, float baseVal, double unitFactor, String symbol) {
        charges.add(new Charge(x, y, baseVal, unitFactor, symbol));
        selectedChargeIndex = charges.size() - 1;
        notifySelection();
        notifyProbe();
        invalidate();
    }


    public void removeSelectedCharge() {
        if (selectedChargeIndex >= 0 && selectedChargeIndex < charges.size()) {
            charges.remove(selectedChargeIndex);
            selectedChargeIndex = charges.isEmpty() ? -1 : charges.size() - 1;
            notifySelection();
            notifyProbe();
            invalidate();
        }
    }


    public void updateSelectedChargeValue(float newValue, double unitFactor, String symbol) {
        if (selectedChargeIndex >= 0 && selectedChargeIndex < charges.size()) {
            Charge c = charges.get(selectedChargeIndex);
            c.setBaseValue(newValue);
            c.setUnitFactor(unitFactor);
            c.setUnitSymbol(symbol);
            notifyProbe();
            invalidate();
        }
    }


    public Charge getSelectedCharge() {
        if (selectedChargeIndex >= 0 && selectedChargeIndex < charges.size()) {
            return charges.get(selectedChargeIndex);
        }
        return null;
    }


    public void loadPreset(String presetType, double unitFactor, String symbol) {
        charges.clear();
        float w = getWidth() > 0 ? getWidth() : 900f;
        float h = getHeight() > 0 ? getHeight() : 800f;
        float cx = w / 2f;
        float cy = h / 2f;


        switch (presetType) {
            case "DIPOLE":
                charges.add(new Charge(cx - 180f, cy, 2.0f, unitFactor, symbol));
                charges.add(new Charge(cx + 180f, cy, -2.8f, unitFactor, symbol));
                break;
            case "EQUAL":
                charges.add(new Charge(cx - 180f, cy, 3.0f, unitFactor, symbol));
                charges.add(new Charge(cx + 180f, cy, 3.0f, unitFactor, symbol));
                break;
            case "QUADRUPOLE":
                charges.add(new Charge(cx - 140f, cy - 140f, 3.0f, unitFactor, symbol));
                charges.add(new Charge(cx + 140f, cy - 140f, -3.0f, unitFactor, symbol));
                charges.add(new Charge(cx - 140f, cy + 140f, -3.0f, unitFactor, symbol));
                charges.add(new Charge(cx + 140f, cy + 140f, 3.0f, unitFactor, symbol));
                break;
        }
        selectedChargeIndex = charges.isEmpty() ? -1 : charges.size() - 1;
        notifySelection();
        notifyProbe();
        invalidate();
    }


    public void clearAll() {
        charges.clear();
        selectedChargeIndex = -1;
        particleRunning = false;
        notifySelection();
        notifyProbe();
        invalidate();
    }


    // Toggles de visualización
    public boolean toggleVectors() { showVectors = !showVectors; invalidate(); return showVectors; }
    public boolean toggleLines() { showFieldLines = !showFieldLines; invalidate(); return showFieldLines; }
    public boolean togglePotential() { showPotentialMap = !showPotentialMap; invalidate(); return showPotentialMap; }
    public boolean toggleProbe() { showProbe = !showProbe; invalidate(); return showProbe; }


    public boolean toggleTestParticle() {
        particleRunning = !particleRunning;
        if (particleRunning) {
            partX = probeX;
            partY = probeY;
            partVx = 0f;
            partVy = 0f;
        }
        invalidate();
        return particleRunning;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#0B0E14"));


        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) return;


        // 1. MAPA DE POTENCIAL (Heatmap)
        if (showPotentialMap && !charges.isEmpty()) {
            int cell = 36;
            for (int x = 0; x < w; x += cell) {
                for (int y = 0; y < h; y += cell) {
                    double v = 0;
                    for (Charge c : charges) {
                        double dx = (x - c.getX()) / PhysicsCalculator.PIXELS_PER_METER;
                        double dy = (y - c.getY()) / PhysicsCalculator.PIXELS_PER_METER;
                        double r = Math.max(0.05, Math.hypot(dx, dy));
                        v += (PhysicsCalculator.K * c.getChargeInCoulombs()) / r;
                    }
                    int alpha = (int) Math.min(130, Math.abs(v) / 400.0);
                    paintPotential.setColor(v >= 0 ? Color.argb(alpha, 229, 57, 53) : Color.argb(alpha, 30, 136, 229));
                    canvas.drawRect(x, y, x + cell, y + cell, paintPotential);
                }
            }
        }


        // 2. LÍNEAS DE CAMPO CONTINUAS (Integración espacial)
        if (showFieldLines && !charges.isEmpty() && !isDragging) {
            drawFieldLines(canvas, w, h);
        }


        // 3. MALLA DE VECTORES CON FLECHAS
        if (showVectors && !charges.isEmpty()) {
            int step = isDragging ? 95 : 68;
            for (int x = step / 2; x < w; x += step) {
                for (int y = step / 2; y < h; y += step) {
                    PhysicsCalculator.DetailedFieldResult res = PhysicsCalculator.calculateDetailed(x, y, charges);
                    if (res.eMagnitude > 0) {
                        float len = 24f;
                        double rad = Math.toRadians(-res.angleDegrees); // Invertir Y para pantalla
                        float ex = x + (float) (len * Math.cos(rad));
                        float ey = y + (float) (len * Math.sin(rad));


                        // Gradiente cian a amarillo según intensidad
                        float intensity = (float) Math.min(1.0, Math.log10(res.eMagnitude + 1) / 6.0);
                        int rCol = (int) (40 + 180 * intensity);
                        int gCol = (int) (180 + 50 * intensity);
                        int bCol = (int) (200 - 130 * intensity);
                        paintVector.setColor(Color.rgb(rCol, gCol, bCol));


                        drawArrow(canvas, x, y, ex, ey, 8f, paintVector);
                    }
                }
            }
        }


        // 4. CARGAS PUNTUALES (+ y -)
        for (int i = 0; i < charges.size(); i++) {
            Charge c = charges.get(i);
            boolean isPos = c.getBaseValue() >= 0;
            canvas.drawCircle(c.getX(), c.getY(), 26f, isPos ? paintPos : paintNeg);
            if (i == selectedChargeIndex) {
                canvas.drawCircle(c.getX(), c.getY(), 31f, paintSelectedRing);
            }
            canvas.drawText(isPos ? "+" : "−", c.getX(), c.getY() + 11f, paintText);
        }


        // 5. SONDA DE MEDICIÓN + VECTOR BLANCO RESULTANTE
        if (showProbe) {
            canvas.drawCircle(probeX, probeY, 14f, paintProbe);
            canvas.drawLine(probeX - 20, probeY, probeX + 20, probeY, paintProbe);
            canvas.drawLine(probeX, probeY - 20, probeX, probeY + 20, paintProbe);


            if (!charges.isEmpty()) {
                PhysicsCalculator.DetailedFieldResult probeRes = PhysicsCalculator.calculateDetailed(probeX, probeY, charges);
                if (probeRes.eMagnitude > 0) {
                    float arrowLen = (float) Math.min(110f, Math.max(35f, Math.log10(probeRes.eMagnitude + 1) * 18f));
                    double rad = Math.toRadians(-probeRes.angleDegrees);
                    float endX = probeX + (float) (arrowLen * Math.cos(rad));
                    float endY = probeY + (float) (arrowLen * Math.sin(rad));
                    drawArrow(canvas, probeX, probeY, endX, endY, 16f, paintProbeArrow);
                }
            }
        }


        // 6. PARTÍCULA DE PRUEBA DINÁMICA (Simulación de movimiento)
        if (particleRunning && !charges.isEmpty()) {
            PhysicsCalculator.DetailedFieldResult res = PhysicsCalculator.calculateDetailed(partX, partY, charges);
            double rad = Math.toRadians(-res.angleDegrees);
            float accel = (float) Math.min(2.5, res.eMagnitude * 1e-4);
            partVx = (partVx + (float) (accel * Math.cos(rad))) * 0.96f;
            partVy = (partVy + (float) (accel * Math.sin(rad))) * 0.96f;
            partX += partVx;
            partY += partVy;


            canvas.drawCircle(partX, partY, 12f, paintTestParticle);


            if (partX < 0 || partX > w || partY < 0 || partY > h) {
                partX = probeX;
                partY = probeY;
                partVx = 0;
                partVy = 0;
            }
            postInvalidateDelayed(20);
        }
    }


    private void drawFieldLines(Canvas canvas, int w, int h) {
        int seedsPerCharge = 16;
        float stepSize = 12f;
        int maxSteps = 95;


        for (Charge source : charges) {
            if (Math.abs(source.getBaseValue()) < 0.05f) continue;
            int dir = source.getBaseValue() > 0 ? 1 : -1;


            for (int s = 0; s < seedsPerCharge; s++) {
                double angle = (2 * Math.PI * s) / seedsPerCharge;
                float currX = source.getX() + (float) (30f * Math.cos(angle));
                float currY = source.getY() + (float) (30f * Math.sin(angle));


                Path path = new Path();
                path.moveTo(currX, currY);


                for (int step = 0; step < maxSteps; step++) {
                    PhysicsCalculator.DetailedFieldResult res = PhysicsCalculator.calculateDetailed(currX, currY, charges);
                    if (res.eMagnitude <= 0) break;


                    double rad = Math.toRadians(-res.angleDegrees);
                    currX += (float) (dir * stepSize * Math.cos(rad));
                    currY += (float) (dir * stepSize * Math.sin(rad));


                    if (currX < 0 || currX > w || currY < 0 || currY > h) break;
                    path.lineTo(currX, currY);


                    // Detener si llega a otra carga
                    boolean hitCharge = false;
                    for (Charge target : charges) {
                        if (target != source && Math.hypot(currX - target.getX(), currY - target.getY()) < 26f) {
                            hitCharge = true;
                            break;
                        }
                    }
                    if (hitCharge) break;
                }
                canvas.drawPath(path, paintFieldLine);
            }
        }
    }


    private void drawArrow(Canvas canvas, float x1, float y1, float x2, float y2, float headSize, Paint paint) {
        canvas.drawLine(x1, y1, x2, y2, paint);
        double angle = Math.atan2(y2 - y1, x2 - x1);
        float x3 = (float) (x2 - headSize * Math.cos(angle - Math.PI / 6));
        float y3 = (float) (y2 - headSize * Math.sin(angle - Math.PI / 6));
        float x4 = (float) (x2 - headSize * Math.cos(angle + Math.PI / 6));
        float y4 = (float) (y2 - headSize * Math.sin(angle + Math.PI / 6));
        canvas.drawLine(x2, y2, x3, y3, paint);
        canvas.drawLine(x2, y2, x4, y4, paint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (showProbe && Math.hypot(x - probeX, y - probeY) < 65f) {
                    draggingProbe = true;
                    isDragging = true;
                    return true;
                }
                for (int i = charges.size() - 1; i >= 0; i--) {
                    if (Math.hypot(x - charges.get(i).getX(), y - charges.get(i).getY()) < 65f) {
                        selectedChargeIndex = i;
                        isDragging = true;
                        notifySelection();
                        invalidate();
                        return true;
                    }
                }
                break;


            case MotionEvent.ACTION_MOVE:
                if (draggingProbe) {
                    probeX = Math.max(20, Math.min(getWidth() - 20, x));
                    probeY = Math.max(20, Math.min(getHeight() - 20, y));
                    notifyProbe();
                    invalidate();
                } else if (isDragging && selectedChargeIndex != -1) {
                    Charge c = charges.get(selectedChargeIndex);
                    c.setX(Math.max(20, Math.min(getWidth() - 20, x)));
                    c.setY(Math.max(20, Math.min(getHeight() - 20, y)));
                    notifyProbe();
                    invalidate();
                }
                break;


            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                draggingProbe = false;
                isDragging = false;
                invalidate();
                break;
        }
        return true;
    }


    private void notifyProbe() {
        if (listener != null) {
            PhysicsCalculator.DetailedFieldResult res = PhysicsCalculator.calculateDetailed(probeX, probeY, charges);
            float xm = probeX / PhysicsCalculator.PIXELS_PER_METER;
            float ym = (getHeight() > 0 ? (getHeight() - probeY) : probeY) / PhysicsCalculator.PIXELS_PER_METER;
            listener.onDataUpdated(res, xm, ym);
        }
    }


    private void notifySelection() {
        if (listener != null) {
            listener.onChargeSelected(getSelectedCharge(), selectedChargeIndex);
        }
    }
}