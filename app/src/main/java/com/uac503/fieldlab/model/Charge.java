package com.uac503.fieldlab.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "charges")
public class Charge {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private double x;
    private double y;
    private double charge;

    public Charge(double x, double y, double charge) {
        this.x = x;
        this.y = y;
        this.charge = charge;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double getCharge() { return charge; }
    public void setCharge(double charge) { this.charge = charge; }
}
