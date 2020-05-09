package com.uetplus.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.uetplus.R;

public class ReminderBroadcast extends BroadcastReceiver {

    public static String tittle = "";
    public static String description = "";
    public static int id = 200;
    public static String channelId =  "HUAN TEST";
    public static int icon = R.drawable.ic_about;

    @Override
    public void onReceive(Context context, Intent intent){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(icon)
                .setContentTitle(tittle)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(id,builder.build());

    }

}
