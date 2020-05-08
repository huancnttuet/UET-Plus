package com.uetplus.ui.menu.examtime;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uetplus.R;
import com.uetplus.ui.MainActivity;
import com.uetplus.ui.SaveSharedPreference;
import com.uetplus.ui.services.Api;
import com.uetplus.ui.services.Router;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamTimeFragment extends Fragment {

    public static String[] days;
    public static int[] months = {31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    int today, beginOfMonth;
    String month, year;

    DateFormat dateFormat;
    Date date;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Lịch thi");
        final View root = inflater.inflate(R.layout.fragment_exam_time, container, false);

        RadioButton list_view_rb = (RadioButton) root.findViewById(R.id.list_view);
        list_view_rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout calendarViewLayout = root.findViewById(R.id.calendar_view_layout);
                LinearLayout listViewLayout = root.findViewById(R.id.list_view_layout);
                listViewLayout.setVisibility(LinearLayout.VISIBLE);
                calendarViewLayout.setVisibility(LinearLayout.GONE);
            }
        });
        RadioButton calendar_view_rb = (RadioButton) root.findViewById(R.id.calendar_view);
        calendar_view_rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout calendarViewLayout = root.findViewById(R.id.calendar_view_layout);
                LinearLayout listViewLayout = root.findViewById(R.id.list_view_layout);
                listViewLayout.setVisibility(LinearLayout.GONE);
                calendarViewLayout.setVisibility(LinearLayout.VISIBLE);
            }
        });

        String examtimeCache = SaveSharedPreference.getCache(getActivity(),"examtime");
        if(examtimeCache.length() != 0){
            Gson gson = new Gson();
            Type listType = new TypeToken<List<List<String>>>() {}.getType();
            List<List<String>> list = gson.fromJson(examtimeCache,listType);
            root.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            showListExamTime(list, root);
            showCalendarExamTime(list, root);
        } else{
            String mssv = SaveSharedPreference.getUserName(getActivity());
            Log.v("Cache",mssv);
            Router service = Api.getRetrofitInstance().create(Router.class);
            Call<List<List<String>>> call = service.getExamTime(mssv);
            call.enqueue(new Callback<List<List<String>>>() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
                    root.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    Log.d("Notify", "Success");
                    final List<List<String>> List_All_Exam_Time = response.body();
                    if(List_All_Exam_Time == null || List_All_Exam_Time.size() == 0){
                        openSortDialog(root, R.layout.dialog_error);
                        return;
                    } else {
                        showListExamTime(List_All_Exam_Time, root);
                        showCalendarExamTime(List_All_Exam_Time, root);
                        Gson gson = new Gson();
                        String value = gson.toJson(List_All_Exam_Time);
                        SaveSharedPreference.setCache(getActivity(),"examtime",value);
                    }
                }
                @Override
                public void onFailure(Call<List<List<String>>> call, Throwable t) {
                    Log.d("Notify", "Failed");
                    openSortDialog(root, R.layout.dialog_error);
                }
            });
        }
        return root;
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

    public void openSortDialog(View root, int type){
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


    public void openDialog(List<String> subject, View v) {
        final Dialog dialog = new Dialog(v.getContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exam_time);

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


    public void showListExamTime(List<List<String>> List_All_Exam_Time, View v){
        LinearLayout listViewLayout = v.findViewById(R.id.list_view_layout);
        CardView[] cardViews = new CardView[List_All_Exam_Time.size()];
        LinearLayout[] linearLayouts = new LinearLayout[List_All_Exam_Time.size()];
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int height = (int) getResources().getDimension(R.dimen.home_card_view_height);

        LinearLayout.LayoutParams cardViewParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 134);
        cardViewParam.setMargins(5,5,5,5);
        param.setMargins(3,3,3,3);
        for(int i = 0 ; i < List_All_Exam_Time.size();++i){
            cardViews[i] = new CardView(v.getContext());
            cardViews[i].setLayoutParams(cardViewParam);
            cardViews[i].setCardElevation(15);
            cardViews[i].setRadius(15);

            linearLayouts[i]= new LinearLayout(v.getContext());
            linearLayouts[i].setLayoutParams(param);
            linearLayouts[i].setOrientation(LinearLayout.VERTICAL);
            linearLayouts[i].setPadding(2,2,2,2);
            TextView subjectName = new TextView(v.getContext());
            TextView examTime = new TextView(v.getContext());
            TextView sbd = new TextView(v.getContext());

            subjectName.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
            subjectName.setText(List_All_Exam_Time.get(i).get(7));
            examTime.setText("Địa điểm : "+List_All_Exam_Time.get(i).get(11));
            examTime.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
            sbd.setText("SBD : " + List_All_Exam_Time.get(i).get(5) + "        " + List_All_Exam_Time.get(i).get(9) + " - " + List_All_Exam_Time.get(i).get(8));
            sbd.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
            linearLayouts[i].addView(subjectName);
            linearLayouts[i].addView(sbd);
            linearLayouts[i].addView(examTime);
            cardViews[i].addView(linearLayouts[i]);
            listViewLayout.addView(cardViews[i]);
            final int finalJ = i;
            final List<List<String>> examTimeList = List_All_Exam_Time;
            cardViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDialog(examTimeList.get(finalJ), v);
                }
            });

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void showCalendarExamTime(List<List<String>> List_All_Exam_Time, View root){
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

        GridLayout calendar = root.findViewById(R.id.calendar);
        dateFormat = new SimpleDateFormat("yyyy");
        Calendar cal_start = Calendar.getInstance();
        Calendar cal_end = Calendar.getInstance();
        cal_start.set(Calendar.YEAR, Integer.parseInt(listyear.get(0)));
        cal_start.set(Calendar.MONTH, Integer.parseInt(listmonth.get(0)) - 1);
        cal_start.set(Calendar.DAY_OF_MONTH, Integer.parseInt(listday.get(0)));
        cal_end.set(Calendar.YEAR, Integer.parseInt(listyear.get(listyear.size()-1)));
        cal_end.set(Calendar.MONTH, Integer.parseInt(listmonth.get(listmonth.size()-1)) - 1);
        cal_end.set(Calendar.DAY_OF_MONTH, Integer.parseInt(listday.get(listday.size()-1)));
        TextView StartTime = root.findViewById(R.id.start_time);
        TextView EndTime = root.findViewById(R.id.end_time);

        StartTime.setText("Từ " + new SimpleDateFormat("dd/MM/yyyy").format(cal_start.getTime()));
        EndTime.setText(" đến " + new SimpleDateFormat("dd/MM/yyyy").format(cal_end.getTime()));

        TextView currentMonth = root.findViewById(R.id.month);
        TextView currentYear = root.findViewById(R.id.year);

        currentMonth.setText("   "+(new SimpleDateFormat("MMMM", Locale.US).format(cal_start.getTime())).toUpperCase());
        currentYear.setText("   "+listyear.get(0) + "   ");

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
            Toast.makeText(root.getContext(), pe.getMessage(), Toast.LENGTH_LONG).show();
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
        for(int i = 7 ; i < days.length; i++){
            cardViews[i] = new CardView(root.getContext());
            param[i] = new GridLayout.LayoutParams();
            param[i].height = 0;
            param[i].width = 0;
            param[i].setMargins(2,2,2,2);
            param[i].rowSpec = GridLayout.spec(GridLayout.UNDEFINED,1f);
            param[i].columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            cardViews[i].setLayoutParams(param[i]);
            cardViews[i].setCardElevation(15);
            cardViews[i].setRadius(15);

            boxs[i] = new LinearLayout(root.getContext());
            boxs[i].setLayoutParams(param_default);
            boxs[i].setOrientation(LinearLayout.VERTICAL);
            boxs[i].setGravity(Gravity.CENTER_VERTICAL);

            if(!"".equals(days[i])){
                boxs[i].setTag(days[i]);
            }

            textViews[i] = new TextView(root.getContext());
            textViews[i].setLayoutParams(param_text);
            textViews[i].setTextColor(Color.BLACK);
            textViews[i].setGravity(Gravity.CENTER);
            textViews[i].setText(days[i]);
            boxs[i].addView(textViews[i]);
            cardViews[i].addView(boxs[i]);
            calendar.addView(cardViews[i]);
        }

        for(int j = 0 ; j < listday.size(); ++j){
            for(int i = 7 ; i <days.length;i++){
                if(boxs[i].getTag() != null){
                    if(boxs[i].getTag().equals(listday.get(j))){
                        TextView t = new TextView(root.getContext());
                        t.setLayoutParams(param_text);
                        t.setText(subjectnames.get(j));
                        t.setTextSize(TypedValue.COMPLEX_UNIT_DIP,6);
                        boxs[i].addView(t);
                        final int finalJ = j;
                        final List<List<String>> listExamTime = List_All_Exam_Time;
                        cardViews[i].setCardBackgroundColor(Color.CYAN);
                        cardViews[i].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openDialog(listExamTime.get(finalJ), v);
                            }
                        });
                    }
                }
            }
        }
    }
}
