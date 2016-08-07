package com.example.gili.fishingnet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.vision.barcode.Barcode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Gili on 27/07/2016.
 */
public class MeetingsFragment extends Fragment  {

    // Firebase
    Firebase mRootRef;

    // Data
    ArrayList<MeetingsModel> Meetings = new ArrayList<>();
    FirebaseRecyclerAdapter<MeetingsModel, MeetingsViewHolder> adapter;


    // UI
    RecyclerView mRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        CoordinatorLayout recyclerLayout = (CoordinatorLayout) inflater.inflate(R.layout.fragment_recycler, container, false);
        mRootRef = new Firebase("https://fishingnet-dd809.firebaseio.com/Meetings");
        mRecyclerView = (RecyclerView) recyclerLayout.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(manager);



        FloatingActionButton fab = (FloatingActionButton) recyclerLayout.findViewById(R.id.fab_2);

        adapter = new FirebaseRecyclerAdapter<MeetingsModel, MeetingsViewHolder>(MeetingsModel.class, R.layout.list_item_meetings, MeetingsViewHolder.class, mRootRef) {
            @Override
            protected void populateViewHolder(MeetingsViewHolder MeetingsViewHolder, MeetingsModel rm, int i)
            {
                MeetingsViewHolder.time.setText(rm.time);
                MeetingsViewHolder.name.setText(rm.name);
                MeetingsViewHolder.location.setText(rm.location);
                MeetingsViewHolder.details.setText(rm.details);
            }
        };

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = adapter.getItemCount();
                int lastVisiblePosition =
                        manager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    manager.scrollToPosition(positionStart);
                }
            }
        });

        mRecyclerView.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (UserAccount.getUserAccount() != null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("הכנס פרטי מפגש");

                    // Get the layout inflater
                    LayoutInflater inflater = getActivity().getLayoutInflater();

                    final View addMeetingsView = inflater.inflate(R.layout.meeting, null);






                    // Inflate and set the layout for the dialog
                    // Pass null as the parent view because its going in the dialog layout
                    builder.setView(addMeetingsView)
                            // Set up the buttons
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    EditText details = (EditText) addMeetingsView.findViewById(R.id.detailes);
                                    String detailsText = details.getText().toString();

                                    EditText location = (EditText) addMeetingsView.findViewById(R.id.location);
                                    String locationText = location.getText().toString();


                                    String lat = null;
                                    String lng = null;

                                    GoogleSignInAccount lastUser = UserAccount.getUserAccount();
                                    String email = lastUser.getEmail();
                                    String name = lastUser.getDisplayName();
                                    Calendar c = Calendar.getInstance();
                                    String time = c.get(Calendar.DATE) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);

                                    MeetingsModel meeting = new MeetingsModel(name,locationText,detailsText,time);
                                    Meetings.add(meeting);
                                    mRootRef.push().setValue(meeting);

                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builder.show();
                } else {
                    Toast.makeText(getContext(), "Log in please", Toast.LENGTH_LONG).show();
                }
            }
        });

        //adapter.notifyDataSetChanged();

        return recyclerLayout;
    }






    public static class MeetingsViewHolder extends RecyclerView.ViewHolder {

        TextView location;
        TextView details;
        TextView name;
        TextView time;

        public MeetingsViewHolder(View itemView) {
            super(itemView);
            location = (TextView) itemView.findViewById(R.id.card_location1);
            details = (TextView) itemView.findViewById(R.id.card_details1);
            name = (TextView) itemView.findViewById(R.id.card_name1);
            time = (TextView) itemView.findViewById(R.id.card_time1);
        }
    }

    //location





}
