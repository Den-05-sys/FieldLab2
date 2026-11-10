package com.uac503.fieldlab.ui.theory;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.uac503.fieldlab.R;
import com.uac503.fieldlab.model.TheoryTopic;


import java.util.List;


public class TheoryAdapter extends RecyclerView.Adapter<TheoryAdapter.TheoryViewHolder> {


    private final List<TheoryTopic> topicList;


    public TheoryAdapter(List<TheoryTopic> topicList) {
        this.topicList = topicList;
    }


    @NonNull
    @Override
    public TheoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theory_topic, parent, false);
        return new TheoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TheoryViewHolder holder, int position) {
        TheoryTopic topic = topicList.get(position);


        holder.tvNumber.setText(String.valueOf(topic.getId()));
        holder.tvTitle.setText(topic.getTitle());
        holder.tvLocation.setText(topic.getCategoryLocation());
        holder.tvContent.setText(topic.getContentText());


        if (topic.getFormulaText() != null && !topic.getFormulaText().isEmpty()) {
            holder.tvFormula.setText(topic.getFormulaText());
            holder.tvFormula.setVisibility(View.VISIBLE);
        } else {
            holder.tvFormula.setVisibility(View.GONE);
        }


        // Alternar visibilidad desplegable
        boolean isExpanded = topic.isExpanded();
        holder.layoutDetails.setVisibility(isExpanded ? View.VISIBLE : View.GONE);


        holder.itemView.setOnClickListener(v -> {
            topic.setExpanded(!topic.isExpanded());
            notifyItemChanged(position);
        });
    }


    @Override
    public int getItemCount() {
        return topicList.size();
    }


    public static class TheoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber, tvTitle, tvLocation, tvContent, tvFormula;
        LinearLayout layoutDetails;


        public TheoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tv_topic_number);
            tvTitle = itemView.findViewById(R.id.tv_topic_title);
            tvLocation = itemView.findViewById(R.id.tv_topic_location);
            tvContent = itemView.findViewById(R.id.tv_topic_content);
            tvFormula = itemView.findViewById(R.id.tv_topic_formula);
            layoutDetails = itemView.findViewById(R.id.layout_details);
        }
    }
}