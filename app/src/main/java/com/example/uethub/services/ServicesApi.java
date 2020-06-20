package com.example.uethub.services;


import android.util.Log;


import com.example.uethub.api.LoginRest;
import com.example.uethub.api.SupportRest;
import com.example.uethub.api.UETRest;
import com.example.uethub.models.DeviceModel;
import com.example.uethub.models.ExamTimeModel;
import com.example.uethub.models.GradesModel;
import com.example.uethub.models.Information;
import com.example.uethub.models.ResponseModel;
import com.example.uethub.models.TimeTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ServicesApi {
    //////////////////////////////////////////////////////////////////////////////////
    public static List<TimeTable> getAll(String call) {
        String json = SupportRest.get(call);
        //Log.v("donate", "JSON RESULT : " + json);
        Type collectionType = new TypeToken<List<TimeTable>>(){}.getType();

        return new Gson().fromJson(json, collectionType);
    }
    //////////////////////////////////////////////////////////////////////////////////
    public static List<TimeTable> getTimeTableByMssv(String call,String mssv) {
        String json = SupportRest.get(call + "?mssv=" + mssv);
        Log.v("TimeTable", "JSON RESULT : " + json);
        Type objType = new TypeToken<List<TimeTable>>(){}.getType();

        return new Gson().fromJson(json, objType);
    }

    public static List<List<String>> getNews(String call){
        String json = LoginRest.get("/news" + call);
        Log.v("News", "JSON RESULT : " + json);
        Type objType = new TypeToken<List<List<String>>>(){}.getType();

        return new Gson().fromJson(json, objType);
    }

    public static Information getInformation(String call){
        String json = LoginRest.get("/information" + call);
        Log.v("Information", "JSON RESULT : " + json);
        Type objType = new TypeToken<Information>(){}.getType();

        return new Gson().fromJson(json, objType);
    }
    public static GradesModel getGrades(String call){
        String json = SupportRest.get("/score" + call);
        Log.v("Grades", "JSON RESULT : " + json);
        Type objType = new TypeToken<GradesModel>(){}.getType();

        return new Gson().fromJson(json, objType);
    }

    public static List<ExamTimeModel> getExamtimeByMssv(String mssv){
        String json = SupportRest.get("/exam-time?mssv=" + mssv);
        Log.v("Examtime", "JSON RESULT : " + json);
        Type objType = new TypeToken<List<ExamTimeModel>>(){}.getType();

        return new Gson().fromJson(json, objType);
    }

    //////////////////////////////////////////////////////////////////////////////////
    public static String deleteAll(String call) {
        return SupportRest.delete(call);
    }
    //////////////////////////////////////////////////////////////////////////////////
    public static String delete(String call, String id) {
        return SupportRest.delete(call + "/" + id);
    }
    //////////////////////////////////////////////////////////////////////////////////
    public static ResponseModel insertMyDevice(DeviceModel deviceModel) {
        Type objType = new TypeToken<DeviceModel>(){}.getType();
        String json = new Gson().toJson(deviceModel, objType);
        String response = UETRest.post("/device",json);
        Type objType2 = new TypeToken<ResponseModel>(){}.getType();
        return new Gson().fromJson(response,objType2);
    }

    public static ResponseModel deleteMyDevice(DeviceModel deviceModel) {
        Type objType = new TypeToken<DeviceModel>(){}.getType();
        String json = new Gson().toJson(deviceModel, objType);
        String response = UETRest.post("/device/delete",json);
        Type objType2 = new TypeToken<ResponseModel>(){}.getType();
        return new Gson().fromJson(response,objType2);
    }
}
