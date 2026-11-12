package com.uac503.fieldlab.model;

public class FieldResult {
    public double ex;
    public double ey;
    public double eMagnitude;
    public double angleDegrees;
    public double potential;


    public FieldResult() {
    }

    public FieldResult(double ex, double ey, double eMagnitude, double angleDegrees, double potential) {
        this.ex = ex;
        this.ey = ey;
        this.eMagnitude = eMagnitude;
        this.angleDegrees = angleDegrees;
        this.potential = potential;
    }
}
