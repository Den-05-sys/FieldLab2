package com.uac503.fieldlab.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.uac503.fieldlab.model.Scenario;
import java.util.List;

@Dao
public interface ScenarioDao {
    @Insert
    void insertScenario(Scenario scenario);

    @Query("SELECT * FROM scenarios ORDER BY id DESC")
    List<Scenario> getAllScenarios();

    @Query("DELETE FROM scenarios WHERE id = :id")
    void deleteScenario(int id);
}
