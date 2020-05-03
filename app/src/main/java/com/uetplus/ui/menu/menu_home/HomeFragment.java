package com.uetplus.ui.menu.menu_home;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uetplus.R;

import com.uetplus.ui.MainActivity;
import com.uetplus.ui.SaveSharedPreference;

import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Home");
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        String data = SaveSharedPreference.getPrefData(getActivity());
        Gson gson = new Gson();
        Type listType = new TypeToken<List<List<List<String>>>>() {}.getType();
        List<List<List<String>>> list = gson.fromJson(data,listType);
        List<List<String>> courses = list.get(1);
        LinearLayout coursesView = view.findViewById(R.id.courses_list_view);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,134);
        LinearLayout.LayoutParams paramWrapper = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param.setMargins(20,10,20,10);
        for(int i = 0; i < courses.size(); i++){
            List<String> ele = courses.get(i);
            String courseName = ele.get(0);
            final String courseUrl = ele.get(1);
            String courseCode = "";
            Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(courseName);
            while(m.find()) {
               courseCode = m.group(1);
               String[] code = courseCode.split("_");
               if(code.length == 3){
                   courseCode = code[1] + " " + code[2];
               }
               if(code.length == 2){
                   courseCode = code[1];
               }
            }
            CardView cardView = new CardView(view.getContext());
            cardView.setLayoutParams(param);
            cardView.setRadius(9);
            LinearLayout wrapper = new LinearLayout(view.getContext());
            wrapper.setLayoutParams(paramWrapper);
            wrapper.setGravity(Gravity.CENTER);
            wrapper.setOrientation(LinearLayout.HORIZONTAL);

            CardView cardViewCode = new CardView(view.getContext());
            LinearLayout.LayoutParams paramCard = new LinearLayout.LayoutParams(134,134);
            cardViewCode.setLayoutParams(paramCard);
            cardViewCode.setRadius(9);
            cardViewCode.setCardBackgroundColor(Color.parseColor("#FF5555"));

            TextView codeText = new TextView(view.getContext());
            codeText.setText(courseCode);
            codeText.setTextColor(Color.WHITE);
            codeText.setTextSize(10);
            codeText.setGravity(Gravity.CENTER);

            TextView nameText = new TextView(view.getContext());
            nameText.setText(courseName);
            nameText.setTextSize(19);
            nameText.setGravity(Gravity.CENTER);
            cardViewCode.addView(codeText);
            wrapper.addView(cardViewCode);
            wrapper.addView(nameText);

            cardView.addView(wrapper);
            coursesView.addView(cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(courseUrl); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });

        }
        return view;
    }
}
