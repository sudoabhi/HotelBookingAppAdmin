package com.example.hotelloadmin.IntroPackage;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.hotelloadmin.SignUpPackage.RegistrationClub1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.hotelloadmin.R;

public class SelectorActivity extends AppCompatActivity {

    Button club;
    Button company;
    Button other;
    DatabaseReference admin_reference;
    FirebaseUser user;
    RelativeLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        user= FirebaseAuth.getInstance().getCurrentUser();

        parentLayout=findViewById(R.id.layout);

        admin_reference=FirebaseDatabase.getInstance().getReference().child("HotelLoAdmin");


        club=findViewById(R.id.club_button);
        company=findViewById(R.id.company_button);
        other=findViewById(R.id.others_button);

        club.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SelectorActivity.this, RegistrationClub1.class);
                admin_reference.child(user.getUid()).child("ClubEntry").setValue("Club");
                startActivity(intent);
            }
        });

        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent intent=new Intent(SelectorActivity.this,ScreenSlidePagerActivity2.class);
                admin_reference.child(user.getUid()).child("ClubEntry").setValue("Company");
                startActivity(intent);*/

                // We have removed this for now

                Snackbar snackbar = Snackbar
                        .make(parentLayout, "We are currently accepting individuals only. We will be there for you soon. Stay Tuned!", Snackbar.LENGTH_LONG);
                snackbar.show();

            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               /*Intent intent=new Intent(SelectorActivity.this,ScreenSlidePagerActivity3.class);
                admin_reference.child(user.getUid()).child("ClubEntry").setValue("Others");
                startActivity(intent);*/


                //We have removed this for now

                Snackbar snackbar = Snackbar
                        .make(parentLayout, "We are currently accepting individuals only. We will be there for you soon. Stay Updated!", Snackbar.LENGTH_LONG);
                snackbar.show();


            }
        });
    }
}
