package com.uac503.fieldlab.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "history")
public class HistoryEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private String type; // "Simulación", "Reto", "Quiz"
    private String detail;
    private String score;
    private String date;


    public HistoryEntry(int userId, String type, String detail, String score, String date) {
        this.userId = userId;
        this.type = type;
        this.detail = detail;
        this.score = score;
        this.date = date;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public String getType() { return type; }
    public String getDetail() { return detail; }
    public String getScore() { return score; }
    public String getDate() { return date; }
}