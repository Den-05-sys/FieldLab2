
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
    public String getType() { return type; }

    public double getCharge() { return charge; }
    public double getDistance() { return distance; }
    public double getResult() { return result; }

    public String getUnit() { return unit; }
    public String getDate() { return date; }
}

