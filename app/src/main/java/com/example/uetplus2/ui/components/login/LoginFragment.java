package com.example.uetplus2.ui.components.login;


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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.example.uetplus2.MainActivity;
import com.example.uetplus2.R;
import com.example.uetplus2.cache.SaveSharedPreference;
import com.example.uetplus2.models.Information;
import com.example.uetplus2.services.login.GetInformation;

import java.util.List;




/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        Button loginButton = view.findViewById(R.id.btn_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                TextView username = view.findViewById(R.id.username);
                TextView password = view.findViewById(R.id.et_password);
                final String input1 = username.getText().toString();
                String input2 = password.getText().toString();
                if (input1.matches("")) {
                    Toast.makeText(v.getContext(), "Bạn chưa nhập Tên đăng nhập :'D ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (input2.matches("")) {
                    Toast.makeText(v.getContext(), "Bạn chưa nhập mật khẩu :'D ", Toast.LENGTH_SHORT).show();
                    return;
                }
                view.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                if(inputManager != null && getActivity().getCurrentFocus() != null)
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

//                I_LoginApi service = LoginApi.getRetrofitInstance().create(I_LoginApi.class);
//                Call call = service.getInfo(input1,input2);
//                call.enqueue(new Callback<List<List<List<String>>>>() {
//                    @Override
//                    public void onResponse(Call<List<List<List<String>>>> call, Response<List<List<List<String>>>> response) {
//                        List<List<List<String>>> data = response.body();
//                        if(data == null){
//                            view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
//                            Log.v("Login","Fail000");
//                            showSortDialog(view,R.layout.dialog_error);
//                            return;
//                        } else{
//                            Log.v("Login","Success");
//                            SaveSharedPreference.setPrefData(getActivity(),data);
//                            SaveSharedPreference.setUserName(getActivity(),input1);
////                            if(SaveSharedPreference.getUserName(getActivity()).length() != 0){
////                                callApiGetFirst(SaveSharedPreference.getUserName(getActivity()));
////                            }
//                            startActivity(new Intent(view.getContext(), MainActivity.class));
//                            getActivity().finish();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<List<List<String>>>> call, Throwable t) {
//                            view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
//                            Log.v("Login","Fail");
//                            showSortDialog(view,R.layout.dialog_error);
//                    }
//                });

                new GetInformation(getContext(), new GetInformation.AsyncResponse() {
                    @Override
                    public void processFinish(Information output) {

                        if(output == null){
                            view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            Log.v("Login","Fail000");
                            showSortDialog(view,R.layout.dialog_error);
                            return;
                        } else{
                            Log.v("Login","Success");
                            SaveSharedPreference.setPrefData(getActivity(),output);
                            SaveSharedPreference.setUserName(getActivity(),input1);
                            SaveSharedPreference.setFullName(getActivity(),output.fullname);
//                            if(SaveSharedPreference.getUserName(getActivity()).length() != 0){
//                                callApiGetFirst(SaveSharedPreference.getUserName(getActivity()));
//                            }
                            startActivity(new Intent(view.getContext(), MainActivity.class));
                            getActivity().finish();
                        }
                    }
                }).execute("?username=" + input1 + "&password=" + input2 );

            }
        });
        return view;
    }

    public void showSortDialog(View v, int type){
        v.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        final Dialog message = new Dialog(getContext());
        message.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        message.requestWindowFeature(Window.FEATURE_NO_TITLE);
        message.setContentView(type);
        message.show();
        Button cancel = message.findViewById(R.id.cancel_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message.dismiss();
            }
        });
    }

//    public void callApiGetFirst(String mssv){
//        Router service = Api.getRetrofitInstance().create(Router.class);
//        Call call = service.getTimeTable(mssv);
//        call.enqueue(new Callback<List<List<String>>>() {
//            @Override
//            public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
//                List<List<String>> list = response.body();
//                Gson gson = new Gson();
//                String value = gson.toJson(list);
//                SaveSharedPreference.setCache(getActivity(),"timetable", value);
//            }
//
//            @Override
//            public void onFailure(Call<List<List<String>>> call, Throwable t) {
//                Log.v("getFirst","Fail");
//            }
//        });
//
//        Call call2 = service.getExamTime(mssv);
//        call2.enqueue(new Callback<List<List<String>>>() {
//            @Override
//            public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
//                List<List<String>> list = response.body();
//                Gson gson = new Gson();
//                String value = gson.toJson(list);
//                SaveSharedPreference.setCache(getActivity(),"examtime", value);
//            }
//
//            @Override
//            public void onFailure(Call<List<List<String>>> call, Throwable t) {
//                Log.v("getFirst","Fail");
//            }
//        });
//    }

}
