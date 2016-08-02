package com.example.gili.fishingnet;
import com.firebase.client.Firebase;

public class FishingNetwork extends android.app.Application {

    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
