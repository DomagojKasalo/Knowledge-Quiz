package com.example.seminar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private List<QuizResult> resultsList;

    public ResultAdapter(List<QuizResult> resultsList) {
        this.resultsList = resultsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuizResult result = resultsList.get(position);
        holder.textViewScore.setText("Score: " + result.getScore());
        holder.textViewDate.setText("Date: " + DateFormat.getDateTimeInstance().format(new Date(result.getTimestamp())));
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewScore, textViewDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewScore = itemView.findViewById(R.id.textViewScore);
            textViewDate = itemView.findViewById(R.id.textViewDate);
        }
    }
}

