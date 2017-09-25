package com.example.kyu.myapplication;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String[] myDataset = {"안녕", "오늘", "뭐했어", "영화볼래?"};

    EditText etText;
    Button btnSend;
    String email;

    FirebaseDatabase database;
    List<Chat> mChat;

    String stChatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        etText = (EditText) findViewById(R.id.etText);
        btnSend = (Button) findViewById(R.id.btnFinish);

        database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }

        Intent intent = getIntent();
        stChatId = intent.getStringExtra("friendUid");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stText = etText.getText().toString();

                if(stText.equals("") || stText.isEmpty()){
                    Toast.makeText(ChatActivity.this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(ChatActivity.this, email + "," + stText, Toast.LENGTH_SHORT).show();

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());

                    DatabaseReference myRef = database.getReference().child(stChatId).child("chat").child(formattedDate);

                    Hashtable<String, String> chat = new Hashtable<String, String>();
                    chat.put("email", email);
                    chat.put("text", stText);

                    myRef.setValue(chat);

                    etText.setText("");
                }


            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mChat = new ArrayList<>();

        mAdapter = new MyAdapter(mChat, email, ChatActivity.this);
        mRecyclerView.setAdapter(mAdapter);

        DatabaseReference myRef = database.getReference().child(stChatId).child("chat");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Chat comment = dataSnapshot.getValue(Chat.class);

                // [START_EXCLUDE]
                // Update RecyclerView

                mChat.add(comment);
                mRecyclerView.scrollToPosition(mChat.size()-1);
                mAdapter.notifyItemInserted(mChat.size() - 1);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}