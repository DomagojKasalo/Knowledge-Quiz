package com.example.seminar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button buttonStartQuiz, buttonLogout, buttonTopResults, buttonPerfectScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStartQuiz = findViewById(R.id.buttonStartQuiz);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonTopResults = findViewById(R.id.buttonTopResults);
        buttonPerfectScores = findViewById(R.id.buttonPerfectScores);

        buttonStartQuiz.setOnClickListener(v -> startQuiz());
        buttonLogout.setOnClickListener(v -> logoutUser());
        buttonTopResults.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TopResultsActivity.class);
            startActivity(intent);
        });
        buttonPerfectScores.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PerfectScoreActivity.class);
            startActivity(intent);
        });
    }

    private void startQuiz() {
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        startActivity(intent);
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}