package com.example.uetplus2.api;


import android.util.Log;

import com.example.uetplus2.models.TimeTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class UetSupportApi {
    //////////////////////////////////////////////////////////////////////////////////
    public static List<TimeTable> getAll(String call) {
        String json = Rest.get(call);
        //Log.v("donate", "JSON RESULT : " + json);
        Type collectionType = new TypeToken<List<TimeTable>>(){}.getType();

        return new Gson().fromJson(json, collectionType);
    }
    //////////////////////////////////////////////////////////////////////////////////
    public static List<TimeTable> get(String call,String mssv) {
        String json = Rest.get(call + "?mssv=" + mssv);
        Log.v("TimeTable", "JSON RESULT : " + json);
        Type objType = new TypeToken<List<TimeTable>>(){}.getType();

        return new Gson().fromJson(json, objType);
    }

    public static List<List<String>> getNews(String call){
        String json = LoginRest.get("/news" + call);
        Log.v("TimeTable", "JSON RESULT : " + json);
        Type objType = new TypeToken<List<List<String>>>(){}.getType();

        return new Gson().fromJson(json, objType);
    }

    //////////////////////////////////////////////////////////////////////////////////
    public static String deleteAll(String call) {
        return Rest.delete(call);
    }
    //////////////////////////////////////////////////////////////////////////////////
    public static String delete(String call, String id) {
        return Rest.delete(call + "/" + id);
    }
    //////////////////////////////////////////////////////////////////////////////////
    public static String insert(String call,TimeTable donation) {
        Type objType = new TypeToken<TimeTable>(){}.getType();
        String json = new Gson().toJson(donation, objType);

        return Rest.post(call,json);
    }
}
