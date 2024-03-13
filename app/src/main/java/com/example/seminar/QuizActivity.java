package com.example.seminar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.seminar.Question;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView textViewQuestion, textViewTimer, textViewScore, textViewCongrats;
    private Button[] answerButtons = new Button[4];
    private List<Question> questionList;
    private Question currentQuestion;
    private int score = 0;
    private final long START_TIME_IN_MILLIS = 30000;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = START_TIME_IN_MILLIS;
    private int currentQuestionIndex = 0;
    private boolean quizFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewCongrats = findViewById(R.id.textViewCongrats);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewScore = findViewById(R.id.textViewScore);
        answerButtons[0] = findViewById(R.id.buttonAnswer1);
        answerButtons[1] = findViewById(R.id.buttonAnswer2);
        answerButtons[2] = findViewById(R.id.buttonAnswer3);
        answerButtons[3] = findViewById(R.id.buttonAnswer4);

        questionList = new ArrayList<>();

        startTimer();

        if (savedInstanceState != null) {
            currentQuestionIndex = savedInstanceState.getInt("CurrentQuestionIndex");
            timeLeftInMillis = savedInstanceState.getLong("TimeLeft");
            score = savedInstanceState.getInt("Score");
            loadQuestionsFromFirebase();
        } else {
            loadQuestionsFromFirebase();
            startTimer();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CurrentQuestionIndex", currentQuestionIndex);
        outState.putLong("TimeLeft", timeLeftInMillis);
        outState.putInt("Score", score);
    }


    private void loadQuestionsFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference questionsRef = database.getReference("questions");

        questionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                questionList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Question question = snapshot.getValue(Question.class);
                    questionList.add(question);
                }
                nextQuestion();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(QuizActivity.this, "Error loading questions: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void nextQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            currentQuestion = questionList.get(currentQuestionIndex++);
            textViewQuestion.setText(currentQuestion.getQuestion());
            for (int i = 0; i < answerButtons.length; i++) {
                answerButtons[i].setText(currentQuestion.getAnswers().get(i));
                answerButtons[i].setEnabled(true);
                int answerIndex = i;
                answerButtons[i].setOnClickListener(v -> checkAnswer(answerIndex));
            }
            updateCountDownText();
        } else {
            finishQuiz();
        }
    }

    private void checkAnswer(int answerIndex) {
        if (answerIndex == currentQuestion.getCorrectAnswer()) {
            score += 10;
            textViewScore.setText("Score: " + score);
        } else {
            score -= 5;
            if (score < 0) {
                score = 0;
            }
            textViewScore.setText("Score: " + score);
        }
        nextQuestion();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                finishQuiz();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        textViewTimer.setText(timeFormatted);
    }

    private void finishQuiz() {
        if (quizFinished) {
            return;
        }
        quizFinished = true;
        for (Button button : answerButtons) {
            button.setVisibility(View.GONE);
        }
        textViewQuestion.setVisibility(View.GONE);
        textViewTimer.setVisibility(View.GONE);
        textViewScore.setVisibility(View.GONE);

        textViewCongrats.setVisibility(View.VISIBLE);
        textViewCongrats.setText("Čestitamo!");

        Animation celebrationAnimation = AnimationUtils.loadAnimation(this, R.anim.celebration_animation);
        textViewCongrats.startAnimation(celebrationAnimation);

        new Handler().postDelayed(() -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String currentUserId = user.getUid();
                QuizResult result = new QuizResult(currentUserId, score);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference resultsRef = database.getReference("results");
                resultsRef.push().setValue(result).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(QuizActivity.this, "Error saving result", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            }
        }, 5000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}