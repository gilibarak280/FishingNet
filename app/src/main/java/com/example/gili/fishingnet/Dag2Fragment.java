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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Almog on 02/08/2016.
 */

public class Dag2Fragment extends Fragment{

    // Firebase
    Firebase mRootRef;

    // Data
    ArrayList<Dag2Model> ad = new ArrayList<>();
    FirebaseRecyclerAdapter<Dag2Model, adViewHolder> adapter;

    // UI
    RecyclerView mRecyclerView;

    private static int RESULT_LOAD_IMG = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        LinearLayout recyclerLayout = (LinearLayout) inflater.inflate(R.layout.fragment_recycler, container, false);

        mRootRef = new Firebase("https://fishingnet-dd809.firebaseio.com/ads");
        mRecyclerView = (RecyclerView) recyclerLayout.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);

        FloatingActionButton fab = (FloatingActionButton) recyclerLayout.findViewById(R.id.fab_2);

        adapter = new FirebaseRecyclerAdapter<Dag2Model, adViewHolder>(Dag2Model.class, R.layout.recycler_list_item, adViewHolder.class, mRootRef) {
            @Override
            protected void populateViewHolder(adViewHolder adViewHolder, Dag2Model rm, int i) {
                // TODO: set real image from database
                //adViewHolder.image.setImageBitmap(BitmapFactory.decodeFile(rm.image));
                ad.add(rm);
                adViewHolder.image.setImageResource(R.drawable.backend);
                adViewHolder.headline.setText(rm.headline);
                adViewHolder.description.setText(rm.description);
                adViewHolder.price.setText(rm.price);
            }
        };

        mRecyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("הכנס מודעה חדשה");

                // Get the layout inflater
                LayoutInflater inflater = getActivity().getLayoutInflater();

                final View addadView = inflater.inflate(R.layout.dag2, null);

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(addadView)
                        // Set up the buttons
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText headline = (EditText) addadView.findViewById(R.id.headline_edit_text);
                                String headlineText = headline.getText().toString();

                                EditText description = (EditText) addadView.findViewById(R.id.description_edit_text);
                                String descriptionText = description.getText().toString();

                                ImageButton galleryButton = (ImageButton) addadView.findViewById(R.id.gallery_button);
                                galleryButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        loadImageFromGallery();
                                    }
                                });

                                ImageButton cameraButton = (ImageButton) addadView.findViewById(R.id.camera_button);
                                cameraButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                });

                                // Your functionality here
                                //mRootRef.push().setValue(.....);

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
                //TODO: find out which format is expected in Firebase, while model needs to be saved

            } else {
                Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    public static class adViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView headline;
        TextView description;
        TextView price;

        public adViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.card_image);
            headline = (TextView) itemView.findViewById(R.id.card_headline);
            description = (TextView) itemView.findViewById(R.id.card_description);
            price = (TextView)itemView.findViewById(R.id.card_description);
        }
    }
}