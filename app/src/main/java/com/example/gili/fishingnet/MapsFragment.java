package com.example.gili.fishingnet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    Firebase mRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FrameLayout mapLayout = (FrameLayout) inflater.inflate(R.layout.maps_fragment, container, false);

        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.maps_fragment);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager() //getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //conditionTextView = (TextView) weatherLayout.findViewById(R.id.textViewCondition);
        //buttonSunny = (Button) weatherLayout.findViewById(R.id.buttonSunny);
        //buttonFoggy = (Button) weatherLayout.findViewById(R.id.buttonFoggy);
        mRef = new Firebase("https://fishingnet-dd809.firebaseio.com/reports");



        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                ReportModel reportm = dataSnapshot.getValue(ReportModel.class);
//                Log.v("E_CHILD_ADDED", message);
//                mMessages.add(message);
//                adapter.notifyDataSetChanged();

                //rm.headline
                //rm.description
                if (reportm.lat!=null && reportm.lng!=null) {
                    LatLng location = new LatLng(Double.parseDouble(reportm.lat), Double.parseDouble(reportm.lng));
                    //Marker marker = new MarkerOptions().position(location).title(reportm.headline).snippet(reportm.description)
                    mMap.addMarker(new MarkerOptions().position(location).title(reportm.headline).snippet(reportm.description));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
//        buttonSunny.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mRef.setValue("gili");
//            }
//        });
//
//        buttonFoggy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mRef.setValue("moran");
//            }
//        });
//
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String text = dataSnapshot.getValue(String.class);
//                conditionTextView.setText(text);
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
        return mapLayout;

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng haifa = new LatLng(32.7940, 34.9896);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(haifa,8));
    }
}
