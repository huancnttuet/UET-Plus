package com.example.uethub.ui.menu.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.uethub.MainActivity;
import com.example.uethub.R;
import com.example.uethub.cache.SaveSharedPreference;
import com.example.uethub.models.NotificationModel;
import com.example.uethub.ui.Base;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class NotificationsFragment extends Base {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Thông báo");

        String data = SaveSharedPreference.getPrefNotify(root.getContext());
        if(data.length() != 0){
            Gson gson = new Gson();
            Type listType = new TypeToken<List<NotificationModel>>() {}.getType();
            List<NotificationModel> list = gson.fromJson(data, listType);
            ListView listView  = root.findViewById(R.id.notifications_list_view);
            NotificationsAdapter adapter = new NotificationsAdapter(root.getContext(),list);
            listView.setAdapter(adapter);
        }

        return root;
    }
}