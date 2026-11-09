
package com.uac503.fieldlab.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.uac503.fieldlab.model.User;
import com.uac503.fieldlab.model.Scenario;
import com.uac503.fieldlab.model.HistoryEntry;
import com.uac503.fieldlab.model.Calculation;

@Database(
        entities = {
                User.class,
                Scenario.class,
                HistoryEntry.class,
                Calculation.class
        },
        version = 3,
        exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract ScenarioDao scenarioDao();
    public abstract HistoryDao historyDao();
    public abstract CalculationDao calculationDao();

    static final Migration MIGRATION_2_3 =
            new Migration(2, 3) {
                @Override
                public void migrate(SupportSQLiteDatabase db) {

                    db.execSQL(
                            "CREATE TABLE IF NOT EXISTS calculations (" +
                                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                    "userId INTEGER NOT NULL, " +
                                    "type TEXT NOT NULL, " +
                                    "charge REAL NOT NULL, " +
                                    "distance REAL NOT NULL, " +
                                    "result REAL NOT NULL, " +
                                    "unit TEXT NOT NULL, " +
                                    "date TEXT NOT NULL, " +
                                    "FOREIGN KEY(userId) REFERENCES users(id) " +
                                    "ON UPDATE NO ACTION ON DELETE CASCADE)"
                    );

                    db.execSQL(
                            "CREATE INDEX IF NOT EXISTS " +
                                    "index_calculations_userId " +
                                    "ON calculations(userId)"
                    );
                }
            };

    public static AppDatabase getInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {

                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "fieldlab_db"
                            )
                            .addMigrations(MIGRATION_2_3)
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
