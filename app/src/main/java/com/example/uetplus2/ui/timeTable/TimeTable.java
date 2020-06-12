package com.example.uetplus2.ui.timeTable;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.uetplus2.MainActivity;
import com.example.uetplus2.R;
import com.example.uetplus2.api.Rest;
import com.example.uetplus2.api.UetSupportApi;
import com.example.uetplus2.ui.home.HomeViewModel;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


public class TimeTable extends Fragment {

    private TimeTableViewModel timeTableViewModel;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        timeTableViewModel =
                ViewModelProviders.of(this).get(TimeTableViewModel.class);
        root = inflater.inflate(R.layout.fragment_time_table, container, false);
        new GetTimeTableByMssv(root.getContext()).execute("/schedule/test","18020962");
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        ((MainActivity)getActivity()).setActionBarTitle("Thời khóa biểu");
        super.onCreateOptionsMenu(menu, inflater);
    }


    public void drawTimeTable(List<com.example.uetplus2.models.TimeTable>list){

        List<LinearLayout> listWeeks = new ArrayList<>();
        listWeeks.add((LinearLayout) root.findViewById(R.id.monday));
        listWeeks.add((LinearLayout) root.findViewById(R.id.tuesday));
        listWeeks.add((LinearLayout) root.findViewById(R.id.wednesday));
        listWeeks.add((LinearLayout) root.findViewById(R.id.thusday));
        listWeeks.add((LinearLayout) root.findViewById(R.id.friday));
        listWeeks.add((LinearLayout) root.findViewById(R.id.saturday));
        listWeeks.add((LinearLayout) root.findViewById(R.id.sunday));

        for (int i = 0; i < list.size(); i++) {
            TextView textView = new TextView(root.getContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            String start = Integer.parseInt(list.get(i).lesson.split("-")[0]) + 6 + "h";
            String end = Integer.parseInt(list.get(i).lesson.split("-")[1]) + 7 + "h";
            textView.setText(list.get(i).subject_name + "\n" + start + "-" + end + "\n" + list.get(i).room + "\n" + "Nhóm: " + list.get(i).note);
            textView.setBackground(ContextCompat.getDrawable(textView.getContext(), R.drawable.border_subjects));
            textView.setGravity(Gravity.CENTER);
            int index = Integer.parseInt(list.get(i).day_week) - 2;
//            setOnClick(textView,i,list);
            listWeeks.get(index).addView(textView);
        }

    }

    private class GetTimeTableByMssv extends AsyncTask<String, Void, List<com.example.uetplus2.models.TimeTable>> {

        protected ProgressDialog dialog;
        protected Context context;

        public GetTimeTableByMssv(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog = new ProgressDialog(context, 1);
            this.dialog.setMessage("Retrieving TimeTable List");
            this.dialog.show();
        }

        @Override
        protected List<com.example.uetplus2.models.TimeTable> doInBackground(String... params) {
            try {
                Log.v("TimeTable", "Get timetable by mssv");
                return (List<com.example.uetplus2.models.TimeTable>) UetSupportApi.get((String) params[0], (String) params[1]);
            }
            catch (Exception e) {
                Log.v("TimeTable", "ERROR : " + e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<com.example.uetplus2.models.TimeTable> result) {
            super.onPostExecute(result);
            drawTimeTable(result);
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }


}