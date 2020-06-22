package com.example.uethub.ui.components.timetable;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.uethub.MainActivity;
import com.example.uethub.R;
import com.example.uethub.cache.SaveSharedPreference;
import com.example.uethub.models.TimeTable;
import com.example.uethub.services.timetable.GetTimeTableByMssv;
import com.example.uethub.ui.Base;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class TimeTableFragment extends Base {

    public View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Thời khóa biểu");
        root = inflater.inflate(R.layout.fragment_time_table, container, false);


        final String mssv = SaveSharedPreference.getCache(getActivity(),"username");
//        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.pullToRefresh);
//        pullToRefresh.setColorSchemeResources(R.color.calendar_today,R.color.colorPrimary,R.color.colorLightRed);
//        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshData(mssv);
//                pullToRefresh.setRefreshing(false);
//            }
//        });

        String timetableCache = SaveSharedPreference.getCache(getActivity(), "timetable");
        if (timetableCache.length() != 0) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<TimeTable>>() {
            }.getType();
            List<TimeTable> list = gson.fromJson(timetableCache, listType);
//            draw(root,list);
            drawTimeTable(list);
        } else {
            refreshData(mssv);
        }

        //fullscreen
        ImageView btn_fullscreen = (ImageView) root.findViewById(R.id.timetable_fullscreen);
        btn_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FullScreenTimeTable.class);
                startActivity(intent);
            }
        });

        return root;
    }


    public void refreshData(String mssv) {
        new GetTimeTableByMssv(getContext(), new GetTimeTableByMssv.AsyncResponse() {
            @Override
            public void processFinish(List<TimeTable> output) {
                if (output != null) {
//                    draw(root, output);
                    drawTimeTable(output);
                    Gson gson = new Gson();
                    String value = gson.toJson(output);
                    SaveSharedPreference.setCache(getActivity(), "timetable", value);

                } else {
                    Log.d("Notify", "Failed");
                    openSortDialog(root, R.layout.dialog_error);
                    return;
                }
            }
        }).execute("/schedule/test", mssv);
    }

    public void openSortDialog(View root, int type) {
        root.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        final Dialog message = new Dialog(root.getContext());
        message.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        message.requestWindowFeature(Window.FEATURE_NO_TITLE);
        message.setContentView(type);
        message.show();
        Button cancel = message.findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.dismiss();
            }
        });
    }

//    public void draw(View root, List<TimeTable> List_All_Subject){
//        LinearLayout mon = root.findViewById(R.id.monday);
//        LinearLayout tue = root.findViewById(R.id.tuesday);
//        LinearLayout wed = root.findViewById(R.id.wednesday);
//        LinearLayout thu = root.findViewById(R.id.thursday);
//        LinearLayout fri = root.findViewById(R.id.friday);
//        LinearLayout sat = root.findViewById(R.id.saturday);
//        LinearLayout sun = root.findViewById(R.id.sunday);
//        mon.removeAllViews();
//        tue.removeAllViews();
//        wed.removeAllViews();
//        thu.removeAllViews();
//        fri.removeAllViews();
//        sat.removeAllViews();
//        sun.removeAllViews();
//        Button[] list_button = new Button[50];
//        int k = 0;
//        for(TimeTable subject : List_All_Subject){
//
//            list_button[k]= new Button(getContext());
//            list_button[k].setId(k);
//            list_button[k].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
//            list_button[k].setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
//            list_button[k].setText(subject.getSubject_name());
//            if(subject.getDay_week().equals("2")){
//                mon.addView(list_button[k]);
//            }
//            if(subject.getDay_week().equals("3")){
//                tue.addView(list_button[k]);
//            }
//            if(subject.getDay_week().equals("4")){
//                wed.addView(list_button[k]);
//            }
//            if(subject.getDay_week().equals("5")){
//                thu.addView(list_button[k]);
//            }
//            if(subject.getDay_week().equals("6")){
//                fri.addView(list_button[k]);
//            }
//            if(subject.getDay_week().equals("7")){
//                sat.addView(list_button[k]);
//            }
//            if(subject.getDay_week().equals("CN")){
//                sun.addView(list_button[k]);
//            }
//            k++;
//        }
//
//        for(int i=0; i < k;i++){
//            setOnClick(list_button[i], List_All_Subject);
//        }
//    }

    public void setOnClick(TextView btn, final List<TimeTable> l, final int i) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.v("click", String.valueOf(v.getId()));
                TimeTable subject = l.get(i);
