package com.example.uethub.ui.menu.notifications;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.uethub.R;
import com.example.uethub.extensions.TimeAgo;
import com.example.uethub.models.NotificationModel;
import com.example.uethub.ui.components.PDFView.PDF;
import com.example.uethub.ui.components.examtime.ExamTimeFragment;
import com.example.uethub.ui.components.timetable.TimeTableFragment;
import com.example.uethub.ui.components.webview.WebViewFragment;
import com.google.common.collect.Lists;

import java.util.List;

public class NotificationsAdapter extends BaseAdapter {

    private Context context;
    private List<NotificationModel> notify_list;

    public NotificationsAdapter(Context context, List<NotificationModel> notify_list){
        this.context = context;
        this.notify_list = Lists.reverse(notify_list);
    }

    @Override
    public int getCount() {
        if(notify_list != null) return notify_list.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        @SuppressLint("ViewHolder") View root = inflater.inflate(R.layout.notification_item,parent,false);


        TextView title = root.findViewById(R.id.title);
        TextView message = root.findViewById(R.id.message);
        TextView created_at = root.findViewById(R.id.created_at);

        NotificationModel notify = notify_list.get(position);

        title.setText(notify.title);
        message.setText(notify.message);
        created_at.setText(TimeAgo.getTimeAgo(notify.created_at));

        CardView cardView = root.findViewById(R.id.notification_item);
        cardView.setOnClickListener(v -> {
            if(notify.action.equals("webview")){
                WebViewFragment fragment = new WebViewFragment();
                Bundle args = new Bundle();
                args.putString("url", notify.payload);
                fragment.setArguments(args);
                FragmentTransaction transaction = ((FragmentActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack("WEBVIEW_TAG");
                transaction.commit();
            } else if (notify.action.equals("timetable")){
                Fragment timetableFragment = new TimeTableFragment();
                FragmentTransaction transaction = ((FragmentActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, timetableFragment,"TIMETABLE_TAG");
                transaction.addToBackStack("TIMETABLE_TAG");
                transaction.commit();
            } else if(notify.action.equals("examtime")){
                Fragment fragment = new ExamTimeFragment();
                FragmentTransaction transaction = ((FragmentActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment,"EXAMTIME_TAG");
                transaction.addToBackStack("EXAMTIME_TAG");
                transaction.commit();
            } else if(notify.action.equals("grades")){
                FragmentTransaction transaction = ((FragmentActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new PDF(notify.payload));
                transaction.addToBackStack("PDF");
                transaction.commit();
            }
        });

        return root;
    }
}
