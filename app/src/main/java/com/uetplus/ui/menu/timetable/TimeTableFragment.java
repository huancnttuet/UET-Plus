package com.uetplus.ui.menu.timetable;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uetplus.R;

import com.uetplus.ui.MainActivity;
import com.uetplus.ui.SaveSharedPreference;
import com.uetplus.ui.services.Api;
import com.uetplus.ui.services.Router;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeTableFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Thời khóa biểu");
        final View root = inflater.inflate(R.layout.fragment_time_table, container, false);

        String timetableCache = SaveSharedPreference.getCache(getActivity(),"timetable");
        if(timetableCache.length() != 0){
            Gson gson = new Gson();
            Type listType = new TypeToken<List<List<String>>>() {}.getType();
            List<List<String>> list = gson.fromJson(timetableCache,listType);
            root.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            draw(root,list);

        } else{
            Router service = Api.getRetrofitInstance().create(Router.class);
            String mssv = SaveSharedPreference.getUserName(getActivity());
            Log.v("Cache",mssv);
            Call<List<List<String>>> call = service.getTimeTable(mssv);
            call.enqueue(new Callback<List<List<String>>>() {
                @Override
                public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
                    Log.d("Notify", "Success");
                    root.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    List<List<String>> List_All_Subject = response.body();
                    if(List_All_Subject == null || List_All_Subject.size() == 0){
                        Log.d("Notify", "Not Success");
                        openSortDialog(root,R.layout.dialog_error);
                        return;
                    } else {
                        draw(root,List_All_Subject);
                        Gson gson = new Gson();
                        String value = gson.toJson(List_All_Subject);
                        SaveSharedPreference.setCache(getActivity(),"timetable",value);
                    }
                }

                @Override
                public void onFailure(Call<List<List<String>>> call, Throwable t) {
                    Log.d("Notify", "Failed");
                    openSortDialog(root,R.layout.dialog_error);
                    return;
                }
            });
        }


        return root;
    }


    public void openSortDialog(View root, int type ){
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

    public void draw(View root, List<List<String>> List_All_Subject){
        LinearLayout mon = root.findViewById(R.id.monday);
        LinearLayout tue = root.findViewById(R.id.tuesday);
        LinearLayout wed = root.findViewById(R.id.wednesday);
        LinearLayout thu = root.findViewById(R.id.thursday);
        LinearLayout fri = root.findViewById(R.id.friday);
        LinearLayout sat = root.findViewById(R.id.saturday);
        LinearLayout sun = root.findViewById(R.id.sunday);
        Button[] list_button = new Button[50];
        int k = 0;
        for(List<String> subject : List_All_Subject){

            list_button[k]= new Button(getContext());
            list_button[k].setId(k);
            list_button[k].setTextSize(9);
            list_button[k].setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
            list_button[k].setText(subject.get(0));
            if(subject.get(6).equals("2")){
                mon.addView(list_button[k]);
            }
            if(subject.get(6).equals("3")){
                tue.addView(list_button[k]);
            }
            if(subject.get(6).equals("4")){
                wed.addView(list_button[k]);
            }
            if(subject.get(6).equals("5")){
                thu.addView(list_button[k]);
            }
            if(subject.get(6).equals("6")){
                fri.addView(list_button[k]);
            }
            if(subject.get(6).equals("7")){
                sat.addView(list_button[k]);
            }
            if(subject.get(6).equals("CN")){
                sun.addView(list_button[k]);
            }
            k++;
        }
        List<String> subject = List_All_Subject.get(1);

        for(int i=0; i < k;i++){
            setOnClick(list_button[i], List_All_Subject);
        }
    }

    public void setOnClick(Button btn, final List<List<String>> l){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("click", String.valueOf(v.getId()));
                List<String> subject = l.get(v.getId());
                openDialog(subject);
            }
        });
    }

    public void openDialog(List<String> subject) {
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
        class_name.setText(subject.get(0));
        class_code.setText(subject.get(2));
        teacher.setText(subject.get(3));
        student_total.setText(subject.get(4) + " sinh viên");
        String[] time = subject.get(7).split("-");
        lession.setText("Thời gian: " + convertTime(Integer.parseInt(time[0])) + " - " + convertTime(Integer.parseInt(time[1])+1));
        classroom.setText("Địa điểm: " + subject.get(8));
        dialog.show();
        Button exit_btn = dialog.findViewById(R.id.exit_btn);
        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public String convertTime(int lession){
        switch (lession){
            case 1:
                return "7h";
            case 2:
                return "8h";
            case 3:
                return  "9h";
            case 4:
                return  "10h";
            case 5:
                return  "11h";
            case 6:
                return  "12h";
            case 7:
                return  "13h";
            case 8:
                return  "14h";
            case 9:
                return  "15h";
            case 10:
                return  "16h";
            case 11:
                return  "17h";
            case 12:
                return  "18h";
            case 13:
                return  "19h";
        }
        return "0h";
    }
}
