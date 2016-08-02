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

    public Dag2Model(){
    }

    public Dag2Model(String headline,String description,String price,String image){
        this.imageBString = image;
        this.hline = headline;
        this.details = description;
        this.price=price;
    }
}
