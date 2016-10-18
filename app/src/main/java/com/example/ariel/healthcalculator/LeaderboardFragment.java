package com.example.ariel.healthcalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Ariel on 9/7/2016.
 */
public class LeaderboardFragment extends Fragment {

    ListView listView;
    ArrayList<String> scores;
    String myScore;
    String firstName;
    ArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        scores = new ArrayList<>();
        scores.add("Ariel Abramov : 25");
        scores.add("Tomer Abramov : 50");
        Singleton.getInstance().setScores(scores);


        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        listView = (ListView) view.findViewById(R.id.list_view);
        Singleton.getInstance().setListView(listView);
        adapter = new ArrayAdapter(getContext(),R.layout.leaderboard_list_item,Singleton.getInstance().getScores());
        listView.setAdapter(adapter);


        return view;
    }


}
