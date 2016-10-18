package com.example.ariel.healthcalculator;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Ariel on 10/17/2016.
 */
public class Singleton {
    private static Singleton ourInstance = new Singleton();

    private String firstName;
    private String yourScore;

    private ListView listView;
    private ArrayList scores;

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {


    }


    public ArrayList getScores() {
        return scores;
    }

    public void setScores(ArrayList scores) {
        this.scores = scores;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getYourScore() {
        return yourScore;
    }

    public void setYourScore(String yourScore) {
        this.yourScore = yourScore;

    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

}
