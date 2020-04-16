package com.example.uet_schedule.View;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uet_schedule.R;

public class ScheduleScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_screen);
        getSupportActionBar().hide(); //<< this
        TextView time = new TextView(this);
        time.setText("huantest");

        LinearLayout col0 = findViewById(R.id.col);
        col0.setBackgroundColor(Color.TRANSPARENT);
        time.setTextColor(Color.RED);
        col0.addView(time);

    }
}
