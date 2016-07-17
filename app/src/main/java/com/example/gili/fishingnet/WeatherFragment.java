package com.example.gili.fishingnet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by Gili on 17/07/2016.
 */
public class WeatherFragment extends Fragment {

    TextView conditionTextView;
    Button buttonSunny;
    Button buttonFoggy;
    Firebase mRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout weatherLayout = (LinearLayout) inflater.inflate(R.layout.fragment_weather, container, false);

        conditionTextView = (TextView) weatherLayout.findViewById(R.id.textViewCondition);
        buttonSunny = (Button) weatherLayout.findViewById(R.id.buttonSunny);
        buttonFoggy = (Button) weatherLayout.findViewById(R.id.buttonFoggy);
        mRef = new Firebase("https://fishingnet-dd809.firebaseio.com/weather");


        buttonSunny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.setValue("gili");
            }
        });

        buttonFoggy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRef.setValue("moran");
            }
        });

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                conditionTextView.setText(text);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        return weatherLayout;
    }
}
