package com.example.gili.fishingnet;

import android.media.Image;

/**
 * Created by Gili on 27/07/2016.
 */
public class ReportModel {

    //Image image;
    // Pass a custom Java object to Firebase: the class that defines it need a default constructor
    // that takes no arguments and has public getters for the properties to be assigned.
    public String headline;
    public String description;

//    public ReportModel(Image image,String headline,String description){
//
//        this.image = image;
//        this.headline = headline;
//        this.description = description;
//    }

    public ReportModel(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ReportModel(String headline,String description){

        this.headline = headline;
        this.description = description;
    }
}
