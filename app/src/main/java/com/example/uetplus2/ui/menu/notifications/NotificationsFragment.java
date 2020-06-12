package com.example.uetplus2.ui.menu.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.uetplus2.MainActivity;
import com.example.uetplus2.R;
import com.example.uetplus2.ui.Base;

public class NotificationsFragment extends Base {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Thông báo");
        return root;
    }
}