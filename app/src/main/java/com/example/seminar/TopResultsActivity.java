package com.example.seminar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TopResultsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewTopResults;
    private TopResultsAdapter adapter;
    private List<QuizResult> topResultsList;
    private Button buttonReturnToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_results);

        recyclerViewTopResults = findViewById(R.id.recyclerViewTopResults);
        buttonReturnToMain = findViewById(R.id.buttonReturnToMain);
        topResultsList = new ArrayList<>();
        adapter = new TopResultsAdapter(topResultsList);
        recyclerViewTopResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTopResults.setAdapter(adapter);

        loadTopResults();

        buttonReturnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadTopResults() {
        DatabaseReference resultsRef = FirebaseDatabase.getInstance().getReference("results");
        Query topResultsQuery = resultsRef.orderByChild("score").limitToLast(10);

        topResultsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                topResultsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    QuizResult result = snapshot.getValue(QuizResult.class);
                    if (result != null) {
                        topResultsList.add(result);
                    }
                }
                Collections.reverse(topResultsList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(TopResultsActivity.this, "Failed to load top results.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static class TopResultsAdapter extends RecyclerView.Adapter<TopResultsAdapter.ViewHolder> {
        private List<QuizResult> resultsList;

        public TopResultsAdapter(List<QuizResult> resultsList) {
            this.resultsList = resultsList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_result, parent, false);
            return new ViewHolder(view);
        }

        @Override
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
}

