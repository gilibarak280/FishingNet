package com.example.gili.fishingnet;

/**
 * Created by Gili on 27/07/2016.
 */
public class ReportModel {

    // Pass a custom Java object to Firebase: the class that defines it need a default constructor
    // that takes no arguments and has public getters for the properties to be assigned.

    public String imageBitmapString;
    public String headline;
    public String description;
    public String lat;
    public String lng;
    public String name;
    public String email;
    public String time;


    public ReportModel(String selectedImageBitmapString,
                       String headline, String description, String lat, String lng,
                       String email,String name,String time){

        this.imageBitmapString = selectedImageBitmapString;
        this.headline = headline;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
        this.email = email;
        this.name = name;
        this.time = time;
    }

    public ReportModel(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
}
