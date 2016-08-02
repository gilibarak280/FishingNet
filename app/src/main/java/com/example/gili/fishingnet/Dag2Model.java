package com.example.gili.fishingnet;

import android.media.Image;

/**
 * Created by Gili on 27/07/2016.
 */
public class Dag2Model {

    public String hline;
    public String detailes;
    public String price;

    public Dag2Model(){
    }

    public Dag2Model(String headline,String description,String price){

        this.hline = headline;
        this.detailes = description;
        this.price=price;
    }
}
