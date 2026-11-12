package com.uac503.fieldlab.model;


public class Charge {
    private float x;
    private float y;
    private float baseValue;      // Ej: -2.8
    private double unitFactor;    // Ej: 1e-6 para µC
    private String unitSymbol;    // Ej: "µC"


    public Charge(float x, float y, float baseValue, double unitFactor, String unitSymbol) {
        this.x = x;
        this.y = y;
        this.baseValue = baseValue;
        this.unitFactor = unitFactor;
        this.unitSymbol = unitSymbol;
    }


    // Constructor de compatibilidad
    public Charge(float x, float y, float microCoulombs) {
        this(x, y, microCoulombs, 1e-6, "µC");
    }


    public float getX() { return x; }
    public void setX(float x) { this.x = x; }


    public float getY() { return y; }
    public void setY(float y) { this.y = y; }


    public float getBaseValue() { return baseValue; }
    public void setBaseValue(float baseValue) { this.baseValue = baseValue; }


    public double getUnitFactor() { return unitFactor; }
    public void setUnitFactor(double unitFactor) { this.unitFactor = unitFactor; }


    public String getUnitSymbol() { return unitSymbol; }
    public void setUnitSymbol(String unitSymbol) { this.unitSymbol = unitSymbol; }


    /** Devuelve la carga real en Coulombs (C) para el motor físico */
    public double getChargeInCoulombs() {
        return baseValue * unitFactor;
    }


    /** Compatibilidad rápida */
    public float getCharge() {
        return baseValue;
    }
}