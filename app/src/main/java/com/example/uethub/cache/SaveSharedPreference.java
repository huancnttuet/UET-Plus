package com.example.uethub.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.uethub.models.Information;
import com.google.gson.Gson;

public class SaveSharedPreference
{
    static final String PREF_USER_NAME= "username";
    static final String PREF_FULL_NAME= "fullname";
    static final String PREF_DATA = "data";
    static final String PREF_TIME_TABLE = "timetable";
    static final String PREF_EXAM_TIME= "examtime";
    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setPrefData(Context ctx, Information data)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString(PREF_DATA, json);
        editor.commit();
    }

    public static String getPrefData(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_DATA,"");
    }

    public static void setFullName(Context ctx, String data)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_FULL_NAME, data);
        editor.commit();
    }

    public static String getFullName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_FULL_NAME,"");
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static String getCache(Context ctx,String cacheName)
    {
        return getSharedPreferences(ctx).getString(cacheName, "");
    }

    public static void setCache(Context ctx, String cacheName, String value)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(cacheName, value);
        editor.commit();
    }

    public static void cleanCache(Context ctx){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}