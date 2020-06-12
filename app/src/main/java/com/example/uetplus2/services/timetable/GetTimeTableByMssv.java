package com.example.uetplus2.services.timetable;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.uetplus2.services.ServicesApi;
import com.example.uetplus2.models.TimeTable;

import java.util.List;

public class GetTimeTableByMssv extends AsyncTask<String, Void, List<TimeTable>> {

    protected ProgressDialog dialog;
    protected Context context;

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(List<TimeTable> output);
    }

    public GetTimeTableByMssv.AsyncResponse delegate = null;
    public GetTimeTableByMssv(Context context, GetTimeTableByMssv.AsyncResponse delegate)
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
    protected List<TimeTable> doInBackground(String... params) {
        try {
            Log.v("TimeTable", "Get timetable by mssv");
            return (List<TimeTable>) ServicesApi.get((String) params[0], (String) params[1]);
        }
        catch (Exception e) {
            Log.v("TimeTable", "ERROR : " + e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<TimeTable> result) {
        super.onPostExecute(result);
        delegate.processFinish(result);
        if (dialog.isShowing())
            dialog.dismiss();
    }
}
