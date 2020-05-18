package com.uetplus.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uetplus.R;

import com.uetplus.ui.menu.examtime.ExamTimeFragment;
import com.uetplus.ui.menu.grades.GradesFragment;
import com.uetplus.ui.menu.menu_about.AboutFragment;
import com.uetplus.ui.menu.menu_home.HomeFragment;
import com.uetplus.ui.menu.news.NewsFragment;
import com.uetplus.ui.menu.news_notice.NewsNoticeFragment;
import com.uetplus.ui.menu.timetable.TimeTableFragment;
import com.uetplus.ui.menu.menu_setting.SettingFragment;
import com.uetplus.ui.menu_profile.ProfileActivity;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public Toolbar toolbar;
    private MenuItem menuHome, menuTimeTable, menuSetting, menuAbout, menuLogout, menuExamTime, menuGrades, menuNews , menuNewsNotice;
    boolean doubleBackToExitPressedOnce = false;

    public MainActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);

        if(SaveSharedPreference.getUserName(this).length() == 0){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else {

            setContentView(R.layout.activity_main);

            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            loadFragment(new HomeFragment());

            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.setItemIconTintList(null); //disable tint on each icon to use color icon svg

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            //custom header view
            View headerView = navigationView.getHeaderView(0);
            RelativeLayout container = headerView.findViewById(R.id.container);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                }
            });

            String username = SaveSharedPreference.getUserName(this);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<List<List<String>>>>() {}.getType();
            String cacheData = SaveSharedPreference.getPrefData(this);
            List<List<List<String>>> list = gson.fromJson(cacheData,listType);
            List<String> urlImage = list.get(2).get(0);
            String fullname = list.get(0).get(0).get(1);
            new MainActivity.DownloadImageTask((CircleImageView) headerView.findViewById(R.id.imageView))
                    .execute(urlImage.get(0));

            AppCompatTextView navUserName = headerView.findViewById(R.id.atv_name_header);
            navUserName.setText(fullname);

            TextView navEmail = headerView.findViewById(R.id.tv_email_header);
            navEmail.setText(username);
        }

        if (!isMyServiceRunning()){
            Intent serviceIntent = new Intent(this,BootReceiver.class);
            startService(serviceIntent);
        }

    }

    /**
     * Fragment
     **/
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.enter,R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                    .replace(R.id.content_frame, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    /**
     * Menu Bottom Navigation Drawer
     * */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;

        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.menu_home:
                fragment = new HomeFragment();
                break;
            case R.id.menu_time_table:
                fragment = new TimeTableFragment();
                break;
            case R.id.menu_exam_time:
                fragment = new ExamTimeFragment();
                break;
            case R.id.menu_grades:
                fragment = new GradesFragment();
                break;
            case R.id.menu_setting:
                fragment = new SettingFragment();
                break;
            case R.id.menu_about:
                fragment = new AboutFragment();
                break;
            case R.id.menu_news:
                fragment = new NewsFragment();
                break;
            case R.id.menu_news_notice:
                fragment = new NewsNoticeFragment();
                break;
            case R.id.menu_logout:
                dialogExit();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return loadFragment(fragment);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //Hidden Menu Bard For All Fragments
        menuHome = menu.findItem(R.id.menu_home);
        menuNews = menu.findItem(R.id.menu_news);
        menuNewsNotice = menu.findItem(R.id.menu_news_notice);
        menuTimeTable = menu.findItem(R.id.menu_time_table);
        menuExamTime = menu.findItem(R.id.menu_exam_time);
        menuGrades = menu.findItem(R.id.menu_grades);
        menuSetting = menu.findItem(R.id.menu_setting);
        menuAbout = menu.findItem(R.id.menu_about);
        menuLogout = menu.findItem(R.id.menu_logout);

        if(menuHome != null && menuNews != null && menuNewsNotice != null && menuTimeTable != null && menuExamTime != null && menuGrades != null && menuSetting != null &&
                menuAbout != null && menuLogout != null )

            menuHome.setVisible(false);
            menuNews.setVisible(false);
            menuNewsNotice.setVisible(false);
            menuTimeTable.setVisible(false);
            menuExamTime.setVisible(false);
            menuGrades.setVisible(false);
            menuSetting.setVisible(false);
            menuAbout.setVisible(false);
            menuLogout.setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    private void dialogExit() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Bạn muốn đăng xuất ?");
        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SaveSharedPreference.cleanCache(MainActivity.this);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if(drawer == null) {
            Log.v("drawer", "null");
            finish();
            return;
        }
        Log.v("drawer", "Not null");
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            int count = getSupportFragmentManager().getBackStackEntryCount();

            if (count == 0) {
                // klik double tap to exit
                if (doubleBackToExitPressedOnce) {
                    finishAffinity();
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Tap again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            } else {
                getSupportFragmentManager().popBackStack();
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nav_drawer, menu);
        return true;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        CircleImageView bmImage;

        public DownloadImageTask(CircleImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (MyService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
