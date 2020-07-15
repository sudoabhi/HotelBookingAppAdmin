package com.example.hotelloadmin.EventsPackage;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.hotelloadmin.R;

import com.example.hotelloadmin.ScannerPackage.BookedTicketsSolo;
import com.example.hotelloadmin.ScannerPackage.BookedTicketsTeam;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventDetailsTicketsActivity extends AppCompatActivity {



    DatabaseReference parentReference;
    DatabaseReference eventReference;
    DatabaseReference adminReference;

    FirebaseUser user;

    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;

    private LinearLayoutManager mLayoutManager;


    List<EventDetailsTicketsData> posts;

    int position=-2;

    String EventKey;

    android.support.v7.widget.Toolbar toolbar;

    String EventNameS;
    ProgressBar progressBar;


    ChildEventListener cel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details_tickets);



        progressBar=(ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle(null);

        user = FirebaseAuth.getInstance().getCurrentUser();
        parentReference = FirebaseDatabase.getInstance().getReference().child("HotelLo");
        eventReference = FirebaseDatabase.getInstance().getReference().child("Hotels");
        adminReference = FirebaseDatabase.getInstance().getReference().child("HotelLoAdmin");

        Intent i = getIntent();
        EventKey = i.getStringExtra("EventKey");


        posts = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_tickets);
        mLayoutManager = new LinearLayoutManager(EventDetailsTicketsActivity.this);
        mAdapter = new EventDetailsTicketsActivity.TicketAdapter(EventDetailsTicketsActivity.this, posts);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        posts.clear();



        fetch();


        cel=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String TicketName = dataSnapshot.child("Name").getValue(String.class);
                String TicketCategroy = dataSnapshot.child("Category").getValue(String.class);
                String TicketPrice = dataSnapshot.child("Price").getValue(String.class);
                String TicketDescription = dataSnapshot.child("Description").getValue(String.class);
                String MinTeamMembers=dataSnapshot.child("MinTeamMembers").getValue(String.class);
                String MaxTeamMembers=dataSnapshot.child("TeamMembers").getValue(String.class);
                String TEAM_CHECK=dataSnapshot.child("TEAM_CHECK").getValue(String.class);
                String PRICE_CHECK=dataSnapshot.child("PRICE_CHECK").getValue(String.class);
                String TicketKey=dataSnapshot.child("TicketKey").getValue(String.class);


                posts.add(new EventDetailsTicketsData(TicketName, TicketCategroy, TicketDescription, TicketPrice,MinTeamMembers,MaxTeamMembers,TEAM_CHECK,PRICE_CHECK,TicketKey));

                mAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

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
        eventReference.child(EventKey).child("Tickets").addChildEventListener(cel);

    }


    @Override
    protected void onDestroy() {
        eventReference.child(EventKey).child("Tickets").removeEventListener(cel);
        super.onDestroy();
    }

    class TicketAdapter extends RecyclerView.Adapter<EventDetailsTicketsActivity.TicketAdapter.MyViewHolder>{

        private List<EventDetailsTicketsData> posts2;
        private Context context;



        public class MyViewHolder extends RecyclerView.ViewHolder{

            TextView TicketName;
            TextView TicketCategory;
            TextView TicketDescription;
            TextView TicketPrice;
            TextView TeamContext;
            ImageView TeamImage;
            ImageView IndividualImage;
            TextView TeamPayment;
            TextView IndividualPayment;
            LinearLayout ticket1;
            LinearLayout ticket3;
            CardView ticket2;


            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                TicketName=(TextView)itemView.findViewById(R.id.ticket_name);
                TicketCategory=(TextView)itemView.findViewById(R.id.ticket_category);
                TicketPrice=(TextView)itemView.findViewById(R.id.ticket_price);
                TicketDescription=(TextView)itemView.findViewById(R.id.ticket_description);
                TeamContext=itemView.findViewById(R.id.team_context);
                TeamImage=itemView.findViewById(R.id.team_image);
                IndividualImage=itemView.findViewById(R.id.individual_image);
                TeamPayment=itemView.findViewById(R.id.team_payment);
                IndividualPayment=itemView.findViewById(R.id.individual_payment);
                ticket2 =itemView.findViewById(R.id.ticket2);
                ticket1 =itemView.findViewById(R.id.ticket1);
                ticket3 =itemView.findViewById(R.id.ticket3);
            }


        }

        public TicketAdapter(Context context,List<EventDetailsTicketsData> posts2) {
            this.context=context;
            this.posts2=posts2;

        }



        @NonNull
        @Override
        public TicketAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_event_details_tickets,parent,false);
            EventDetailsTicketsActivity.TicketAdapter.MyViewHolder vh = new  EventDetailsTicketsActivity.TicketAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull TicketAdapter.MyViewHolder holder, int i) {

            final EventDetailsTicketsData c = posts2.get(i);
            holder.TicketName.setText(c.getTicketName());
            holder.TicketCategory.setText(c.getTicketCategory());
            holder.TicketDescription.setText(c.getTicketDescription());
            //  holder.TicketPrice.setText("₹ "+c.getTicketPrice());


            holder.ticket2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*if(c.getTEAM_CHECK().equals("Individual")){
                        Intent intent=new Intent(EventDetailsTicketsActivity.this, BookedTicketsSolo.class);
                        intent.putExtra("TicketKey",c.getTicketKey());
                        intent.putExtra("TicketName",c.getTicketName());
                        intent.putExtra("EventKey",EventKey);
                        startActivity(intent);
                    }
                    if(c.getTEAM_CHECK().equals("Team")){
                        Intent intent=new Intent(EventDetailsTicketsActivity.this, BookedTicketsTeam.class);
                        intent.putExtra("TicketKey",c.getTicketKey());
                        intent.putExtra("TicketName",c.getTicketName());
                        intent.putExtra("EventKey",EventKey);
                        startActivity(intent);
                    }*/

                    /*Intent intent=new Intent(EventDetailsTicketsActivity.this, BookedTickets.class);
                    intent.putExtra("TicketKey",c.getTicketKey());
                    intent.putExtra("EventKey",EventKey);
                    intent.putExtra("TEAM_CHECK",c.getTEAM_CHECK());
                    startActivity(intent);*/


                }
            });

            holder.ticket3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    /*if(c.getTEAM_CHECK().equals("Individual")){
                        Intent intent=new Intent(EventDetailsTicketsActivity.this, BookedTicketsSolo.class);
                        intent.putExtra("TicketKey",c.getTicketKey());
                        intent.putExtra("TicketName",c.getTicketName());
                        intent.putExtra("EventKey",EventKey);
                        startActivity(intent);
                    }
                    if(c.getTEAM_CHECK().equals("Team")){
                        Intent intent=new Intent(EventDetailsTicketsActivity.this, BookedTicketsTeam.class);
                        intent.putExtra("TicketKey",c.getTicketKey());
                        intent.putExtra("TicketName",c.getTicketName());
                        intent.putExtra("EventKey",EventKey);
                        startActivity(intent);
                    }*/

                    /*Intent intent=new Intent(EventDetailsTicketsActivity.this, BookedTickets.class);
                    intent.putExtra("TicketKey",c.getTicketKey());
                    intent.putExtra("EventKey",EventKey);
                    intent.putExtra("TEAM_CHECK",c.getTEAM_CHECK());
                    startActivity(intent);*/


                }
            });

            holder.ticket1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
