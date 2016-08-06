//package com.example.gili.fishingnet;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Base64;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.firebase.client.Firebase;
//import com.firebase.ui.FirebaseRecyclerAdapter;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.jar.Attributes;
//
//public class MeetingsFragment extends Fragment {
//
//    Firebase mRootRef;
//    ArrayList<MeetingsModel> Meetings = new ArrayList<>();
//    FirebaseRecyclerAdapter<MeetingsModel,MeetingViewHolder> adapter;
//    RecyclerView mRecyclerView;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
//    {
//        LinearLayout recyclerLayout = (LinearLayout) inflater.inflate(R.layout.fragment_recycler, container, false);
//        mRootRef = new Firebase("https://fishingnet-dd809.firebaseio.com/meetings");
//        mRecyclerView = (RecyclerView) recyclerLayout.findViewById(R.id.recycler_view);
//        mRecyclerView.setHasFixedSize(true);
//        LinearLayoutManager manager = new LinearLayoutManager(getContext());
//        mRecyclerView.setLayoutManager(manager);
//        FloatingActionButton fab = (FloatingActionButton)recyclerLayout.findViewById(R.id.fab_2);
//
//        adapter = new FirebaseRecyclerAdapter<MeetingsModel, MeetingViewHolder>(MeetingsModel.class,R.layout.recycler_list_item, MeetingViewHolder.class,mRootRef)
//        {
//            @Override
//            protected void populateViewHolder(MeetingViewHolder MeetingViewHolder, MeetingsModel rm, int i)
//            {
//                MeetingViewHolder.detailes.setText(rm.details);
//                MeetingViewHolder.name.setText(rm.name);
//                MeetingViewHolder.location.setText(rm.location);
//            }
//        };
//
//        mRecyclerView.setAdapter(adapter);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("הכנס פרטי מפגש");
//
//                // Get the layout inflater
//                LayoutInflater inflater = getActivity().getLayoutInflater();
//
//                final View addMeetingView = inflater.inflate(R.layout.fragment_meeting2, null);
//
//                builder.setView(addMeetingView).setPositiveButton("שלח", new DialogInterface.OnClickListener()
//                {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which)
//                            {
//
//                                EditText name = (EditText) addMeetingView.findViewById(R.id.name);
//                                String nameText = name.getText().toString();
//
//                                EditText location = (EditText) addMeetingView.findViewById(R.id.location);
//                                String locationText = location.getText().toString();
//
//                                EditText detailes = (EditText) addMeetingView.findViewById(R.id.detailes);
//                                String detailesText = detailes.getText().toString();
//
//                                MeetingsModel meeting = new MeetingsModel(nameText,locationText,detailesText);
//                                Meetings.add(meeting);
//                                mRootRef.push().setValue(meeting);
//
//                                adapter.notifyDataSetChanged();
//                            }
//                        }
//                ).setNegativeButton("ביטול", new DialogInterface.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                builder.show();
//            }
//        });
//
//        return recyclerLayout;
//    }
//
//    public static class MeetingViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView image;
//        TextView name;
//        TextView location;
//        TextView detailes;
//
//        public MeetingViewHolder(View itemView) {
//            super(itemView);
//
//            image = (ImageView) itemView.findViewById(R.id.card_image);
//            location = (TextView) itemView.findViewById(R.id.location);
//            detailes = (TextView) itemView.findViewById(R.id.detailes);
//            name = (TextView) itemView.findViewById(R.id.name);
//        }
//    }
//}
