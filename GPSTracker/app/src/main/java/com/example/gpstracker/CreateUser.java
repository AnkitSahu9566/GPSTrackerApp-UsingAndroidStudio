package com.example.gpstracker;

public class CreateUser {

    public CreateUser()
    {

    }
    public String name;

    public CreateUser(String name, String email, String password, String code, String isSharing, String lat, String lng, String imageUri,String userid) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.code = code;
        this.isSharing = isSharing;
        this.lat = lat;
        this.lng = lng;
        this.imageUri = imageUri;
        this.userid = userid;
    }

    public String email;
    public String password;
    public String code;
    public String isSharing;
    public String lat;
    public String lng;
    public String imageUri;
    public String userid;
}
