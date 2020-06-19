package com.example.uethub.services.examtime;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.example.uethub.models.ExamTimeModel;
import com.example.uethub.services.ServicesApi;

import java.util.List;

public class GetExamtimeByMssv extends AsyncTask<String, Void, List<ExamTimeModel>> {

    protected ProgressDialog dialog;
    protected Context context;

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(List<ExamTimeModel> output);
    }

    public AsyncResponse delegate = null;
    public GetExamtimeByMssv(Context context, AsyncResponse delegate)
    {
        this.context = context;
        this.delegate = delegate;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog = new ProgressDialog(context, 3);
        this.dialog.setMessage("Đang tải");
        this.dialog.show();
    }

    @Override
    protected List<ExamTimeModel> doInBackground(String... params) {
        try {
            Log.v("Exam Time", "Get examtime by mssv");
            return (List<ExamTimeModel>) ServicesApi.getExamtimeByMssv((String) params[0]);
        }
        catch (Exception e) {
            Log.v("Examtime", "ERROR : " + e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<ExamTimeModel> result) {
        super.onPostExecute(result);
        delegate.processFinish(result);
        if (dialog.isShowing())
            dialog.dismiss();
    }
}
