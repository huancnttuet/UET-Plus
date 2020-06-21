package com.example.uethub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.example.uethub.ui.menu.dashboard.DashboardFragment;

import com.example.uethub.ui.menu.home.HomeFragment;
import com.example.uethub.ui.menu.notifications.NotificationsFragment;
import com.example.uethub.ui.menu.profile.ProfileFragment;


import com.wwdablu.soumya.lottiebottomnav.FontBuilder;
import com.wwdablu.soumya.lottiebottomnav.FontItem;
import com.wwdablu.soumya.lottiebottomnav.ILottieBottomNavCallback;
import com.wwdablu.soumya.lottiebottomnav.LottieBottomNav;
import com.wwdablu.soumya.lottiebottomnav.MenuItem;
import com.wwdablu.soumya.lottiebottomnav.MenuItemBuilder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ILottieBottomNavCallback {

    LottieBottomNav bottomNav;

    ArrayList<MenuItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String f_noti = getIntent().getStringExtra("MISSION");
        int firstNav = 0;
        System.out.println(f_noti);
        if(f_noti != null && f_noti.equals("fragment_notifications")){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new NotificationsFragment())
                    .commitNow();
            firstNav = 2;
        } else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, DashboardFragment.newInstance())
                        .commitNow();
            }
        }


        bottomNav = findViewById(R.id.bottom_nav);

        FontItem fontItem = FontBuilder.create("Dashboard")
                .selectedTextColor(Color.BLACK)
                .unSelectedTextColor(Color.GRAY)
                .selectedTextSize(16) //SP
                .unSelectedTextSize(12) //SP
                .setTypeface(Typeface.createFromAsset(getAssets(), "coffeesugar.ttf"))
                .build();

        MenuItem item1 = MenuItemBuilder.create("home.json", MenuItem.Source.Assets, fontItem, "dash")
                .pausedProgress(1f)
                .loop(false)
                .build();

        SpannableString spannableString = new SpannableString("Môn học");
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        fontItem = FontBuilder.create(fontItem).setTitle(spannableString).build();
        MenuItem item2 = MenuItemBuilder.createFrom(item1, fontItem)
                .selectedLottieName("books1.json")
                .unSelectedLottieName("books1.json")
                .loop(true)
                .build();

        fontItem = FontBuilder.create(fontItem).setTitle("Thông báo").build();
        MenuItem item3 = MenuItemBuilder.createFrom(item1, fontItem)
                .selectedLottieName("message_cupid.json")
                .unSelectedLottieName("message_cupid.json")
                .pausedProgress(0.75f)
                .build();

        fontItem = FontBuilder.create(fontItem).setTitle("Cá nhân").build();
        MenuItem item4 = MenuItemBuilder.createFrom(item1, fontItem)
                .selectedLottieName("profile1.json")
                .unSelectedLottieName("profile1.json")
                .build();

        list = new ArrayList<>(4);
        list.add(item1);
        list.add(item2);
        list.add(item3);
        list.add(item4);

        bottomNav.setCallback(this);
        bottomNav.setMenuItemList(list);
        bottomNav.setSelectedIndex(firstNav);


    }

    @Override
    public void onMenuSelected(int oldIndex, int newIndex, MenuItem menuItem) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Log.v("HAHAH", String.valueOf(newIndex));
        switch (newIndex){
            case 0:
                transaction.replace(R.id.container, new DashboardFragment()).commit();
                break;
            case 1:
                transaction.replace(R.id.container, new HomeFragment()).commit();
                break;
            case 2 :
                transaction.replace(R.id.container, new NotificationsFragment()).commit();
                break;
            case 3:
                transaction.replace(R.id.container, new ProfileFragment()).commit();
                break;
            default:
                break;
        }


    }

    @Override
    public void onAnimationStart(int index, MenuItem menuItem) {
        //
    }

    @Override
    public void onAnimationEnd(int index, MenuItem menuItem) {

    }

    @Override
    public void onAnimationCancel(int index, MenuItem menuItem) {

    }


}
