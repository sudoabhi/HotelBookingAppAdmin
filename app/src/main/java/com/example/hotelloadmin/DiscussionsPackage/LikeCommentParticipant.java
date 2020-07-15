package com.example.hotelloadmin.DiscussionsPackage;

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
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.hotelloadmin.R;
import com.example.hotelloadmin.StudentsList.PeopleClick;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LikeCommentParticipant extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    List<ParticipantsObject> participants;
    DatabaseReference blogReference;
    String BlogKey;
    String CommentKey;
    DatabaseReference parentRef;
    DatabaseReference adminRef;
    Toolbar toolbar;
    int participant_count;

    ChildEventListener cel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_comment_participant);

        BlogKey=getIntent().getStringExtra("BlogKey");
        CommentKey=getIntent().getStringExtra("CommentKey");
        participant_count=getIntent().getIntExtra("Count",0);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comment Likes");

        toolbar.setTitleTextColor(ContextCompat.getColor(LikeCommentParticipant.this,R.color.black));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        blogReference= FirebaseDatabase.getInstance().getReference();
        blogReference=blogReference.child("HotelierBlogs");
        parentRef= FirebaseDatabase.getInstance().getReference().child("HotelLo");
        adminRef= FirebaseDatabase.getInstance().getReference().child("HotelLoAdmin");;

        participants=new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mAdapter = new LikeCommentParticipant.MyAdapter(LikeCommentParticipant.this, participants);



        mLayoutManager = new LinearLayoutManager(LikeCommentParticipant.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);

        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);



        mRecyclerView.setAdapter(mAdapter);

        fetch();

    }

    public void fetch(){

        cel=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                String userID=  dataSnapshot.getKey();
                getDetails(userID);



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
        blogReference.child(BlogKey).child("Comments").child(CommentKey).child("Likes").addChildEventListener(cel);



    }


    @Override
    protected void onDestroy() {
        blogReference.child(BlogKey).child("Comments").child(CommentKey).child("Likes").removeEventListener(cel);
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void getDetails(final String userID){
        Log.i("Keyc",""+userID);

        parentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(userID)){

                    String ImageUrl=   dataSnapshot.child(userID).child("ImageUrl").getValue(String.class);
                    String UserName=  dataSnapshot.child(userID).child("UserName").getValue(String.class);
                    String UserUid=  dataSnapshot.child(userID).child("UserUID").getValue(String.class);
                    String ClubLocation=dataSnapshot.child(userID).child("ClubLocation").getValue(String.class);

                    participants.add(new ParticipantsObject(UserName,UserUid,ClubLocation,ImageUrl,null));
                    mAdapter.notifyDataSetChanged();
                }
                else{

                    adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String ImageUrl=   dataSnapshot.child(userID).child("ProfileImageUrl").getValue(String.class);
                            String UserName=  dataSnapshot.child(userID).child("ClubName").getValue(String.class);
                            String UserUid=  dataSnapshot.child(userID).child("UserUID").getValue(String.class);
                            String ClubLocation=dataSnapshot.child(userID).child("ClubLocation").getValue(String.class);

                            participants.add(new ParticipantsObject(UserName,UserUid,ClubLocation,ImageUrl,null));
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


    }

    class MyAdapter extends RecyclerView.Adapter<LikeCommentParticipant.MyAdapter.MyViewHolder>{


        private List<ParticipantsObject> participantsX;
        private Context context;

        public MyAdapter(Context context, List<ParticipantsObject> participantsX) {
            this.context=context;
            this.participantsX=participantsX;

        }


        @NonNull
        @Override
        public LikeCommentParticipant.MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.likeparticipantsmodel,parent,false);
            LikeCommentParticipant.MyAdapter.MyViewHolder vh = new  LikeCommentParticipant.MyAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull LikeCommentParticipant.MyAdapter.MyViewHolder holder, int i) {

            final ParticipantsObject c= participantsX.get(i);

            holder.UserName.setText(c.getUserName());

            holder.UserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    parentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(c.getUserUID())){
                                Intent intent=new Intent(LikeCommentParticipant.this, PeopleClick.class);
                                intent.putExtra("UserUID",c.getUserUID());
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(c.getUserUID())){

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });
            holder.parent_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(c.getUserUID())){
                                Intent intent=new Intent(LikeCommentParticipant.this, PeopleClick.class);
                                intent.putExtra("UserUID",c.getUserUID());
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(c.getUserUID())){

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
            holder.inner_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(c.getUserUID())){
                                Intent intent=new Intent(LikeCommentParticipant.this, PeopleClick.class);
                                intent.putExtra("UserUID",c.getUserUID());
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(c.getUserUID())){

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });


            holder.UserClubLocation.setText(c.getUserClubLocation());
          //  holder.Activity.setText(c.getActivity());
            Picasso.get()
                    .load(c.getProfileImage())
                    .into(holder.ProfileImage);

        }

        @Override
        public int getItemCount() {
            return participantsX.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            TextView UserName;
            TextView  UserClubLocation;
            CircleImageView ProfileImage;
            LinearLayout inner_layout;
            RelativeLayout parent_layout;
           // TextView Activity;


            public MyViewHolder(@NonNull View itemView) {


                super(itemView);


                UserName=(TextView)itemView.findViewById(R.id.username);
                UserClubLocation=(TextView)itemView.findViewById(R.id.club_location);
                ProfileImage=(CircleImageView) itemView.findViewById(R.id.profile_image);
                inner_layout=itemView.findViewById(R.id.inner_layout);
                parent_layout=itemView.findViewById(R.id.parent_layout);
              //  Activity=(TextView)itemView.findViewById(R.id.activity);



            }
        }



    }
}
