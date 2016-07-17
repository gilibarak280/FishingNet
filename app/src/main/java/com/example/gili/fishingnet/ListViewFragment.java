package com.example.gili.fishingnet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Gili on 17/07/2016.
 */
public class ListViewFragment extends Fragment {

    // Firebase
    Firebase mRootRef;
    ArrayList<String> mMessages = new ArrayList<>();

    // UI
    TextView mTextView;
    ListView mListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout listLayout = (LinearLayout) inflater.inflate(R.layout.fragment_list, container, false);

        mRootRef = new Firebase("https://fishingnet-dd809.firebaseio.com/listview");
        mTextView = (TextView)listLayout.findViewById(R.id.textView4);
        mListView = (ListView)listLayout.findViewById(R.id.listView);

        Firebase messagesRef = mRootRef.child("messages");
        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String message = dataSnapshot.getValue(String.class);
                Log.v("E_VALUE",message);
                mTextView.setText(message);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return listLayout;
    }
}
