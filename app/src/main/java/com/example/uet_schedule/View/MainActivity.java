package com.example.uet_schedule.View;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uet_schedule.Model.MSSV;
import com.example.uet_schedule.Model.Student;
import com.example.uet_schedule.Model.Subject;
import com.example.uet_schedule.Presenter.I_SubjectGetDataService;
import com.example.uet_schedule.Presenter.SubjectClientInstance;
import com.example.uet_schedule.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.uet_schedule.View.SplashScreen.List_All_Subject;

public class MainActivity extends AppCompatActivity {

    ImageView BackgroundApp, BGIcon ;
    LinearLayout textsplash, texthome;
    GridLayout menus;
    Animation frombottom, bganim, clovernim;
    public static List<Student> List_Student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_main);

//        BackgroundApp = (ImageView) findViewById(R.id.bg);
//        BGIcon = (ImageView) findViewById(R.id.clover);
//        textsplash = (LinearLayout) findViewById(R.id.textsplash);
//        texthome = (LinearLayout) findViewById(R.id.texthome) ;
        menus =  findViewById(R.id.menus);

        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);



//        BackgroundApp.animate().translationY(-1000).setDuration(1000).setStartDelay(2500);
//        BGIcon.animate().alpha(0).setDuration(1000).setStartDelay(500);

//        textsplash.animate().translationY(140).alpha(0).setStartDelay(800).setStartDelay(300);
//        texthome.startAnimation(frombottom);
        menus.startAnimation(frombottom);

        LinearLayout btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(v.getContext());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.input_dialog);
                TextView title = dialog.findViewById(R.id.title);
                title.setText("Xem thời khóa biểu");
                dialog.show();
                Button exit_btn = dialog.findViewById(R.id.cancel_btn);
                exit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button yes_btn = dialog.findViewById(R.id.yes_btn);
                yes_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,ScheduleScreen.class);
                        EditText input = dialog.findViewById(R.id.input);
                        String mssv = input.getText().toString();
                        if (mssv.matches("")) {
                            Toast.makeText(v.getContext(), "Bạn chưa nhập gì cả :'D", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        intent.putExtra("mssv",mssv);
                        startActivity(intent);
                    }
                });

            }
        });

        LinearLayout btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(v.getContext());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.input_dialog);
                dialog.show();
                Button exit_btn = dialog.findViewById(R.id.cancel_btn);
                exit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button yes_btn = dialog.findViewById(R.id.yes_btn);
                yes_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,ExamTimeSreen.class);
                        EditText input = dialog.findViewById(R.id.input);
                        String mssv = input.getText().toString();
                        if (mssv.matches("")) {
                            Toast.makeText(v.getContext(), "Bạn chưa nhập gì cả :'D", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        intent.putExtra("mssv",mssv);
                        startActivity(intent);
                    }
                });

            }
        });

        LinearLayout btn3 = findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ScoreScreen.class));
            }
        });



    }

    public void openDialog() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        dialog.show();
        Button exit_btn = dialog.findViewById(R.id.exit_btn);
        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}
