package com.example.hotelloadmin.EventsPackage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hotelloadmin.HomePackage.AttendeesData;
import com.example.hotelloadmin.R;
import com.example.hotelloadmin.StudentsList.PeopleClick;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventAnalyticsAttendeesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    List<AttendeesData> attendees;
    DatabaseReference eventsReference;

    DatabaseReference parentRef;

    Toolbar toolbar;
    int attendees_count;
    FirebaseUser user;

    String EventKey;

    ChildEventListener cel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendees);


        Intent i = getIntent();
        EventKey = i.getStringExtra("EventKey");
        attendees_count = i.getIntExtra("AttendeesCount",0);


        user= FirebaseAuth.getInstance().getCurrentUser();

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event Attendees");

        toolbar.setTitleTextColor(ContextCompat.getColor(EventAnalyticsAttendeesActivity.this,R.color.black));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        eventsReference= FirebaseDatabase.getInstance().getReference().child("Hotels");
        parentRef= FirebaseDatabase.getInstance().getReference().child("HotelLo");


        attendees=new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mAdapter = new EventAnalyticsAttendeesActivity.MyAdapter(EventAnalyticsAttendeesActivity.this, attendees);



        mLayoutManager = new LinearLayoutManager(EventAnalyticsAttendeesActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);

        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);



        mRecyclerView.setAdapter(mAdapter);

        getAttendees();

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    protected void onDestroy() {
        eventsReference.child(EventKey).child("Tickets").removeEventListener(cel);

        super.onDestroy();
    }


    public void getAttendees(){

        final ArrayList<String> userUid=new ArrayList<>();

        cel=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                for (DataSnapshot ad : dataSnapshot.child("BookedTickets").getChildren()){

                    if(Objects.equals(dataSnapshot.child("TEAM_CHECK").getValue(String.class), "Individual")){
                        if(Objects.equals(ad.child("Verified").getValue(String.class), "true")) {
                            if (!userUid.contains(ad.getKey())) {
                                userUid.add(ad.getKey());
                            }
                        }
                    }
                    if(Objects.equals(dataSnapshot.child("TEAM_CHECK").getValue(String.class), "Team")){

                        for(DataSnapshot ds: ad.getChildren()){

                            if(ds.hasChildren()){
                                if(Objects.equals(ds.child("Verified").getValue(String.class), "true")){
                                    if (!userUid.contains(ds.getKey())) {
                                        userUid.add(ds.getKey());
                                    }
                                }
                            }
                        }
                    }
                }

                /*//removing duplicates //same person might have booked multiple tickets
                Set<String> set = new HashSet<>(userUid);
                userUid.clear();
                userUid.addAll(set);*/

                if(userUid.size()==attendees_count){

                    eventsReference.child(EventKey).child("Tickets").removeEventListener(cel);
                    for(final String uid: userUid){


                        parentRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                String userName=dataSnapshot.child("UserName").getValue(String.class);
                                String collegeName=dataSnapshot.child("CollegeName").getValue(String.class);
                                String districtName=dataSnapshot.child("DistrictName").getValue(String.class);
                                String profilePic=dataSnapshot.child("ImageUrl").getValue(String.class);

                                attendees.add(new AttendeesData(userName,uid,collegeName,profilePic,districtName,
                                        null,null,null));
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        eventsReference.child(EventKey).child("Tickets").addChildEventListener(cel);








        /*parentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<String> userUid=new ArrayList<>();
                ArrayList<String> eventName=new ArrayList<>();

                for (AttendeesData ad : attendees){
                    userUid.add(ad.getUserUID());
                    eventName.add(ad.getEventName());
                }

                Log.e("uid",""+userUid);
                Log.e("eventN",""+eventName);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


    }












    class MyAdapter extends RecyclerView.Adapter<EventAnalyticsAttendeesActivity.MyAdapter.MyViewHolder>{


        private List<AttendeesData> attendeesList;
        private Context context;

        public MyAdapter(Context context, List<AttendeesData> attendeesX) {
            this.context=context;
            this.attendeesList =attendeesX;

        }


        @NonNull
        @Override
        public EventAnalyticsAttendeesActivity.MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_event_analytics_attendees,parent,false);
            EventAnalyticsAttendeesActivity.MyAdapter.MyViewHolder vh = new  EventAnalyticsAttendeesActivity.MyAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final EventAnalyticsAttendeesActivity.MyAdapter.MyViewHolder holder, int position) {

            final AttendeesData c= attendeesList.get(position);

            holder.progressBar.setVisibility(View.VISIBLE);
            holder.ll2.setVisibility(View.INVISIBLE);


            holder.ProfileImage.setClickable(false);

            holder.UserName.setText(c.getUserName());

            holder.UserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(EventAnalyticsAttendeesActivity.this, PeopleClick.class);
                    intent.putExtra("UserUID",c.getUserUID());
                    startActivity(intent);
                }
            });


            holder.College.setText(c.getCollege()+", "+c.getDistrict());

            Picasso.get()
                    .load(c.getProfileImage())
                    .into(holder.ProfileImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.ProfileImage.setClickable(true);
                            holder.ProfileImage.setVisibility(View.VISIBLE);
                            holder.ll2.setVisibility(View.VISIBLE);
                            holder.progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });

            holder.ProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(EventAnalyticsAttendeesActivity.this, PeopleClick.class);
                    intent.putExtra("UserUID",c.getUserUID());
                    startActivity(intent);
                }
            });



        }

        @Override
        public int getItemCount() {
            return attendeesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            TextView UserName;
            TextView  College;
            CircleImageView ProfileImage;
            ProgressBar progressBar;
            LinearLayout ll2;

            // TextView Activity;


            public MyViewHolder(@NonNull View itemView) {


                super(itemView);

                progressBar=itemView.findViewById(R.id.progressBar);
                ll2=itemView.findViewById(R.id.ll2);
                UserName=(TextView)itemView.findViewById(R.id.name);
                College=(TextView)itemView.findViewById(R.id.college);
                ProfileImage=(CircleImageView) itemView.findViewById(R.id.profile_image);




            }
        }



    }



}
