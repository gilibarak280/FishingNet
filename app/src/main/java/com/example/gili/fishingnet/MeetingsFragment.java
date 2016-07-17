package com.example.gili.fishingnet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Gili on 06/07/2016.
 */
public class MeetingsFragment extends Fragment {

    private ArrayList<Meeting> meetings;
    private Firebase rootRef;
    private RecyclerView recyclerView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //ReclyclerView, Adapter
        //ReadLocalJSON readLocalJSON = new ReadLocalJSON();
        //courses = readLocalJSON.getCourses(getActivity());

        //Float Button

        final int size = getResources().getDimensionPixelSize(R.dimen.fab_size);
        final ImageButton imageButton = (ImageButton) getActivity().findViewById(R.id.fab_1);

        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Mejorando.la: Aprende a crear el futuro de la Web",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meetings, container, false);


        rootRef = new Firebase("https://fishingnet-dd809.firebaseio.com/meetings");
        Firebase meetingsRef = rootRef.child("meetings");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new MeetingsAdapter(meetings, R.layout.list_item_meetings));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Por si quieren configurar algom como Grilla solo cambian la linea de arriba por esta:
        //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final MeetingsAdapter adapter = new MeetingsAdapter(meetings, R.layout.list_item_meetings);
        recyclerView.setAdapter(adapter);

        /////ignore
//        rootRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String text = dataSnapshot.getValue(String.class);
//                //someUIElement.setText(text);
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
        /////end ignore

        meetingsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Meeting meeting = dataSnapshot.getValue(Meeting.class);
//                meetings.add(0,meeting);
//                adapter.notifyDataSetChanged();
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

        Meeting m1 = new Meeting(rootView);
        //m1.image = (ImageView) rootView.findViewById(R.id.image);
        //m1.headline = (TextView) rootView.findViewById(R.id.name);
       // m1.details = (TextView) rootView.findViewById(R.id.description);
        m1.gili = "gili";
        meetingsRef.setValue(m1);

        return rootView;
    }
}
