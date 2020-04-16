package com.example.uet_schedule.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
    LinearLayout textsplash, texthome, menus;
    Animation frombottom, bganim, clovernim;
    public static List<Student> List_Student;

//    public void getStudentfromMSSV(MSSV mssv, View view){
//        //post api -- get student from mssv
//
//        I_SubjectGetDataService service = SubjectClientInstance.getRetrofitInstance().create(I_SubjectGetDataService.class);
//
//        Call<List<Student>> call = service.getStudent(mssv);
//        call.enqueue(new Callback<List<Student>>() {
//            @Override
//            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
//                Log.d("Notify", "POST Success");
//                List_Student = response.body();
//                for(Student s : List_Student) {
//                    Log.d("Student", s.getName());
//                }
//                display(view);
//            }
//
//            @Override
//            public void onFailure(Call<List<Student>> call, Throwable t) {
//                Log.d("Notify POST", "POST Failed");
//            }
//        });
//    }
//
//    public void display(View view){
//        TableLayout c = (TableLayout) view.getRootView().findViewById(R.id.table_all_subject);
//
//        for(int i = 0; i < List_Student.size(); i++){
//            TableRow tr =  new TableRow(view.getContext());
//            TextView c1 = new TextView(view.getContext());
//            c1.setText(List_Student.get(i).getStt());
//            TextView c2 = new TextView(view.getContext());
//            c2.setText(List_Student.get(i).getName());
//            TextView c3 = new TextView(view.getContext());
//            c3.setText(List_Student.get(i).getSubject());
//            tr.addView(c1);
//            tr.addView(c2);
//            tr.addView(c3);
//            c.addView(tr);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_main);

        BackgroundApp = (ImageView) findViewById(R.id.bg);
        BGIcon = (ImageView) findViewById(R.id.clover);
        textsplash = (LinearLayout) findViewById(R.id.textsplash);
        texthome = (LinearLayout) findViewById(R.id.texthome) ;
        menus = (LinearLayout) findViewById(R.id.menus);

        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);



        BackgroundApp.animate().translationY(-1000).setDuration(1000).setStartDelay(2500);
        BGIcon.animate().alpha(0).setDuration(1000).setStartDelay(500);

        textsplash.animate().translationY(140).alpha(0).setStartDelay(800).setStartDelay(300);
        texthome.startAnimation(frombottom);
        menus.startAnimation(frombottom);

        LinearLayout btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ScheduleScreen.class));
            }
        });

//        Button btn =findViewById(R.id.btn_get_all_subject);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("Click","Click");
//                for(int i = 0; i<List_All_Subject.size();i++){
//                    Log.d("CodeFull Subject: ", List_All_Subject.get(i) + " " + i);
//                }
//                TableLayout c = (TableLayout) view.getRootView().findViewById(R.id.table_all_subject);
//
//                for(int i = 0; i < List_All_Subject.size(); i++){
//                    TableRow tr =  new TableRow(view.getContext());
//                    TextView c1 = new TextView(view.getContext());
//                    c1.setText(List_All_Subject.get(i).getStt());
//                    TextView c2 = new TextView(view.getContext());
//                    c2.setText(List_All_Subject.get(i).getName());
//                    TextView c3 = new TextView(view.getContext());
//                    c3.setText(List_All_Subject.get(i).getClassRoom());
//                    tr.addView(c1);
//                    tr.addView(c2);
//                    tr.addView(c3);
//                    c.addView(tr);
//                }
//
//            }
//        });
//
//        Button find_btn = findViewById(R.id.find_btn);
//        EditText mssv_input = findViewById(R.id.mssv_input);
//
//        find_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MSSV mssv = new MSSV(mssv_input.getText().toString());
//                getStudentfromMSSV(mssv,view);
//
//            }
//        });


    }

}
