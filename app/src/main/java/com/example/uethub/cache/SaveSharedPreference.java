package com.example.uethub.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.uethub.models.Information;
import com.example.uethub.models.NotificationModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SaveSharedPreference
{
    static final String PREF_USER_NAME= "username";
    static final String PREF_FULL_NAME= "fullname";
    static final String PREF_DATA = "data";
    static final String PREF_NOTIFY = "notify";
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

    public static String getPrefNotify(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_NOTIFY,"");
    }

    public static void setPrefNotify(Context ctx, NotificationModel data)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        List<NotificationModel> list = new ArrayList<NotificationModel>();
        String cache = getPrefNotify(ctx);
        Gson gson = new Gson();

        if(cache.length() == 0 ){
            list.add(data);
        } else {
            Type listType = new TypeToken<List<NotificationModel>>() {}.getType();
            list = gson.fromJson(cache, listType);
            list.add(data);
        }
        String json = gson.toJson(list);
        editor.putString(PREF_NOTIFY, json);
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