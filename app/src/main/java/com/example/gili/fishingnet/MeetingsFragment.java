package com.example.gili.fishingnet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

/**
 * Created by Gili on 06/07/2016.
 */
public class MeetingsFragment extends Fragment {

    private EditText txtName,txtLocation, txtdetails;
    private Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        RelativeLayout meetingLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_meetings, container, false);
        Button continueButton = (Button)meetingLayout.findViewById(R.id.btn);
        txtName = (EditText)meetingLayout.findViewById(R.id.name);
        txtLocation = (EditText)meetingLayout.findViewById(R.id.location);
        txtdetails = (EditText)meetingLayout.findViewById(R.id.details);
        spinner = (Spinner)meetingLayout.findViewById(R.id.spinner);
        //final ArrayList<UserInfo> list = new ArrayList<>(); // list contains userinfo

//        continueButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                DateFormat dayFormat = new SimpleDateFormat("dd.MM, HH:mm");
//                UserInfo user1 = new UserInfo();
//
//                user1.Location=txtLocation.getText().toString();
//                user1.Name = txtName.getText().toString();
//                user1.Time = dayFormat.format(Calendar.getInstance().getTime());
//                user1.Style = spinner.getSelectedItem().toString();
//                user1.Details = txtdetails.getText().toString();
//
//                list.add(0, user1);// push to location 0
//
//                Intent intent2 = new Intent(MeetingsActivity.this, Meetings.class);
//                intent2.putExtra("dataset", list);// can be sent because it is ArrayList<UserInfo>, UserInfo is seralizable
//                startActivity(intent2);
//            }
//        });
        return meetingLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
