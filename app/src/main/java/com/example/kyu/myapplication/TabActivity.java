package com.example.kyu.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.support.design.widget.BottomNavigationView;
import android.widget.Toast;


public class TabActivity extends AppCompatActivity {
    long lastPressed;
    Fragment fragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.navigation_home :
                    fragment = new HomeFragment();
                    switchFragment(fragment);

                    return true;
                case R.id.navigation_dashboard :
                    fragment = new FriendsFragment();
                    switchFragment(fragment);

                    return true;
                case R.id.navigation_notification :
                    fragment = new ProfileFragment();
                    switchFragment(fragment);

                    return true;

            }

            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment fragment = new HomeFragment();
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    public void switchFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.content, fragment);
        transaction.commit();

    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - lastPressed <1500){
            finish();
        }
        Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        lastPressed = System.currentTimeMillis();
    }
}
