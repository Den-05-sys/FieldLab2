package com.uac503.fieldlab.ui.simulator;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.uac503.fieldlab.data.repository.ScenarioRepository;
import com.uac503.fieldlab.model.Scenario;
import java.util.List;
import java.util.concurrent.Executors;

public class SimulatorViewModel extends AndroidViewModel {
    private ScenarioRepository repository;
    private MutableLiveData<List<Scenario>> allScenarios;

    public SimulatorViewModel(@NonNull Application application) {
        super(application);
        repository = new ScenarioRepository(application);
        allScenarios = new MutableLiveData<>();
        loadScenarios();
    }

    public void loadScenarios() {
        Executors.newSingleThreadExecutor().execute(() -> {
            allScenarios.postValue(repository.getAllScenarios());
        });
    }

    public LiveData<List<Scenario>> getAllScenarios() {
        return allScenarios;
    }

    public void insertScenario(Scenario scenario) {
        repository.insertScenario(scenario);
        loadScenarios();
    }
}
