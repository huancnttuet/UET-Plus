package com.example.uethub.services.device;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.uethub.models.DeviceModel;
import com.example.uethub.models.ResponseModel;
import com.example.uethub.services.ServicesApi;



public class InsertMyDevice extends AsyncTask<DeviceModel, Void, ResponseModel> {

    protected ProgressDialog dialog;

    // you may separate this or combined to caller class.
    public interface AsyncResponse {
        void processFinish(ResponseModel output);
    }

    public AsyncResponse delegate = null;
    public InsertMyDevice(AsyncResponse delegate)
    {
        this.delegate = delegate;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ResponseModel doInBackground(DeviceModel... params) {
        try {
            Log.v("Send My Device", "Send My Device");
            return (ResponseModel) ServicesApi.insertMyDevice((DeviceModel) params[0]);
        }
        catch (Exception e) {
            Log.v("Send My Device", "ERROR : " + e);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ResponseModel result) {
        super.onPostExecute(result);
        delegate.processFinish(result);

    }
}
