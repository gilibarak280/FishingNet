package com.example.gili.fishingnet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Gili on 27/07/2016.
 */
public class MeetingsFragment extends Fragment {

    // Firebase
    Firebase mRootRef;


    // Data
    ArrayList<MeetingsModel> Meetings = new ArrayList<>();
    FirebaseRecyclerAdapter<MeetingsModel,MeetingsViewHolder> adapter;
    String selectedImageBitmapString;

    // UI
    RecyclerView mRecyclerView;

    private int flag;
    private static int RESULT_LOAD_IMG = 1;
    private static final int CAMERA_REQUEST = 1888;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        CoordinatorLayout recyclerLayout = (CoordinatorLayout) inflater.inflate(R.layout.fragment_recycler, container, false);

        mRootRef = new Firebase("https://fishingnet-dd809.firebaseio.com/Meetings");
        mRecyclerView = (RecyclerView) recyclerLayout.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(manager);

        FloatingActionButton fab = (FloatingActionButton)recyclerLayout.findViewById(R.id.fab_2);

        adapter = new FirebaseRecyclerAdapter<MeetingsModel, MeetingsViewHolder>(MeetingsModel.class,R.layout.list_item_meetings, MeetingsViewHolder.class,mRootRef)
        {
            @Override
            protected void populateViewHolder(MeetingsViewHolder MeetingViewHolder, MeetingsModel rm, int i)
            {

                MeetingViewHolder.location.setText(rm.Location);
                MeetingViewHolder.date.setText(rm.Date);
                MeetingViewHolder.name.setText(rm.name);
                MeetingViewHolder.time.setText(rm.time);
                MeetingViewHolder.detils.setText(rm.detils);
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
                    builder.setTitle("הכנס פרטי מפגש ");

                    // Get the layout inflater
                    LayoutInflater inflater = getActivity().getLayoutInflater();

                    final View addMeetingView = inflater.inflate(R.layout.meeting, null);

                    // Inflate and set the layout for the dialog
                    // Pass null as the parent view because its going in the dialog layout
                    builder.setView(addMeetingView)
                            // Set up the buttons
                            .setPositiveButton("שלח", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    EditText location = (EditText) addMeetingView.findViewById(R.id.location);
                                    String locationText = location.getText().toString();

                                    EditText detailes = (EditText) addMeetingView.findViewById(R.id.detailes);
                                    String detailesText = detailes.getText().toString();

                                    EditText time = (EditText) addMeetingView.findViewById(R.id.date);
                                    String timeText = time.getText().toString();

                                    GoogleSignInAccount lastUser = UserAccount.getUserAccount();
                                    String name = lastUser.getDisplayName();
                                    Calendar c = Calendar.getInstance();
                                    String cur_time = c.get(Calendar.DATE) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);

                                    MeetingsModel MeetingsModel = new MeetingsModel(locationText,timeText,detailesText,name,cur_time);
                                    Meetings.add(MeetingsModel);
                                    mRootRef.push().setValue(MeetingsModel);

                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    builder.show();
                }
                else{
                    Toast.makeText(getContext(),"Log in please",Toast.LENGTH_LONG).show();
                }
            }
        });

        //adapter.notifyDataSetChanged();

        return recyclerLayout;

    }




    public static class MeetingsViewHolder extends RecyclerView.ViewHolder {

        TextView location;
        TextView date;
        TextView detils;
        TextView name;
        TextView time;

        public MeetingsViewHolder(View itemView) {
            super(itemView);
            location = (TextView) itemView.findViewById(R.id.card_location9);
            date = (TextView) itemView.findViewById(R.id.card_time9);
            detils = (TextView)itemView.findViewById(R.id.card_details9);
            name = (TextView)itemView.findViewById(R.id.card_name9);
            time = (TextView)itemView.findViewById(R.id.card_curr_time);
        }
    }

}