
package com.uac503.fieldlab.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "calculations",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("userId")}
)
public class Calculation {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private String type;
    private double charge;
    private double distance;
    private double result;
    private String unit;
    private String date;

    public Calculation() {}

    public Calculation(int userId, String type,
                       double charge, double distance,
                       double result, String unit, String date) {
        this.userId = userId;
        this.type = type;
        this.charge = charge;
        this.distance = distance;
        this.result = result;
        this.unit = unit;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getCharge() { return charge; }
    public void setCharge(double charge) { this.charge = charge; }
    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }
    public double getResult() { return result; }
    public void setResult(double result) { this.result = result; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}

