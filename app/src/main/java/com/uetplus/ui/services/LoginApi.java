package com.uetplus.ui.services;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginApi {

    private static Retrofit retrofit;
    private static final String BASE_URL = " https://uet-login.herokuapp.com/";

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
