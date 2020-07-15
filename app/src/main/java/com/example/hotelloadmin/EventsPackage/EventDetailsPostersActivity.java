package com.example.hotelloadmin.EventsPackage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hotelloadmin.NewEventPackage.EventPostersRecyclerData;
import com.example.hotelloadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EventDetailsPostersActivity extends AppCompatActivity {


    DatabaseReference parentReference;
    DatabaseReference eventReference;
    DatabaseReference adminReference;


    FirebaseUser user;
    String key;
    String eventName;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    List<EventPostersRecyclerData> list;

    ChildEventListener cel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details_posters);

        parentReference = FirebaseDatabase.getInstance().getReference().child("HotelLo");
        eventReference = FirebaseDatabase.getInstance().getReference().child("Hotels");
        adminReference = FirebaseDatabase.getInstance().getReference().child("HotelLoAdmin");

        Intent i = getIntent();
        key = i.getStringExtra("EventKey");
        eventName=i.getStringExtra("EventName");


        user = FirebaseAuth.getInstance().getCurrentUser();

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ((AppCompatActivity) EventDetailsPostersActivity.this).setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(eventName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        list = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.event_banner_rv);
        mLayoutManager = new LinearLayoutManager(EventDetailsPostersActivity.this);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        mAdapter = new EventDetailsPosterAdapter(EventDetailsPostersActivity.this, list);
        mRecyclerView.setAdapter(mAdapter);
        list.clear();


        cel=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String Pic=dataSnapshot.child("EventBannerURL").getValue(String.class);
                String Pic_Key=dataSnapshot.child("EventBannerKey").getValue(String.class);
                list.add(new EventPostersRecyclerData(Pic,Pic_Key));
                mAdapter.notifyDataSetChanged();

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

        adminReference.child(user.getUid()).child("Events").child(key).child("Posters").addChildEventListener(cel);



    }


    @Override
    protected void onDestroy() {
        adminReference.child(user.getUid()).child("Events").child(key).child("Posters").removeEventListener(cel);
        super.onDestroy();
    }

    class EventDetailsPosterAdapter extends RecyclerView.Adapter<EventDetailsPostersActivity.EventDetailsPosterAdapter.MyViewHolder> {

        private List<EventPostersRecyclerData> postersList;
        Context context;


        // stores and recycles views as they are scrolled off screen
        public class MyViewHolder extends RecyclerView.ViewHolder {


            ImageView poster_pic;
            ProgressBar progressBar;

            public MyViewHolder(View itemView) {
                super(itemView);

                poster_pic = itemView.findViewById(R.id.poster_pic);

                progressBar = itemView.findViewById(R.id.progressBar);
            }

        }


        public EventDetailsPosterAdapter(Context context, List<EventPostersRecyclerData> postersList) {
            this.postersList = postersList;
            this.context = context;
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_row_event_details_banners, parent, false);

            return new EventDetailsPostersActivity.EventDetailsPosterAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, final int position) {

            EventPostersRecyclerData data = postersList.get(position);

            Picasso.get()
                    .load(data.getposter_pic())
                    .error(R.drawable.photo_selector3)
                    .into(viewHolder.poster_pic, new Callback() {
                        @Override
                        public void onSuccess() {

                            viewHolder.progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError(Exception e) {

                            Toast.makeText(context, "Failed to load posters", Toast.LENGTH_SHORT).show();

                        }


                    });



        }

        @Override
        public int getItemCount() {
            return postersList.size();
        }


    }


}





