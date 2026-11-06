package com.uac503.fieldlab.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.List;


@Entity(tableName = "scenarios")
public class Scenario {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private List<Charge> charges;
    private String dateCreated;


    public Scenario(String name, List<Charge> charges, String dateCreated) {
        this.name = name;
        this.charges = charges;
        this.dateCreated = dateCreated;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public List<Charge> getCharges() { return charges; }
    public String getDateCreated() { return dateCreated; }
}
