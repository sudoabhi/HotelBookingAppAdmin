package com.example.hotelloadmin.DiscussionsPackage;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hotelloadmin.EventsPackage.EventDiscussions;
import com.example.hotelloadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPost extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    List<PostHintsModel> post_hints;
    EditText blog_et;
    CardView EdiTextCardView;
    String blogs;
    DatabaseReference newblogreference;
    FirebaseDatabase blog_database;
    DatabaseReference blog_reference;
    FirebaseDatabase parentDatabase;
    DatabaseReference parentReference;

    String key;
    String todays_date;
    String todays_time;

    String ImageUrl;
    String UserSkill;
    String dayS;
    String monthS;
    String yearS;
    String am_pm;

    FirebaseUser user;
    String UploadedBy;

    String UserName;
    String ClubLocation;

    String likeS;
    String dislikeS;

    int no_likes;
    int no_dislikes;
    ProgressBar progressBar;

    String EventKeyX;
    String EventNameXX;


    android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        Intent i=getIntent();
        EventKeyX= i.getStringExtra("EventKey");
        EventNameXX=i.getStringExtra("EventNameXX");

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(EventKeyX==null){
            blog_database = FirebaseDatabase.getInstance();
            blog_reference = blog_database.getReference();
            blog_reference=blog_reference.child("HotelierBlogs");
        }
        else{
            blog_database=FirebaseDatabase.getInstance();
            blog_reference = blog_database.getReference();
            blog_reference=blog_reference.child("EventBlogs").child(EventKeyX);
        }
        parentDatabase = FirebaseDatabase.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();
        parentReference = parentDatabase.getReference();
        parentReference=parentReference.child("HotelLoAdmin");

        EdiTextCardView = (CardView)findViewById(R.id.post_blog);
        progressBar=(ProgressBar) findViewById(R.id.progress_bar);


        blog_et = (EditText) findViewById(R.id.post_blog_et);

        post_hints=new ArrayList<>();
       /* post_hints.add(new PostHintsModel(R.drawable.ic_remember,"How would you like to be remembered ?"));
        post_hints.add(new PostHintsModel(R.drawable.ic_sad,"I am feeling sad, cheer me up."));
        post_hints.add(new PostHintsModel(R.drawable.ic_made,"Hey look what I have made recently."));
        post_hints.add(new PostHintsModel(R.drawable.ic_friendship,"Who will accompany me to this event ?"));
        post_hints.add(new PostHintsModel(R.drawable.ic_handshake,"Need a partner for my ongoing project."));*/
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mAdapter = new AddPost.MyAdapter(AddPost.this, post_hints);



        mLayoutManager = new LinearLayoutManager(AddPost.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);

        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);



        mRecyclerView.setAdapter(mAdapter);

       
        EdiTextCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeEditText();
            }
        });


    }

    private void getTodaysTime() {

        Calendar calendar = Calendar.getInstance();


        int hourS = calendar.get(Calendar.HOUR_OF_DAY);
        yearS = Integer.toString(calendar.get(Calendar.YEAR));
        int monthofYear = calendar.get(Calendar.MONTH);
        dayS = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        int minuteS = calendar.get(Calendar.MINUTE);
        //    Toast.makeText(getActivity(),hourS+minutesS,Toast.LENGTH_SHORT).show();

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourS);
        datetime.set(Calendar.MINUTE, minuteS);

        String minute;
        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        if (datetime.get(Calendar.MINUTE) < 10) {

            minute = "0" + datetime.get(Calendar.MINUTE);
        } else minute = "" + datetime.get(Calendar.MINUTE);

        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";
        todays_time = strHrsToShow + ":" + minute + " " + am_pm;

        switch (monthofYear + 1) {
            case 1:
                monthS = "Jan";
                break;
            case 2:
                monthS = "Feb";
                break;
            case 3:
                monthS = "March";
                break;
            case 4:
                monthS = "April";
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

        todays_date = "" + dayS + " " + monthS + ", " + yearS;


    }


    public void publishPost(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(blog_et.getWindowToken(), 0);

        getTodaysTime();

        blogs = blog_et.getText().toString();
        if (blogs.length() == 0) {
            Toast.makeText(AddPost.this, "Post cannot be empty", Toast.LENGTH_LONG).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            blog_et.setText("");
            blog_et.setCursorVisible(false);
            final String skill = "Android Development";
            newblogreference = blog_reference.push();
            likeS = "false";
            dislikeS = "false";
            no_likes = 0;
            no_dislikes = 0;




            key = newblogreference.getKey();
            final DatabaseReference blog_node_reference = blog_reference.child(key);

            final Map<String, Object> newBlog = new HashMap<>();
            parentReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ImageUrl = dataSnapshot.child(user.getUid()).child("ProfileImageUrl").getValue(String.class);
                    UserName = dataSnapshot.child(user.getUid()).child("ClubName").getValue(String.class);
                    ClubLocation = dataSnapshot.child(user.getUid()).child("ClubLocation").getValue(String.class);

                    newBlog.put("UpdatedDay", todays_date);
                    newBlog.put("UpdatedTime", todays_time);
                    newBlog.put("ProfileImage", ImageUrl);
                    newBlog.put("BlogText", blogs);
                    newBlog.put("Skill", skill);
                    newBlog.put("UserName", UserName);
                    newBlog.put("ClubLocation",ClubLocation);

                    newBlog.put("UploadedBy", user.getUid());

                    newBlog.put("Key", key);
                    newBlog.put("NumberLikes", no_likes);
                    newBlog.put("NumberDislikes", no_dislikes);




                    blog_node_reference.updateChildren(newBlog).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            blog_reference.child(key).child("LikeStatus").child(user.getUid()).setValue(likeS);
                            blog_reference.child(key).child("DislikeStatus").child(user.getUid()).setValue(dislikeS);
                            progressBar.setVisibility(View.INVISIBLE);
                            if(EventKeyX==null){
                                //Intent intent=new Intent(AddPost.this, BlogFeedActivity.class);
                                //startActivity(intent);
                                finish();
                            }
                            else{
                                Intent intent=new Intent(AddPost.this, EventDiscussions.class);
                                intent.putExtra("EventKey", EventKeyX);
                                intent.putExtra("EventNameXX",EventNameXX);
                                startActivity(intent);
                                finish();

                            }


                        }
                    });


                   /* blog_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            posts.clear();
                            mAdapter.notifyDataSetChanged();
                            Log.i("HWW", "HWW");

                            fetch();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }





    }


    public void initializeEditText(){

        blog_et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


    }



   /* @Override
    public boolean onSupportNavigateUp() {
        Log.e("Work","Work");
        onBackPressed();
        return super.onSupportNavigateUp();
    }*/

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_post, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.publish:

                publishPost();
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return true;


    }

    @Override
    public void onBackPressed() {
        /*Intent intent=new Intent(AddPost.this, BlogFeedActivity.class);
        startActivity(intent);
        finish();*/
        super.onBackPressed();
    }

    class MyAdapter extends RecyclerView.Adapter<AddPost.MyAdapter.MyViewHolder>{


        private List<PostHintsModel> post_hintsX;
        private Context context;

        public MyAdapter(Context context, List<PostHintsModel> post_hintsX) {
            this.context=context;
            this.post_hintsX=post_hintsX;

        }


        @NonNull
        @Override
        public AddPost.MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_hint_model,parent,false);
            AddPost.MyAdapter.MyViewHolder vh = new  AddPost.MyAdapter.MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull AddPost.MyAdapter.MyViewHolder holder, int i) {

            final PostHintsModel c= post_hintsX.get(i);

            holder.UserName.setText(c.getText());
            holder.ProfileImage.setImageResource(c.getImage());

            holder.ParentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    blog_et.setText(c.getText());
                    blog_et.setSelection(c.getText().toString().length());
                    initializeEditText();
                    Toast.makeText(AddPost.this,"Click \"PUBLISH\" to proceed",Toast.LENGTH_SHORT).show();


                }
            });


        }

        @Override
        public int getItemCount() {
            return post_hintsX.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            TextView UserName;

            ImageView ProfileImage;
            RelativeLayout ParentView;



            public MyViewHolder(@NonNull View itemView) {


                super(itemView);


                UserName=(TextView)itemView.findViewById(R.id.username);
                ParentView=(RelativeLayout)itemView.findViewById(R.id.ParentView);

                ProfileImage=(ImageView) itemView.findViewById(R.id.profile_image);




            }
        }




    }
}
