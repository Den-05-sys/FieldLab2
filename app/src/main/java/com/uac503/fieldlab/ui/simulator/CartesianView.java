package com.uac503.fieldlab.ui.simulator;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


import com.uac503.fieldlab.model.Charge;
import com.uac503.fieldlab.model.FieldResult;
import com.uac503.fieldlab.physics.PhysicsCalculator;


import java.util.ArrayList;
import java.util.List;


public class CartesianView extends View {
    private final List<Charge> charges = new ArrayList<>();
    private final Paint paintAxis = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintGrid = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintVector = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintCharge = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);


    private float probeCartX = 0.12f; // metros
    private float probeCartY = 0.00f; // metros


    public CartesianView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        paintAxis.setColor(Color.WHITE);
        paintAxis.setStrokeWidth(4f);


        paintGrid.setColor(Color.parseColor("#2A3447"));
        paintGrid.setStrokeWidth(2f);
        paintGrid.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));


        paintVector.setColor(Color.parseColor("#FFD54F"));
        paintVector.setStrokeWidth(5f);


        paintCharge.setColor(Color.parseColor("#FF5252"));
        paintText.setColor(Color.WHITE);
        paintText.setTextSize(26f);
    }


    public void updateCartesianData(List<Charge> chargesList, float px, float py) {
        this.charges.clear();
        this.charges.addAll(chargesList);
        this.probeCartX = px;
        this.probeCartY = py;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#0E1117"));


        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        float scale = 1800f; // Escala para transformar metros a px


        // Ejes X e Y
        canvas.drawLine(0, cy, getWidth(), cy, paintAxis);
        canvas.drawLine(cx, 0, cx, getHeight(), paintAxis);


        canvas.drawText("X (m)", getWidth() - 60, cy - 15, paintText);
        canvas.drawText("Y (m)", cx + 20, 40, paintText);


        // Dibujar Cargas en Coordenadas
        for (Charge c : charges) {
            float px = cx + (c.getX() / 1000f) * scale;
            float py = cy - (c.getY() / 1000f) * scale;


            paintCharge.setColor(c.getCharge() >= 0 ? Color.parseColor("#FF5252") : Color.parseColor("#448AFF"));
            canvas.drawCircle(px, py, 24f, paintCharge);
            canvas.drawText(String.format("q(%.2f, %.2f)", c.getX()/1000f, c.getY()/1000f), px + 30, py - 10, paintText);
        }


        // Punto de Evaluación (Sonda P)
        float pxP = cx + probeCartX * scale;
        float pyP = cy - probeCartY * scale;


        canvas.drawCircle(pxP, pyP, 16f, paintVector);
        canvas.drawText(String.format("P(%.2f, %.2f)", probeCartX, probeCartY), pxP + 20, pyP + 30, paintText);


        // Dibujar vector resultante
        if (!charges.isEmpty()) {
            FieldResult res = PhysicsCalculator.calculateAtPoint(pxP, pyP, charges);
            float len = (float) Math.min(150, Math.log10(res.eMagnitude + 1) * 20);
            float dx = (float) (len * Math.cos(Math.toRadians(res.angleDegrees)));
            float dy = (float) (len * Math.sin(Math.toRadians(res.angleDegrees)));


            canvas.drawLine(pxP, pyP, pxP + dx, pyP - dy, paintVector);
        }
    }
}