package com.example.hotelloadmin.EventsPackage;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hotelloadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditEventDetails extends AppCompatActivity {


    TextInputEditText event_name;

    TextInputEditText location;
    TextInputEditText event_address;

    TextInputLayout nameinput;
    ProgressBar progressBar;
    LinearLayout details_lay;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;


    ArrayAdapter<String> adapter;
    FirebaseUser user;
    String event_nameS;
    String event_descriptionS;


    TextInputEditText event_description;

    TextView next;



    String locationS;
    String addressS;
    String todays_date;
    TextView tv;




    EditText event_type;
    String event_typeS;
    Date todays_date_oc;

    FirebaseDatabase eventsDatabase;
    DatabaseReference eventsReference;
    DatabaseReference newEventsReference;
    FirebaseDatabase adminDatabase;
    DatabaseReference adminReference;
    DatabaseReference newAdminReference;

    String EventKey;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event_details);









        user = FirebaseAuth.getInstance().getCurrentUser();
        event_type=(EditText) findViewById(R.id.event_type);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        details_lay=findViewById(R.id.details_lay);


        event_name=(TextInputEditText) findViewById(R.id.event_name);
        //event_name.setEnabled(false);
        event_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Event Name cannot be changed.", Toast.LENGTH_SHORT).show();

            }

        });
        nameinput=findViewById(R.id.eventinput);
        nameinput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Event Name cannot be changed.", Toast.LENGTH_SHORT).show();

            }
        });

        location= (TextInputEditText) findViewById(R.id.event_location);
        event_address=findViewById(R.id.event_address);
        next=findViewById(R.id.save);
        tv=findViewById(R.id.tv);




        event_description=(TextInputEditText)findViewById(R.id.event_description);



        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        ((AppCompatActivity)EditEventDetails.this).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });


        user = FirebaseAuth.getInstance().getCurrentUser();

        adminDatabase = FirebaseDatabase.getInstance();
        adminReference = adminDatabase.getReference().child("HotelLoAdmin");


        eventsDatabase=FirebaseDatabase.getInstance();
        eventsReference=eventsDatabase.getReference().child("Hotels");


        Intent i = getIntent();
        EventKey = i.getStringExtra("EventKey");


        eventsReference.child(EventKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                event_name.setText(dataSnapshot.child("EventName").getValue(String.class));
                event_description.setText(dataSnapshot.child("EventDescription").getValue(String.class));

                event_type.setText(dataSnapshot.child("EventTypeX").getValue(String.class));

                event_address.setText(dataSnapshot.child("EventAddress").getValue(String.class));
                location.setText(dataSnapshot.child("EventLocation").getValue(String.class));
                locationS=location.getText().toString();
                addressS=event_address.getText().toString();





                progressBar.setVisibility(View.GONE);
                details_lay.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });











        /*event_description.setMovementMethod(new ScrollingMovementMethod());
        ScrollingMovementMethod.getInstance();*/

        /*View.OnTouchListener listener= new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                return false;
            }

        };


        event_description.setOnTouchListener(listener);
        event_name.setOnTouchListener(listener);
        location.setOnTouchListener(listener);
        event_address.setOnTouchListener(listener);*/

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event_nameS=event_name.getText().toString();
                event_descriptionS=event_description.getText().toString();

                locationS=location.getText().toString();
                addressS=event_address.getText().toString();
                event_typeS=event_type.getText().toString();

                if(TextUtils.isEmpty(event_nameS)||TextUtils.isEmpty(event_descriptionS)||
                        TextUtils.isEmpty(event_typeS)||
                        (TextUtils.isEmpty(locationS)&&TextUtils.isEmpty(addressS)))
                {

                    Toast.makeText(getApplicationContext(), "Please fill in the empty fields", Toast.LENGTH_SHORT).show();

                }

                else {
                    sendData();
                    /////Upload new data here

                    finish();

                }

            }
        });


    }


    private void sendData()
    {

        Map<String, Object> newEvents = new HashMap<>();
        //   newEvents.put("UploadedDate",todays_date);
        newEvents.put("EventName", event_nameS);
        newEvents.put("EventDescription", event_descriptionS);

        newEvents.put("EventLocation", locationS);
        newEvents.put("EventAddress", addressS);
        newEvents.put("LastEditedDate", todays_date);
        newEvents.put("EVENT_TYPEX",event_typeS);


        newAdminReference=adminReference.child(user.getUid()).child("Events").child(EventKey);
        newAdminReference.updateChildren(newEvents);



        final Map<String, Object> Eventsdb = new HashMap<>();
        Eventsdb.put("LastEditedDate",todays_date);
        Eventsdb.put("EventName", event_nameS);

        Eventsdb.put("EventDescription",event_descriptionS);

        Eventsdb.put("EventTypeX",event_typeS);

        Eventsdb.put("EventLocation", locationS);
        Eventsdb.put("EventAddress", addressS);



        newEventsReference=eventsReference.child(EventKey);
        newEventsReference.updateChildren(Eventsdb);






        Toast.makeText(getApplicationContext(),"Event Details Edited Successfully",Toast.LENGTH_LONG).show();



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
