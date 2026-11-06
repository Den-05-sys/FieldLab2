package com.uac503.fieldlab.data.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.uac503.fieldlab.model.HistoryEntry;
import java.util.List;


@Dao
public interface HistoryDao {
    @Insert
    void insertHistory(HistoryEntry entry);


    @Query("SELECT * FROM history WHERE userId = :userId ORDER BY id DESC")
    List<HistoryEntry> getHistoryByUser(int userId);
}