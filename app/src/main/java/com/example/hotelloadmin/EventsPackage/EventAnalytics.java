package com.example.hotelloadmin.EventsPackage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hotelloadmin.R;
import com.example.hotelloadmin.StudentsList.InterestedStudents;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import im.dacer.androidcharts.LineView;


public class EventAnalytics extends AppCompatActivity {


    DatabaseReference adminReference;
    FirebaseUser user;

    DatabaseReference eventsReference;
    String EventKey;
    String EventName;
    int position;
    TextView likes;
    TextView bookmarks;
    TextView interested;
    TextView tickets_sold;
    TextView attendees;
    TextView revenue;



    LinearLayout liked_parent;
    LinearLayout bookmarked_parent;
    LinearLayout interested_parent;
    LinearLayout tickets_parent;
    LinearLayout revenue_parent;
    LinearLayout attendees_parent;

    int count1=0;
    int count2=0;
    int count3=0;



    int solo_tickets_count=0;
    int team_tickets_count=0;
    int total_ticket_count=0;
    int attendees_count=0;
    int revenue_count=0;


    int temp=0;



    ArrayList<String> userUid=new ArrayList<>();

    String todays_date;

    SharedPreferences preferencesGet;
    SharedPreferences preferencesPut ;
    SharedPreferences.Editor editor ;


