package com.uetplus.ui.menu.grades;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.uetplus.R;
import com.uetplus.ui.MainActivity;
import com.uetplus.ui.services.Api;
import com.uetplus.ui.services.Router;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GradesFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Điểm thi");
        final View root = inflater.inflate(R.layout.fragment_grades, container, false);

        final EditText find_input = root.findViewById(R.id.find_input);
        Button find_all_btn = root.findViewById(R.id.find_all_btn);
        Button find_btn = root.findViewById(R.id.find_btn);

        root.findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = find_input.getText().toString();
                if (input.matches("")) {
                    Toast.makeText(v.getContext(), "Bạn chưa nhập gì cả :'D", Toast.LENGTH_SHORT).show();
                    return;
                }
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                if(getActivity().getCurrentFocus() != null)
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                root.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                Router service = Api.getRetrofitInstance().create(Router.class);
                Call<List<List<List<String>>>> call = service.searchGrades(input,76,0);
                call.enqueue(new Callback<List<List<List<String>>>>() {
                    @Override
                    public void onResponse(Call<List<List<List<String>>>> call, Response<List<List<List<String>>>> response) {
                        Log.d("Notify", "Success");
                        root.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        List<List<List<String>>> result = response.body();
                        if(result.size() == 0){
                            showSortDialog(root,R.layout.dialog_message);
                        }
                        draw(result.get(0),root);
                    }
                    @Override
                    public void onFailure(Call<List<List<List<String>>>> call, Throwable t) {
                        Log.d("Notify", "Failed");
                        showSortDialog(root,R.layout.dialog_error);

                    }
                });

            }
        });


        find_all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                root.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                Router service = Api.getRetrofitInstance().create(Router.class);
                Call<List<List<String>>> call = service.getScore(76);
                call.enqueue(new Callback<List<List<String>>>() {
                    @Override
                    public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
                        Log.d("Notify", "Success");
                        root.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        List<List<String>> result = response.body();
                        if(result == null || result.size() == 0){
                            showSortDialog(root,R.layout.dialog_error);
                            return;
                        }
                        draw(result,root);
                    }
                    @Override
                    public void onFailure(Call<List<List<String>>> call, Throwable t) {
                        Log.d("Notify", "Failed");
                        showSortDialog(root,R.layout.dialog_error);
                    }
                });
            }
        });

        return root;
    }



    public void draw(final List<List<String>>  result, View v ){
        LinearLayout grade_list_layout = v.findViewById(R.id.grade_list);
        grade_list_layout.removeAllViews();

        CardView[] cardViews = new CardView[result.size()];
        LinearLayout[] layouts = new LinearLayout[result.size()];
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,180);
        TextView[] textViews1 = new TextView[result.size()];
        TextView[] textViews2 = new TextView[result.size()];
        TextView[] textViews3 = new TextView[result.size()];
        final String[] urlPdfs = new String[result.size()];

        param.setMargins(10,10,10,10);
        for(int i = 0 ; i < result.size(); ++i) {
            cardViews[i] = new CardView(v.getContext());
            cardViews[i].setLayoutParams(param);
            cardViews[i].setRadius(10);
            cardViews[i].setCardElevation(10);

            layouts[i] = new LinearLayout(v.getContext());
            layouts[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            layouts[i].setOrientation(LinearLayout.VERTICAL);
            layouts[i].setPadding(10,10,10,10);
            textViews1[i] = new TextView(v.getContext());
            textViews1[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textViews1[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
            textViews1[i].setText(result.get(i).get(0));

            textViews2[i] = new TextView(v.getContext());
            textViews2[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textViews2[i].setText(result.get(i).get(1));
            textViews2[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);

            textViews3[i] = new TextView(v.getContext());
            textViews3[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textViews3[i].setText(result.get(i).get(3));
            textViews3[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);

            layouts[i].addView(textViews1[i]);
            layouts[i].addView(textViews2[i]);
            layouts[i].addView(textViews3[i]);

            cardViews[i].addView(layouts[i]);

            grade_list_layout.addView(cardViews[i]);
            urlPdfs[i] = "http://112.137.129.30/viewgrade/"+ result.get(i).get(2);

            final int finalI = i;
            cardViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Fragment fragment = new PDFViewerFragment();
                    Bundle arguments = new Bundle();
                    arguments.putString( "urlPdf" , urlPdfs[finalI]);
                    arguments.putString("subjectName", result.get(finalI).get(0));
                    fragment.setArguments(arguments);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .add(R.id.fragment_grades, fragment)
                            .addToBackStack("grades")
                            .commit();

                }
            });

        }
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

}
