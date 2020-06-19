package com.example.uethub.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.uethub.App;
import com.example.uethub.models.TimeTable;

import java.util.List;

public class Base extends Fragment {
    public App app;
    public List<TimeTable> timeTableList;
    public List<List<String>> hotNews,studentNews;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app =(App) getActivity().getApplication();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
