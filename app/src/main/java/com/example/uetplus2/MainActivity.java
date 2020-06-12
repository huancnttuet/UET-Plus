package com.example.uetplus2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.uetplus2.cache.SaveSharedPreference;
import com.example.uetplus2.models.TimeTable;
import com.example.uetplus2.services.timetable.GetTimeTableByMssv;
import com.example.uetplus2.ui.activity.LoginActivity;
import com.example.uetplus2.ui.components.grades.GradesFragment;
import com.example.uetplus2.ui.components.timetable.TimeTableFragment;
import com.example.uetplus2.ui.menu.dashboard.BottomNavigationViewBehavior;
import com.example.uetplus2.ui.menu.dashboard.DashboardFragment;
import com.example.uetplus2.ui.menu.home.HomeFragment;
import com.example.uetplus2.ui.menu.notifications.NotificationsFragment;
import com.example.uetplus2.ui.menu.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.sql.Time;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public List<TimeTable> timeTableList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SaveSharedPreference.getUserName(this).length() == 0){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else {
            setContentView(R.layout.activity_main);
            BottomNavigationView navView = findViewById(R.id.nav_view);
//        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navView.getLayoutParams();
            layoutParams.setBehavior(new BottomNavigationViewBehavior());
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_profile)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Log.v("NAV", String.valueOf(item.getItemId()));
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    Log.v("NAV" , "dashboard");
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new DashboardFragment()).addToBackStack(null).commit();
                    break;
                case R.id.navigation_home:
                    Log.v("NAV" , "home");
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).addToBackStack(null).commit();
                    break;
                case R.id.navigation_notifications:
                    Log.v("NAV" , "notification");
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new GradesFragment()).addToBackStack(null).commit();
                    break;
                case R.id.navigation_profile:
                    Log.v("NAV", "profile");
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new ProfileFragment()).commit();
                    break;
            }
            return true;
        }
    };

//    public void setActionBarTitle(String title) {
//        getSupportActionBar().setTitle(title);
//    }

}