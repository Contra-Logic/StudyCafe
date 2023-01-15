package com.example.StudyCafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.myapplication.R;

public class RewardsPage extends AppCompatActivity {
    private String name;
    private int points = 0;

    //mutator to collect points
    public void collectPoints() {
        points = points + 1;

    }


    //accessor returns points
    public int getPoints() {
        return points;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards__page);

    }
}