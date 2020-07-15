package com.example.hotelloadmin.NewEventPackage;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hotelloadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;

public class NewEventActivity0 extends AppCompatActivity {

    TextInputEditText event_name;



    TextInputEditText location;
    TextInputEditText event_address;


    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    String[] compete_mode;
    ArrayAdapter<String> adapter;
    FirebaseUser user;






    TextInputEditText event_description;
    private String month;
    ImageView next;
    String monthS;
    String dayS;
    String yearS;
    String locationS;
    String addressS;
    String todays_date;
    TextView tv;

    LinearLayout online_event;
    LinearLayout offline_event;

    EditText event_type;
    String event_typeS;
    Date todays_date_oc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event__frag0);

        online_event=(LinearLayout) findViewById(R.id.online_event);
        offline_event=(LinearLayout) findViewById(R.id.offline_event);

        compete_mode = new String[]{"Offline", "Online","Both"};
        adapter = new ArrayAdapter(NewEventActivity0.this, android.R.layout.simple_list_item_1, compete_mode);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        user = FirebaseAuth.getInstance().getCurrentUser();
        event_type=(EditText) findViewById(R.id.event_type);

        event_name=(TextInputEditText) findViewById(R.id.event_name);
        location= (TextInputEditText) findViewById(R.id.event_location);
        event_address=findViewById(R.id.event_address);
        next=(ImageView) findViewById(R.id.next);
        tv=findViewById(R.id.tv);



        event_description=(TextInputEditText)findViewById(R.id.event_description);


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ((AppCompatActivity) NewEventActivity0.this).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });













        Calendar calendar = Calendar.getInstance();
        yearS = Integer.toString(calendar.get(Calendar.YEAR)) ;
        int monthofYear = calendar.get(Calendar.MONTH);
        todays_date_oc=calendar.getTime();

        dayS = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        if(calendar.get(Calendar.DAY_OF_MONTH)<10){
            dayS="0"+dayS;
        }

        switch(monthofYear+1) {
            case 1:
                monthS = "Jan";
                break;
            case 2:
                monthS = "Feb";
                break;
            case 3:
                monthS= "March";
                break;
            case 4:
                monthS= "April";
                break;
            case 5:
                monthS = "May";
                break;
            case 6:
                monthS = "June";
                break;
            case 7:
                monthS = "July";
                break;
            case 8:
                monthS = "August";
                break;
            case 9:
                monthS = "Sept";
                break;
            case 10:
                monthS = "Oct";
                break;
            case 11:
                monthS = "Nov";
                break;
            case 12:
                monthS = "Dec";
                break;
        }


        todays_date=""+dayS+" "+monthS+", "+yearS;


        final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);


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
        location.setOnTouchListener(listener);*/

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String event_nameS=event_name.getText().toString();
                String event_descriptionS=event_description.getText().toString();

                locationS=location.getText().toString();
                addressS=event_address.getText().toString();
                event_typeS=event_type.getText().toString();

                if(TextUtils.isEmpty(event_nameS)||TextUtils.isEmpty(event_descriptionS)||
                        TextUtils.isEmpty(event_typeS)||
                        (TextUtils.isEmpty(locationS)&&offline_event.getVisibility()==View.VISIBLE&&TextUtils.isEmpty(addressS))
                        )
                {
                    //startActivity(new Intent(NewEventActivity0.this,NewEventActivity2.class));
                    Toast.makeText(NewEventActivity0.this, "Please fill in the empty fields", Toast.LENGTH_SHORT).show();

                }

                else {
                    sendData();


                }

            }
        });


    }


    private void sendData()
    {

        Intent i = new Intent(NewEventActivity0.this, NewEventActivity1.class);



        i.putExtra("SENDER_KEY", "New_Event_Frag0");
        i.putExtra("TODAYS_DATE", todays_date);
        i.putExtra("EVENT_NAME", event_name.getText().toString());
        i.putExtra("EVENT_DESCRIPTION",event_description.getText().toString());





        i.putExtra("EVENT_LOCATION", locationS);

        i.putExtra("EVENT_ADDRESS", addressS);
        i.putExtra("EVENT_TYPEX",event_typeS);



        startActivity(i);

    }
}
