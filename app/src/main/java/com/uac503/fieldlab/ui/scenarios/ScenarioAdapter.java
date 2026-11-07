package com.uac503.fieldlab.ui.scenarios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.uac503.fieldlab.R;
import com.uac503.fieldlab.model.Scenario;
import java.util.ArrayList;
import java.util.List;

public class ScenarioAdapter extends RecyclerView.Adapter<ScenarioAdapter.ViewHolder> {

    private List<Scenario> scenarios = new ArrayList<>();

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios != null ? scenarios : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scenario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Scenario scenario = scenarios.get(position);
        holder.tvName.setText(scenario.getName());
        int chargeCount = scenario.getCharges() != null ? scenario.getCharges().size() : 0;
        holder.tvDetails.setText("Cargas configuradas: " + chargeCount + " | Creado: " + scenario.getDateCreated());
    }

    @Override
    public int getItemCount() {
        return scenarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_scenario_name);
            tvDetails = itemView.findViewById(R.id.tv_scenario_details);
        }
    }
}
