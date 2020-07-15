package com.example.hotelloadmin.EventsPackage;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelloadmin.R;
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

public class EventDetailsOrganisersActivity extends AppCompatActivity {



    DatabaseReference parentReference;
    DatabaseReference eventReference;
    DatabaseReference adminReference;

    FirebaseUser user;


    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;

    private LinearLayoutManager mLayoutManager;


    List<EventDetailsOrganisersData> posts;

    String EventKey;

    android.support.v7.widget.Toolbar toolbar;

    String EventNameS;
    ProgressBar progressBar;

    ChildEventListener cel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details_organisers);



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

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_organisers);
        mLayoutManager = new LinearLayoutManager(EventDetailsOrganisersActivity.this);
        mAdapter = new EventDetailsOrganisersActivity.OrganiserAdapter(EventDetailsOrganisersActivity.this, posts);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        posts.clear();



        fetch();


        cel=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String Name = dataSnapshot.child("Name").getValue(String.class);
                String Role = dataSnapshot.child("Role").getValue(String.class);
                String Phone = dataSnapshot.child("Phone").getValue(String.class);
                String Email = dataSnapshot.child("Email").getValue(String.class);
                String Pic=dataSnapshot.child("Pic").getValue(String.class);
                String Key=dataSnapshot.child("OrganiserKey").getValue(String.class);

                posts.add(new EventDetailsOrganisersData(Name, Role,  Email,Phone,Pic,Key));
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
        adminReference.child(user.getUid()).child("Events").child(EventKey).child("Organisers").addChildEventListener(cel);

    }


    @Override
    protected void onDestroy() {
        adminReference.child(user.getUid()).child("Events").child(EventKey).child("Organisers").removeEventListener(cel);
        super.onDestroy();
    }

    class OrganiserAdapter extends RecyclerView.Adapter<EventDetailsOrganisersActivity.OrganiserAdapter.MyViewHolder2>{

        private List<EventDetailsOrganisersData> posts3;
        private Context context;



        public class MyViewHolder2 extends RecyclerView.ViewHolder{

            TextView Name;
            TextView Role;
            TextView Phone;
            TextView Email;
            ImageView Pic;
            LinearLayout lay1;
            ProgressBar progressBar2;
            View view2;


            public MyViewHolder2(@NonNull View itemView) {
                super(itemView);
                Name=(TextView)itemView.findViewById(R.id.organiser_name);
                Phone=(TextView)itemView.findViewById(R.id.organiser_phone);
                Email=(TextView)itemView.findViewById(R.id.organiser_email);
                Role=(TextView)itemView.findViewById(R.id.organiser_role);
                Pic=itemView.findViewById(R.id.organiser_pic);
                lay1=itemView.findViewById(R.id.lay1);
                lay1.setVisibility(View.INVISIBLE);
                progressBar2=itemView.findViewById(R.id.progressBar2);
                view2=itemView.findViewById(R.id.view2);

            }


        }

        public OrganiserAdapter(Context context,List<EventDetailsOrganisersData> posts3) {
            this.context=context;
            this.posts3=posts3;

        }



        @NonNull
        @Override
        public EventDetailsOrganisersActivity.OrganiserAdapter.MyViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_event_details_organisers,parent,false);
            EventDetailsOrganisersActivity.OrganiserAdapter.MyViewHolder2 vh = new  EventDetailsOrganisersActivity.OrganiserAdapter.MyViewHolder2(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull final EventDetailsOrganisersActivity.OrganiserAdapter.MyViewHolder2 holder, int i) {

            final EventDetailsOrganisersData c = posts3.get(i);
            holder.Name.setText(c.getOrganiserName());
            holder.Role.setText(c.getOrganiserRole());
            holder.Phone.setText("(+91)"+c.getOrganiserPhone());
            holder.Email.setText(c.getOrganiserEmail());

            Picasso.get()
                    .load(c.getOrganiserPic())
                    .error(android.R.drawable.stat_notify_error)
                    //.resize(65,80)
                    //.centerCrop()
                    .into(holder.Pic, new Callback() {
                        @Override
                        public void onSuccess() {

                            holder.progressBar2.setVisibility(View.GONE);
                            holder.lay1.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {

                            Toast.makeText(context, "Failed to load organisers", Toast.LENGTH_SHORT).show();

                        }


                    });

            if(i==posts3.size()-1){
                holder.view2.setVisibility(View.INVISIBLE);
            }


        }

        @Override
        public int getItemCount() {
            return posts3.size();
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
