package com.example.gili.fishingnet;


/**
 * Created by Gili on 27/07/2016.
 */
public class MeetingsModel {

    public String Location;
    public String Date;
    public String detils;
    public String name;
    public String time;


    public MeetingsModel(){
    }

    public MeetingsModel(String Location,String Date,String detils,String name,String time)
    {
        this.Location = Location;
        this.Date=Date;
        this.detils=detils;
        this.name=name;
        this.time=time;

    }
}
