package com.uac503.fieldlab.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
public class Charge {
    private float x;       // Posición X en metros o px
    private float y;       // Posición Y en metros o px
    private float charge;  // Magnitud en microcoulombs (µC)


    public Charge(float x, float y, float charge) {
        this.x = x;
        this.y = y;
        this.charge = charge;
    }


    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public float getCharge() { return charge; }
    public void setCharge(float charge) { this.charge = charge; }
}
