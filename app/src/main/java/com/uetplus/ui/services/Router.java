package com.uetplus.ui.services;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Router {
    @GET ("/schedule")
    Call<List<List<String>>> getTimeTable(@Query("mssv") String mssv);

    @GET ("/score/getAll")
    Call<List<List<String>>> getScore(@Query("term") int term);

    @GET ("/score/search")
    Call<List<List<List<String>>>> searchGrades(@Query("input") String input, @Query("term") int term, @Query("type_education") int type_education);

    @GET ("/exam-time")
    Call<List<List<String>>> getExamTime(@Query("mssv") String mssv);

    //login

    @GET("/infomation")
    Call<List<List<List<String>>>> getInfo(@Query("username") String username, @Query("password") String password);

}
