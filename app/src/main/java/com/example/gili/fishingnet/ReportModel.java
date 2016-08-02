package com.example.gili.fishingnet;

import android.media.Image;

/**
 * Created by Gili on 27/07/2016.
 */
public class ReportModel {

    Image image;
    String headline;
    String description;

    public ReportModel(Image image,String headline,String description){

        this.image = image;
        this.headline = headline;
        this.description = description;
    }
}
