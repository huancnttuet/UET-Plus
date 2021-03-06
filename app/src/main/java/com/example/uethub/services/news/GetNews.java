package com.example.uethub.services.news;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.example.uethub.models.NewsModel;
import com.example.uethub.services.ServicesApi;

import java.util.List;

public class GetNews extends AsyncTask<String, Void, List<NewsModel>> {

    protected ProgressDialog dialog;


    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(List<NewsModel> output);
    }

    public AsyncResponse delegate = null;
    public GetNews( AsyncResponse delegate)
    {

        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected List<NewsModel> doInBackground(String... params) {
        try {
            Log.v("News", "Get newsfeed");
            return (List<NewsModel>) ServicesApi.getNews((String) params[0]);
        }
        catch (Exception e) {
            Log.v("News", "ERROR : " + e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<NewsModel> result) {
        super.onPostExecute(result);
        delegate.processFinish(result);

    }
}

