package com.example.gili.fishingnet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.ArrayList;

public class MeetingsFragment extends Fragment {

    Firebase mRootRef;
    ArrayList<MeetingsModel> meetings = new ArrayList<>();
    FirebaseRecyclerAdapter<MeetingsModel,MeetingViewHolder> adapter;
    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        CoordinatorLayout recyclerLayout = (CoordinatorLayout) inflater.inflate(R.layout.fragment_meetings, container, false);
        mRootRef = new Firebase("https://fishingnet-dd809.firebaseio.com/meetings");
        mRecyclerView = (RecyclerView) recyclerLayout.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        FloatingActionButton fab = (FloatingActionButton)recyclerLayout.findViewById(R.id.fab_1);

        adapter = new FirebaseRecyclerAdapter<MeetingsModel, MeetingViewHolder>(MeetingsModel.class,R.layout.list_item_meetings, MeetingViewHolder.class,mRootRef)
        {
            @Override
            protected void populateViewHolder(MeetingViewHolder meetingViewHolder, MeetingsModel rm, int i)
            {
                meetingViewHolder.description.setText(rm.details);
                meetingViewHolder.name.setText(rm.name);
                //meetingViewHolder.location.setText(rm.location);
            }
        };

        mRecyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("הכנס פרטי מפגש");

                // Get the layout inflater
                LayoutInflater inflater = getActivity().getLayoutInflater();

                final View addMeetingView = inflater.inflate(R.layout.fragment_new_meeting, null);

                builder.setView(addMeetingView).setPositiveButton("שלח", new DialogInterface.OnClickListener()
                {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                EditText name = (EditText) addMeetingView.findViewById(R.id.name);
                                String nameText = name.getText().toString();

                                EditText location = (EditText) addMeetingView.findViewById(R.id.location);
                                String locationText = location.getText().toString();

                                EditText detailes = (EditText) addMeetingView.findViewById(R.id.details);
                                String detailesText = detailes.getText().toString();

                                MeetingsModel meeting = new MeetingsModel(nameText,locationText,detailesText);
                                meetings.add(meeting);
                                mRootRef.push().setValue(meeting);

                                adapter.notifyDataSetChanged();
                            }
                        }
                ).setNegativeButton("ביטול", new DialogInterface.OnClickListener()
                        {
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

    public static class MeetingViewHolder extends RecyclerView.ViewHolder {

        //ImageView image;
        TextView name;
        //TextView location;
        TextView description;

        public MeetingViewHolder(View itemView) {
            super(itemView);

            //image = (ImageView) itemView.findViewById(R.id.card_image);
            //location = (TextView) itemView.findViewById(R.id.location);
            description = (TextView) itemView.findViewById(R.id.description);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
