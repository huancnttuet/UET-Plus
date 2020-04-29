package com.example.uet_schedule.View;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.uet_schedule.Presenter.I_SubjectGetDataService;
import com.example.uet_schedule.Presenter.SubjectClientInstance;
import com.example.uet_schedule.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScoreScreen extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.grades_screen);
        getSupportActionBar().hide(); //<< this

        EditText find_input = findViewById(R.id.find_input);
        Button find_all_btn = findViewById(R.id.find_all_btn);
        Button find_btn = findViewById(R.id.find_btn);

        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = find_input.getText().toString();
                if (input.matches("")) {
                    Toast.makeText(v.getContext(), "Bạn chưa nhập gì cả :'D", Toast.LENGTH_SHORT).show();
                    return;
                }
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                I_SubjectGetDataService service = SubjectClientInstance.getRetrofitInstance().create(I_SubjectGetDataService.class);
                Call<List<List<List<String>>>> call = service.searchGrades(input,76,0);
                call.enqueue(new Callback<List<List<List<String>>>>() {
                    @Override
                    public void onResponse(Call<List<List<List<String>>>> call, Response<List<List<List<String>>>> response) {
                        Log.d("Notify", "Success");
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        List<List<List<String>>> result = response.body();
                        if(result.size() == 0){
                            showDialog();
                        }
                        draw(result.get(0),v);
                    }
                    @Override
                    public void onFailure(Call<List<List<List<String>>>> call, Throwable t) {
                        Log.d("Notify", "Failed");
                        showDialog();
                    }
                });

            }
        });


        find_all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                I_SubjectGetDataService service = SubjectClientInstance.getRetrofitInstance().create(I_SubjectGetDataService.class);
                Call<List<List<String>>> call = service.getScore(76);
                call.enqueue(new Callback<List<List<String>>>() {
                    @Override
                    public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
                        Log.d("Notify", "Success");
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        List<List<String>> result = response.body();
                        if(result.size() == 0){
                           showDialog();
                        }
                        draw(result,v);
                    }
                    @Override
                    public void onFailure(Call<List<List<String>>> call, Throwable t) {
                        Log.d("Notify", "Failed");
                       showDialog();
                    }
                });


            }
        });

    }

    public void draw(List<List<String>>  result, View v ){
        LinearLayout grade_list_layout = findViewById(R.id.grade_list);
        grade_list_layout.removeAllViews();

        CardView[] cardViews = new CardView[result.size()];
        LinearLayout[] layouts = new LinearLayout[result.size()];
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,180);
        TextView[] textViews1 = new TextView[result.size()];
        TextView[] textViews2 = new TextView[result.size()];
        TextView[] textViews3 = new TextView[result.size()];
        String[] urlPdfs = new String[result.size()];

        param.setMargins(10,10,10,10);
        for(int i = 0 ; i < result.size(); ++i) {
            cardViews[i] = new CardView(v.getContext());
            cardViews[i].setLayoutParams(param);
            cardViews[i].setRadius(10);
            cardViews[i].setCardElevation(10);

            layouts[i] = new LinearLayout(v.getContext());
            layouts[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layouts[i].setOrientation(LinearLayout.VERTICAL);
            layouts[i].setPadding(10,10,10,10);
            textViews1[i] = new TextView(v.getContext());
            textViews1[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textViews1[i].setTextSize(20);
            textViews1[i].setText(result.get(i).get(0));

            textViews2[i] = new TextView(v.getContext());
            textViews2[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textViews2[i].setText(result.get(i).get(1));

            textViews3[i] = new TextView(v.getContext());
            textViews3[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textViews3[i].setText(result.get(i).get(3));

            layouts[i].addView(textViews1[i]);
            layouts[i].addView(textViews2[i]);
            layouts[i].addView(textViews3[i]);

            cardViews[i].addView(layouts[i]);

            grade_list_layout.addView(cardViews[i]);
            urlPdfs[i] = "http://112.137.129.30/viewgrade/"+ result.get(i).get(2);

            int finalI = i;
            cardViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent = new Intent(ScoreScreen.this,PDFViewerScreen.class);
                    intent.putExtra("urlPdf", urlPdfs[finalI]);
                    startActivity(intent);
                }
            });

        }
    }

    public void showDialog(){
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        Dialog message = new Dialog(ScoreScreen.this);
        message.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        message.requestWindowFeature(Window.FEATURE_NO_TITLE);
        message.setContentView(R.layout.error_dialog);
        message.show();
        Button cancel = message.findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.dismiss();
            }
        });
    }
}
