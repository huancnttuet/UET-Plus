package com.example.uetplus2.services.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.uetplus2.services.ServicesApi;
import com.example.uetplus2.models.Information;

public class GetInformation extends AsyncTask<String, Void, Information> {

    protected ProgressDialog dialog;
    protected Context context;

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(Information output);
    }

    public GetInformation.AsyncResponse delegate = null;
    public GetInformation(Context context, GetInformation.AsyncResponse delegate)
    {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog = new ProgressDialog(context, 4);
        this.dialog.setMessage("Đang tải");
        this.dialog.show();
    }

    @Override
    protected Information doInBackground(String... params) {
        try {
            Log.v("Login", "Logging in");
            return (Information) ServicesApi.getInformation((String) params[0]);
        }
        catch (Exception e) {
            Log.v("Login", "ERROR : " + e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Information result) {
        super.onPostExecute(result);
        delegate.processFinish(result);
        if (dialog.isShowing())
            dialog.dismiss();
    }
}


