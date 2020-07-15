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
import com.example.hotelloadmin.ScannerPackage.BookedTicketsSolo;
import com.example.hotelloadmin.ScannerPackage.BookedTicketsTeam;
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

public class EventAnalyticsRevenueActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    List<EventAnalyticsRevenueData> revenue;
    DatabaseReference eventsReference;

    DatabaseReference parentRef;

    Toolbar toolbar;
    int attendees_count;
    FirebaseUser user;

    String EventKey;

    ChildEventListener cel;


    int solo_tickets_count=0;
    int team_tickets_count=0;
    int total_ticket_count=0;
    int revenue_count=0;
    int temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_analytics_revenue);


        Intent i = getIntent();
        EventKey = i.getStringExtra("EventKey");



        user= FirebaseAuth.getInstance().getCurrentUser();

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event Revenue");

        toolbar.setTitleTextColor(ContextCompat.getColor(EventAnalyticsRevenueActivity.this,R.color.black));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        eventsReference= FirebaseDatabase.getInstance().getReference().child("Hotels");
        parentRef= FirebaseDatabase.getInstance().getReference().child("HotelLo");


        revenue=new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mAdapter = new EventAnalyticsRevenueActivity.MyAdapter(EventAnalyticsRevenueActivity.this, revenue);



        mLayoutManager = new LinearLayoutManager(EventAnalyticsRevenueActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);

        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);



        mRecyclerView.setAdapter(mAdapter);

        getRevenue();

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


    public void getRevenue(){

        final ArrayList<String> userUid=new ArrayList<>();

        cel=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                String ticketName=dataSnapshot.child("Name").getValue(String.class);
                String teamCheck=dataSnapshot.child("TEAM_CHECK").getValue(String.class);
                String priceCheck=dataSnapshot.child("PRICE_CHECK").getValue(String.class);
                String ticketKey=dataSnapshot.child("TicketKey").getValue(String.class);

                if(teamCheck.equals("Individual")){

                    solo_tickets_count=(int)dataSnapshot.child("BookedTickets").getChildrenCount();

                    String price = dataSnapshot.child("Price").getValue(String.class);
                    int priceInt = Integer.parseInt(price);
                    int booked_tick_count =(int)dataSnapshot.child("BookedTickets").getChildrenCount();
                    if (booked_tick_count != 0) {
                        revenue_count = (booked_tick_count * priceInt);
                    }

                    revenue.add(new EventAnalyticsRevenueData(ticketName,teamCheck,priceCheck,solo_tickets_count,
                            revenue_count,ticketKey));
                    mAdapter.notifyDataSetChanged();

                }


                if(teamCheck.equals("Team")) {

                    team_tickets_count = (int) dataSnapshot.child("BookedTickets").getChildrenCount();


                    if (dataSnapshot.getChildrenCount() > 0) {
                        for (DataSnapshot ds : dataSnapshot.child("BookedTickets").getChildren()) {

                            for (DataSnapshot ds2 : ds.getChildren()) {

                                if (ds2.hasChildren()) {

                                    if (priceCheck.equals("Individual")) {
                                        temp = temp + 1;

                                    }

                                }

                            }

                        }
                    }
                    String price = dataSnapshot.child("Price").getValue(String.class);
                    int priceInt = Integer.parseInt(price);
                    if (priceCheck.equals("Individual")) {
                        if (temp != 0) {
                            revenue_count = (temp * priceInt);
                        }
                    }
                    if (priceCheck.equals("Team")) {
                        int booked_tick_count = (int) dataSnapshot.child("BookedTickets").getChildrenCount();
                        if (booked_tick_count != 0) {
                            revenue_count = (booked_tick_count * priceInt);
                        }
                    }

                    revenue.add(new EventAnalyticsRevenueData(ticketName,teamCheck,priceCheck,team_tickets_count,
                            revenue_count,ticketKey));
                    mAdapter.notifyDataSetChanged();


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










    }












    class MyAdapter extends RecyclerView.Adapter<EventAnalyticsRevenueActivity.MyAdapter.MyViewHolder>{


        private List<EventAnalyticsRevenueData> revenueDataList;
        private Context context;

        public MyAdapter(Context context, List<EventAnalyticsRevenueData> revenueData) {
            this.context=context;
            this.revenueDataList =revenueData;

        }


        @NonNull
        @Override
        public EventAnalyticsRevenueActivity.MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_event_analytics_revenue,parent,false);
            EventAnalyticsRevenueActivity.MyAdapter.MyViewHolder vh = new  EventAnalyticsRevenueActivity.MyAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final EventAnalyticsRevenueActivity.MyAdapter.MyViewHolder holder, int position) {

            final EventAnalyticsRevenueData c= revenueDataList.get(position);


            holder.Name.setText(c.getTicketName());

            holder.sold.setText(""+c.getTicketSold());

            holder.revenue.setText("\u20B9 "+c.getRevenue());

            holder.parent_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(c.getTeamCheck().equals("Individual")){
                        Intent intent=new Intent(EventAnalyticsRevenueActivity.this, BookedTicketsSolo.class);
                        intent.putExtra("TicketKey",c.getTicketKey());
                        intent.putExtra("TicketName",c.getTicketName());
                        intent.putExtra("EventKey",EventKey);
                        startActivity(intent);
                    }
                    if(c.getTeamCheck().equals("Team")){
                        Intent intent=new Intent(EventAnalyticsRevenueActivity.this, BookedTicketsTeam.class);
                        intent.putExtra("TicketKey",c.getTicketKey());
                        intent.putExtra("TicketName",c.getTicketName());
                        intent.putExtra("EventKey",EventKey);
                        startActivity(intent);
                    }
                }
            });






        }

        @Override
        public int getItemCount() {
            return revenueDataList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            TextView Name;
            TextView  sold;
            TextView revenue;
            LinearLayout parent_layout;

            // TextView Activity;


            public MyViewHolder(@NonNull View itemView) {


                super(itemView);



                Name=(TextView)itemView.findViewById(R.id.name);
                sold=(TextView)itemView.findViewById(R.id.sold);
                revenue=itemView.findViewById(R.id.revenue);
                parent_layout=itemView.findViewById(R.id.parent_layout);



            }
        }



    }



}
