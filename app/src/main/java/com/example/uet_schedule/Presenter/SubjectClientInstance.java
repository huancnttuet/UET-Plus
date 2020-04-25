package com.example.uet_schedule.Presenter;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubjectClientInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://flaskapp-postgre.herokuapp.com/";

    public static Retrofit getRetrofitInstance(){

        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
