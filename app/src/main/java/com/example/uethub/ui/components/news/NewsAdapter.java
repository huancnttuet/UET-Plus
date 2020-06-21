package com.example.uethub.ui.components.news;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.uethub.R;
import com.example.uethub.models.NewsModel;
import com.example.uethub.services.extensions.DownloadImageTask;
import com.example.uethub.ui.components.webview.WebViewFragment;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends BaseAdapter {

    private Context context;
    private List<NewsModel> news_list;

    public NewsAdapter(Context context, List<NewsModel> news_list){
        this.context = context;
        this.news_list  = news_list;

    }

    public void setNews_list(List<NewsModel> news_list){
        this.news_list = news_list;
    }


    @Override
    public int getCount() {
        if(news_list != null) return news_list.size();
        return 0;
    }

    @Override
    public NewsModel getItem(int position) {
        return news_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        @SuppressLint("ViewHolder") View root = inflater.inflate(R.layout.news_item,parent,false);

        NewsModel newsModel = news_list.get(position);

        ImageView imageView =  root.findViewById(R.id.imageView);
        TextView textView = root.findViewById(R.id.textView);

        Glide.with(root)
                .load(newsModel.image)
                .centerCrop()
                .placeholder(R.drawable.uet_icon2)
                .into(imageView);
//        new DownloadImageTask(imageView).execute(newsModel.image);
        textView.setText(newsModel.name);
        LinearLayout layout = root.findViewById(R.id.news_item);

        layout.setOnClickListener(v -> {
            WebViewFragment fragment = new WebViewFragment();
            Bundle args = new Bundle();
            args.putString("url", newsModel.url);
            fragment.setArguments(args);
            FragmentTransaction transaction = ((FragmentActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack("WEBVIEW_TAG");
            transaction.commit();
        });

        return root;
    }
}
