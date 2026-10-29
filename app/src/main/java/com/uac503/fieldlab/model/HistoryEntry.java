package com.uac503.fieldlab.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class HistoryEntry {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String details;
    private long timestamp;

    public HistoryEntry(String title, String details, long timestamp) {
        this.title = title;
        this.details = details;
        this.timestamp = timestamp;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public String getDetails() { return details; }
    public long getTimestamp() { return timestamp; }
}
