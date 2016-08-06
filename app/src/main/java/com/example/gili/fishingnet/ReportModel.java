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

    public ReportModel(String image,String headline,String description,String lat,String lng){

        this.imageBitmapString = image;
        this.headline = headline;
        this.description = description;
        this.lat = lat;
        this.lng = lng;

    }

    public ReportModel(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
}
