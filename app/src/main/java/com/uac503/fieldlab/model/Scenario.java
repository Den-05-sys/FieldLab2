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


    public Scenario() {}

    public Scenario(String name, List<Charge> charges, String dateCreated) {
        this.name = name;
        this.charges = charges;
        this.dateCreated = dateCreated;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Charge> getCharges() { return charges; }
    public void setCharges(List<Charge> charges) { this.charges = charges; }
    public String getDateCreated() { return dateCreated; }
    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }
}
