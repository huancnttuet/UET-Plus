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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        setContentView(R.layout.activity_score_screen);
        getSupportActionBar().hide(); //<< this

        EditText find_input = findViewById(R.id.find_input);
        Button find_all_btn = findViewById(R.id.find_all_btn);
        Button find_btn = findViewById(R.id.find_btn);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        find_all_btn.setOnClickListener(new View.OnClickListener() {
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
//                setContentView(R.layout.activity_splash_screen);
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
                            Dialog message = new Dialog(v.getContext());
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

                        LinearLayout[] name_layouts = new LinearLayout[result.size()];
                        LinearLayout[] code_layouts = new LinearLayout[result.size()];
                        LinearLayout[] updated_at_layouts = new LinearLayout[result.size()];
                        Button[] score_names = new Button[result.size()] ;
                        Button[] score_codes = new Button[result.size()] ;
                        Button[] score_updated_ats = new Button[result.size()] ;
                        LinearLayout SubjectNameLayout = findViewById(R.id.subject_name);
                        LinearLayout SubjectCodeLayout = findViewById(R.id.subject_code);
                        LinearLayout UpdatedAtLayout = findViewById(R.id.updated_at);
                        for(int i = 0 ; i < result.size(); ++i) {
                            score_names[i] = new Button(v.getContext());
                            score_names[i].setLayoutParams(new LinearLayout.LayoutParams(300,150));
                            score_codes[i] = new Button(v.getContext());
                            score_codes[i].setLayoutParams(new LinearLayout.LayoutParams(200,150));
                            score_updated_ats[i] = new Button(v.getContext());
                            score_updated_ats[i].setLayoutParams(new LinearLayout.LayoutParams(200,150));

                            name_layouts[i] = new LinearLayout(v.getContext());
                            name_layouts[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            name_layouts[i].setOrientation(LinearLayout.HORIZONTAL);
                            code_layouts[i] = new LinearLayout(v.getContext());
                            code_layouts[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            code_layouts[i].setOrientation(LinearLayout.HORIZONTAL);
                            updated_at_layouts[i] = new LinearLayout(v.getContext());
                            updated_at_layouts[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            updated_at_layouts[i].setOrientation(LinearLayout.HORIZONTAL);
                            score_names[i].setText(result.get(i).get(0));
                            score_codes[i].setText(result.get(i).get(1));
                            score_updated_ats[i].setText(result.get(i).get(3));
                            String urlPdf = "http://112.137.129.30/viewgrade/"+ result.get(i).get(2);

                            score_names[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent();
                                    intent = new Intent(ScoreScreen.this,PDFViewerScreen.class);
                                    intent.putExtra("urlPdf", urlPdf);
                                    startActivity(intent);
                                }
                            });
                            name_layouts[i].addView(score_names[i]);
                            code_layouts[i].addView(score_codes[i]);
                            updated_at_layouts[i].addView(score_updated_ats[i]);
                            SubjectNameLayout.addView(name_layouts[i]);
                            SubjectCodeLayout.addView(code_layouts[i]);
                            UpdatedAtLayout.addView(updated_at_layouts[i]);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<List<String>>> call, Throwable t) {
                        Log.d("Notify", "Failed");
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        Dialog message = new Dialog(v.getContext());
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
                });


            }
        });


    }
}