//                Log.d("subject: ",subject.subject_name);
                openDialog(subject);
            }
        });
    }

    public void openDialog(TimeTable subject) {
        final Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_time_table);

        TextView class_name, class_code, teacher, student_total, lession, classroom;
        class_name = dialog.findViewById(R.id.class_name);
        class_code = dialog.findViewById(R.id.class_code);
        teacher = dialog.findViewById(R.id.teacher);
        student_total = dialog.findViewById(R.id.student_total);
        lession = dialog.findViewById(R.id.lession);
        classroom = dialog.findViewById(R.id.classroom);
        class_name.setText(subject.getSubject_name());
        class_code.setText(subject.getSubject_code());
        teacher.setText(subject.getTeacher());
        student_total.setText(subject.getStudent_total() + " sinh viên");
        String[] time = subject.getLesson().split("-");
        lession.setText("Thời gian: " + convertTime(Integer.parseInt(time[0])) + " - " + convertTime(Integer.parseInt(time[1]) + 1));
        classroom.setText("Địa điểm: " + subject.getRoom());
        dialog.show();
        Button exit_btn = dialog.findViewById(R.id.exit_btn);
        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //change to fragment notification
        Button btn_notification = (Button) dialog.findViewById(R.id.btn_set_notification);
        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                intent.putExtra(AlarmClock.EXTRA_HOUR, Integer.parseInt(time[0]) + 6 - 1);
                intent.putExtra(AlarmClock.EXTRA_MINUTES, 30);
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, "Đến giờ đi học " + subject.subject_name + " rồi!!");
                startActivity(intent);
            }
        });

    }

    public void drawTimeTable(List<TimeTable> list) {

        List<LinearLayout> listWeeks = new ArrayList<>();
        listWeeks.add((LinearLayout) root.findViewById(R.id.monday));
        listWeeks.add((LinearLayout) root.findViewById(R.id.tuesday));
        listWeeks.add((LinearLayout) root.findViewById(R.id.wednesday));
        listWeeks.add((LinearLayout) root.findViewById(R.id.thursday));
        listWeeks.add((LinearLayout) root.findViewById(R.id.friday));
        listWeeks.add((LinearLayout) root.findViewById(R.id.saturday));
        listWeeks.add((LinearLayout) root.findViewById(R.id.sunday));

        for (int i = 0; i < listWeeks.size(); i++) {
            listWeeks.get(i).removeAllViews();
        }

        for (int i = 0; i < list.size(); i++) {
            TextView textView = new TextView(root.getContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            String start = Integer.parseInt(list.get(i).lesson.split("-")[0]) + 6 + "h";
            String end = Integer.parseInt(list.get(i).lesson.split("-")[1]) + 7 + "h";
            textView.setText(list.get(i).subject_name + "\n" + start + "-" + end + "\n" + list.get(i).room + "\n" + "Nhóm: " + list.get(i).note);
            textView.setBackground(ContextCompat.getDrawable(textView.getContext(), R.drawable.border_subjects));
            textView.setGravity(Gravity.CENTER);
            int index = Integer.parseInt(list.get(i).day_week) - 2;
            setOnClick(textView, list, i);
            listWeeks.get(index).addView(textView);
        }

    }

    public String convertTime(int lession) {
        switch (lession) {
            case 1:
                return "7h";
            case 2:
                return "8h";
            case 3:
                return "9h";
            case 4:
                return "10h";
            case 5:
                return "11h";
            case 6:
                return "12h";
            case 7:
                return "13h";
            case 8:
                return "14h";
            case 9:
                return "15h";
            case 10:
                return "16h";
            case 11:
                return "17h";
            case 12:
                return "18h";
            case 13:
                return "19h";
        }
        return "0h";
    }
}
