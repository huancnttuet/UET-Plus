package com.example.uethub.ui.menu.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.uethub.MainActivity;
import com.example.uethub.R;
import com.example.uethub.cache.SaveSharedPreference;

import com.example.uethub.firebase.MyDeviceID;
import com.example.uethub.ui.Base;

public class SettingFragment extends Base {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Cài đặt");
        Switch switch1 = view.findViewById(R.id.switch_notify);
        String notify_cache = SaveSharedPreference.getCache(view.getContext(),"setting_notify");
        if(notify_cache != null && notify_cache.equals("1")){
            switch1.setChecked(true);
        } else {
            switch1.setChecked(false);
        }
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                MyDeviceID.sendToken(view);
            } else {
                MyDeviceID.deleteMyDeviceID(view);
            }
        });

        return view;
    }
}
