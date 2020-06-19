package com.example.uethub.services.grades;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.uethub.models.GradesModel;
import com.example.uethub.services.ServicesApi;


public class GetGrades extends AsyncTask<String, Void, GradesModel> {
    protected ProgressDialog dialog;
    protected Context context;

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(GradesModel output);
    }

    public GetGrades.AsyncResponse delegate = null;
    public GetGrades(Context context, GetGrades.AsyncResponse delegate)
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
    protected GradesModel doInBackground(String... params) {
        try {
            Log.v("Login", "Logging in");
            return (GradesModel) ServicesApi.getGrades((String) params[0]);
        }
        catch (Exception e) {
            Log.v("Login", "ERROR : " + e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(GradesModel result) {
        super.onPostExecute(result);
        delegate.processFinish(result);
        if (dialog.isShowing())
            dialog.dismiss();
    }
}
