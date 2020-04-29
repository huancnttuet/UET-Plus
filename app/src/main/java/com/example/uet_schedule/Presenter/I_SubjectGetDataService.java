package com.example.uet_schedule.Presenter;

import com.example.uet_schedule.Model.MSSV;
import com.example.uet_schedule.Model.Student;
import com.example.uet_schedule.Model.Subject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface I_SubjectGetDataService {
    @GET ("/schedule")
    Call<List<List<String>>> getSubject(@Query("mssv") String mssv);

    @GET ("/score/getAll")
    Call<List<List<String>>> getScore(@Query("term") int term);

    @GET ("/score/search")
    Call<List<List<List<String>>>> searchGrades(@Query("input") String input,@Query("term") int term,@Query("type_education") int type_education);

    @GET ("/exam-time")
    Call<List<List<String>>> getExamTime(@Query("mssv") String mssv);

    @Headers({"Content-Type: application/json"})
    @POST ("/get-data-subject-from-mssv")
    Call<List<Student>> getStudent(@Body MSSV body);
}
