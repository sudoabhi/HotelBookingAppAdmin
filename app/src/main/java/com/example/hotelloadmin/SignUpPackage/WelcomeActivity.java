package com.example.hotelloadmin.SignUpPackage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelloadmin.IntroPackage.ScreenActivity;
import com.example.hotelloadmin.HomePackage.MainActivity;
import com.example.hotelloadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    ValueEventListener listener;
    TextView name;
    TextView wlcm;
    TextView progressTv;
    ProgressBar progress;
    String club_nameS;
    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference().child("HotelLoAdmin");
        name = findViewById(R.id.name);
        wlcm = findViewById(R.id.wlcm);
        wlcm.setVisibility(View.GONE);
        progress=findViewById(R.id.myProgress);
        progressTv=findViewById(R.id.myTextProgress);



        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(currentUser.getUid())) {
                    if (dataSnapshot.child(currentUser.getUid()).hasChild("ClubName")) {
                        club_nameS = dataSnapshot.child(currentUser.getUid()).child("ClubName").getValue(String.class);

                        if (club_nameS != null) {
                            progress.setVisibility(View.GONE);
                            progressTv.setVisibility(View.GONE);
                            wlcm.setVisibility(View.VISIBLE);
                            name.setText(club_nameS);
                            name.setVisibility(View.VISIBLE);

                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                            }, 4000);

                            remove(); //see bottom of this code

                        } else {
                                    /*Intent intent = new Intent(ScreenActivity.this, ScreenSlidePagerActivity.class);
                                    startActivity(intent);
                                    remove();
                                    finishAffinity();*/
                            Toast.makeText(getApplicationContext(), "There was an error with the registration!\n Please Login Again to continue.", Toast.LENGTH_LONG).show();
                            firebaseAuth.signOut();
                            dbRef.child(currentUser.getUid()).child("Registered").setValue("False");
                            Intent intent = new Intent(getApplicationContext(), ScreenActivity.class);
                            startActivity(intent);
                            remove();
                            finishAffinity();

                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "There was an error with the registration!\n Please Login Again to continue.", Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        dbRef.child(currentUser.getUid()).child("Registered").setValue("False");
                        remove();
                        Intent intent = new Intent(getApplicationContext(), ScreenActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "There was an error with the registration!\n Please Login Again to continue.", Toast.LENGTH_LONG).show();
                    firebaseAuth.signOut();
                    remove();
                    Intent intent = new Intent(getApplicationContext(), ScreenActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        };
        dbRef.addValueEventListener(listener);

    }


    public void remove(){
        dbRef.removeEventListener(listener);
    }


    @Override
    protected void onStop() {

        super.onStop();
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            handler.removeCallbacksAndMessages(null);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }
}