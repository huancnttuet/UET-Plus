package com.example.uet_schedule.Presenter;

import com.example.uet_schedule.Model.Subject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface I_SubjectGetDataService {
    @GET ("/get-data-subject")
    Call<List<Subject>> getSubject();
}
