package com.example.uetplus2.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class LoginRest {

    private static HttpURLConnection httpCon = null;
    private static URL url;

    private static final String hostURL = "https://uet-login.herokuapp.com";
    private static final String LocalhostURL = "http://192.168.0.13:3000";


    public static void setup(String request) {
        try {
            url = new URL(hostURL + request);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setUseCaches(false);
            httpCon.setReadTimeout(15 * 1000); // 15 seconds to timeout
            httpCon.setRequestProperty("Content-Type", "application/json");
            httpCon.setRequestProperty("Accept", "application/json");
        } catch (Exception e) {
            Log.v("uetplus", "REST SETUP ERROR" + e.getMessage());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    public static String get(String url) {

        BufferedReader reader = null;
        StringBuilder stringBuilder = null;

        try {
            setup(url);
            httpCon.setRequestMethod("GET");
            httpCon.setDoInput(true);
            httpCon.connect();

            Log.v("uetplus", "GET REQUEST is : " + httpCon.getRequestMethod() + " " + httpCon.getURL());

            // read the output from the server
            reader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
            stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line);

            reader.close();
            Log.v("uetplus", "JSON GET REQUEST : " + stringBuilder.toString());
        } catch (Exception e) {
            Log.v("uetplus", "GET REQUEST ERROR" + e.getMessage());
        }

        return stringBuilder.toString();
    }

}