package com.example.uet_schedule.Model;
import com.google.gson.annotations.SerializedName;

public class Student {
    @SerializedName ( "stt" )
    private String stt;

    @SerializedName ( "mssv" )
    private String mssv;

    @SerializedName ( "name" )
    private String name;

    @SerializedName ( "dateOfBirth" )
    private String dataOfBirth;

    @SerializedName ( "classUniversity" )
    private String classUniversity;

    @SerializedName ( "codeFull" )
    private String codeFull;

    @SerializedName ( "subject" )
    private String subject;

    @SerializedName ( "group" )
    private String group;

    @SerializedName ( "credits" )
    private String credits;

    @SerializedName ( "status" )
    private String status;

    @SerializedName ( "nullChar" )
    private String nullChar;

    public Student(String stt, String mssv, String name, String dataOfBirth, String classUniversity, String codeFull, String subject, String group, String credits, String status, String nullChar) {
        this.stt = stt;
        this.mssv = mssv;
        this.name = name;
        this.dataOfBirth = dataOfBirth;
        this.classUniversity = classUniversity;
        this.codeFull = codeFull;
        this.subject = subject;
        this.group = group;
        this.credits = credits;
        this.status = status;
        this.nullChar = nullChar;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getClassUniversity() {
        return classUniversity;
    }

    public void setClassUniversity(String classUniversity) {
        this.classUniversity = classUniversity;
    }

    public String getCodeFull() {
        return codeFull;
    }

    public void setCodeFull(String codeFull) {
        this.codeFull = codeFull;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getDataOfBirth() {
        return dataOfBirth;
    }

    public void setDataOfBirth(String dataOfBirth) {
        this.dataOfBirth = dataOfBirth;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setNullChar(String nullChar) {
        this.nullChar = nullChar;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNullChar() {
        return nullChar;
    }

    public String getStatus() {
        return status;
    }

    public String getSubject() {
        return subject;
    }

}
