package com.example.hotelloadmin.HomePackage;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hotelloadmin.DiscussionsPackage.ParticipantsObject;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttendeesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    List<AttendeesData> attendees;


    DatabaseReference parentRef;
    DatabaseReference adminRef;
    Toolbar toolbar;
    int attendees_count;
    FirebaseUser user;


    ChildEventListener cel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendees);


        user= FirebaseAuth.getInstance().getCurrentUser();

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Attendees");

        toolbar.setTitleTextColor(ContextCompat.getColor(AttendeesActivity.this,R.color.black));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);






        parentRef= FirebaseDatabase.getInstance().getReference().child("HotelLo");
        adminRef= FirebaseDatabase.getInstance().getReference().child("HotelLoAdmin");

        attendees=new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mAdapter = new AttendeesActivity.MyAdapter(AttendeesActivity.this, attendees);



        mLayoutManager = new LinearLayoutManager(AttendeesActivity.this);
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
        //adminRef.child(user.getUid()).child("Attendees").removeEventListener(cel);

        super.onDestroy();
    }

    public void getAttendees(){




        adminRef.child(user.getUid()).child("Attendees").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                for(DataSnapshot ds1: dataSnapshot.getChildren()){
                    final String Uid=ds1.getKey();
                    final String eventN;

                    final ArrayList<String> eventName=new ArrayList<>();
                    final ArrayList<String> dateBooked=new ArrayList<>();

                    for (DataSnapshot ds2 : ds1.getChildren()){

                        eventName.add(ds2.child("EventName").getValue(String.class));
                        dateBooked.add(ds2.child("DateBooked").getValue(String.class));


                    }


                    Set<String> set = new HashSet<>(eventName);
                    if(set.size()>1){
                        eventN=eventName.get(eventName.size()-1)+" + "+(set.size()-1);
                    }
                    else {
                        eventN=eventName.get(eventName.size()-1);
                    }

                    parentRef.child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String userName = dataSnapshot.child("UserName").getValue(String.class);
                            String collegeName = dataSnapshot.child("CollegeName").getValue(String.class);
                            String districtName = dataSnapshot.child("DistrictName").getValue(String.class);
                            String profilePic = dataSnapshot.child("ImageUrl").getValue(String.class);

                            attendees.add(new AttendeesData(userName, Uid, collegeName, profilePic, districtName, null,
                                    eventN, dateBooked.get(dateBooked.size()-1)));
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



/*

        cel=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                final String Uid=dataSnapshot.getKey();
                final String eventN;

                final ArrayList<String> eventName=new ArrayList<>();
                final ArrayList<String> dateBooked=new ArrayList<>();

                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    Map<String, String> map = (HashMap<String, String>) ds.getValue();
                    eventName.add(map.get("EventName"));
                    dateBooked.add(map.get("DateBooked"));

                }
                Set<String> set = new HashSet<>(eventName);
                if(set.size()>1){
                    eventN=eventName.get(eventName.size()-1)+" + "+(set.size()-1);
                }
                else {
                    eventN=eventName.get(eventName.size()-1);
                }


                    parentRef.child(Uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String userName = dataSnapshot.child("UserName").getValue(String.class);
                            String collegeName = dataSnapshot.child("CollegeName").getValue(String.class);
                            String districtName = dataSnapshot.child("DistrictName").getValue(String.class);
                            String profilePic = dataSnapshot.child("ImageUrl").getValue(String.class);

                            attendees.add(new AttendeesData(userName, Uid, collegeName, profilePic, districtName, null,
                                    eventN, dateBooked.get(dateBooked.size()-1)));
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

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
        adminRef.child(user.getUid()).child("Attendees").addChildEventListener(cel);*/




    }











    class MyAdapter extends RecyclerView.Adapter<AttendeesActivity.MyAdapter.MyViewHolder>{


        private List<AttendeesData> attendeesList;
        private Context context;

        public MyAdapter(Context context, List<AttendeesData> attendeesX) {
            this.context=context;
            this.attendeesList =attendeesX;

        }


        @NonNull
        @Override
        public AttendeesActivity.MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_attendees,parent,false);
            AttendeesActivity.MyAdapter.MyViewHolder vh = new  AttendeesActivity.MyAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final AttendeesActivity.MyAdapter.MyViewHolder holder, int position) {

            final AttendeesData c= attendeesList.get(position);

            holder.progressBar.setVisibility(View.VISIBLE);
            holder.ll2.setVisibility(View.INVISIBLE);



            holder.ProfileImage.setClickable(false);

            holder.UserName.setText(c.getUserName());

            holder.UserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(AttendeesActivity.this, PeopleClick.class);
                    intent.putExtra("UserUID",c.getUserUID());
                    startActivity(intent);
                }
            });
            holder.EventName.setText(c.getEventName());

            holder.DateBooked.setText(c.getDateBooked());

            holder.College.setText(c.getCollege()+", "+c.getDistrict());

            Picasso.get()
                    .load(c.getProfileImage())
                    .noFade()
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
                    Intent intent=new Intent(AttendeesActivity.this, PeopleClick.class);
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
            TextView EventName;
            TextView DateBooked;
            LinearLayout ll2;
            ProgressBar progressBar;
            CircleImageView ProfileImage;

            // TextView Activity;


            public MyViewHolder(@NonNull View itemView) {


                super(itemView);

                EventName=itemView.findViewById(R.id.event_name);
                UserName=(TextView)itemView.findViewById(R.id.name);
                College=(TextView)itemView.findViewById(R.id.college);
                ProfileImage=(CircleImageView) itemView.findViewById(R.id.profile_image);
                progressBar=itemView.findViewById(R.id.progressBar);
                ll2=itemView.findViewById(R.id.ll2);
                DateBooked=itemView.findViewById(R.id.date_booked);


            }
        }



    }
}
