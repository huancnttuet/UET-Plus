package com.example.uetplus2.ui.menu.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.uetplus2.R;
import com.example.uetplus2.cache.SaveSharedPreference;
import com.example.uetplus2.models.TimeTable;
import com.example.uetplus2.services.extensions.DownloadImageTask;
import com.example.uetplus2.services.news.GetNews;
import com.example.uetplus2.ui.Base;
import com.example.uetplus2.ui.components.examtime.ExamTimeFragment;
import com.example.uetplus2.ui.components.grades.GradesFragment;
import com.example.uetplus2.ui.components.timetable.TimeTableFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Base {

    SliderView sliderView;
    private SliderAdapterExample adapter;
    LinearLayout snLayout ;
    LinearLayout schedule_btn, examtime_btn, grades_btn;
    TextView helloWorld;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        helloWorld =root.findViewById(R.id.hello_world);
        String fullname = SaveSharedPreference.getFullName(getContext());
        helloWorld.setText("Xin chào, "+fullname);
        sliderView = root.findViewById(R.id.imageSlider);
        snLayout = root.findViewById(R.id.student_news);
        adapter = new SliderAdapterExample(getContext());

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
                Log.v("sl ","clcik");
            }
        });

        schedule_btn = root.findViewById(R.id.schedule_btn);
        schedule_btn.setClickable(true);
        examtime_btn = root.findViewById(R.id.examtime_btn);
        examtime_btn.setClickable(true);
        grades_btn = root.findViewById(R.id.grades_btn);
        grades_btn.setClickable(true);

        schedule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment timetableFragment = new TimeTableFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, timetableFragment,"TIMETABLE_TAG");
                transaction.addToBackStack("TIMETABLE_TAG");
                transaction.commit();
            }
        });

        examtime_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ExamTimeFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, fragment,"EXAMTIME_TAG");
                transaction.addToBackStack("EXAMTIME_TAG");
                transaction.commit();
            }
        });

        grades_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new GradesFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, fragment,"GRADES_TAG");
                transaction.addToBackStack("GRADES_TAG");
                transaction.commit();
            }
        });

        final SwipeRefreshLayout pullToRefresh = getActivity().findViewById(R.id.pullToRefresh);
        pullToRefresh.setColorSchemeResources(R.color.calendar_today,R.color.colorPrimary,R.color.colorLightRed);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                pullToRefresh.setRefreshing(false);
            }
        });

        String studentNewsCache = SaveSharedPreference.getCache(getActivity(),"studentNews");
        String hotNewsCache = SaveSharedPreference.getCache(getActivity(),"hotNews");

        if(studentNewsCache.length() != 0 || hotNewsCache.length() != 0) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<List<String>>>() {}.getType();

            List<List<String>> list1 = gson.fromJson(studentNewsCache, listType);
            List<List<String>> list2 = gson.fromJson(hotNewsCache, listType);
            loadStudentNewsCardView(list1);
            for (List<String> news : list2){
                addUrlImage(news.get(1), news.get(0));
            }
        }
        else {
            refreshData();
        }

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }


    public void refreshData(){
        new GetNews(getContext(), new GetNews.AsyncResponse() {
            @Override
            public void processFinish(List<List<String>> student_news) {
                if(student_news != null){
                    Gson gson = new Gson();
                    String value = gson.toJson(student_news);
                    SaveSharedPreference.setCache(getActivity(), "studentNews", value);
                    loadStudentNewsCardView(student_news);
                }
            }
        }).execute("/getstudentnews");

        new GetNews(getContext(),new GetNews.AsyncResponse(){
            @Override
            public void processFinish(List<List<String>> hot_news) {
                if(hot_news != null){
                    Gson gson = new Gson();
                    String value = gson.toJson(hot_news);
                    SaveSharedPreference.setCache(getActivity(), "hotNews", value);
                    for (List<String> news : hot_news){
                        addUrlImage(news.get(1), news.get(0));
                    }
                }
            }
        }).execute("/getdashboard");
    }

    public void addUrlImage(String url, String description){
        SliderItem sliderItem = new SliderItem();
        sliderItem.setDescription(description);
        sliderItem.setImageUrl(url);
        adapter.addItem(sliderItem);
    }



    public void loadStudentNewsCardView(List<List<String>> studentNewsList){
        int width = (int) getResources().getDimension(R.dimen.cardview_width);
        int height = (int) getResources().getDimension(R.dimen.cardview_width);
        int marginSize = (int) getResources().getDimension(R.dimen.margin_cardview);
        int heightImage = (int) getResources().getDimension(R.dimen.height_image);
        LinearLayout.LayoutParams param_default = new LinearLayout.LayoutParams(width,height);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, height);
        LinearLayout.LayoutParams param_image = new LinearLayout.LayoutParams(width,2*height/3);
        LinearLayout.LayoutParams param_text = new LinearLayout.LayoutParams(width, height);
        param_default.setMargins(marginSize,marginSize,marginSize,marginSize);

        for (List<String> sn: studentNewsList
             ) {
            CardView cardView = new CardView(getContext());
            cardView.setLayoutParams(param_default);
            cardView.setRadius(12f);
            snLayout.addView(cardView);
            LinearLayout lnLayout = new LinearLayout(getContext());
            lnLayout.setLayoutParams(param);
            lnLayout.setOrientation(LinearLayout.VERTICAL);

            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(param_image);
            imageView.setImageResource(R.drawable.uet_icon2);
            new DownloadImageTask(imageView).execute(sn.get(1));
            cardView.addView(imageView);

            TextView textView = new TextView(getContext());

            textView.setLayoutParams(param_text);
            textView.setTextSize(19f);
            textView.setText(sn.get(0));
            textView.setGravity(Gravity.BOTTOM);
            textView.setPadding(marginSize,marginSize,marginSize,marginSize);
            cardView.addView(textView);
        }
    }

//    public void renewItems(View view) {
//        List<SliderItem> sliderItemList = new ArrayList<>();
//        //dummy data
//        for (int i = 0; i < 5; i++) {
//            SliderItem sliderItem = new SliderItem();
//            sliderItem.setDescription("Slider Item " + i);
//            if (i % 2 == 0) {
//                sliderItem.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
//            } else {
//                sliderItem.setImageUrl("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
//            }
//            sliderItemList.add(sliderItem);
//        }
//        adapter.renewItems(sliderItemList);
//    }
//
//    public void removeLastItem(View view) {
//        if (adapter.getCount() - 1 >= 0)
//            adapter.deleteItem(adapter.getCount() - 1);
//    }
//
//    public void addNewItem(View view) {
//        SliderItem sliderItem = new SliderItem();
//        sliderItem.setDescription("Slider Item Added Manually");
//        sliderItem.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
//        adapter.addItem(sliderItem);
//    }

}
