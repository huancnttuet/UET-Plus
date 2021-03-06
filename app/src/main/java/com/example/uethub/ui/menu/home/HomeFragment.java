package com.example.uethub.ui.menu.home;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.uethub.MainActivity;
import com.example.uethub.R;
import com.example.uethub.cache.SaveSharedPreference;
import com.example.uethub.models.Information;
import com.example.uethub.ui.components.webview.WebViewFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Môn học");
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        String data = SaveSharedPreference.getPrefData(getActivity());

        Gson gson = new Gson();
        Type listType = new TypeToken<Information>() {}.getType();
        Information inforData = gson.fromJson(data,listType);
        List<List<String>> courses = inforData.courses;
        LinearLayout coursesView = view.findViewById(R.id.courses_list_view);
        int width = (int) getResources().getDimension(R.dimen.home_card_view_width);
        int height = (int) getResources().getDimension(R.dimen.home_card_view_height);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
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
            LinearLayout.LayoutParams paramCard = new LinearLayout.LayoutParams(width,height);
            cardViewCode.setLayoutParams(paramCard);
            cardViewCode.setRadius(9);
            cardViewCode.setCardBackgroundColor(Color.parseColor("#FF5555"));

            TextView codeText = new TextView(view.getContext());
            codeText.setText(courseCode);
            codeText.setTextColor(Color.WHITE);
            codeText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,10);
            codeText.setGravity(Gravity.CENTER);

            TextView nameText = new TextView(view.getContext());
            nameText.setText(courseName);
            nameText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,19);
            nameText.setGravity(Gravity.CENTER);
            cardViewCode.addView(codeText);
            wrapper.addView(cardViewCode);
            wrapper.addView(nameText);

            cardView.addView(wrapper);
            coursesView.addView(cardView);
            cardView.setBackground(getResources().getDrawable(R.drawable.backgroud_button));
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    WebViewFragment fragment = new WebViewFragment();
                    Bundle args = new Bundle();
                    args.putString("url", courseUrl);
                    fragment.setArguments(args);
                    FragmentTransaction transaction = ((FragmentActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, fragment);
                    transaction.addToBackStack("WEBVIEW_TAG");
                    transaction.commit();
                }
            });

        }
        return view;
    }
}
