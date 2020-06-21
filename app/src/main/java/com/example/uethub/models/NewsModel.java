package com.example.uethub.models;

public class NewsModel {
    public String name;
    public String url;
    public String image;
    public NewsModel(String name, String image, String url){
        this.name = name;
        this.image = image;
        this.url = url;
    }
}
