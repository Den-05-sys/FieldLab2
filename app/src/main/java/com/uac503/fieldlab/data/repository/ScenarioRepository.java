package com.uac503.fieldlab.data.repository;

import android.app.Application;
import com.uac503.fieldlab.data.db.AppDatabase;
import com.uac503.fieldlab.data.db.ScenarioDao;
import com.uac503.fieldlab.model.Scenario;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScenarioRepository {
    private ScenarioDao scenarioDao;
    private ExecutorService executorService;

    public ScenarioRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        scenarioDao = db.scenarioDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public List<Scenario> getAllScenarios() {
        return scenarioDao.getAllScenarios();
    }

    public void insertScenario(Scenario scenario) {
        executorService.execute(() -> scenarioDao.insertScenario(scenario));
    }

    public void deleteScenario(int id) {
        executorService.execute(() -> scenarioDao.deleteScenario(id));
    }
}
