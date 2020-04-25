package com.example.uet_schedule.View;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import com.example.uet_schedule.Presenter.I_SubjectGetDataService;
import com.example.uet_schedule.Presenter.SubjectClientInstance;
import com.example.uet_schedule.R;

import org.w3c.dom.Text;

import pl.polidea.view.ZoomView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamTimeSreen extends AppCompatActivity {

    public static String[] days;
    public static int[] months = {31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    int today, beginOfMonth;
    String month, year;

    DateFormat dateFormat;
    Date date;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_exam_time_screen);
        Intent intent = getIntent();
        String mssv = intent.getStringExtra("mssv");
        Log.v("dada",mssv);
        I_SubjectGetDataService service = SubjectClientInstance.getRetrofitInstance().create(I_SubjectGetDataService.class);
        Call<List<List<String>>> call = service.getExamTime(mssv);
        call.enqueue(new Callback<List<List<String>>>() {
            @Override
            public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                Log.d("Notify", "Success");
                List<List<String>> List_All_Exam_Time = response.body();
                if(List_All_Exam_Time.size() == 0){
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

                    List<String> subjectnames =  new ArrayList<String>();
                    List<String> listday =  new ArrayList<String>();
                    List<String> listmonth =  new ArrayList<String>();
                    List<String> listyear =  new ArrayList<String>();
                    String sn;
                    String d,m,y;
                    for(List<String> r : List_All_Exam_Time){
                        sn = r.get(7);
                        d = r.get(8).split("/")[0];
                        m =r.get(8).split("/")[1];
                        y =r.get(8).split("/")[2];
                        subjectnames.add(sn);
                        listday.add(d);
                        listmonth.add(m);
                        listyear.add(y);
                    }

                    GridLayout calendar = findViewById(R.id.calendar);
                    dateFormat = new SimpleDateFormat("yyyy");
                    Calendar cal_start = Calendar.getInstance();
                    Calendar cal_end = Calendar.getInstance();
                    cal_start.set(Calendar.YEAR, Integer.parseInt(listyear.get(0)));
                    cal_start.set(Calendar.MONTH, Integer.parseInt(listmonth.get(0)) - 1);
                    cal_start.set(Calendar.DAY_OF_MONTH, Integer.parseInt(listday.get(0)));
                    cal_end.set(Calendar.YEAR, Integer.parseInt(listyear.get(listyear.size()-1)));
                    cal_end.set(Calendar.MONTH, Integer.parseInt(listmonth.get(listmonth.size()-1)) - 1);
                    cal_end.set(Calendar.DAY_OF_MONTH, Integer.parseInt(listday.get(listday.size()-1)));
                    TextView StartTime = findViewById(R.id.start_time);
                    TextView EndTime = findViewById(R.id.end_time);

                    StartTime.setText("Từ " + new SimpleDateFormat("dd/MM/yyyy").format(cal_start.getTime()));
                    EndTime.setText(" đến " + new SimpleDateFormat("dd/MM/yyyy").format(cal_end.getTime()));
                    date = cal_start.getTime();
                    months[1] = Feb(Integer.parseInt(dateFormat.format(date)));
                    dateFormat = new SimpleDateFormat("MM");
                    int numDays = months[Integer.parseInt(dateFormat.format(date))-1] + 6; // Number of days in the month as well as making sure not to override the day names
                    // Check which day of the month the month started on. Eg: April 1st 2016 is a Friday
                    dateFormat = new SimpleDateFormat("MM");
                    month = dateFormat.format(date);
                    dateFormat = new SimpleDateFormat("yyyy");
                    year = dateFormat.format(date);
                    try {
                        beginOfMonth = (Day("01"+month+year))-1; // Get the beginning of the month (-1 because Android recognizes Sunday as the first day)
                    } catch (ParseException pe) {
                        Toast.makeText(getApplicationContext(), pe.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    if (beginOfMonth == 0) {
                        beginOfMonth = 7;
                    }
                    days = new String[numDays+beginOfMonth];
                    days[0] = "Mon";
                    days[1] = "Tue";
                    days[2] = "Wed";
                    days[3] = "Thu";
                    days[4] = "Fri";
                    days[5] = "Sat";
                    days[6] = "Sun";
                    dateFormat = new SimpleDateFormat("dd");
                    String temp = dateFormat.format(date);
                    today = Integer.parseInt(temp);

                    if(beginOfMonth != 0) {
                        for (int i = 7; i <= (5 + beginOfMonth); i++) {
                            days[i] = "";
                        }
                    }
                    for (int i = (6 + beginOfMonth); i <= (days.length-1); i++) {
                        days[i] = Integer.toString(i-beginOfMonth-5);
                    }

                    CardView[] cardViews = new CardView[days.length];
                    GridLayout.LayoutParams[] param = new GridLayout.LayoutParams[days.length];
                    LinearLayout[] boxs = new LinearLayout[days.length];
                    LinearLayout.LayoutParams param_default = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    LinearLayout.LayoutParams param_text = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    param_default.setMargins(5,5,5,5);
                    TextView[] textViews = new TextView[days.length];
                    for(int i = 0 ; i < days.length; i++){
                        cardViews[i] = new CardView(getBaseContext());
                        param[i] = new GridLayout.LayoutParams();
                        param[i].height = 0;
                        param[i].width = 0;
                        param[i].setMargins(2,2,2,2);
                        param[i].rowSpec = GridLayout.spec(GridLayout.UNDEFINED,1f);
                        param[i].columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                        cardViews[i].setLayoutParams(param[i]);
                        cardViews[i].setCardElevation(15);
                        cardViews[i].setRadius(15);

                        boxs[i] = new LinearLayout(getBaseContext());
                        boxs[i].setLayoutParams(param_default);
                        boxs[i].setOrientation(LinearLayout.VERTICAL);
                        boxs[i].setGravity(Gravity.CENTER_VERTICAL);

                        if(!"".equals(days[i])){
                            boxs[i].setTag(days[i]);
                        }

                        textViews[i] = new TextView(getBaseContext());
                        textViews[i].setLayoutParams(param_text);
                        textViews[i].setTextColor(Color.BLACK);
                        textViews[i].setGravity(Gravity.CENTER);

                        textViews[i].setText(days[i]);
                        boxs[i].addView(textViews[i]);
                        cardViews[i].addView(boxs[i]);
//            cardViews[i].addView(textViews[i]);
                        calendar.addView(cardViews[i]);
                    }


                   for(int j = 0 ; j < listday.size(); ++j){
                       for(int i = 0 ; i <days.length;i++){
                           if(boxs[i].getTag() != null){
                               if(boxs[i].getTag().equals(listday.get(j))){
                                   TextView t = new TextView(getBaseContext());
                                   t.setLayoutParams(param_text);
                                   t.setText(subjectnames.get(j));
                                   t.setTextSize(6);
                                   boxs[i].addView(t);
                                   int finalJ = j;
                                   cardViews[i].setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                            openDialog(List_All_Exam_Time.get(finalJ));
                                       }
                                   });
                               }
                           }
                       }
                   }
                }
            }
            @Override
            public void onFailure(Call<List<List<String>>> call, Throwable t) {
                Log.d("Notify", "Failed");
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            }
        });


    }

    public int Feb(int year) {
        int temp;
        try {
            temp = year / 4;
        } catch (Exception e) {
            return 28;
        }
        return 29;
    }
    public int Day(String day) throws ParseException {
        DateFormat df = new SimpleDateFormat("ddMMyyyy");
        try {
            Date d = df.parse(String.valueOf(day));
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return c.get(Calendar.DAY_OF_WEEK);
        } catch (Exception e) {
            ParseException pe = new ParseException("There was a problem getting the date.", 0);
            throw pe;
        }
    }


    public void openDialog(List<String> subject) {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.exam_time_dialog);

        TextView subject_name, subject_code, SBD, type_exam, exam_time, classroom;
        subject_name = dialog.findViewById(R.id.subject_name);
        subject_code = dialog.findViewById(R.id.subject_code);
        SBD = dialog.findViewById(R.id.SBD);
        type_exam = dialog.findViewById(R.id.type_exam);
        exam_time = dialog.findViewById(R.id.exam_time);
        classroom = dialog.findViewById(R.id.classroom);
        subject_name.setText(subject.get(7));
        subject_code.setText(subject.get(6));
        SBD.setText("SBD : " + subject.get(5));
        type_exam.setText("Hình thức thi : " + subject.get(13));
        exam_time.setText("Thời gian thi : " + subject.get(9) +" " + subject.get(8));
        classroom.setText("Địa điểm: " + subject.get(11));
        dialog.show();
        Button exit_btn = dialog.findViewById(R.id.exit_btn);
        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }




//        CalendarView simpleCalendarView = (CalendarView) findViewById(R.id.simpleCalendarView); // get the reference of CalendarView
//        long selectedDate = simpleCalendarView.getDate();
//        long currentTime = Calendar.getInstance().getTimeInMillis();
//        simpleCalendarView.setDate(currentTime);



}
