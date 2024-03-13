package com.example.seminar;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PerfectScoreActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PerfectScoreAdapter adapter;
    private Set<String> perfectScoreUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfect_score_activity);

        recyclerView = findViewById(R.id.recyclerViewPerfectScores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        perfectScoreUsers = new HashSet<>();
        adapter = new PerfectScoreAdapter(new ArrayList<>(perfectScoreUsers));
        recyclerView.setAdapter(adapter);

        loadPerfectScores();
    }

    private void loadPerfectScores() {
        DatabaseReference resultsRef = FirebaseDatabase.getInstance().getReference("results");
        Query query = resultsRef.orderByChild("score").equalTo(100);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userId = snapshot.child("userId").getValue(String.class);
                    if (userId != null && !perfectScoreUsers.contains(userId)) {
                        perfectScoreUsers.add(userId);
                        adapter.updateList(new ArrayList<>(perfectScoreUsers));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
