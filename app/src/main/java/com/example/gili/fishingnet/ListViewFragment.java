package com.example.gili.fishingnet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

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

        mListView = (ListView)listLayout.findViewById(R.id.listView);
        mTextView = (TextView)listLayout.findViewById(R.id.textView4);
        //final ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, mMessages);
        //mListView.setAdapter(adapter);

        Firebase messagesRef = mRootRef.child("messages");

        FirebaseListAdapter<String> adapter = new FirebaseListAdapter<String>(getActivity(),String.class,android.R.layout.simple_list_item_1,messagesRef) {
            @Override
            protected void populateView(View view, String s, int i) {
                TextView text = (TextView)view.findViewById(android.R.id.text1);
                text.setText(s);
            }
        };
        mListView.setAdapter(adapter);
//        messagesRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                String message = dataSnapshot.getValue(String.class);
//                Log.v("E_CHILD_ADDED", message);
//                mMessages.add(message);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                String message = dataSnapshot.getValue(String.class);
//                Log.v("E_CHILD_CHANGED", message);
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                String message = dataSnapshot.getValue(String.class);
//                Log.v("E_CHILD_REMOVED", message);
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });

        return listLayout;
    }
}
