package com.example.uethub.models;

public class NotificationModel
{
    public String title;
    public String message;
    public long created_at;
    public NotificationModel(String title, String message, long created_at){
        this.title = title;
        this.message = message;
        this.created_at = created_at;
    }
}
