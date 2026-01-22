package com.example.mentalhealthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoreActivity extends AppCompatActivity {

    TextView tv3;
    Button done;
    QuizDetails quiz;
    QuizDatabase quizDb;
    TextView tvScore;
    Dialog dialog;

    ProgressBar pbc1, pbc2, pbc3, pbc4, pbc5, pbc6, pbc7, pbc8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Intent intent = getIntent();
        String score = intent.getStringExtra(QuizActivity.EXTRA_SCORE),
                score0 = intent.getStringExtra(QuizActivity.EXTRA_SCORE_0),
                score1 = intent.getStringExtra(QuizActivity.EXTRA_SCORE_1),
                score2 = intent.getStringExtra(QuizActivity.EXTRA_SCORE_2),
                score3 = intent.getStringExtra(QuizActivity.EXTRA_SCORE_3),
                score4 = intent.getStringExtra(QuizActivity.EXTRA_SCORE_4),
                score5 = intent.getStringExtra(QuizActivity.EXTRA_SCORE_5),
                score6 = intent.getStringExtra(QuizActivity.EXTRA_SCORE_6),
                score7 = intent.getStringExtra(QuizActivity.EXTRA_SCORE_7);

        int sc = Integer.parseInt(score);
        float sc1 = Integer.parseInt(score0);
        float sc2 = Integer.parseInt(score1);
        float sc3 = Integer.parseInt(score2);
        float sc4 = Integer.parseInt(score3);
        float sc5 = Integer.parseInt(score4);
        float sc6 = Integer.parseInt(score5);
        float sc7 = Integer.parseInt(score6);
        float sc8 = Integer.parseInt(score7);
        float[] arr = {sc1, sc2, sc3, sc4, sc5, sc6, sc7, sc8};

        Date date = new Date();
        DateFormat dtf = new SimpleDateFormat("yyyy/MM/dd");
        String x = dtf.format(date);

        quiz = new QuizDetails(sc, arr, getDateHash(x));
        quizDb = new QuizDatabase(ScoreActivity.this);
        quizDb.addNewQuiz(quiz);

        pbc1 = findViewById(R.id.pbc1);
        pbc2 = findViewById(R.id.pbc2);
        pbc3 = findViewById(R.id.pbc3);
        pbc4 = findViewById(R.id.pbc4);
        pbc5 = findViewById(R.id.pbc5);
        pbc6 = findViewById(R.id.pbc6);
        pbc7 = findViewById(R.id.pbc7);
        pbc8 = findViewById(R.id.pbc8);

        tvScore = findViewById(R.id.txtScoreTotal);
        dialog = new Dialog(ScoreActivity.this);

        int scp1 = ((int) sc1) * 100 / 12;
        int scp2 = ((int) sc2) * 100 / 12;
        int scp3 = ((int) sc3) * 100 / 12;
        int scp4 = ((int) sc4) * 100 / 12;
        int scp5 = ((int) sc5) * 100 / 12;
        int scp6 = ((int) sc6) * 100 / 12;
        int scp7 = ((int) sc7) * 100 / 16;
        int scp8 = ((int) sc8) * 100 / 12;

        pbc1.setProgress(scp1);
        pbc2.setProgress(scp2);
        pbc3.setProgress(scp3);
        pbc4.setProgress(scp4);
        pbc5.setProgress(scp5);
        pbc6.setProgress(scp6);
        pbc7.setProgress(scp7);
        pbc8.setProgress(scp8);

        done = findViewById(R.id.done);
        tv3 = findViewById(R.id.tv3);

        String status;
        if (sc < 20) status = "Very Low";
        else if (sc < 40) status = "Low";
        else if (sc < 60) status = "Moderate";
        else if (sc < 80) status = "High";
        else status = "Very High";

        tv3.setText(status);
        tvScore.setText(String.valueOf(sc));

        done.setOnClickListener(view -> {
            Intent i = new Intent(ScoreActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });
    }

    private int getDateHash(String x) {
        try {
            int day = Integer.parseInt(x.substring(8, 10));
            int month = Integer.parseInt(x.substring(5, 7));
            int year = Integer.parseInt(x.substring(0, 4));
            return year * 31 * 12 + month * 31 + day;
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.explanation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.Explanation) {

            Button btnClose;
            dialog.setContentView(R.layout.score_description);
            dialog.show();
            btnClose = dialog.findViewById(R.id.close2);

            btnClose.setOnClickListener(v -> dialog.dismiss());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
