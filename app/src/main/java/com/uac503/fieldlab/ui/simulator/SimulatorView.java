package com.uac503.fieldlab.ui.simulator;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.uac503.fieldlab.model.Charge;
import com.uac503.fieldlab.model.FieldResult;
import com.uac503.fieldlab.physics.PhysicsCalculator;


import java.util.ArrayList;
import java.util.List;


public class SimulatorView extends View {
    private final List<Charge> charges = new ArrayList<>();
    private final Paint paintPos = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintNeg = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintProbe = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintVector = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);


    private float probeX = 300, probeY = 300;
    private int selectedIndex = -1;
    private boolean draggingProbe = false;
    private boolean showVectors = true;


    public SimulatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        paintPos.setColor(Color.parseColor("#E53935"));
        paintNeg.setColor(Color.parseColor("#1E88E5"));
        paintProbe.setColor(Color.parseColor("#FFD54F"));
        paintVector.setColor(Color.parseColor("#26C6DA"));
        paintVector.setStrokeWidth(4f);
        paintText.setColor(Color.WHITE);
        paintText.setTextSize(32f);
        paintText.setTextAlign(Paint.Align.CENTER);
    }


    public void addCharge(float x, float y, float q) {
        charges.add(new Charge(x, y, q));
        invalidate();
    }


    public List<Charge> getCharges() { return charges; }
    public float getProbeX() { return probeX; }
    public float getProbeY() { return probeY; }
    public void setVectorVisibility(boolean visible) {
        this.showVectors = visible;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#0E1117"));


        // Dibujar Malla de Vectores de Campo
        if (showVectors && !charges.isEmpty()) {
            int step = 80;
            for (int x = 40; x < getWidth(); x += step) {
                for (int y = 40; y < getHeight(); y += step) {
                    FieldResult res = PhysicsCalculator.calculateAtPoint(x, y, charges);
                    if (res.eMagnitude > 0) {
                        float len = (float) Math.min(30, Math.log10(res.eMagnitude + 1) * 8);
                        float dx = (float) (len * Math.cos(Math.toRadians(res.angleDegrees)));
                        float dy = (float) (len * Math.sin(Math.toRadians(res.angleDegrees)));
                        canvas.drawLine(x, y, x + dx, y + dy, paintVector);
                    }
                }
            }
        }


        // Dibujar Cargas Puntuales
        for (Charge c : charges) {
            Paint p = c.getCharge() >= 0 ? paintPos : paintNeg;
            canvas.drawCircle(c.getX(), c.getY(), 36f, p);
            String txt = (c.getCharge() > 0 ? "+" : "") + (int) c.getCharge();
            canvas.drawText(txt, c.getX(), c.getY() + 10f, paintText);
        }


        // Dibujar Sonda
        canvas.drawCircle(probeX, probeY, 18f, paintProbe);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (Math.hypot(x - probeX, y - probeY) < 50) {
                    draggingProbe = true;
                    return true;
                }
                for (int i = 0; i < charges.size(); i++) {
                    if (Math.hypot(x - charges.get(i).getX(), y - charges.get(i).getY()) < 50) {
                        selectedIndex = i;
                        return true;
                    }
                }
                break;


            case MotionEvent.ACTION_MOVE:
                if (draggingProbe) {
                    probeX = x;
                    probeY = y;
                    invalidate();
                } else if (selectedIndex != -1) {
                    charges.get(selectedIndex).setX(x);
                    charges.get(selectedIndex).setY(y);
                    invalidate();
                }
                break;


            case MotionEvent.ACTION_UP:
                draggingProbe = false;
                selectedIndex = -1;
                break;
        }
        return true;
    }
}