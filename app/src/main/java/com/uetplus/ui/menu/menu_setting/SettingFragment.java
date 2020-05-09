package com.uetplus.ui.menu.menu_setting;


import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.uetplus.R;

import com.uetplus.ui.MainActivity;
import com.uetplus.ui.ReminderBroadcast;
import com.uetplus.ui.SaveSharedPreference;
import com.uetplus.ui.services.Api;
import com.uetplus.ui.services.Router;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    public String CHANNEL_ID = "HUAN TEST";

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Cài đặt");

        final View view = inflater.inflate(R.layout.fragment_setting, container, false);

        Switch timetableSwitch = view.findViewById(R.id.switch1);
        final Switch examtimeSwitch = view.findViewById(R.id.switch2);
        Switch gradesSwitch = view.findViewById(R.id.switch3);

        examtimeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked){
                    String mssv = SaveSharedPreference.getUserName(view.getContext());
                    Router service = Api.getRetrofitInstance().create(Router.class);
                    Call<List<List<String>>> call = service.getExamTime(mssv);
                    call.enqueue(new Callback<List<List<String>>>() {
                        @Override
                        public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
                            List<List<String>> list = response.body();
                            String startDateString = list.get(0).get(8);
                            long time_left = checkDaysLeft(startDateString);
                            String title = "Môn " + list.get(0).get(7);
                            String description = "Còn " + String.valueOf(time_left) + " ngày đến ngày thi :'D";
                            Intent intent1 = new Intent(view.getContext(), ReminderBroadcast.class);
                            createNotification(view,title, description,100,1, intent1);

                            String startDateString2 = list.get(0).get(8);
                            long time_left2 = checkDaysLeft(startDateString2);
                            String title2 = "Môn " + list.get(0).get(7);
                            String description2 = "Còn " + String.valueOf(time_left2) + " ngày đến ngày thi :'D";
                            Intent intent2 = new Intent(view.getContext(), ReminderBroadcast.class);
                            createNotification(view,title2, description2,1000,2,intent2);
                        }

                        @Override
                        public void onFailure(Call<List<List<String>>> call, Throwable t) {
                            examtimeSwitch.setChecked(false);
                        }
                    });



                }else {

                }
            }
        });


        return view;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public long checkDaysLeft(String startDateString){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = new Date();
        try {
            startDate = df.parse(startDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = startDate.getTime();
        Calendar now = Calendar.getInstance();
        long now_time = now.getTimeInMillis();
        long time_left = (time - now_time)/(60*60*24*1000);
        Log.v("Time", String.valueOf(time) + " " + String.valueOf(now_time) + " " + String.valueOf(time_left));
        return time_left;
    }

    public void createNotification(View view, String title, String description, long timeCount, int requestCode ,Intent intent){

        ReminderBroadcast.tittle = title;
        ReminderBroadcast.description = description;

        PendingIntent pendingIntent = PendingIntent.getBroadcast(view.getContext(),requestCode,intent,0);
        AlarmManager alarmManager  =(AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        long timeNow = System.currentTimeMillis();

        alarmManager.set(AlarmManager.RTC_WAKEUP,timeNow+timeCount,pendingIntent);
    }

}
