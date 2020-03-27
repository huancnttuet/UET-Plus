package com.example.uet_schedule.Model;

import com.google.gson.annotations.SerializedName;

public class Subject {
    @SerializedName ( "stt" )
    private String stt;

    @SerializedName ( "code" )
    private String code;

    @SerializedName("name")
    private String name;

    @SerializedName("credits")
    private String credits;

    @SerializedName("codeFull")
    private String codeFull;

    @SerializedName("teacher")
    private String teacher;

    @SerializedName("numberStudent")
    private String numberStudent;

    @SerializedName("sessionOfDay")
    private String sessionOfDay;

    @SerializedName("daysOfTheWeek")
    private String daysOfTheWeek;

    @SerializedName("lession")
    private String lession;

    @SerializedName("classRoom")
    private String classRoom;

    @SerializedName("group")
    private String group;

    public Subject(String stt, String code, String name, String credits, String codeFull, String teacher, String numberStudent, String sessionOfDay, String daysOfTheWeek, String lession, String classRoom, String group) {
        this.stt = stt;
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.codeFull = codeFull;
        this.teacher = teacher;
        this.numberStudent = numberStudent;
        this.sessionOfDay = sessionOfDay;
        this.daysOfTheWeek = daysOfTheWeek;
        this.lession = lession;
        this.classRoom = classRoom;
        this.group = group;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getCodeFull() {
        return codeFull;
    }

    public void setCodeFull(String codeFull) {
        this.codeFull = codeFull;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getNumberStudent() {
        return numberStudent;
    }

    public void setNumberStudent(String numberStudent) {
        this.numberStudent = numberStudent;
    }

    public String getSessionOfDay() {
        return sessionOfDay;
    }

    public void setSessionOfDay(String sessionOfDay) {
        this.sessionOfDay = sessionOfDay;
    }

    public String getDaysOfTheWeek() {
        return daysOfTheWeek;
    }

    public void setDaysOfTheWeek(String daysOfTheWeek) {
        this.daysOfTheWeek = daysOfTheWeek;
    }

    public String getLession() {
        return lession;
    }

    public void setLession(String lession) {
        this.lession = lession;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
