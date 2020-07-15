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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegistrationClub3 extends AppCompatActivity {

    ArrayAdapter adapter2;

    String form_year;
    TextInputEditText club_location_detail;
    String formation_year[];
    Button next_fragment;
    String club_locs_detail;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser currentUser;
    Boolean flag=false;

    ProgressBar LayoutprogressBar;
    FrameLayout content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment13);


        database=FirebaseDatabase.getInstance();
        reference = database.getReference().child("HotelLoAdmin");
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        next_fragment=(Button)findViewById(R.id.next_viewpager);
        Spinner formation_years = (Spinner) findViewById(R.id.spn2);
        // club_location=(TextInputEditText)view.findViewById(R.id.et_club_location);
        club_location_detail=findViewById(R.id.et_club_location_detail);


        LayoutprogressBar=(ProgressBar) findViewById(R.id.progressBar);
        LayoutprogressBar.setVisibility(View.INVISIBLE);
        content=findViewById(R.id.content);

        formation_year = new String[]{"2010","2011","2012","2013", "2014", "2015", "2016", "2017", "2018", "2019"};

        adapter2 = new ArrayAdapter(RegistrationClub3.this, android.R.layout.simple_list_item_1, formation_year);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        formation_years.setAdapter(adapter2);



        formation_years.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                form_year=adapterView.getItemAtPosition(i).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });






        next_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // String club_locs= club_location.getText().toString();
                club_locs_detail=club_location_detail.getText().toString();
                if(TextUtils.isEmpty(form_year)||TextUtils.isEmpty(club_locs_detail)){

                    next_fragment.setClickable(true);
                    LayoutprogressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(RegistrationClub3.this, "Please fill in the empty fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    next_fragment.setClickable(false);
                    LayoutprogressBar.setVisibility(View.VISIBLE);
                    content.setForeground(new ColorDrawable(Color.parseColor("#60000000")));
                    sendData();

                }




            }
        });



    }


    private void sendData()
    {

        reference.child(currentUser.getUid()).child("ClubAddress").setValue(club_locs_detail)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        next_fragment.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                        Toast.makeText(RegistrationClub3.this, "An error occurred, with error message:  "+e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
        reference.child(currentUser.getUid()).child("ClubFormationYear").setValue(form_year)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(RegistrationClub3.this,RegistrationClub4.class));
                        next_fragment.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        next_fragment.setClickable(true);
                        LayoutprogressBar.setVisibility(View.INVISIBLE);
                        content.setForeground(null);
                        Toast.makeText(RegistrationClub3.this, "An error occurred, with error message:  "+e.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });



    }
}
