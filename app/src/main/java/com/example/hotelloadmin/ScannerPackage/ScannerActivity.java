package com.example.hotelloadmin.ScannerPackage;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hotelloadmin.HomePackage.MainActivity;
import com.example.hotelloadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScannerActivity extends AppCompatActivity {


    DatabaseReference eventsReference;
    String EventKey;
    String TicketKey;
    String AllotedKey;
    String UserUID;
    FirebaseUser user;
    CircleImageView imageView;
    TextView UserNameT;
    TextView RollNumT;
    String Phone;
    TextView ClubLocationT;
    TextView FeesPaidT;
    TextView TeamNameT;
    TextView TicketNameT;
    TextView EventNameT;
    IntentIntegrator qrScan;
    Button ScanNext;
    ProgressBar progressBar2;
    LinearLayout LL_parent;

    Toolbar toolbar;

    int count;
    long number;
    String Purpose;


    ImageView call;
    ImageView dm;
    ImageView email;

    TextView booked_timing;
    TextView scanned_timing;

    String todays_time;
    String todays_date;
    String Use;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        eventsReference = FirebaseDatabase.getInstance().getReference().child("Hotels");
        imageView = (CircleImageView) findViewById(R.id.profile_image);
        progressBar2 = (ProgressBar) findViewById(R.id.progress_bar2);
        LL_parent = (LinearLayout) findViewById(R.id.LL_parent);
        call = (ImageView) findViewById(R.id.call);
        qrScan = new IntentIntegrator(this);
        dm=(ImageView) findViewById(R.id.dm);
        email=(ImageView)findViewById(R.id.mail);

        Intent i = getIntent();
        user = FirebaseAuth.getInstance().getCurrentUser();
        Use=i.getStringExtra("Use");
        EventKey = i.getStringExtra("EventKey");
        TicketKey = i.getStringExtra("TicketKey");
        AllotedKey = i.getStringExtra("AllotedKey");
        UserUID = i.getStringExtra("UserUID");
        Purpose = i.getStringExtra("Purpose");


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Back to Home");

        toolbar.setTitleTextColor(ContextCompat.getColor(ScannerActivity.this, android.R.color.black));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        UserNameT = (TextView) findViewById(R.id.user_name);
        RollNumT = (TextView) findViewById(R.id.roll_number);
        ClubLocationT = (TextView) findViewById(R.id.club_location);
        EventNameT = (TextView) findViewById(R.id.event_name);
        FeesPaidT = (TextView) findViewById(R.id.fees_paid);
        TeamNameT = (TextView) findViewById(R.id.team_name);
        TicketNameT = (TextView) findViewById(R.id.ticket_name);
        ScanNext = (Button) findViewById(R.id.scan_next);
        booked_timing=(TextView) findViewById(R.id.booked_timing);
        scanned_timing=(TextView) findViewById(R.id.scanned_timing);


        ScanNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ScannerActivity.this.finish();
                qrScan.initiateScan();
            }
        });

        if (Purpose != null && Purpose.equals("BookedPeople")) {

            ScanNext.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Back");

            eventsReference.child(EventKey).child("Tickets").child(TicketKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("TEAM_CHECK").getValue(String.class).equals("Team")) {
                        showConfirmation("Team");
                    } else {
                        showConfirmation("Individual");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }
            });

        } else {


            eventsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(EventKey)) {
                        dataSnapshot.child(EventKey).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("Tickets").hasChild(TicketKey)) {

                                    if (dataSnapshot.child("UploadedBy").getValue(String.class).equals(user.getUid())) {

                                        if (AllotedKey != null) {

                                            dataSnapshot.child("Tickets").child(TicketKey).child("BookedTickets").child(AllotedKey).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.hasChild(UserUID)) {

                                                        if (dataSnapshot.child(UserUID).child("Verified").getValue(String.class).equals("false")) {
                                                            getTodaysTime();
                                                            dataSnapshot.child(UserUID).child("Verified").getRef().setValue("true");
                                                            dataSnapshot.child(UserUID).child("ScannedDate").getRef().setValue(todays_date);
                                                            dataSnapshot.child(UserUID).child("ScannedTime").getRef().setValue(todays_time);
                                                       //     scanned_timing.setText("Ticket scanned on "+todays_date+" at "+todays_time);
                                                            showConfirmation("Team");
                                                            count = 0;
                                                            number = dataSnapshot.getChildrenCount();
                                                            if (dataSnapshot.hasChild("Verified")) {
                                                                number = number - 2;
                                                            } else {
                                                                number = number - 1;
                                                            }
                                                            dataSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                                @Override
                                                                public void onChildAdded(DataSnapshot dataSnapshot2, String s) {
                                                                    if ((!dataSnapshot2.getKey().equals("Verified")) && (!dataSnapshot2.getKey().equals("TeamName"))) {
                                                                        Log.e("Check8", "" + dataSnapshot2.getKey());
                                                                        if (dataSnapshot2.child("Verified").getValue(String.class).equals("true")) {
                                                                            count++;
                                                                            if (count == number) {
                                                                                eventsReference.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(AllotedKey).child("Verified").setValue("true");
                                                                            }

                                                                        }

                                                                    }


                                                                }

                                                                @Override
                                                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                                                }

                                                                @Override
                                                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                                                }

                                                                @Override
                                                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });

                                                        } else {
                                                            ThrowError("Ticket is already scanned. Beware and check user credentials","Refetch");
                                                            // Toast.makeText(ScannerActivity.this,"Ticket is already scanned. Beware and check user credentials",Toast.LENGTH_LONG).show();
                                                        }


                                                    } else {

                                                        ThrowError("User is not successfully registered","");

                                                        // Toast.makeText(ScannerActivity.this,"User is not successfully registered",Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                        } else {


                                            dataSnapshot.child("Tickets").child(TicketKey).child("BookedTickets").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.hasChild(UserUID)) {

                                                        if (dataSnapshot.child(UserUID).child("Verified").getValue(String.class).equals("false")) {
                                                            getTodaysTime();
                                                            dataSnapshot.child(UserUID).child("Verified").getRef().setValue("true");
                                                            dataSnapshot.child(UserUID).child("ScannedDate").getRef().setValue(todays_date);
                                                            dataSnapshot.child(UserUID).child("ScannedTime").getRef().setValue(todays_time);
                                                         //   scanned_timing.setText("Ticket scanned on "+todays_date+" at "+todays_time);
                                                            showConfirmation("Individual");
         /*                                              count=0;
                                                       number=dataSnapshot.getChildrenCount();
                                                       dataSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                                                           @Override
                                                           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                               if(dataSnapshot.child("Verified").getValue(String.class).equals("true")){
                                                                   count++;
                                                                   if(count==number){
                                                                       eventsReference.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(AllotedKey).child("Verified").setValue("true");
                                                                   }

                                                               }

                                                           }

                                                           @Override
                                                           public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                                           }

                                                           @Override
                                                           public void onChildRemoved(DataSnapshot dataSnapshot) {

                                                           }

                                                           @Override
                                                           public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                                           }

                                                           @Override
                                                           public void onCancelled(DatabaseError databaseError) {

                                                           }
                                                       });*/

                                                        } else {
                                                            ThrowError("Ticket is already scanned. Beware and check user credentials","Refetch");
                                                            //Toast.makeText(ScannerActivity.this,"Ticket is already scanned. Beware and check user credentials",Toast.LENGTH_LONG).show();
                                                        }


                                                    } else {

                                                        ThrowError("User is not successfully registered","");
                                                        // Toast.makeText(ScannerActivity.this,"User is not successfully registered",Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                        }


                                    } else {
                                        ThrowError("Ticket does not belong to this organizer","");
                                        //Toast.makeText(ScannerActivity.this,"Ticket does not belong to this organizer",Toast.LENGTH_LONG).show();
                                    }

                                } else {
                                    ThrowError("Ticket might be deleted by the organizer","");
                                    //  Toast.makeText(ScannerActivity.this,"Ticket might be deleted by the organizer",Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    } else {

                        ThrowError("Event might be deleted by the organizer","");


                        // Toast.makeText(ScannerActivity.this,"Event might be deleted by the organizer",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }


    }


    public void showConfirmation(final String TEAM_CHECK) {


        eventsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String EventName = dataSnapshot.child(EventKey).child("EventName").getValue(String.class);
                String TicketName = dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("Name").getValue(String.class);
                String TicketCategory = dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("Category").getValue(String.class);
                final   String ClubName=dataSnapshot.child(EventKey).child("ClubName").getValue(String.class);
               final String ClubImageURL=dataSnapshot.child(EventKey).child("ClubImageURL").getValue(String.class);

             //   final String BookedDate=dataSnapshot.child(E)
                String Feespaid;

                DatabaseReference parentReference=FirebaseDatabase.getInstance().getReference();
                parentReference.child(UserUID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String ImageURl=dataSnapshot.child("ImageUrl").getValue(String.class);
                        final String UserName=dataSnapshot.child("UserName").getValue(String.class);
                        String RollNum=dataSnapshot.child("RollNum").getValue(String.class);
                        String ClubLocation=dataSnapshot.child("ClubLocation").getValue(String.class);
                        final String UserEmail=dataSnapshot.child("UserEmail").getValue(String.class);


                        UserNameT.setText(UserName);
                        RollNumT.setText(RollNum);
                        ClubLocationT.setText(ClubLocation);

                        email.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                        "mailto",UserEmail, null));
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                                emailIntent.putExtra(Intent.EXTRA_EMAIL, UserEmail); // String[] addresses
                                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                            }
                        });


                        dm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {



                            }
                        });





                        Picasso.get()
                                .load(Uri.parse(ImageURl)) // mCategory.icon is a string
                                .resize(800, 800)
                                .centerCrop()
                                .noFade()
                                .error(R.drawable.addphoto) // default image to load
                                .into(imageView, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {

                                        LL_parent.setVisibility(View.VISIBLE);
                                        progressBar2.setVisibility(View.GONE);

                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                String Date;
                String Time;
                String ScannedDate;
                String ScannedTime;

                if (TEAM_CHECK.equals("Team")) {
                    Feespaid = dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(AllotedKey).child(UserUID).child("FeePaid").getValue(String.class);
                    TeamNameT.setVisibility(View.VISIBLE);
                     Date=dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(AllotedKey).child(UserUID).child("Date").getValue(String.class);
                     Time=dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(AllotedKey).child(UserUID).child("Time").getValue(String.class);
                    Phone = dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(AllotedKey).child(UserUID).child("Phone").getValue(String.class);
                    final String TeamName = dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(AllotedKey).child("TeamName").getValue(String.class);
                    ScannedDate=dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(AllotedKey).child(UserUID).child("ScannedDate").getValue(String.class);
                    ScannedTime=dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(AllotedKey).child(UserUID).child("ScannedTime").getValue(String.class);
                    if(dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(AllotedKey).child(UserUID).child("Verified").getValue(String.class).equals("true")){
                        scanned_timing.setVisibility(View.VISIBLE);
                       // Toast.makeText(ScannerActivity.this,"",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        scanned_timing.setVisibility(View.GONE);
                       // Toast.makeText(ScannerActivity.this,"Hii2",Toast.LENGTH_SHORT).show();
                    }

                    if (Purpose != null && Purpose.equals("BookedPeople")) {
                        TeamNameT.setText("Team Name:- " + TeamName);
                        if(Use!=null&&Use.equals("CASE")){

                            TeamNameT.setText("Go to " + TeamName + " " + "\uD83D\uDD17");
                            TeamNameT.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(ScannerActivity.this, BookedTicketsSolo.class);
                                    intent.putExtra("TicketKey", TicketKey);
                                    intent.putExtra("EventKey", EventKey);
                                    intent.putExtra("TeamNameC", TeamName);
                                    intent.putExtra("AllotedKey", AllotedKey);

                                    startActivity(intent);

                                }
                            });

                        }
                    } else {
                        TeamNameT.setText("Go to " + TeamName + " " + "\uD83D\uDD17");
                        TeamNameT.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(ScannerActivity.this, BookedTicketsSolo.class);
                                intent.putExtra("TicketKey", TicketKey);
                                intent.putExtra("EventKey", EventKey);
                                intent.putExtra("TeamNameC", TeamName);
                                intent.putExtra("AllotedKey", AllotedKey);

                                startActivity(intent);

                            }
                        });
                    }

                } else {
                    Feespaid = dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(UserUID).child("FeePaid").getValue(String.class);
                    Date=dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(UserUID).child("Date").getValue(String.class);
                    Time=dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(UserUID).child("Time").getValue(String.class);
                    Phone = dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(UserUID).child("Phone").getValue(String.class);
                    ScannedDate=dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(UserUID).child("ScannedDate").getValue(String.class);
                    ScannedTime=dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(UserUID).child("ScannedTime").getValue(String.class);

                    if(dataSnapshot.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(UserUID).child("Verified").getValue(String.class).equals("true")){
                        scanned_timing.setVisibility(View.VISIBLE);
                       // Toast.makeText(ScannerActivity.this,"Hii3",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        scanned_timing.setVisibility(View.GONE);
                      //  Toast.makeText(ScannerActivity.this,"Hii4",Toast.LENGTH_SHORT).show();
                    }
                    TeamNameT.setVisibility(View.GONE);
                }

                booked_timing.setText("Ticket booked on "+Date+" at "+Time);
                scanned_timing.setText("Ticket scanned on "+ScannedDate+" at "+ScannedTime);


                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // Toast.makeText(ScannerActivity.this,"Hello Kaun",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",Phone, null));
                        startActivity(intent);
                    }
                });




                EventNameT.setText("Event:- "+EventName);
                TicketNameT.setText("TicketName:- "+TicketName+"("+TicketCategory+")");
                FeesPaidT.setText("Fees Paid:- ₹"+Feespaid);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



     /*   DatabaseReference parentReference=FirebaseDatabase.getInstance().getReference();
        parentReference.child(UserUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String ImageURl=dataSnapshot.child("ImageUrl").getValue(String.class);
                String UserName=dataSnapshot.child("UserName").getValue(String.class);
                String RollNum=dataSnapshot.child("RollNum").getValue(String.class);
                String ClubLocation=dataSnapshot.child("ClubLocation").getValue(String.class);


                UserNameT.setText(UserName);
                UserNameT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(ScannerActivity.this, PeopleClick.class);
                        intent.putExtra("UserUID",UserUID);
                        startActivity(intent);
                    }
                });
                RollNumT.setText(RollNum);
                ClubLocationT.setText(ClubLocation);


                Picasso.get()
                        .load(Uri.parse(ImageURl)) // mCategory.icon is a string
                        .resize(800, 800)
                        .centerCrop()
                        .noFade()
                        .error(R.drawable.addphoto) // default image to load
                        .into(imageView, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                                LL_parent.setVisibility(View.VISIBLE);
                                progressBar2.setVisibility(View.GONE);
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent=new Intent(ScannerActivity.this, PeopleClick.class);
                                        intent.putExtra("UserUID",UserUID);
                                        startActivity(intent);
                                    }
                                });

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public boolean onSupportNavigateUp() {

        if(Purpose!=null&&Purpose.equals("BookedPeople")){
            onBackPressed();
            return super.onSupportNavigateUp();
        }
        else{
            Intent intent=new Intent(ScannerActivity.this,MainActivity.class);
            startActivity(intent);
            ScannerActivity.this.finish();
            return super.onSupportNavigateUp();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Toast.makeText(ScannerActivity.this,"Result Not Found"+requestCode+resultCode+imageReturnedIntent,Toast.LENGTH_SHORT).show();

        IntentResult result=IntentIntegrator.parseActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(ScannerActivity.this,"Result Not Found",Toast.LENGTH_SHORT).show();
            }
            else{

                Log.e("Boys", "h"+result.getContents().toString());
                //  textView.setText(""+result.getContents().toString());
                try {

                    JSONObject obj = new JSONObject(result.getContents());


                    EventKey=obj.getString("EventKey");
                    TicketKey=obj.getString("TicketKey");
                    if(obj.has("AllotedKey")){
                        AllotedKey=obj.getString("AllotedKey");
                    }
                   // Toast.makeText(ScannerActivity.this,""+AllotedKey,Toast.LENGTH_SHORT).show();
                    UserUID=obj.getString("UserUID");

                    Intent intent=new Intent(ScannerActivity.this, ScannerActivity.class);
                    intent.putExtra("EventKey",EventKey);
                    intent.putExtra("TicketKey",TicketKey);
                    if(AllotedKey!=null){
                        intent.putExtra("AllotedKey",AllotedKey);
                    }

                    intent.putExtra("UserUID",UserUID);
                    startActivity(intent);


                  //  Toast.makeText(ScannerActivity.this,"id="+obj.getString("AllotedKey")+" name="+obj.getString("name"),Toast.LENGTH_SHORT).show();

                } catch (Throwable t) {
                    Log.d("My App", " "+t.getMessage()+result.getContents());
                  //  Toast.makeText(ScannerActivity.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();
                }

                // Log.e("ODD",result.getContents().to)

                // textView.setText(result.getContents());

                {


                }
            }

        }
        /*else{
           // Toast.makeText(MainActivity.this,"Mehnat bekar gyi",Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode,resultCode,imageReturnedIntent);
        }*/


    }

    public void ThrowError(String error,String Use){

        Intent intent=new Intent(ScannerActivity.this,ErrorActivity.class);
        intent.putExtra("Error",error);
        intent.putExtra("Use",Use);
        intent.putExtra("EventKey",EventKey);
        intent.putExtra("TicketKey",TicketKey);
        intent.putExtra("AllotedKey",AllotedKey);
        intent.putExtra("UserUID",UserUID);
        intent.putExtra("Purpose","BookedPeople");



        ScannerActivity.this.finish();
        startActivity(intent);
    }

    private void getTodaysTime(){

        Calendar calendar = Calendar.getInstance();
        String am_pm = null;
        String monthS=null;


        int hourS=calendar.get(Calendar.HOUR_OF_DAY);
        String yearS = Integer.toString(calendar.get(Calendar.YEAR)) ;
        int monthofYear = calendar.get(Calendar.MONTH);
        String dayS = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        int minuteS=calendar.get(Calendar.MINUTE);
        //    Toast.makeText(getActivity(),hourS+minutesS,Toast.LENGTH_SHORT).show();

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourS);
        datetime.set(Calendar.MINUTE, minuteS);

        String minute;
        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm= "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        if(datetime.get(Calendar.MINUTE)<10){

            minute= "0"+datetime.get(Calendar.MINUTE);
        }
        else minute=""+datetime.get(Calendar.MINUTE);

        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";
        todays_time=strHrsToShow + ":" + minute + " " + am_pm;



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





    }
}
