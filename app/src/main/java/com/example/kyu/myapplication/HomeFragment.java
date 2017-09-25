package com.example.kyu.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

/*
        listview = (ListView) v.findViewById(R.id.listview1) ;
        listview.setAdapter(mAdapter) ;

        mAdapter =  new ArrayAdapter(getActivity(, android.R.layout.simple_list_item_1, mList);

*/

        Button btn = (Button) v.findViewById(R.id.btn01);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListActivity.class);
                context.startActivity(intent);

            }
        });

        return v;
    }
}
