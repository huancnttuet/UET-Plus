package com.example.uet_schedule.View;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.uet_schedule.R;

import static com.example.uet_schedule.View.SplashScreen.List_All_Subject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int i = 0; i<List_All_Subject.size();i++){
            Log.d("CodeFull Subject: ", List_All_Subject.get(i).getCodeFull() + " " + i);
        }

    }

}
