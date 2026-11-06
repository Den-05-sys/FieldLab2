package com.uac503.fieldlab.data.db;


import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


import com.uac503.fieldlab.model.HistoryEntry;
import com.uac503.fieldlab.model.Scenario;
import com.uac503.fieldlab.model.User;


@Database(entities = {User.class, Scenario.class, HistoryEntry.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;


    public abstract ScenarioDao scenarioDao();
    public abstract HistoryDao historyDao();


    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "fieldlab_db"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}