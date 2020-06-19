package com.example.uethub.ui.components.grades;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.uethub.MainActivity;
import com.example.uethub.R;
import com.example.uethub.models.GradesModel;
import com.example.uethub.services.grades.GetGrades;
import com.example.uethub.ui.components.PDFView.PDF;

import java.util.List;


public class GradesFragment extends Fragment implements AdapterView.OnItemClickListener {

    public List<List<String>>  list_all_grades;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Điểm thi");
        final View root = inflater.inflate(R.layout.fragment_grades, container, false);



        final EditText find_input = root.findViewById(R.id.find_input);
        Button find_all_btn = root.findViewById(R.id.find_all_btn);
        Button find_btn = root.findViewById(R.id.find_btn);


        drawGrades(list_all_grades,root);

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


                new GetGrades(getContext(), new GetGrades.AsyncResponse() {
                    @Override
                    public void processFinish(GradesModel output) {
                        if(output != null){
                            list_all_grades = output.subject_list;
                            drawGrades(output.subject_list,root);
                        }else  {
                            showSortDialog(root,R.layout.dialog_message);
                        }
                    }
                }).execute("/search?input="+ input + "&term=76");
            }
        });


        find_all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetGrades(getContext(), new GetGrades.AsyncResponse() {
                    @Override
                    public void processFinish(GradesModel output) {
                        if(output != null){
                            list_all_grades = output.subject_list;
                            drawGrades(output.subject_list,root);
                        }else  {
                            showSortDialog(root,R.layout.dialog_message);
                        }
                    }
                }).execute("?term=76");
            }
        });

        return root;
    }

    public void drawGrades(final List<List<String>>  result, View v){
        ListView listGrades = (ListView) v.findViewById(R.id.grade_list);
        GradesAdapter gradesAdapter = new GradesAdapter(v.getContext(),result);
        listGrades.setAdapter(gradesAdapter);
        listGrades.setOnItemClickListener(this);
    }



//    public void draw(final List<List<String>>  result, View v ){
//        LinearLayout grade_list_layout = v.findViewById(R.id.grade_list);
//        grade_list_layout.removeAllViews();
//
//        CardView[] cardViews = new CardView[result.size()];
//        LinearLayout[] layouts = new LinearLayout[result.size()];
//        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,180);
//        TextView[] textViews1 = new TextView[result.size()];
//        TextView[] textViews2 = new TextView[result.size()];
//        TextView[] textViews3 = new TextView[result.size()];
//        final String[] urlPdfs = new String[result.size()];
//
//        param.setMargins(10,10,10,10);
//        for(int i = 0 ; i < result.size(); ++i) {
//            cardViews[i] = new CardView(v.getContext());
//            cardViews[i].setLayoutParams(param);
//            cardViews[i].setRadius(10);
//            cardViews[i].setCardElevation(10);
//
//            layouts[i] = new LinearLayout(v.getContext());
//            layouts[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            layouts[i].setOrientation(LinearLayout.VERTICAL);
//            layouts[i].setPadding(10,10,10,10);
//            textViews1[i] = new TextView(v.getContext());
//            textViews1[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            textViews1[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
//            textViews1[i].setText(result.get(i).get(0));
//
//            textViews2[i] = new TextView(v.getContext());
//            textViews2[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            textViews2[i].setText(result.get(i).get(1));
//            textViews2[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
//
//            textViews3[i] = new TextView(v.getContext());
//            textViews3[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            textViews3[i].setText(result.get(i).get(3));
//            textViews3[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
//
//            layouts[i].addView(textViews1[i]);
//            layouts[i].addView(textViews2[i]);
//            layouts[i].addView(textViews3[i]);
//
//            cardViews[i].addView(layouts[i]);
//
//            grade_list_layout.addView(cardViews[i]);
//            urlPdfs[i] = "http://112.137.129.30/viewgrade/"+ result.get(i).get(2);
//
//            final int finalI = i;
//            cardViews[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Fragment fragment = new PDFViewerFragment();
//                    Bundle arguments = new Bundle();
//                    arguments.putString( "urlPdf" , urlPdfs[finalI]);
//                    arguments.putString("subjectName", result.get(finalI).get(0));
//                    fragment.setArguments(arguments);
//                    FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction()
//                            .add(R.id.fragment_grades, fragment)
//                            .addToBackStack("grades")
//                            .commit();
//
//                }
//            });
//
//        }
//    }

    public void showSortDialog(View v, int type){

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       List<String> grade = list_all_grades.get(position);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new PDF(grade.get(2)));
        transaction.addToBackStack("PDF");
        transaction.commit();
    }
}
