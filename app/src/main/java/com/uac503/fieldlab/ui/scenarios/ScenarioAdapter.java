package com.uac503.fieldlab.ui.scenarios;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uac503.fieldlab.R;
import com.uac503.fieldlab.model.Scenario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScenarioAdapter extends RecyclerView.Adapter<ScenarioAdapter.ViewHolder> {

    private final List<Scenario> scenarios = new ArrayList<>();

    public void setScenarios(List<Scenario> scenarios) {
        this.scenarios.clear();
        if (scenarios != null) {
            this.scenarios.addAll(scenarios);
        }
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
        String chargeLabel = chargeCount == 1 ? "1 carga" : chargeCount + " cargas";
        holder.tvDetails.setText(chargeLabel + " · Emulación de campo");

        String dateCreated = scenario.getDateCreated();
        if ("Preset".equals(dateCreated)) {
            holder.tvDate.setText("Plantilla lista para explorar");
        } else {
            holder.tvDate.setText(formatDate(dateCreated));
        }
    }

    private String formatDate(String dateCreated) {
        if (dateCreated == null || dateCreated.trim().isEmpty()) {
            return "Fecha no disponible";
        }

        try {
            SimpleDateFormat storedFormat =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = storedFormat.parse(dateCreated);
            if (date == null) {
                return dateCreated;
            }

            SimpleDateFormat displayFormat =
                    new SimpleDateFormat("d MMM yyyy · HH:mm", Locale.forLanguageTag("es-CO"));
            return "Guardado el " + displayFormat.format(date);
        } catch (ParseException e) {
            return dateCreated;
        }
    }

    @Override
    public int getItemCount() {
        return scenarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName;
        final TextView tvDetails;
        final TextView tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_scenario_name);
            tvDetails = itemView.findViewById(R.id.tv_scenario_details);
            tvDate = itemView.findViewById(R.id.tv_scenario_date);
        }
    }
}
