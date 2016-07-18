package com.example.gili.fishingnet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;

/**
 * Created by Gili on 18/07/2016.
 */
public class RecyclerViewFragment extends Fragment {

    // Firebase
    Firebase mRootRef;

    // Data
    //ArrayList<String> mItems = new ArrayList<>();
    FirebaseRecyclerAdapter<String,MessageViewHolder> adapter;

    // UI
    RecyclerView mRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout recyclerLayout = (LinearLayout) inflater.inflate(R.layout.fragment_recycler, container, false);

        mRootRef = new Firebase("https://fishingnet-dd809.firebaseio.com/recyclerview");
        mRecyclerView = (RecyclerView) recyclerLayout.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);

        FloatingActionButton fab = (FloatingActionButton)recyclerLayout.findViewById(R.id.fab_2);

        adapter = new FirebaseRecyclerAdapter<String, MessageViewHolder>(String.class,android.R.layout.two_line_list_item, MessageViewHolder.class,mRootRef) {
            @Override
            protected void populateViewHolder(MessageViewHolder messageViewHolder, String s, int i) {
                messageViewHolder.mText.setText(s);
            }
        };

        mRecyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Insert title here");

                // Get the layout inflater
                LayoutInflater inflater = getActivity().getLayoutInflater();

                final View setPositionView = inflater.inflate(R.layout.edit_position_layout, null);

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(setPositionView)
                        // Set up the buttons
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TextView xStartView = (TextView) setPositionView.findViewById(R.id.x_start);
                                String xStart = xStartView.getText().toString();

                                // Your functionality here
                                mRootRef.push().setValue(xStart);

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

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView mText;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
