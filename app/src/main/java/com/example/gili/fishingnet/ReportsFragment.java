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
public class ReportsFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    // Firebase
    Firebase mRootRef;

    // Data
    ArrayList<ReportModel> reports = new ArrayList<>();
    FirebaseRecyclerAdapter<ReportModel, ReportViewHolder> adapter;
    String selectedImageBitmapString;

    //location
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    // UI
    RecyclerView mRecyclerView;

    private int flag;
    private static int RESULT_LOAD_IMG = 1;
    private static final int CAMERA_REQUEST = 1888;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        CoordinatorLayout recyclerLayout = (CoordinatorLayout) inflater.inflate(R.layout.fragment_recycler, container, false);

        mRootRef = new Firebase("https://fishingnet-dd809.firebaseio.com/reports");
        mRecyclerView = (RecyclerView) recyclerLayout.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(manager);

        //location google
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        mGoogleApiClient.connect();


        FloatingActionButton fab = (FloatingActionButton) recyclerLayout.findViewById(R.id.fab_2);

        adapter = new FirebaseRecyclerAdapter<ReportModel, ReportViewHolder>(ReportModel.class, R.layout.recycler_list_item, ReportViewHolder.class, mRootRef) {
            @Override
            protected void populateViewHolder(ReportViewHolder reportViewHolder, ReportModel rm, int i) {
                if (rm.imageBitmapString != null) {
                    byte[] decodedString = Base64.decode(rm.imageBitmapString, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    reportViewHolder.image.setImageBitmap(bitmap);
                }
                reportViewHolder.headline.setText(rm.headline);
                reportViewHolder.description.setText(rm.description);
                reportViewHolder.name.setText(rm.name);
                reportViewHolder.time.setText(rm.time);
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
                    builder.setTitle("הכנס דיווח חדש");

                    // Get the layout inflater
                    LayoutInflater inflater = getActivity().getLayoutInflater();

                    final View addReportView = inflater.inflate(R.layout.add_report_layout, null);

                    ImageButton galleryButton = (ImageButton) addReportView.findViewById(R.id.gallery_button);

                    galleryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            flag = 1;
                            loadImageFromGallery();
                        }
                    });

                    ImageButton cameraButton = (ImageButton) addReportView.findViewById(R.id.camera_button);
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
                    builder.setView(addReportView)
                            // Set up the buttons
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText headline = (EditText) addReportView.findViewById(R.id.headline_edit_text);
                                    String headlineText = headline.getText().toString();

                                    EditText description = (EditText) addReportView.findViewById(R.id.description_edit_text);
                                    String descriptionText = description.getText().toString();

                                    CheckBox locationCheckbox = (CheckBox) addReportView.findViewById(R.id.location_checbox);
                                    String lat = null;
                                    String lng = null;
                                    if (locationCheckbox.isChecked()) {
                                        //TODO: Add point

                                        lat = new String(Double.toString(currentLatitude));
                                        lng = new String(Double.toString(currentLongitude));
                                    }

                                    GoogleSignInAccount lastUser = UserAccount.getUserAccount();
                                    String email = lastUser.getEmail();
                                    String name = lastUser.getDisplayName();
                                    Calendar c = Calendar.getInstance();
                                    String time = c.get(Calendar.DATE) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);

                                    ReportModel report = new ReportModel(selectedImageBitmapString, headlineText, descriptionText, lat, lng, email, name, time);
                                    reports.add(report);
                                    mRootRef.push().setValue(report);

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

    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        // Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
   /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            // Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView headline;
        TextView description;
        TextView name;
        TextView time;

        public ReportViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.card_image);
            headline = (TextView) itemView.findViewById(R.id.card_headline);
            description = (TextView) itemView.findViewById(R.id.card_description);
            name = (TextView) itemView.findViewById(R.id.card_name);
            time = (TextView) itemView.findViewById(R.id.card_time);
        }
    }

    //location

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            //Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {}





}
