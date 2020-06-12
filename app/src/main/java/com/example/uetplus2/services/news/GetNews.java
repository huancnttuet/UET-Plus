package com.example.uetplus2.services.news;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.uetplus2.api.ServicesApi;

import java.util.List;

public class GetNews extends AsyncTask<String, Void, List<List<String>>> {

    protected ProgressDialog dialog;
    protected Context context;

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(List<List<String>> output);
    }

    public AsyncResponse delegate = null;
    public GetNews(Context context, AsyncResponse delegate)
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
    protected List<List<String>> doInBackground(String... params) {
        try {
            Log.v("News", "Get newsfeed");
            return (List<List<String>>) ServicesApi.getNews((String) params[0]);
        }
        catch (Exception e) {
            Log.v("News", "ERROR : " + e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<List<String>> result) {
        super.onPostExecute(result);
        delegate.processFinish(result);
        if (dialog.isShowing())
            dialog.dismiss();
    }
}

