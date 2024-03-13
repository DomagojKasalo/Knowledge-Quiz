package com.example.seminar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PerfectScoreAdapter extends RecyclerView.Adapter<PerfectScoreAdapter.ViewHolder> {
    private List<String> userEmails;

    public PerfectScoreAdapter(List<String> userEmails) {
        this.userEmails = userEmails;
    }

    public void updateList(List<String> newUsers) {
        userEmails.clear();
        userEmails.addAll(newUsers);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.perfect_score_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String userId = userEmails.get(position);
        holder.textViewEmail.setText(userId);
    }

    @Override
    public int getItemCount() {
        return userEmails.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEmail;

        ViewHolder(View view) {
            super(view);
            textViewEmail = view.findViewById(R.id.textViewPlayerName);
        }
    }
}
