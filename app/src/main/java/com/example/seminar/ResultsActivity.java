package com.example.seminar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewResults;
    private ResultAdapter adapter;
    private List<QuizResult> resultList;
    private Button buttonPlayAgain, buttonExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        recyclerViewResults = findViewById(R.id.recyclerViewResults);
        buttonPlayAgain = findViewById(R.id.buttonPlayAgain);
        buttonExit = findViewById(R.id.buttonExit);

        resultList = new ArrayList<>();
        adapter = new ResultAdapter(resultList);
        recyclerViewResults.setAdapter(adapter);
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));

        initializeFirebaseResults();

        buttonPlayAgain.setOnClickListener(v -> {
            Intent playIntent = new Intent(ResultsActivity.this, MainActivity.class);
            startActivity(playIntent);
        });

        buttonExit.setOnClickListener(v -> {
            finishAffinity();
        });
    }

    private void initializeFirebaseResults() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        if (currentUserId == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        DatabaseReference resultsRef = FirebaseDatabase.getInstance().getReference("results");
        resultsRef.orderByChild("userId").equalTo(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resultList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    QuizResult result = snapshot.getValue(QuizResult.class);
                    resultList.add(result);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ResultsActivity.this, "Error loading results: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
