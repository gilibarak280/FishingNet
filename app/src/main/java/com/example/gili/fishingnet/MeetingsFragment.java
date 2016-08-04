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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class MeetingsFragment extends Fragment {

    // Data
    private ArrayList<MeetingsModel> meetings = new ArrayList<>();
    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter<MeetingsModel, MeetingViewHolder> adapter2;
    Firebase rootRef;
    String selectedImageBitmapString;
    RecyclerView mRecyclerView;
    private int flag;
    private static int RESULT_LOAD_IMG = 1;
    private static final int CAMERA_REQUEST = 1888;

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        final int size = getResources().getDimensionPixelSize(R.dimen.fab_size);
        final ImageButton imageButton = (ImageButton) getActivity().findViewById(R.id.fab_1);
        imageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Mejorando.la: Aprende a crear el futuro de la Web",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout recyclerLayout = (LinearLayout) inflater.inflate(R.layout.fragment_recycler, container, false);

        View rootView = inflater.inflate(R.layout.fragment_meetings, container, false);
        rootRef = new Firebase("https://fishingnet-dd809.firebaseio.com/meetings");
        Firebase meetingsRef = rootRef.child("meetings");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new MeetingsAdapter(meetings, R.layout.list_item_meetings));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FloatingActionButton fab = (FloatingActionButton) recyclerLayout.findViewById(R.id.fab_2);

        adapter = new FirebaseRecyclerAdapter<MeetingsModel, MeetingViewHolder>(MeetingsModel.class, R.layout.recycler_list_item, MeetingViewHolder.class, rootRef) {
            @Override
            protected void populateViewHolder(MeetingViewHolder MeetingViewHolder, MeetingsModel rm, int i) {
                // TODO: set real image from database
                if (rm.imageBString != null)
                {
                    byte[] decodedString = Base64.decode(rm.imageBString, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    MeetingViewHolder.setImageBitmap(bitmap);
                }
                else
                {
                }
                MeetingViewHolder.details.setText(rm.details);
                MeetingViewHolder.location.setText(rm.location);
                MeetingViewHolder.name.setText(rm.name);
            }
        };

        mRecyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("הכנס פרטים למפגש");

                // Get the layout inflater
                LayoutInflater inflater = getActivity().getLayoutInflater();

                final View addMeetingsView = inflater.inflate(R.layout.fragment_meeting2, null);

                ImageButton galleryButton = (ImageButton) addMeetingsView.findViewById(R.id.gallery_button);

                galleryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        flag = 1;
                        loadImageFromGallery();
                    }
                });

                ImageButton cameraButton = (ImageButton) addMeetingsView.findViewById(R.id.camera_button);
                cameraButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        flag = 2;
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                });

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(addMeetingsView)
                        // Set up the buttons
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                                EditText name1 = (EditText) addMeetingsView.findViewById(R.id.name);
                                String name = name1.getText().toString();

                                EditText location1 = (EditText) addMeetingsView.findViewById(R.id.location);
                                String location = location1.getText().toString();

                                EditText details1 = (EditText) addMeetingsView.findViewById(R.id.details);
                                String details = details1.getText().toString();

                                MeetingsModel meeting = new MeetingsModel(name, location, details,selectedImageBitmapString);
                                meetings.add(meeting);
                                rootRef.push().setValue(meeting);

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
            }
        });

        //adapter.notifyDataSetChanged();

        return recyclerLayout;
    }

    public void loadImageFromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (flag) {
            case (1):
                onGalleryActivityResult(requestCode, resultCode, data);
                break;
            case (2):
                onCameraActivityResult(requestCode, resultCode, data);
                break;
            default:
        }
    }

    public void onGalleryActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            getActivity();
            if (requestCode == RESULT_LOAD_IMG && resultCode == Activity.RESULT_OK
                    && null != data) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst(); // Move to first row
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                File image = new File(imgDecodableString);
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap imageBitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
                Bitmap selectedImageBitmap = Bitmap.createBitmap(imageBitmap);

                // Encode Bitmap to String
                ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
                selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
                selectedImageBitmap.recycle();
                byte[] byteArray = bYtE.toByteArray();
                selectedImageBitmapString = Base64.encodeToString(byteArray, Base64.DEFAULT);

            } else {
                Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }



    public void onCameraActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // Encode Bitmap to String
            ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
            photo.recycle();
            byte[] byteArray = bYtE.toByteArray();
            selectedImageBitmapString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
    }
    public static class MeetingViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView location;
        TextView details;
        TextView name;

        public MeetingViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.card_image);
            location = (TextView) itemView.findViewById(R.id.card_location);
            details = (TextView) itemView.findViewById(R.id.card_details);
            name = (TextView)itemView.findViewById(R.id.card_name);
        }
    }


//Por si quieren configurar algom como Grilla solo cambian la linea de arriba por esta:
    //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

    recyclerView.setItemAnimator(new

    DefaultItemAnimator()

    );

    final MeetingsAdapter adapter = new MeetingsAdapter(meetings, R.layout.list_item_meetings);
    recyclerView.setAdapter(adapter);


    meetingsRef.addChildEventListener(new

    ChildEventListener() {
        @Override
        public void onChildAdded (DataSnapshot dataSnapshot, String s){
//                Meeting meeting = dataSnapshot.getValue(Meeting.class);
//                meetings.add(0,meeting);
//                adapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged (DataSnapshot dataSnapshot, String s){

        }

        @Override
        public void onChildRemoved (DataSnapshot dataSnapshot){

        }

        @Override
        public void onChildMoved (DataSnapshot dataSnapshot, String s){

        }

        @Override
        public void onCancelled (FirebaseError firebaseError){

        }
    }

    );

    Meeting m1 = new Meeting(rootView);
    //m1.image = (ImageView) rootView.findViewById(R.id.image);
    //m1.headline = (TextView) rootView.findViewById(R.id.name);
    // m1.details = (TextView) rootView.findViewById(R.id.description);
    m1.gili="gili";
    meetingsRef.setValue(m1);

    return rootView;
}


