package com.example.uetplus2;

import android.app.Application;
import android.util.Log;

import com.example.uetplus2.models.TimeTable;

import java.util.List;

public class App extends Application {

    private List<TimeTable> timeTableList;

    public List<TimeTable> getTimeTableList() {
        return timeTableList;
    }

    public void setTimeTableList(List<TimeTable> timeTableList) {
        this.timeTableList = timeTableList;
    }

    @Override
    public void onCreate() {
        Log.v("UET Plus App", "App Started");
        super.onCreate();
    }
}
