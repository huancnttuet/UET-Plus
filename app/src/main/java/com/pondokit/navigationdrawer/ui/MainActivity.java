package com.pondokit.navigationdrawer.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
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
import com.pondokit.navigationdrawer.R;
import com.pondokit.navigationdrawer.ui.menu.menu_about.AboutFragment;
import com.pondokit.navigationdrawer.ui.menu.menu_home.HomeFragment;
import com.pondokit.navigationdrawer.ui.menu.menu_pengurus.PengurusFragment;
import com.pondokit.navigationdrawer.ui.menu.menu_saran_dan_kritikan.SaranDanKritikanFragment;
import com.pondokit.navigationdrawer.ui.menu_profile.ProfileActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public Toolbar toolbar;
    private MenuItem menuHome, menuPengurus, menuSaranDanKritikan, menuAbout, menuLogout;
    boolean doubleBackToExitPressedOnce = false;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        AppCompatTextView navUserName = headerView.findViewById(R.id.atv_name_header);
        navUserName.setText("Budi");

        TextView navEmail = headerView.findViewById(R.id.tv_email_header);
        navEmail.setText("budi@gmail.com");
    }

    /**
     * Fragment
     **/
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
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
            case R.id.menu_pengurus:
                fragment = new PengurusFragment();
                break;
            case R.id.menu_saran_dan_kritikan:
                fragment = new SaranDanKritikanFragment();
                break;
            case R.id.menu_about:
                fragment = new AboutFragment();
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
        menuPengurus = menu.findItem(R.id.menu_pengurus);
        menuSaranDanKritikan = menu.findItem(R.id.menu_saran_dan_kritikan);
        menuAbout = menu.findItem(R.id.menu_about);
        menuLogout = menu.findItem(R.id.menu_logout);

        if(menuHome != null && menuPengurus != null && menuSaranDanKritikan != null &&
                menuAbout != null && menuLogout != null)

            menuHome.setVisible(false);
            menuPengurus.setVisible(false);
            menuSaranDanKritikan.setVisible(false);
            menuAbout.setVisible(false);
            menuLogout.setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    private void dialogExit() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Apakah anda yakin ingin keluar?");
        dialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

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
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nav_drawer, menu);
        return true;
    }
}
