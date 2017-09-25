package com.example.kyu.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import br.com.app.R;

public class FriendsFragment extends Fragment {

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;

    FirebaseDatabase database;
    List<Friend> mFriend;

    FriendAdapter mAdapter;

    String TAG = getClass().getSimpleName();

    String search;

    EditText editTextFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_friends, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.rvFriend);
        editTextFilter = (EditText) v.findViewById(R.id.edit) ;

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mFriend = new ArrayList<>();

        mAdapter = new FriendAdapter(mFriend, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        final FriendAdapter.ViewHolder viewHolder = new FriendAdapter.ViewHolder(v);


        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        final Friend friendgetemail = new Friend();


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue().toString();
                //Log.d(TAG, "Value is: " + value);

                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){

                    final String value2 = dataSnapshot2.getValue().toString();
                    Log.d(TAG, "Value2 is: " + value2);

                    final Friend friend = dataSnapshot2.getValue(Friend.class);
                    // [START_EXCLUDE]
                    // Update RecyclerView



                    editTextFilter.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable edit) {
                            String filterText = edit.toString();
                            Log.d(friend.email , filterText);


                            //if (friend.email == filterText) {
                                if (friend.email.equals(filterText)) {
                                    Log.d(friend.email , "email 내부 입니다");

                                    mFriend.add(friend);
                                    mAdapter.notifyItemInserted(mFriend.size() - 1); // 화면에 보여주는 기능
                                } else if(!friend.email.equals(filterText)) {
                                    mFriend.remove(friend);
                                    mAdapter.notifyItemRemoved(mFriend.size() - 1);
                                }
                            //}

                            /*if (filterText.length() > 0) {
                                mFriend.add(friend);
                                mAdapter.notifyItemInserted(mFriend.size() - 1); // 화면에 보여주는 기능
                            } else {
                                mAdapter.clearTextFilter();
                            }*/
                        }


                    });



                    /*mFriend.add(friend);
                    mAdapter.notifyItemInserted(mFriend.size() - 1);*/
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });




        return v;
    }
}
