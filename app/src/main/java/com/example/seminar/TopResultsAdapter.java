package com.example.seminar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seminar.QuizResult;
import com.example.seminar.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class TopResultsAdapter extends RecyclerView.Adapter<TopResultsAdapter.ViewHolder> {

    private List<QuizResult> resultsList;

    public TopResultsAdapter(List<QuizResult> resultsList) {
        this.resultsList = resultsList;
    }

    @NonNull
    @Override
    public TopResultsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_result, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuizResult result = resultsList.get(position);
        holder.textViewScore.setText(String.valueOf(result.getScore()));
        holder.textViewDate.setText(DateFormat.getDateTimeInstance().format(new Date(result.getTimestamp())));
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewScore, textViewDate;

        public ViewHolder(View view) {
            super(view);
            textViewScore = view.findViewById(R.id.textViewItemScore);
            textViewDate = view.findViewById(R.id.textViewItemDate);
        }
    }

}
