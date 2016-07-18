package com.example.gili.fishingnet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
//import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import java.util.ArrayList;

/**
 * Created by Gili on 18/07/2016.
 */
public class RecyclerViewFragment extends Fragment {

    // Firebase
    Firebase mRootRef;

    // Data
    //ArrayList<String> mItems = new ArrayList<>();
    FirebaseListAdapter<String>  adapter;

    // UI
    ListView mListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout recyclerLayout = (LinearLayout) inflater.inflate(R.layout.fragment_recycler, container, false);

        mRootRef = new Firebase("https://fishingnet-dd809.firebaseio.com/recyclerview");
        mListView = (ListView)recyclerLayout.findViewById(R.id.listView2);
        FloatingActionButton fab = (FloatingActionButton)recyclerLayout.findViewById(R.id.fab_2);
        adapter = new FirebaseListAdapter<String>(getActivity(),String.class,android.R.layout.simple_list_item_1,mRootRef) {
            @Override
            protected void populateView(View view, String s, int i) {
                TextView text = (TextView)view.findViewById(android.R.id.text1);
                text.setText(s);
            }
        };
        mListView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Insert title here");

                // Get the layout inflater
                LayoutInflater inflater = getActivity().getLayoutInflater();

                final View setPositionView = inflater.inflate(R.layout.edit_position_layout, null);

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(setPositionView)
                        // Set up the buttons
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView xStartView = (TextView) setPositionView.findViewById(R.id.x_start);
                                String xStart = xStartView.getText().toString();

                                // Your functionality here
                                mRootRef.push().setValue(xStart);

                                //mItems.add(xStart);
                                //adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
            }
        });

        return recyclerLayout;
    }
}
