package com.uac503.fieldlab.model;

public class FieldResult {
    private double x;
    private double y;
    private double ex;
    private double ey;
    private double magnitude;
    private double potential;

    public FieldResult(double x, double y, double ex, double ey, double magnitude, double potential) {
        this.x = x;
        this.y = y;
        this.ex = ex;
        this.ey = ey;
        this.magnitude = magnitude;
        this.potential = potential;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getEx() { return ex; }
    public double getEy() { return ey; }
    public double getMagnitude() { return magnitude; }
    public double getPotential() { return potential; }
}
