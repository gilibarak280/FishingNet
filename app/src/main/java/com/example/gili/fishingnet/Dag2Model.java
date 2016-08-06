package com.example.gili.fishingnet;

import android.media.Image;

/**
 * Created by Gili on 27/07/2016.
 */
public class Dag2Model {

    public String hline;
    public String details;
    public String price;
    public String imageBString;
    public String name;
    public String email;
    public String time;


    public Dag2Model(){
    }

    public Dag2Model(String selectedImageBitmapString,String headlineText,String descriptionText,String price,String email,String name,String time){
        this.imageBString = selectedImageBitmapString;
        this.hline = headlineText;
        this.details = descriptionText;
        this.price=price;
        this.email=email;
        this.name=name;
        this.time=time;

    }
}
