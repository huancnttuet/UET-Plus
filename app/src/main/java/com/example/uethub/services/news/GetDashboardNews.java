package com.example.uethub.services.news;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.example.uethub.models.NewsModel;
import com.example.uethub.services.ServicesApi;

import java.util.List;

public class GetDashboardNews extends AsyncTask<String, Void, List<List<NewsModel>>> {

    protected ProgressDialog dialog;
    protected Context context;

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(List<List<NewsModel>> output);
    }

    public AsyncResponse delegate = null;
    public GetDashboardNews(Context context, AsyncResponse delegate)
    {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog = new ProgressDialog(context, 2);
        this.dialog.setMessage("Đang tải");
        this.dialog.show();
    }

    @Override
    protected List<List<NewsModel>> doInBackground(String... params) {
        try {
            Log.v("News", "Get newsfeed");
            return (List<List<NewsModel>>) ServicesApi.getDashboardNews((String) params[0]);
        }
        catch (Exception e) {
            Log.v("News", "ERROR : " + e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<List<NewsModel>> result) {
        super.onPostExecute(result);
        delegate.processFinish(result);
        if (dialog.isShowing())
            dialog.dismiss();
    }
}

