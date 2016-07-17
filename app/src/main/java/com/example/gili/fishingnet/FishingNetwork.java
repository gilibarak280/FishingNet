package com.example.gili.fishingnet;

import com.firebase.client.Firebase;

/**
 * Created by Gili on 17/07/2016.
 */
public class FishingNetwork extends android.app.Application {

    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
