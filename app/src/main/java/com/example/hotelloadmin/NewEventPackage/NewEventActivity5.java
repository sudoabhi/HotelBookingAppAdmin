package com.example.hotelloadmin.NewEventPackage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.hotelloadmin.EventsPackage.EventsHistoryActivity;
import com.example.hotelloadmin.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NewEventActivity5 extends AppCompatActivity {



    private FirebaseAuth firebaseAuth;





    public String event_description;
    public String todays_date;
    public String event_name;
    public String location;
    public String event_address;
    ArrayList<String> event_banner=new ArrayList<String>();
    ArrayList<String> event_banner_key=new ArrayList<String>();

    public ArrayList<String> question=new ArrayList<String>();
    public ArrayList<String> answer=new ArrayList<String>();
    public ArrayList<String> organiser_name=new ArrayList<String>();
    public ArrayList<String> organiser_role=new ArrayList<String>();
    public ArrayList<String> organiser_email=new ArrayList<String>();
    public ArrayList<String> organiser_phone=new ArrayList<String>();
    public ArrayList<String> organiser_pic=new ArrayList<String>();
    public ArrayList<String> orgKeyS = new ArrayList<String>();


    ArrayList<Integer> TicketPrices=new ArrayList<Integer>();



    FirebaseDatabase eventsDatabase;
    DatabaseReference eventsReference;
    DatabaseReference newEventsReference;
    FirebaseDatabase adminDatabase;
    DatabaseReference adminReference;
    DatabaseReference newAdminReference;
    FirebaseUser user;
    String key;
    String club_domain;
    String club_name;
    String club_location;
    String club_image_url;
    String club_type;

    String club_college_name=null;
    String club_college_district=null;
    String event_typeX;

    ValueEventListener listener;


    public ArrayList<String> ticket_name=new ArrayList<String>();
    public ArrayList<String> ticket_category=new ArrayList<String>();
    public ArrayList<String> ticket_description=new ArrayList<String>();
    public ArrayList<String> ticket_price=new ArrayList<String>();
    public ArrayList<String> ticket_type=new ArrayList<String>();
    public ArrayList<String> fees_setting=new ArrayList<String>();



        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_event__frag5);

            Intent i=getIntent();
            event_name=i.getStringExtra("EVENT_NAME");
            event_description=i.getStringExtra("EVENT_DESCRIPTION");
            location=i.getStringExtra("EVENT_LOCATION");
            event_address=i.getStringExtra("EVENT_ADDRESS");


            event_typeX=i.getStringExtra("EVENT_TYPEX");



            todays_date=i.getStringExtra("TODAYS_DATE");



            question=i.getStringArrayListExtra("QUESTIONS");
            answer=i.getStringArrayListExtra("ANSWERS");

            organiser_name=i.getStringArrayListExtra("ORG_NAME");
            organiser_role=i.getStringArrayListExtra("ORG_ROLE");
            organiser_email=i.getStringArrayListExtra("ORG_EMAIL");
            organiser_phone=i.getStringArrayListExtra("ORG_PHONE");
            organiser_pic=i.getStringArrayListExtra("ORG_PIC");
            orgKeyS = i.getStringArrayListExtra("ORG_KEY");

            event_banner=i.getStringArrayListExtra("EVENT_BANNER_URL");
            event_banner_key=i.getStringArrayListExtra("EVENT_BANNER_KEY");

            key=i.getStringExtra("EVENT_KEY");

            ticket_name=i.getStringArrayListExtra("TICK_NAME");
            ticket_category=i.getStringArrayListExtra("TICK_CATEGORY");
            ticket_description=i.getStringArrayListExtra("TICK_DESCRIPTION");
            ticket_price=i.getStringArrayListExtra("TICK_PRICE");
            ticket_type=i.getStringArrayListExtra("TICKET_TYPE");
            fees_setting=i.getStringArrayListExtra("FEES_SETTING");






            firebaseAuth = FirebaseAuth.getInstance();
            user = firebaseAuth.getCurrentUser();
            adminDatabase = FirebaseDatabase.getInstance();
            adminReference = adminDatabase.getReference().child("HotelLoAdmin");

            eventsDatabase=FirebaseDatabase.getInstance();
            eventsReference=eventsDatabase.getReference().child("Hotels");


            uploadData();






        }

    private void uploadData() {
        Map<String, Object> newEvents = new HashMap<>();
        //   newEvents.put("UploadedDate",todays_date);
        newEvents.put("EventName", event_name);
        newEvents.put("EventDescription", event_description);





        newEvents.put("EventThumbnailURL", event_banner.get(0));
        newEvents.put("UploadedBy", user.getUid());
        newEvents.put("EventKey",key);
        newEvents.put("UploadedDate", todays_date);
        newEvents.put("EVENT_TYPEX",event_typeX);
        newEvents.put("Ticketing","Enabled");
        newEvents.put("EventLocation", location);
        newEvents.put("EventAddress", event_address);

        newAdminReference=adminReference.child(user.getUid()).child("Events").child(key);
        newAdminReference.updateChildren(newEvents);



        newEventsReference=eventsReference.child(key);


        DatabaseReference newPosterNodes=newEventsReference.child("Posters");
        DatabaseReference newPosterRefer=newAdminReference.child("Posters");
        Map<String, Object> newEvents5= new HashMap<>();

        for(int i=0;i<event_banner.size();i++){
            newEvents5.put("EventBannerURL",event_banner.get(i));
            newEvents5.put("EventBannerKey",event_banner_key.get(i));
            newPosterRefer.child(event_banner_key.get(i)).updateChildren(newEvents5);
            newPosterNodes.child(event_banner_key.get(i)).updateChildren(newEvents5);

            //newPosterRefer.push().updateChildren(newEvents5);
            //newPosterNodes.push().updateChildren(newEvents5);


        }


        DatabaseReference newFAQNodes=newEventsReference.child("FAQs");
        DatabaseReference newfaqrefer=newAdminReference.child("FAQs");
        Map<String, Object> newEvents2= new HashMap<>();

        for(int i=0;i<question.size();i++){
            newEvents2.put("Ques",question.get(i));
            newEvents2.put("Ans",answer.get(i));

            DatabaseReference aa=newFAQNodes.push();
            String keyx=aa.getKey();
            newEvents2.put("FAQKey",keyx);
            newfaqrefer.child(keyx).updateChildren(newEvents2);
            aa.updateChildren(newEvents2);

            //newfaqrefer.push().updateChildren(newEvents2);
            //newFAQNodes.push().updateChildren(newEvents2);


        }

        // newfaqrefer.updateChildren(newEvents2);
        DatabaseReference newOrganiserNodes=newEventsReference.child("Organisers");
        DatabaseReference neworganiserrefer=newAdminReference.child("Organisers");
        Map<String, Object> newEvents3= new HashMap<>();

        for(int i=0;i<organiser_name.size();i++){
            newEvents3.put("Name",organiser_name.get(i));
            newEvents3.put("Role",organiser_role.get(i));
            newEvents3.put("Email",organiser_email.get(i));
            newEvents3.put("Phone",organiser_phone.get(i));
            newEvents3.put("Pic",organiser_pic.get(i));
            newEvents3.put("OrganiserKey",orgKeyS.get(i));
            neworganiserrefer.child(orgKeyS.get(i)).updateChildren(newEvents3);
            newOrganiserNodes.child(orgKeyS.get(i)).updateChildren(newEvents3);
        }

        DatabaseReference newTicketNodes=newEventsReference.child("Tickets");
        final Map<String, Object> newEvents4= new HashMap<>();

        for(int i=0;i<ticket_name.size();i++){
            newEvents4.put("Name",ticket_name.get(i));
            newEvents4.put("Category",ticket_category.get(i));
            newEvents4.put("Description",ticket_description.get(i));
            newEvents4.put("Price",ticket_price.get(i));
            TicketPrices.add(Integer.parseInt(ticket_price.get(i)));

            //DatabaseReference newTicketNodesN=newTicketNodes.push();
            //String key=newTicketNodesN.getKey();
            //newEvents4.put("TicketKey",key);
            //newTicketNodesN.updateChildren(newEvents4);

            newEvents4.put("PRICE_CHECK", fees_setting.get(i));



            DatabaseReference aa=newTicketNodes.push();
            String keyx=aa.getKey();
            newEvents4.put("TicketKey",keyx);
            newEvents4.put("EventKey",key);
            aa.updateChildren(newEvents4);
        }





        listener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                club_name=dataSnapshot.child(user.getUid()).child("ClubName").getValue(String.class);
                //Toast.makeText(getApplicationContext()," "+club_name,Toast.LENGTH_SHORT).show();
                club_domain=dataSnapshot.child(user.getUid()).child("ClubDomain").getValue(String.class);
                club_image_url=dataSnapshot.child(user.getUid()).child("ProfileImageUrl").getValue(String.class);
                club_location=dataSnapshot.child(user.getUid()).child("ClubLocation").getValue(String.class);
                club_type=dataSnapshot.child(user.getUid()).child("ClubType").getValue(String.class);



                int startingprice= Collections.min(TicketPrices);
                int endingprices=Collections.max(TicketPrices);




                final Map<String, Object> Eventsdb = new HashMap<>();
                Eventsdb.put("UploadedDate",todays_date);
                Eventsdb.put("EventName", event_name);



                Eventsdb.put("EventThumbnailURL", event_banner.get(0));
                Eventsdb.put("UploadedBy", user.getUid());
                Eventsdb.put("EventKey",key);
                Eventsdb.put("ClubDomain",club_domain);
                Eventsdb.put("ClubName",club_name);
                Eventsdb.put("ClubType",club_type);
                Eventsdb.put("EventDescription",event_description);


                Eventsdb.put("ClubImageURL",club_image_url);
                Eventsdb.put("ClubLocation",club_location);
                Eventsdb.put("EventTypeX",event_typeX);

                Eventsdb.put("Ticketing","Enabled");
                /*Eventsdb.put("EventURL",)*/

                Eventsdb.put("StartingPrice",startingprice);
                Eventsdb.put("EndingPrice",endingprices);
                /*Eventsdb.put("TEAM_CHECK",ticket_type);
                Eventsdb.put("PRICE_CHECK", fees_setting);
                Eventsdb.put("TeamMembers", team_sizeS);*/

                Eventsdb.put("EventLocation", location);
                Eventsdb.put("EventAddress", event_address);



                newEventsReference.updateChildren(Eventsdb).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                startActivity(new Intent(NewEventActivity5.this, EventsHistoryActivity.class));
                                finishAffinity();
                            }
                        }, 4000);
                    }
                });


                /*adminReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        newEventsReference.updateChildren(Eventsdb);
                       // newFAQNodes.updateChildren(newEvents2);
                       // newOrganiserNodes.updateChildren(newEvents3);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        adminReference.addValueEventListener(listener);

        Toast.makeText(NewEventActivity5.this, "Hotel Added Successfully", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onDestroy() {
            adminReference.removeEventListener(listener);
        super.onDestroy();
    }
}
