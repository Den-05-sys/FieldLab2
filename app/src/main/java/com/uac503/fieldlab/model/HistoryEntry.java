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


    public HistoryEntry() {}

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
    public void setUserId(int userId) { this.userId = userId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
    public String getScore() { return score; }
    public void setScore(String score) { this.score = score; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}