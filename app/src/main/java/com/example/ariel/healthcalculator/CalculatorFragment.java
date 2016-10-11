package com.example.ariel.healthcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

/**
 * Created by Ariel on 9/7/2016.
 */
public class CalculatorFragment extends Fragment {

    RatingBar sleepRating,dietRating,exerciseRating,socialrating,stressRating;
    Button nextButton;

    float sleepRatingScore,dietRatingScore, exerciseRatingScore, socialRatingScore, stressRatingScore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fragmenttab1.xml
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);
        sleepRating = (RatingBar) view.findViewById(R.id.sleepRating);
        dietRating = (RatingBar) view.findViewById(R.id.dietRating);
        exerciseRating = (RatingBar) view.findViewById(R.id.exerciseRating);
        socialrating = (RatingBar) view.findViewById(R.id.socialRating);
        stressRating = (RatingBar) view.findViewById(R.id.stressRating);
        nextButton = (Button) view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sleepRatingScore = sleepRating.getRating();
                dietRatingScore = dietRating.getRating();
                exerciseRatingScore = exerciseRating.getRating();
                socialRatingScore = socialrating.getRating();
                stressRatingScore = stressRating.getRating();

                Intent intent = new Intent(getContext(),CalcPage2.class);
                intent.putExtra("sleep",sleepRatingScore);
                intent.putExtra("diet",dietRatingScore);
                intent.putExtra("exercise",exerciseRatingScore);
                intent.putExtra("social",socialRatingScore);
                intent.putExtra("stress",stressRatingScore);

                startActivity(intent);
            }
        });


        return view;
    }
}
