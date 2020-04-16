package com.example.uet_schedule.View;

import android.content.Intent;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uet_schedule.Model.Subject;
import com.example.uet_schedule.Presenter.I_SubjectGetDataService;
import com.example.uet_schedule.Presenter.SubjectClientInstance;
import com.example.uet_schedule.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    View View_LoadingRotate;
    public static List<Subject> List_All_Subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        View_LoadingRotate = (View) this.findViewById(R.id.view_loading_rotate);

        final Animation animationRotate = AnimationUtils.loadAnimation(this,R.anim.anim_rotate);
        final Animation animationScale = AnimationUtils.loadAnimation(this,R.anim.anim_scale);

        AnimationSet groupAnimation = new AnimationSet(false);
        groupAnimation.addAnimation(animationRotate);
        groupAnimation.addAnimation(animationScale);
        View_LoadingRotate.startAnimation(groupAnimation);
        //get api -- all subject


        startActivity(new Intent(SplashScreen.this,MainActivity.class));
        finish();
//
//        I_SubjectGetDataService service = SubjectClientInstance.getRetrofitInstance().create(I_SubjectGetDataService.class);
//        Call<List<Subject>> call = service.getSubject();
//        call.enqueue(new Callback<List<Subject>>() {
//            @Override
//            public void onResponse(Call<List<Subject>> call, Response<List<Subject>> response) {
//                Log.d("Notify", "Success");
//                List_All_Subject = response.body();
//                startActivity(new Intent(SplashScreen.this,MainActivity.class));
//                finish();
//            }
//
//            @Override
//            public void onFailure(Call<List<Subject>> call, Throwable t) {
//                Log.d("Notify", "Failed");
//            }
//        });

//        (new Handler()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(SplashScreen.this,MainActivity.class));
//                finish();
//            }
//        },5000);

    }

}
