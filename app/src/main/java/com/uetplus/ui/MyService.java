package com.uetplus.ui;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        // START YOUR TASKS
        System.out.println("Start");
        Log.v("Task", "Start");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // STOP YOUR TASKS
        Log.v("Task", "Stop");
        super.onDestroy();
        System.out.println("Stop");
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}