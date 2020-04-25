package com.example.uet_schedule.Model;

import com.google.gson.annotations.SerializedName;

public class Subject {
//    @SerializedName ( "stt" )
//    private String stt;

    @SerializedName ( "class_code" )
    private String class_code;

    @SerializedName("class_name")
    private String class_name;

    @SerializedName("credit")
    private String credit;

//    @SerializedName("codeFull")
//    private String codeFull;

    @SerializedName("teacher")
    private String teacher;

    @SerializedName("student_total")
    private String student_total;

    @SerializedName("session")
    private String session;

    @SerializedName("day")
    private String day;

    @SerializedName("lession")
    private String lession;

    @SerializedName("classroom")
    private String classroom;

    @SerializedName("note")
    private String note;

    public Subject(String class_code, String class_name, String credit, String teacher, String student_total, String session, String day, String lession, String classroom, String note) {

        this.class_code = class_code;
        this.class_name = class_name;
        this.credit = credit;
        this.teacher = teacher;
        this.student_total = student_total;
        this.session = session;
        this.day = day;
        this.lession = lession;
        this.classroom = classroom;
        this.note = note;
    }



    public String getClasCode() {
        return class_code;
    }

    public void setClassCode(String code) {
        this.class_code = code;
    }

    public String getClassName() {
        return class_name;
    }

    public void setClassName(String name) {
        this.class_name = name;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit= credit;
    }


    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getStudentTotal() {
        return student_total;
    }

    public void setStudentTotal(String student_total) {
        this.student_total = student_total;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getLession() {
        return lession;
    }

    public void setLession(String lession) {
        this.lession = lession;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String group) {
        this.note = group;
    }
}
