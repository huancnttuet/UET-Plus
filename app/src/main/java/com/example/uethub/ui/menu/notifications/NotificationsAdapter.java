package com.example.uethub.ui.menu.notifications;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uethub.R;
import com.example.uethub.extensions.TimeAgo;
import com.example.uethub.models.NotificationModel;
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


        return root;
    }
}
