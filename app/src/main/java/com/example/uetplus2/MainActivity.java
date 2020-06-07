package com.example.uetplus2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.uetplus2.api.UetSupportApi;
import com.example.uetplus2.models.TimeTable;
import com.example.uetplus2.ui.dashboard.BottomNavigationViewBehavior;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        new GetTimeTableByMssv(this).execute("/schedule/test","17020781");

    }


    private class GetTimeTableByMssv extends AsyncTask<String, Void, List<TimeTable>> {

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
        protected List<TimeTable> doInBackground(String... params) {
            try {
                Log.v("TimeTable", "Get timetable by mssv");
                return (List<TimeTable>) UetSupportApi.get((String) params[0], (String) params[1]);
            }
            catch (Exception e) {
                Log.v("TimeTable", "ERROR : " + e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<TimeTable> result) {
            super.onPostExecute(result);

            Log.v("RESULT", String.valueOf(result.size()));
            Log.v("RESULT", result.get(1).getTeacher());
            Log.v("RESULT", result.get(1).getSubject_name());

            if (dialog.isShowing())
                dialog.dismiss();
        }
    }




}