/*
                    if(c.getTEAM_CHECK().equals("Individual")){
                        Intent intent=new Intent(EventDetailsTicketsActivity.this, BookedTicketsSolo.class);
                        intent.putExtra("TicketKey",c.getTicketKey());
                        intent.putExtra("TicketName",c.getTicketName());
                        intent.putExtra("EventKey",EventKey);
                        startActivity(intent);
                    }
                    if(c.getTEAM_CHECK().equals("Team")){
                        Intent intent=new Intent(EventDetailsTicketsActivity.this, BookedTicketsTeam.class);
                        intent.putExtra("TicketKey",c.getTicketKey());
                        intent.putExtra("TicketName",c.getTicketName());
                        intent.putExtra("EventKey",EventKey);
                        startActivity(intent);
                    }*/

                    /*Intent intent=new Intent(EventDetailsTicketsActivity.this, BookedTickets.class);
                    intent.putExtra("TicketKey",c.getTicketKey());
                    intent.putExtra("EventKey",EventKey);
                    intent.putExtra("TEAM_CHECK",c.getTEAM_CHECK());
                    startActivity(intent);*/


                }
            });



            if(c.getPRICE_CHECK().equals("Book and Pay at hotel")){
                holder.TeamImage.setVisibility(View.VISIBLE);
                holder.IndividualImage.setVisibility(View.GONE);
                if(c.getPRICE_CHECK().equals("Book and Pay at hotel")){
                    holder.TeamPayment.setVisibility(View.VISIBLE);
                    holder.TicketPrice.setText("₹"+c.getTicketPrice()+"");
                    holder.IndividualPayment.setVisibility(View.GONE);
                }
                else{
                    holder.TeamPayment.setVisibility(View.GONE);
                    holder.TicketPrice.setText("₹"+c.getTicketPrice()+"");
                    holder.IndividualPayment.setVisibility(View.VISIBLE);
                }
            }
            else{
                holder.TeamImage.setVisibility(View.GONE);
                holder.IndividualImage.setVisibility(View.VISIBLE);
                holder.TeamPayment.setVisibility(View.GONE);
                holder.IndividualPayment.setVisibility(View.GONE);
                holder.TicketPrice.setText("₹"+c.getTicketPrice()+"");
            }


        }

        @Override
        public int getItemCount() {
            return posts2.size();
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void fetch() {


        eventReference.child(EventKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                EventNameS = dataSnapshot.child("EventName").getValue(String.class);
                toolbar.setTitle(EventNameS);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
