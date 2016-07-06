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
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Gili on 06/07/2016.
 */
public class MeetingsFragment extends Fragment {

    private ArrayList<Meetings> meetings;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //ReclyclerView, Adapter
        //ReadLocalJSON readLocalJSON = new ReadLocalJSON();
        //courses = readLocalJSON.getCourses(getActivity());

        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new MeetingsAdapter(meetings, R.layout.list_item_meetings));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Por si quieren configurar algom como Grilla solo cambian la linea de arriba por esta:
        //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

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
        return rootView;
    }
}
