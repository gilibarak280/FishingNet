package com.example.gili.fishingnet;

/**
 * Created by Almog on 04/08/2016.
 */
public class MeetingsModel {
    public String name;
    public String location;
    public String details;
    public String imageBString;


    public MeetingsModel(){
    }

    public MeetingsModel(String name,String location,String details,String imageBString){
        this.name = name;
        this.location = location;
        this.details = details;
        this.imageBString=imageBString;
    }


}

