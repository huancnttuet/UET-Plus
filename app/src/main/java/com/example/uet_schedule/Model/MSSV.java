package com.example.uet_schedule.Model;

import com.google.gson.annotations.SerializedName;

public class MSSV {
    @SerializedName("mssv")
    private String mssv;

    public MSSV(String mssv){
        this.mssv = mssv;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }
}
