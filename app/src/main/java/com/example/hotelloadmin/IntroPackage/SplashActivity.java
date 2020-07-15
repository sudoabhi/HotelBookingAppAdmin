package com.example.hotelloadmin.IntroPackage;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.hotelloadmin.HomePackage.MainActivity;
import com.example.hotelloadmin.R;


public class SplashActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseUser currentUser;
    FirebaseStorage storage;
    StorageReference storageReference;
    String str="";
    LinearLayout icons;
    LinearLayout error;
    Button retry;
    TextView tag;
    ValueEventListener listener;
    ProgressBar progressBar;
    Boolean emailVerified;

    FirebaseDatabase userDatabase;
    DatabaseReference userReference;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        progressBar=findViewById(R.id.my_progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        icons=findViewById(R.id.icons);
        tag=findViewById(R.id.tag);
        error=findViewById(R.id.error);
        retry=findViewById(R.id.retry);
        retry.setEnabled(false);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser();
        Log.e("User",""+currentUser);
        database= FirebaseDatabase.getInstance();


        userDatabase=FirebaseDatabase.getInstance();





        databaseReference = database.getReference().child("HotelLoAdmin");
        userReference=userDatabase.getReference().child("HotelLo");

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        storageReference=storageReference.child("admin_app/");

        checkUser();


        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    progressBar.setVisibility(View.VISIBLE);
                    icons.setVisibility(View.VISIBLE);
                    tag.setVisibility(View.VISIBLE);
                    error.setVisibility(View.INVISIBLE);
                    retry.setEnabled(false);
                    checkUser();

            }
        });

    }


    protected void checkUser(){


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(!isNetworkAvailable()){
                    noConnection();
                    return;
                }

                if (currentUser != null) {


                    emailVerified = currentUser.isEmailVerified();
                    if(!emailVerified){
                        /*Toast.makeText(getApplicationContext(),"Your Email is not verified. " +
                                "Please verify and Login again!",Toast.LENGTH_LONG).show();*/
                        firebaseAuth.signOut();
                        Intent i = new Intent(getApplicationContext(), ScreenActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        progressBar.setVisibility(View.VISIBLE);
                        listener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(currentUser.getUid())) {

                                    if (dataSnapshot.child(currentUser.getUid()).hasChild("Registered")) {

                                        if (dataSnapshot.child(currentUser.getUid()).hasChild("UserType")) {

                                            if ((dataSnapshot.child(currentUser.getUid()).child("UserType").getValue(String.class)).equals("Organiser")) {


                                                str = dataSnapshot.child(currentUser.getUid()).child("Registered").getValue(String.class);

                                                if (str.equals("True")) {
                                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    remove();
                                                    finish();

                                                } else {
                                                    dataSnapshot.child(currentUser.getUid()).getRef().removeValue();
                                                    Toast.makeText(getApplicationContext(), "Registration was incomplete.\n Please login again.", Toast.LENGTH_SHORT).show();
                                                    firebaseAuth.signOut();
                                                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this);
                                                    Intent i = new Intent(getApplicationContext(), ScreenActivity.class);
                                                    startActivity(i, options.toBundle());
                                                    remove();
                                                    finish();
                                                }

                                            } else {
                                                firebaseAuth.signOut();
                                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this);
                                                Intent i = new Intent(getApplicationContext(), ScreenActivity.class);
                                                startActivity(i, options.toBundle());
                                                remove();
                                                finish();
                                            }

                                        } else {
                                            dataSnapshot.child(currentUser.getUid()).getRef().removeValue();
                                            firebaseAuth.signOut();
                                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this);
                                            Intent i = new Intent(getApplicationContext(), ScreenActivity.class);
                                            startActivity(i, options.toBundle());
                                            remove();
                                            finish();
                                        }
                                    }
                                    else {
                                        dataSnapshot.child(currentUser.getUid()).getRef().removeValue();
                                        firebaseAuth.signOut();
                                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this);
                                        Intent i = new Intent(getApplicationContext(), ScreenActivity.class);
                                        startActivity(i, options.toBundle());
                                        remove();
                                        finish();
                                    }

                                }
                                else {
                                    firebaseAuth.signOut();
                                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this);
                                    Intent i = new Intent(getApplicationContext(), ScreenActivity.class);
                                    startActivity(i, options.toBundle());
                                    remove();
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        };

                        databaseReference.addValueEventListener(listener);
                    }

                }
                else {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this);
                    Intent i = new Intent(getApplicationContext(), ScreenActivity.class);
                    startActivity(i,options.toBundle());
                    finish();
                }
            }
        }, 200);
    }


    public void remove(){
        databaseReference.removeEventListener(listener);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finish();
        finishAffinity();
        //android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void noConnection(){
        progressBar.setVisibility(View.INVISIBLE);
        icons.setVisibility(View.INVISIBLE);
        tag.setVisibility(View.INVISIBLE);
        error.setVisibility(View.VISIBLE);
        retry.setEnabled(true);
    }


    @Override
    protected void onStop() {

        super.onStop();
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
