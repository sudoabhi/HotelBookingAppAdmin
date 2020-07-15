package com.example.hotelloadmin.SignUpPackage;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hotelloadmin.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class RegistrationClub4 extends AppCompatActivity {


    ArrayAdapter adapter;

    ArrayAdapter adapter3;
    ArrayAdapter adapter4;
    ArrayAdapter adapter5;
    ArrayList<String> domain;



    //LinearLayout layout;

    String club_domain;


    Button next;
    ProgressBar LayoutprogressBar;
    FrameLayout content;

    FirebaseDatabase database;
    DatabaseReference databaseReference;


    FirebaseUser user;
    Boolean flag=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment14);

        next = (Button) findViewById(R.id.next_viewpager);

        //layout=view.findViewById(R.id.lay1);


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("HotelLoAdmin");

        user = FirebaseAuth.getInstance().getCurrentUser();


        domain = new ArrayList<>(Arrays.asList("1 Star", "2 Star", "3 Star", "4 Star", "5 Star"));
        Collections.sort(domain);


        LayoutprogressBar=(ProgressBar) findViewById(R.id.progressBar);
        LayoutprogressBar.setVisibility(View.INVISIBLE);
        content=findViewById(R.id.content);

        Spinner show_domain = (Spinner) findViewById(R.id.spn1);
        Spinner show_type = (Spinner) findViewById(R.id.spn2);



        adapter = new ArrayAdapter(RegistrationClub4.this, android.R.layout.simple_list_item_1, domain);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        show_domain.setAdapter(adapter);


        show_domain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                club_domain = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(club_domain)){

                    LayoutprogressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(RegistrationClub4.this, "Please fill in the empty fields", Toast.LENGTH_SHORT).show();
                    return;
                }



                LayoutprogressBar.setVisibility(View.VISIBLE);
                content.setForeground(new ColorDrawable(Color.parseColor("#60000000")));
                sendData();

            }
        });

    }








    private void sendData()
    {



databaseReference.child(user.getUid()).child("ClubDomain").setValue(club_domain);
        databaseReference.child(user.getUid()).child("UserUID").setValue(user.getUid());
        databaseReference.child(user.getUid()).child("UserType").setValue("Organiser");
        databaseReference.child(user.getUid()).child("Registered").setValue("True")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        next.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                        Toast.makeText(RegistrationClub4.this, "Registering...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistrationClub4.this, WelcomeActivity.class));
                        finishAffinity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        next.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                        Toast.makeText(RegistrationClub4.this, "An error occurred, with error message:  "+e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });




    }
}
