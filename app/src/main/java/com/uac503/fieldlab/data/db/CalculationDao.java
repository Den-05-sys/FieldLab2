
package com.uac503.fieldlab.data.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.uac503.fieldlab.model.Calculation;
import java.util.List;

@Dao
public interface CalculationDao {

    @Insert
    long insertCalculation(Calculation calculation);

    @Query("SELECT * FROM calculations WHERE userId = :userId ORDER BY id DESC")
    List<Calculation> getCalculationsByUser(int userId);

    @Query("DELETE FROM calculations WHERE id = :id AND userId = :userId")
    void deleteCalculation(int id, int userId);
}

