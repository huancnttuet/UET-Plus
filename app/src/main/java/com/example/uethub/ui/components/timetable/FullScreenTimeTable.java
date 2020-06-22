package com.example.uethub.ui.components.timetable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uethub.R;
import com.example.uethub.cache.SaveSharedPreference;
import com.example.uethub.models.TimeTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FullScreenTimeTable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final String mssv = SaveSharedPreference.getCache(this,"username");

        String timetableCache = SaveSharedPreference.getCache(this,"timetable");
        if(timetableCache.length() != 0){
            Gson gson = new Gson();
            Type listType = new TypeToken<List<TimeTable>>() {}.getType();
            List<TimeTable> list = gson.fromJson(timetableCache,listType);
            drawTimeTable(list);
        }

        ImageView exit_fullscreen = (ImageView) this.findViewById(R.id.exit_fullscreen);
        exit_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void drawTimeTable(List<TimeTable> list) {
        List<LinearLayout> listWeeks = new ArrayList<>();
        listWeeks.add((LinearLayout) this.findViewById(R.id.mon));
        listWeeks.add((LinearLayout) this.findViewById(R.id.tue));
        listWeeks.add((LinearLayout) this.findViewById(R.id.wed));
        listWeeks.add((LinearLayout) this.findViewById(R.id.thu));
        listWeeks.add((LinearLayout) this.findViewById(R.id.fri));
        listWeeks.add((LinearLayout) this.findViewById(R.id.sat));
        listWeeks.add((LinearLayout) this.findViewById(R.id.sun));

        for (int i = 0; i < list.size(); i++) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            String start = Integer.parseInt(list.get(i).lesson.split("-")[0]) + 6 + "h";
            String end = Integer.parseInt(list.get(i).lesson.split("-")[1]) + 7 + "h";
            textView.setText(list.get(i).subject_name + "\n" + start + "-" + end + "\n" + list.get(i).room + "\n" + list.get(i).note);
            textView.setBackground(ContextCompat.getDrawable(textView.getContext(), R.drawable.border_subjects));
            textView.setGravity(Gravity.CENTER);
            int index = Integer.parseInt(list.get(i).day_week) - 2;
//            setOnClick(textView,i,list);
            listWeeks.get(index).addView(textView);
        }

    }
}