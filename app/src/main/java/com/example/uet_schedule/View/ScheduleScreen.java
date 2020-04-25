package com.example.uet_schedule.View;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uet_schedule.Presenter.I_SubjectGetDataService;
import com.example.uet_schedule.Presenter.SubjectClientInstance;
import com.example.uet_schedule.R;

import java.util.ArrayList;
import java.util.List;

import pl.polidea.view.ZoomView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleScreen extends AppCompatActivity {

    private ZoomView zoomView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_screen);
        getSupportActionBar().hide(); //<< this

//        EditText find_input = findViewById(R.id.find_input);
//        Button find_btn = findViewById(R.id.find_btn);

        String input = getIntent().getStringExtra("mssv");

//        InputMethodManager inputManager = (InputMethodManager)
//                getSystemService(Context.INPUT_METHOD_SERVICE);
//
//        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//                InputMethodManager.HIDE_NOT_ALWAYS);


        I_SubjectGetDataService service = SubjectClientInstance.getRetrofitInstance().create(I_SubjectGetDataService.class);
        Call<List<List<String>>> call = service.getSubject(input);
        call.enqueue(new Callback<List<List<String>>>() {
            @Override
            public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
                Log.d("Notify", "Success");
                List<List<String>> List_All_Subject = response.body();
                if(List_All_Subject.size() == 0){
                    Dialog message = new Dialog(getBaseContext());
                    message.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    message.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    message.setContentView(R.layout.message_dialog);
                    message.show();
                    Button cancel = message.findViewById(R.id.cancel_btn);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            message.dismiss();
                        }
                    });
                } else {
                    View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.zoomableview,null, false);
                    v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                    zoomView = new ZoomView(v.getContext());
                    zoomView.addView(v);

                    LinearLayout main_container = (LinearLayout) findViewById(R.id.zoomlayout);

                    main_container.addView(zoomView);

                    LinearLayout mon = findViewById(R.id.monday);
                    LinearLayout tue = findViewById(R.id.tuesday);
                    LinearLayout wed = findViewById(R.id.wednesday);
                    LinearLayout thu = findViewById(R.id.thursday);
                    LinearLayout fri = findViewById(R.id.friday);
                    LinearLayout sat = findViewById(R.id.saturday);
                    LinearLayout sun = findViewById(R.id.sunday);
                    Button[] list_button = new Button[50];
                    int k = 0;
                    for(List<String> subject : List_All_Subject){

                        list_button[k]= new Button(v.getContext());
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
            }
            @Override
            public void onFailure(Call<List<List<String>>> call, Throwable t) {
                Log.d("Notify", "Failed");
            }
        });


    }





    public void setOnClick(Button btn, List<List<String>> l){
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
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);

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
