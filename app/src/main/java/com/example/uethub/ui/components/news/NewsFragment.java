package com.example.uethub.ui.components.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.uethub.MainActivity;
import com.example.uethub.R;
import com.example.uethub.cache.SaveSharedPreference;
import com.example.uethub.models.NewsModel;
import com.example.uethub.services.news.GetNews;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NewsFragment extends Fragment {

    Boolean flag_loading = false;
    List<NewsModel> news_list;
    NewsAdapter adapter;
    ListView listView;
    int page = 2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,container,false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Tin tức");

        String studentNewsCache = SaveSharedPreference.getCache(getActivity(),"studentNews");

        if(studentNewsCache.length() != 0){
            Gson gson = new Gson();
            Type listType = new TypeToken<List<NewsModel>>() {}.getType();
            news_list = gson.fromJson(studentNewsCache, listType);

            adapter = new NewsAdapter(view.getContext(),news_list);
            listView = view.findViewById(R.id.news_list_view);
            listView.setAdapter(adapter);
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                    {
                        if(!flag_loading){
                            flag_loading = true;
                            additems();
                        }

                    }
                }
            });
        }

        return view;
    }

    public void additems(){

        new GetNews(output -> {
            if(output != null){
                page++;
                news_list.addAll(output);
                adapter.notifyDataSetChanged();
            }
            flag_loading = false;

        }).execute("/getstudentnews?page=" + page);
    }
}
