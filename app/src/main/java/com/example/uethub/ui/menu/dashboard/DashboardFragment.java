package com.example.uethub.ui.menu.dashboard;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.bumptech.glide.Glide;
import com.example.uethub.R;
import com.example.uethub.cache.SaveSharedPreference;
import com.example.uethub.models.NewsModel;
import com.example.uethub.services.extensions.DownloadImageTask;
import com.example.uethub.services.news.GetDashboardNews;

import com.example.uethub.ui.Base;
import com.example.uethub.ui.components.examtime.ExamTimeFragment;
import com.example.uethub.ui.components.grades.GradesFragment;
import com.example.uethub.ui.components.news.NewsFragment;
import com.example.uethub.ui.components.timetable.TimeTableFragment;
import com.example.uethub.ui.components.webview.WebViewFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.lang.reflect.Type;
import java.util.List;

public class DashboardFragment extends Base implements SwipeRefreshLayout.OnRefreshListener {

    SliderView sliderView;
    private SliderAdapterExample adapter;
    LinearLayout snLayout ;
    LinearLayout schedule_btn, examtime_btn, grades_btn;
    TextView helloWorld;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

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
            }
        });

        schedule_btn = root.findViewById(R.id.schedule_btn);
        schedule_btn.setClickable(true);
        examtime_btn = root.findViewById(R.id.examtime_btn);
        examtime_btn.setClickable(true);
        grades_btn = root.findViewById(R.id.grades_btn);
        grades_btn.setClickable(true);
        TextView news_all_btn = root.findViewById(R.id.news_all_btn);
        news_all_btn.setOnClickListener(v -> {
            Fragment timetableFragment = new NewsFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, timetableFragment,"NEWS_TAG");
            transaction.addToBackStack("NEWS_TAG");
            transaction.commit();
        });
        schedule_btn.setOnClickListener(v -> {
            Fragment timetableFragment = new TimeTableFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, timetableFragment,"TIMETABLE_TAG");
            transaction.addToBackStack("TIMETABLE_TAG");
            transaction.commit();
        });

        examtime_btn.setOnClickListener(v -> {
            Fragment fragment = new ExamTimeFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment,"EXAMTIME_TAG");
            transaction.addToBackStack("EXAMTIME_TAG");
            transaction.commit();
        });

        grades_btn.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new GradesFragment());
            transaction.addToBackStack("GRADES_TAG");
            transaction.commit();
        });

        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.pullToRefresh);
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
            Type listType = new TypeToken<List<NewsModel>>() {}.getType();

            List<NewsModel> list1 = gson.fromJson(studentNewsCache, listType);
            List<NewsModel> list2 = gson.fromJson(hotNewsCache, listType);
            loadStudentNewsCardView(list1);
            for (NewsModel news : list2){
                addUrlImage(news.image, news.name, news.url);
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
        new GetDashboardNews(getContext(), news_list -> {
            if(news_list != null){
                Gson gson = new Gson();
                List<NewsModel> hot_news = news_list.get(0);
                List<NewsModel> student_news = news_list.get(1);
                String value1 = gson.toJson(hot_news);
                String value2 = gson.toJson(student_news);
                SaveSharedPreference.setCache(getActivity(), "hotNews", value1);
                SaveSharedPreference.setCache(getActivity(), "studentNews", value2);

                loadStudentNewsCardView(student_news);
                for (NewsModel news : hot_news){
                    addUrlImage(news.image, news.name,news.url);
                }
            }
        }).execute("/getdashboard");
    }

    public void addUrlImage(String imageUrl, String description, String url){
        SliderItem sliderItem = new SliderItem();
        sliderItem.setDescription(description);
        sliderItem.setImageUrl(imageUrl);
        sliderItem.setUrl(url);
        adapter.addItem(sliderItem);
    }


    public void loadStudentNewsCardView(List<NewsModel> studentNewsList){
        int width = (int) getResources().getDimension(R.dimen.cardview_width);
        int height = (int) getResources().getDimension(R.dimen.cardview_width);
        int marginSize = (int) getResources().getDimension(R.dimen.margin_cardview);
        int heightImage = (int) getResources().getDimension(R.dimen.height_image);
        LinearLayout.LayoutParams param_default = new LinearLayout.LayoutParams(width,height);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, height);
        LinearLayout.LayoutParams param_image = new LinearLayout.LayoutParams(width,2*height/3);
        LinearLayout.LayoutParams param_text = new LinearLayout.LayoutParams(width, height);
        param_default.setMargins(marginSize,marginSize,marginSize,marginSize);

        for (NewsModel news: studentNewsList
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
            Glide.with(getContext())
                    .load(news.image)
                    .centerCrop()
                    .placeholder(R.drawable.uet_icon2)
                    .into(imageView);
//            imageView.setImageResource(R.drawable.uet_icon2);
//            new DownloadImageTask(imageView).execute(news.image);
            cardView.addView(imageView);

            TextView textView = new TextView(getContext());

            textView.setLayoutParams(param_text);
            textView.setTextSize(19f);
            textView.setText(news.name);
            textView.setGravity(Gravity.BOTTOM);
            textView.setPadding(marginSize,marginSize,marginSize,marginSize);
            cardView.addView(textView);
            final String url = news.url;
            cardView.setBackground(getResources().getDrawable(R.drawable.backgroud_button));
            cardView.setOnClickListener(v -> {
                WebViewFragment fragment = new WebViewFragment();
                Bundle args = new Bundle();
                args.putString("url", url);
                fragment.setArguments(args);
                FragmentTransaction transaction = ((FragmentActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack("WEBVIEW_TAG");
                transaction.commit();
            });
        }
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getContext(), "Refresh", Toast.LENGTH_SHORT).show();
        refreshData();
    }
}
