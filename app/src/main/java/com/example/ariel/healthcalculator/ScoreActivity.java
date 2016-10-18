package com.example.ariel.healthcalculator;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Ariel on 10/18/2016.
 */

public class ScoreActivity extends AppCompatActivity {

    private TextView scoreTextView;
    private Button backButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_activity);

        scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        String score = getIntent().getStringExtra("score");
        scoreTextView.setText(score);
        backButton = (Button) findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