    String flag;

    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout graph_lay;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_analytics);
        Intent i = getIntent();
        EventKey = i.getStringExtra("Key");
        EventName = i.getStringExtra("EventName");
        position=i.getIntExtra("position",-2);

        likes=findViewById(R.id.likes);
        bookmarks=findViewById(R.id.bookmarks);
        interested=findViewById(R.id.interested);
        tickets_sold=findViewById(R.id.tickets_sold);
        attendees=findViewById(R.id.attendees);
        revenue=findViewById(R.id.revenue);

        liked_parent=(LinearLayout)findViewById(R.id.liked_parent);
        bookmarked_parent=(LinearLayout)findViewById(R.id.bookmarked_parent);
        interested_parent=(LinearLayout)findViewById(R.id.interested_parent);
        tickets_parent=(LinearLayout)findViewById(R.id.tickets_parent);
        attendees_parent=(LinearLayout)findViewById(R.id.attendees_parent);
        revenue_parent=(LinearLayout)findViewById(R.id.revenue_parent);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarx);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle(EventName);

        graph_lay=findViewById(R.id.graphs_lay);

        user= FirebaseAuth.getInstance().getCurrentUser();

        eventsReference = FirebaseDatabase.getInstance().getReference().child("Hotels");
        adminReference = FirebaseDatabase.getInstance().getReference().child("HotelLoAdmin");



        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                flag="Same";
                solo_tickets_count=0;
                team_tickets_count=0;
                total_ticket_count=0;
                attendees_count=0;
                revenue_count=0;
                temp=0;
                userUid.clear();
                fetch();

            }
        });

        swipeRefreshLayout.setColorSchemeColors(Color.BLACK,Color.BLUE,Color.RED,Color.YELLOW);




        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, 0);
        Date date = cal.getTime();
        todays_date=dateFormat.format(date);



        preferencesGet = getSharedPreferences("Analytics"+EventKey, MODE_PRIVATE);
        preferencesPut = getSharedPreferences("Analytics"+EventKey, MODE_PRIVATE);


        flag="Same";
        if(preferencesGet.contains("TodaysDate")) {


            get7DatesUpdated();

        }else{

            getDates();
        }





        /*liked_parent.setEnabled(false);
        bookmarked_parent.setEnabled(false);
        interested_parent.setEnabled(false);
        tickets_parent.setEnabled(false);
        revenue_parent.setEnabled(false);
        attendees_parent.setEnabled(false);*/

        liked_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(count2>0) {
                    Intent intent = new Intent(EventAnalytics.this, InterestedStudents.class);
                    intent.putExtra("EventKey", EventKey);
                    intent.putExtra("Check", "Liked");
                    startActivity(intent);
                }
                else{
                    Toast.makeText(EventAnalytics.this,"No one has liked your hotel yet", Toast.LENGTH_SHORT).show();
                }

            }
        });

        bookmarked_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(count3>0) {
                    Intent intent = new Intent(EventAnalytics.this, InterestedStudents.class);
                    intent.putExtra("EventKey", EventKey);
                    intent.putExtra("Check", "Bookmarked");
                    startActivity(intent);
                }
                else{
                    Toast.makeText(EventAnalytics.this,"No one has bookmarked your hotel yet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        interested_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(count1>0) {
                    Intent intent = new Intent(EventAnalytics.this, InterestedStudents.class);
                    intent.putExtra("EventKey", EventKey);
                    intent.putExtra("Check", "Interested");
                    startActivity(intent);
                }
                else{
                    Toast.makeText(EventAnalytics.this,"No one has shown interest in your hotel yet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tickets_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((solo_tickets_count+team_tickets_count)>0) {
                    Intent i = new Intent(EventAnalytics.this, EventDetailsTicketsActivity.class);
                    i.putExtra("EventKey", EventKey);
                    startActivity(i);
                }
                else{
                    Toast.makeText(EventAnalytics.this,"No rooms booked yet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        revenue_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(revenue_count>0) {

                    Intent i = new Intent(EventAnalytics.this, EventAnalyticsRevenueActivity.class);
                    i.putExtra("EventKey", EventKey);
                    startActivity(i);
                }
                else{
                    Toast.makeText(EventAnalytics.this,"Your hotel has not generated any revenue yet", Toast.LENGTH_SHORT).show();
                }

            }
        });


        attendees_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(attendees_count>0) {

                    Intent i = new Intent(EventAnalytics.this, EventAnalyticsAttendeesActivity.class);
                    i.putExtra("EventKey", EventKey);
                    i.putExtra("AttendeesCount", attendees_count);
                    startActivity(i);
                }
                else{
                    Toast.makeText(EventAnalytics.this,"No customers yet to show", Toast.LENGTH_SHORT).show();
                }

            }
        });



        fetch();





        /*eventsReference.child(EventKey).child("Tickets").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds1 : dataSnapshot.getChildren()){


                    if(ds1.child("TEAM_CHECK").getValue(String.class).equals("Individual")){

                        solo_tickets_count+=(int)ds1.child("BookedTickets").getChildrenCount();

                        String price = ds1.child("Price").getValue(String.class);
                        int priceInt = Integer.parseInt(price);
                        int booked_tick_count =(int)ds1.child("BookedTickets").getChildrenCount();
                        if (booked_tick_count != 0) {
                            revenue_count += (booked_tick_count * priceInt);
                        }
                    }


                    if(ds1.child("TEAM_CHECK").getValue(String.class).equals("Team")) {

                        team_tickets_count += (int) ds1.child("BookedTickets").getChildrenCount();


                        if (ds1.getChildrenCount() > 0) {
                            for (DataSnapshot ds3 : ds1.child("BookedTickets").getChildren()) {

                                for (DataSnapshot ds2 : ds3.getChildren()) {

                                    if (ds2.hasChildren()) {

                                        if (ds1.child("PRICE_CHECK").getValue(String.class).equals("Individual")) {
                                            temp = temp + 1;

                                        }

                                    }

                                }

                            }
                        }
                        String price = ds1.child("Price").getValue(String.class);
                        int priceInt = Integer.parseInt(price);
                        if (ds1.child("PRICE_CHECK").getValue(String.class).equals("Individual")) {
                            if (temp != 0) {
                                revenue_count += (temp * priceInt);
                            }
                        }
                        if(ds1.child("PRICE_CHECK").getValue(String.class).equals("Team")) {
                            int booked_tick_count = (int) ds1.child("BookedTickets").getChildrenCount();
                            if (booked_tick_count != 0) {
                                revenue_count += (booked_tick_count * priceInt);
                            }
                        }


                    }



                    //attendees
                    for (DataSnapshot ad : ds1.child("BookedTickets").getChildren()){

                        if(Objects.equals(ds1.child("TEAM_CHECK").getValue(String.class), "Individual")){
                            if(Objects.equals(ad.child("Verified").getValue(String.class), "true")) {
                                userUid.add(ad.getKey());
                            }
                        }
                        if(Objects.equals(ds1.child("TEAM_CHECK").getValue(String.class), "Team")){

                            for(DataSnapshot ds3: ad.getChildren()){

                                if(ds3.hasChildren()){
                                    if(Objects.equals(ds3.child("Verified").getValue(String.class), "true")) {
                                        userUid.add(ds3.getKey());
                                    }
                                }
                            }

                        }

                    }
                    Set<String> set = new HashSet<>(userUid);
                    userUid.clear();
                    userUid.addAll(set);

                    attendees_count=userUid.size();



                }

                eventsReference.child(EventKey).child("Tickets").removeEventListener(this);

                revenue.setText("\u20B9 " + revenue_count);
                tickets_sold.setText(""+(solo_tickets_count+team_tickets_count));
                attendees.setText(""+attendees_count);

                if(revenue_count!=0){
                    revenue_parent.setEnabled(true);
                }
                if((solo_tickets_count+team_tickets_count)!=0){
                    tickets_parent.setEnabled(true);
                }
                if(attendees_count!=0){
                    attendees_parent.setEnabled(true);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/



/*

        cel=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {

                //revenue and ticket count

                if(dataSnapshot.child("TEAM_CHECK").getValue(String.class).equals("Individual")){

                    solo_tickets_count+=(int)dataSnapshot.child("BookedTickets").getChildrenCount();

                    String price = dataSnapshot.child("Price").getValue(String.class);
                    int priceInt = Integer.parseInt(price);
                    int booked_tick_count =(int)dataSnapshot.child("BookedTickets").getChildrenCount();
                    if (booked_tick_count != 0) {
                        revenue_count += (booked_tick_count * priceInt);
                    }
                }


                if(dataSnapshot.child("TEAM_CHECK").getValue(String.class).equals("Team")) {

                    team_tickets_count += (int) dataSnapshot.child("BookedTickets").getChildrenCount();




                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot ds : dataSnapshot.child("BookedTickets").getChildren()) {

                            for (DataSnapshot ds2 : ds.getChildren()) {

                                if (ds2.hasChildren()) {

                                    if (dataSnapshot.child("PRICE_CHECK").getValue(String.class).equals("Individual")) {
                                        temp = temp + 1;

                                    }

                                }

                            }

                        }
                    }
                    String price = dataSnapshot.child("Price").getValue(String.class);
                    int priceInt = Integer.parseInt(price);
                    if (dataSnapshot.child("PRICE_CHECK").getValue(String.class).equals("Individual")) {
                        if (temp != 0) {
                            revenue_count += (temp * priceInt);
                        }
                    }
                    if(dataSnapshot.child("PRICE_CHECK").getValue(String.class).equals("Team")) {
                        int booked_tick_count = (int) dataSnapshot.child("BookedTickets").getChildrenCount();
                        if (booked_tick_count != 0) {
                            revenue_count += (booked_tick_count * priceInt);
                        }
                    }





                }


                //attendees
                for (DataSnapshot ad : dataSnapshot.child("BookedTickets").getChildren()){

                    if(Objects.equals(dataSnapshot.child("TEAM_CHECK").getValue(String.class), "Individual")){
                        if(Objects.equals(ad.child("Verified").getValue(String.class), "true")) {
                            userUid.add(ad.getKey());
                        }
                    }
                    if(Objects.equals(dataSnapshot.child("TEAM_CHECK").getValue(String.class), "Team")){

                        for(DataSnapshot ds: ad.getChildren()){

                            if(ds.hasChildren()){
                                if(Objects.equals(ds.child("Verified").getValue(String.class), "true")) {
                                    userUid.add(ds.getKey());
                                }
                            }
                        }

                    }

                }
                Set<String> set = new HashSet<>(userUid);
                userUid.clear();
                userUid.addAll(set);

                attendees_count=userUid.size();







                revenue.setText("\u20B9 " + revenue_count);
                tickets_sold.setText(""+(solo_tickets_count+team_tickets_count));
                attendees.setText(""+attendees_count);

                if(revenue_count!=0){
                    revenue_parent.setEnabled(true);
                }
                if((solo_tickets_count+team_tickets_count)!=0){
                    tickets_parent.setEnabled(true);
                }
                if(attendees_count!=0){
                    attendees_parent.setEnabled(true);
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
        };
        eventsReference.child(EventKey).child("Tickets").addChildEventListener(cel);

*/




    }



    protected void fetch(){

        count1=0;
        count2=0;
        count3=0;

        eventsReference.child(EventKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count1=(int)dataSnapshot.child("Interested").getChildrenCount();



                if(dataSnapshot.hasChild("LikeCount")){
                    count2=Integer.parseInt(dataSnapshot.child("LikeCount").getValue(String.class));
                }

                if(dataSnapshot.hasChild("BookmarkCount")){
                    count3=Integer.parseInt(dataSnapshot.child("BookmarkCount").getValue(String.class));
                }

                //Log.e("Count",""+count2);

                String upload=dataSnapshot.child("UploadedDate").getValue(String.class);
                if (upload != null) {
                    upload=upload.substring(0,5);
                }
                if (Objects.equals(upload, todays_date)){
                    graph_lay.setVisibility(View.INVISIBLE);
                }


                //ticket,attendee,revenue
                for (DataSnapshot ds1 : dataSnapshot.child("Tickets").getChildren()){

                    /*if(ds1.child("TEAM_CHECK").getValue(String.class).equals("Individual")){

                        solo_tickets_count+=(int)ds1.child("BookedTickets").getChildrenCount();

                        String price = ds1.child("Price").getValue(String.class);
                        int priceInt = Integer.parseInt(price);
                        int booked_tick_count =(int)ds1.child("BookedTickets").getChildrenCount();
                        if (booked_tick_count != 0) {
                            revenue_count += (booked_tick_count * priceInt);
                        }
                    }*/

/*
                    if(ds1.child("TEAM_CHECK").getValue(String.class).equals("Team")) {

                        team_tickets_count += (int) ds1.child("BookedTickets").getChildrenCount();


                        if (ds1.getChildrenCount() > 0) {
                            for (DataSnapshot ds3 : ds1.child("BookedTickets").getChildren()) {

                                for (DataSnapshot ds2 : ds3.getChildren()) {

                                    if (ds2.hasChildren()) {

                                        if (ds1.child("PRICE_CHECK").getValue(String.class).equals("Individual")) {
                                            temp = temp + 1;

                                        }

                                    }

                                }

                            }
                        }
                        String price = ds1.child("Price").getValue(String.class);
                        int priceInt = Integer.parseInt(price);
                        if (ds1.child("PRICE_CHECK").getValue(String.class).equals("Individual")) {
                            if (temp != 0) {
                                revenue_count += (temp * priceInt);
                            }
                        }
                        if(ds1.child("PRICE_CHECK").getValue(String.class).equals("Team")) {
                            int booked_tick_count = (int) ds1.child("BookedTickets").getChildrenCount();
                            if (booked_tick_count != 0) {
                                revenue_count += (booked_tick_count * priceInt);
                            }
                        }


                    }*/



                    //attendees
                    /*for (DataSnapshot ad : ds1.child("BookedTickets").getChildren()){

                        if(Objects.equals(ds1.child("TEAM_CHECK").getValue(String.class), "Individual")){
                            if(Objects.equals(ad.child("Verified").getValue(String.class), "true")) {
                                userUid.add(ad.getKey());
                            }
                        }
                        if(Objects.equals(ds1.child("TEAM_CHECK").getValue(String.class), "Team")){

                            for(DataSnapshot ds3: ad.getChildren()){

                                if(ds3.hasChildren()){
                                    if(Objects.equals(ds3.child("Verified").getValue(String.class), "true")) {
                                        userUid.add(ds3.getKey());
                                    }
                                }
                            }

                        }

                    }*/
                    Set<String> set = new HashSet<>(userUid);
                    userUid.clear();
                    userUid.addAll(set);

                    attendees_count=userUid.size();



                }

                eventsReference.child(EventKey).removeEventListener(this);


                interested.setText(""+count1);
                bookmarks.setText(""+count3);
                likes.setText(""+count2);
                if(count1!=0){
                    interested_parent.setEnabled(true);
                }
                if(count2!=0){
                    liked_parent.setEnabled(true);
                }
                if(count3!=0){
                    bookmarked_parent.setEnabled(true);
                }

                revenue.setText("\u20B9 " + revenue_count);
                tickets_sold.setText(""+(solo_tickets_count+team_tickets_count));
                attendees.setText(""+attendees_count);

                if(revenue_count!=0){
                    revenue_parent.setEnabled(true);
                }
                if((solo_tickets_count+team_tickets_count)!=0){
                    tickets_parent.setEnabled(true);
                }
                if(attendees_count!=0){
                    attendees_parent.setEnabled(true);
                }








                swipeRefreshLayout.setRefreshing(false);



                if (preferencesGet.contains("TodaysLikes")){
                    updateDetails();
                }
                else {
                    storeDetails();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    @Override
    protected void onDestroy() {
        //eventsReference.child(EventKey).child("Tickets").removeEventListener(cel);
        super.onDestroy();
    }









    ArrayList<String> pDate =new ArrayList<>();

    private void getDates() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        cal.add(Calendar.DAY_OF_YEAR, 0);
        Date date = cal.getTime();
        pDate.add(dateFormat.format(date));

        editor = preferencesPut.edit();
        editor.putString("TodaysDate",todays_date);

        for(int x=0;x<6;x++){
            cal.add(Calendar.DAY_OF_YEAR, -(1));
            pDate.add(dateFormat.format(cal.getTime()));
            editor.putString("PDate-"+(x+1),dateFormat.format(cal.getTime()));

        }

        editor.apply();

        Toast.makeText(EventAnalytics.this,"New dates", Toast.LENGTH_SHORT).show();


    }


    private void get7DatesUpdated() {

        editor = preferencesPut.edit();

        if(!Objects.equals(preferencesGet.getString("TodaysDate", ""), todays_date)) {

            flag="Updated";
            pDate.clear();
            pDate.add(todays_date);
            pDate.add(preferencesGet.getString("TodaysDate", ""));
            for (int x = 1; x < 6; x++) {
                pDate.add(preferencesGet.getString("PDate-" + (x), ""));
            }

            for (int x = 5; x > 0; x--) {
                editor.putString("PDate-" + (x+1), preferencesGet.getString("PDate-" + (x), ""));
            }
            editor.putString("PDate-" + (1), preferencesGet.getString("TodaysDate", ""));
            editor.putString("TodaysDate", todays_date);
            Toast.makeText(EventAnalytics.this,"Update dates", Toast.LENGTH_SHORT).show();
        }
        else {
            pDate.clear();
            flag="Same";
            pDate.add(preferencesGet.getString("TodaysDate", ""));
            for (int x = 0; x < 6; x++) {
                pDate.add(preferencesGet.getString("PDate-" + (x + 1), ""));
            }
            Toast.makeText(EventAnalytics.this,"Same dates", Toast.LENGTH_SHORT).show();
        }
        editor.apply();

    }




    public void Graph(){

        LineView lineViewLike = (LineView)findViewById(R.id.line_view_like);
        LineView lineViewBookmark = (LineView)findViewById(R.id.line_view_bookmark);
        LineView lineViewTickets = (LineView)findViewById(R.id.line_view_tickets);
        LineView lineViewAttendees = (LineView)findViewById(R.id.line_view_attendees);
        LineView lineViewRevenue = (LineView)findViewById(R.id.line_view_revenue);

        ArrayList<String> list=new ArrayList<>();
        list= pDate;
        Collections.reverse(list);
        lineViewLike.setBottomTextList(list);
        lineViewLike.setColorArray(new int[] {Color.parseColor("#F44336")});
        lineViewLike.setDrawDotLine(false);
        lineViewLike.setShowPopup(LineView.SHOW_POPUPS_NONE);

        lineViewBookmark.setBottomTextList(list);
        lineViewBookmark.setColorArray(new int[] {Color.parseColor("#2196F3")});
        lineViewBookmark.setDrawDotLine(false);
        lineViewBookmark.setShowPopup(LineView.SHOW_POPUPS_NONE);

        lineViewTickets.setBottomTextList(list);
        lineViewTickets.setColorArray(new int[] {Color.parseColor("#3EB13E")});
        lineViewTickets.setDrawDotLine(false);
        lineViewTickets.setShowPopup(LineView.SHOW_POPUPS_NONE);

        lineViewAttendees.setBottomTextList(list);
        lineViewAttendees.setColorArray(new int[] {Color.parseColor("#EB984E")});
        lineViewAttendees.setDrawDotLine(false);
        lineViewAttendees.setShowPopup(LineView.SHOW_POPUPS_NONE);

        lineViewRevenue.setBottomTextList(list);
        lineViewRevenue.setColorArray(new int[] {R.color.mainColor});
        lineViewRevenue.setDrawDotLine(false);
        lineViewRevenue.setShowPopup(LineView.SHOW_POPUPS_NONE);


        ArrayList<Integer> dataList = new ArrayList<>();
        dataList= pDateLike;
        Collections.reverse(dataList);
        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<>();
        dataLists.add(dataList);
        lineViewLike.setDataList(dataLists);

        dataList = new ArrayList<>();
        dataList= pDateBookmark;
        Collections.reverse(dataList);
        dataLists = new ArrayList<>();
        dataLists.add(dataList);
        lineViewBookmark.setDataList(dataLists);


        dataList = new ArrayList<>();
        dataList= pDateTickets;
        Collections.reverse(dataList);
        dataLists = new ArrayList<>();
        dataLists.add(dataList);
        lineViewTickets.setDataList(dataLists);

        dataList = new ArrayList<>();
        dataList= pDateAttendees;
        Collections.reverse(dataList);
        dataLists = new ArrayList<>();
        dataLists.add(dataList);
        lineViewAttendees.setDataList(dataLists);


        dataList = new ArrayList<>();
        dataList= pDateRevenue;
        Collections.reverse(dataList);
        dataLists = new ArrayList<>();
        dataLists.add(dataList);
        lineViewRevenue.setDataList(dataLists);


    }



    ArrayList<Integer> pDateLike =new ArrayList<>();
    ArrayList<Integer> pDateBookmark =new ArrayList<>();
    ArrayList<Integer> pDateTickets =new ArrayList<>();
    ArrayList<Integer> pDateAttendees =new ArrayList<>();
    ArrayList<Integer> pDateRevenue =new ArrayList<>();


    public void storeDetails(){


        pDateLike.add(count2);
        pDateBookmark.add(count3);
        pDateTickets.add(solo_tickets_count+team_tickets_count);
        pDateAttendees.add(attendees_count);
        pDateRevenue.add(revenue_count);

        editor = preferencesPut.edit();

        editor.putInt("TodaysLikes",count2);
        editor.putInt("TodaysBookmarks",count3);
        editor.putInt("TodaysTickets",solo_tickets_count+team_tickets_count);
        editor.putInt("TodaysAttendees",attendees_count);
        editor.putInt("TodaysRevenue",revenue_count);

        for(int x=0;x<6;x++){
            pDateLike.add(0);
            editor.putInt("PDateLikes-"+(x+1), 0);

            pDateBookmark.add(0);
            editor.putInt("PDateBookmarks-"+(x+1), 0);

            pDateTickets.add(0);
            editor.putInt("PDateTickets-"+(x+1), 0);

            pDateAttendees.add(0);
            editor.putInt("PDateAttendees-"+(x+1), 0);

            pDateRevenue.add(0);
            editor.putInt("PDateRevenue-"+(x+1), 0);
        }
        editor.apply();

        Toast.makeText(EventAnalytics.this,"New", Toast.LENGTH_SHORT).show();

        Graph();

    }




    public void updateDetails(){


        editor = preferencesPut.edit();

        if(Objects.equals(flag, "Updated")) {

            pDateLike.clear();
            pDateBookmark.clear();
            pDateTickets.clear();
            pDateAttendees.clear();
            pDateRevenue.clear();

            pDateLike.add(count2);
            pDateLike.add(preferencesGet.getInt("TodaysLikes", 0));

            pDateBookmark.add(count3);
            pDateBookmark.add(preferencesGet.getInt("TodaysBookmarks", 0));

            pDateTickets.add(solo_tickets_count+team_tickets_count);
            pDateTickets.add(preferencesGet.getInt("TodaysTickets", 0));

            pDateAttendees.add(attendees_count);
            pDateAttendees.add(preferencesGet.getInt("TodaysAttendees", 0));

            pDateRevenue.add(revenue_count);
            pDateRevenue.add(preferencesGet.getInt("TodaysRevenue", 0));

            for (int x = 1; x < 6; x++) {
                pDateLike.add(preferencesGet.getInt("PDateLikes-" + (x), 0));
                pDateBookmark.add(preferencesGet.getInt("PDateBookmarks-" + (x), 0));
                pDateTickets.add(preferencesGet.getInt("PDateTickets-" + (x), 0));
                pDateAttendees.add(preferencesGet.getInt("PDateAttendees-" + (x), 0));
                pDateRevenue.add(preferencesGet.getInt("PDateRevenue-" + (x), 0));

            }

            for (int x = 5; x > 0; x--) {
                editor.putInt("PDateLikes-" + (x+1), preferencesGet.getInt("PDateLikes-" + (x), 0));
                editor.putInt("PDateBookmarks-" + (x+1), preferencesGet.getInt("PDateBookmarks-" + (x), 0));
                editor.putInt("PDateTickets-" + (x+1), preferencesGet.getInt("PDateTickets-" + (x), 0));
                editor.putInt("PDateAttendees-" + (x+1), preferencesGet.getInt("PDateAttendees-" + (x), 0));
                editor.putInt("PDateRevenue-" + (x+1), preferencesGet.getInt("PDateRevenue-" + (x), 0));

            }
            editor.putInt("PDateLikes-" + (1), preferencesGet.getInt("TodaysLikes", 0));
            editor.putInt("TodaysLikes", count2);

            editor.putInt("PDateBookmarks-" + (1), preferencesGet.getInt("TodaysBookmarks", 0));
            editor.putInt("TodaysBookmarks", count3);

            editor.putInt("PDateTickets-" + (1), preferencesGet.getInt("TodaysTickets", 0));
            editor.putInt("TodaysTickets", solo_tickets_count+team_tickets_count);

            editor.putInt("PDateAttendees-" + (1), preferencesGet.getInt("TodaysAttendees", 0));
            editor.putInt("TodaysAttendees", attendees_count);

            editor.putInt("PDateRevenue-" + (1), preferencesGet.getInt("TodaysRevenue", 0));
            editor.putInt("TodaysRevenue", revenue_count);

            Toast.makeText(EventAnalytics.this,"Updated", Toast.LENGTH_SHORT).show();
        }
        else {
            pDateLike.clear();
            pDateBookmark.clear();
            pDateTickets.clear();
            pDateAttendees.clear();
            pDateRevenue.clear();

            pDateLike.add(count2);
            pDateBookmark.add(count3);
            pDateTickets.add(solo_tickets_count+team_tickets_count);
            pDateAttendees.add(attendees_count);
            pDateRevenue.add(revenue_count);

            for (int x = 0; x < 6; x++) {
                pDateLike.add(preferencesGet.getInt("PDateLikes-" + (x + 1), 0));
                pDateBookmark.add(preferencesGet.getInt("PDateBookmarks-" + (x + 1), 0));
                pDateTickets.add(preferencesGet.getInt("PDateTickets-" + (x + 1), 0));
                pDateAttendees.add(preferencesGet.getInt("PDateAttendees-" + (x + 1), 0));
                pDateRevenue.add(preferencesGet.getInt("PDateRevenue-" + (x + 1), 0));

            }

            Toast.makeText(EventAnalytics.this,"Same", Toast.LENGTH_SHORT).show();
        }
        editor.apply();

        Graph();


    }






}
