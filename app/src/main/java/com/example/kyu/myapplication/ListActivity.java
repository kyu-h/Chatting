package com.example.kyu.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class ListActivity extends AppCompatActivity {

    ListView listview = null ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1) ;
        listview = (ListView) findViewById(R.id.listview1) ;
        listview.setAdapter(adapter) ;

        adapter.add("Adam Smith") ;
        adapter.add("Bryan Adams") ;
        adapter.add("Chris Martin") ;
        adapter.add("Daniel Craig") ;
        adapter.add("Eric Clapton") ;
        adapter.add("Frank Sinatra") ;
        adapter.add("Gary Moore") ;
        adapter.add("Helloween") ;
        adapter.add("Ian Hunter") ;
        adapter.add("Jennifer Lopez") ;



        EditText editTextFilter = (EditText)findViewById(R.id.editTextFilter) ;
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                // TODO : item filtering
                String filterText = edit.toString();
                if (filterText.length() > 0) {
                    listview.setFilterText(filterText);
                } else {
                    listview.clearTextFilter();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        }) ;
    }
}
