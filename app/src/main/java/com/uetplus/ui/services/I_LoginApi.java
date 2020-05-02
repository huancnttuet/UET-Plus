package com.uetplus.ui.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface I_LoginApi {

    //login

    @GET("/infomation")
    Call<List<List<List<String>>>> getInfo(@Query("username") String username, @Query("password") String password);

}
