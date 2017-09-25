package com.example.kyu.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Hashtable;

public class LoginActivity extends AppCompatActivity {

    String stEmail;
    String stPassword;

    ProgressBar pbLogin;

    EditText etEmail;
    EditText etPassword;

    String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference myRef;

    FirebaseUser users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        Button btnRegister = (Button) findViewById(R.id.etRegister);
        pbLogin = (ProgressBar) findViewById(R.id.pbLogin);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                users = firebaseAuth.getCurrentUser();
                if (users != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + users.getUid());

                    SharedPreferences sharedPreferences = getSharedPreferences("email", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("uid", users.getUid());
                    editor.putString("email", users.getEmail());
                    editor.apply();

                    Hashtable<String, String> profile = new Hashtable<String, String>();
                    profile.put("email", users.getEmail());
                    profile.put("key", users.getUid());
                    profile.put("photo", "");

                    myRef.child(users.getUid()).setValue(profile);

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stEmail = etEmail.getText().toString();
                stPassword = etPassword.getText().toString();

                Toast.makeText(LoginActivity.this, stEmail+","+stPassword, Toast.LENGTH_SHORT).show();

                if(stEmail.isEmpty() || stEmail.equals("") || stPassword.isEmpty() || stPassword.equals("")){
                    Toast.makeText(LoginActivity.this, "입력해 주세요", Toast.LENGTH_SHORT).show();
                }else {
                    registerUser(stEmail,stPassword);
                }
                registerUser(stEmail,stPassword);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stEmail = etEmail.getText().toString();
                stPassword = etPassword.getText().toString();

                if(stEmail.isEmpty() || stEmail.equals("") || stPassword.isEmpty() || stPassword.equals("")){
                    Toast.makeText(LoginActivity.this, "입력해 주세요", Toast.LENGTH_SHORT).show();
                }else {
                    userLogin(stEmail,stPassword);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void registerUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.


                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(LoginActivity.this, "Authentication success.", Toast.LENGTH_SHORT).show();

                            if (users != null) {
                                Hashtable<String, String> profile = new Hashtable<String, String>();
                                profile.put("email", users.getEmail());
                                profile.put("photo", "");
                                myRef.child(users.getUid()).setValue(profile);

                            }
                        }

                        // ...
                    }
                });
    }

    private void userLogin(String email, String password){
        pbLogin.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        pbLogin.setVisibility(View.GONE);

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        } else{
                            Intent intent = new Intent(LoginActivity.this, TabActivity.class);
                            startActivity(intent);

                        }

                        // ...
                    }
                });
    }
}
