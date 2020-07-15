package com.example.hotelloadmin.DiscussionsPackage;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ablanco.zoomy.Zoomy;


import com.example.hotelloadmin.CommonPackage.AppFAQsActivity;
import com.example.hotelloadmin.CommonPackage.ContactUsActivity;
import com.example.hotelloadmin.CommonPackage.FeedbackActivity;
import com.example.hotelloadmin.EventsPackage.EventsHistoryActivity;
import com.example.hotelloadmin.NewEventPackage.NewEventProcessDescription;

import com.example.hotelloadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogFeedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    RecyclerView mRecyclerView;
    int myLovelyVariable;
    FirebaseDatabase blog_database;
    DatabaseReference blog_reference;
    LinearLayoutManager mLayoutManager;
    String UserName;
    String blogs;
    DatabaseReference newblogreference;
    String key;
    String todays_date;
    String todays_time;
    String monthS;
    String yearS;
    String dayS;
    String am_pm;
    FirebaseUser user;
    String UploadedBy;
    FirebaseDatabase parentDatabase;
    DatabaseReference parentReference;
    String ImageUrl;
    String UserSkill;
    SharedPreferences myPrefs;
    private RecyclerView.Adapter mAdapter;

    String ProfileImage;
    String BlogText;
    String updated_day;
    String updated_time;
    String uploadedBy;
    String sort_category;

    String ClubLocation;
    String ClubLocationX;

    String likeS;
    String dislikeS;
    String Key;
    int spinner_position;
    int no_likes;
    int no_dislikes;
    ArrayList<BlogsObject> posts;
    int NumberOfDislikes;
    int NumberOfLikes;
    Handler mHandler;
    Boolean posted;

    String state;
    CardView EdiTextCardView;
    TextView tv_noresults;
    int m;
    ProgressBar progressBar;
    Toolbar toolbar;
    FloatingActionButton fab_button;
    ArrayList<String> FollowingPeople;
    long[] ChildrenCountS = new long[1];
  /*  TextView Students;
    TextView Organizers;*/


    String BlogFeedType;
    ChildEventListener childEventListener;

    private AdapterView.OnItemLongClickListener mListener;
    private static final int MENU_ITEM_EDIT = 1;
    private static final int MENU_ITEM_DELETE = 2;
    private static final int MENU_ITEM_REPORT = 3;
    // private static final int MENU_ITEM_ITEM4=4

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_feed);

        Intent i=getIntent();
        BlogFeedType=i.getStringExtra("BlogType");


        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Objects.equals(BlogFeedType, "MyBlogFeed")){
            toolbar.setTitle("My Posts");
            toolbar.setSubtitle("");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        state="Students";




        mHandler = new Handler();

        posted = true;

        tv_noresults = (TextView) findViewById(R.id.tv_noresults);

        EdiTextCardView = (CardView) findViewById(R.id.post_blog);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        user = FirebaseAuth.getInstance().getCurrentUser();
       /* Students=(TextView) findViewById(R.id.students);
        Organizers=(TextView) findViewById(R.id.organizers);*/


        blog_database = FirebaseDatabase.getInstance();
        blog_reference = blog_database.getReference();
        blog_reference=blog_reference.child("HotelierBlogs");

        // swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        newblogreference = blog_reference.push();
        UploadedBy = user.getUid();
        progressBar = (ProgressBar) findViewById(R.id.top_progress_bar);
        fab_button=(FloatingActionButton)findViewById(R.id.fab_button);

        parentDatabase = FirebaseDatabase.getInstance();
        parentReference = parentDatabase.getReference();
        parentReference=parentReference.child("HotelLo");


        //  posts = new ArrayList<>();
        //   mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        posts = new ArrayList<>();
        FollowingPeople=new ArrayList<>();

        mAdapter = new BlogFeedActivity.BlogAdapter(BlogFeedActivity.this, posts);

        mLayoutManager = new LinearLayoutManager(BlogFeedActivity.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        //  mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false
        );
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);

        mRecyclerView.setAdapter(mAdapter);



        getFollowingCount(new firebaseXXX2(){

            @Override
            public void onCallBack1(long string) {

                ChildrenCountS[0] =string;
                Log.e("hexXX",""+ChildrenCountS[0]);
            }
        });

        getFollowingPeople(new FirebaseCallBackX2() {
            @Override
            public void onCallBack(ArrayList<String> string) {

                // string.addAll(FollowingPeople);
                FollowingPeople.addAll(string);
                Log.e("SizeXX","h "+string.size());


            }
        });

        getTodaysTime();

        fetch();

      //  Organizers.setTextColor(ContextCompat.getColor(BlogFeedActivity.this, R.color.colorGrey));
     //   Students.setTextColor(ContextCompat.getColor(BlogFeedActivity.this, R.color.mainOrange));

       /* if(state.equals("Students")){
            changeToStudents();
        }
        else{
            changeToOrganizers();
        }
*/

      /*  Students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posts.clear();
                mAdapter.notifyDataSetChanged();
                state="Students";
                progressBar.setVisibility(View.VISIBLE);
                blog_reference = FirebaseDatabase.getInstance("https://ddrx-172d3-2a5f7.firebaseio.com/").getReference();
                changeToStudents();

            }
        });

        Organizers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                posts.clear();
                mAdapter.notifyDataSetChanged();
                state="Organizers";
                progressBar.setVisibility(View.VISIBLE);
                blog_reference=FirebaseDatabase.getInstance("https://organizersblog.firebaseio.com/").getReference();
                changeToOrganizers();

            }
        });

*/









        //   blog_et.setEnabled(false);




        fab_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BlogFeedActivity.this,AddPost.class);
                startActivity(intent);
                //finish();
            }
        });





    }


    private void deleteblog(final String key, final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BlogFeedActivity.this);
        alertDialogBuilder.setTitle("Attention !")
                .setMessage("Are you sure you want to delete this post?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        blog_reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                posts.remove(position);
                                mAdapter.notifyDataSetChanged();
                                Toast.makeText(BlogFeedActivity.this, "Post Deleted", Toast.LENGTH_LONG).show();
                            }
                        });


                    }


                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        })

                // create alert dialog
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

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


    private void editposts(final String key, final String blogtext, final int position) {
        LayoutInflater li = LayoutInflater.from(BlogFeedActivity.this);
        View promptsView = li.inflate(R.layout.edit_posts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BlogFeedActivity.this);

        alertDialogBuilder.setView(promptsView);

        final EditText subject = (EditText) promptsView
                .findViewById(R.id.subject);

        subject.setText(blogtext);
        subject.setSelection(subject.getText().length());

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                InputMethodManager imm = (InputMethodManager) BlogFeedActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(subject.getWindowToken(), 0);
                                blog_reference.child(key).child("BlogText").setValue(subject.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        posts.get(position).setBlogText(subject.getText().toString());
                                        mAdapter.notifyDataSetChanged();
                                        Toast.makeText(BlogFeedActivity.this, "Post Edited", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                // Map<String, Object> newAchievements = new HashMap<>();

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                InputMethodManager imm = (InputMethodManager) BlogFeedActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(subject.getWindowToken(), 0);
                                dialog.cancel();
                            }
                        });


        AlertDialog alertDialog = alertDialogBuilder.create();


        alertDialog.show();
        subject.requestFocus();
        InputMethodManager imm = (InputMethodManager) BlogFeedActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


    }

    public void open_allusers_menu() {

        LayoutInflater li = LayoutInflater.from(BlogFeedActivity.this);
        View promptsView = li.inflate(R.layout.newsfeed_allusers_options, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BlogFeedActivity.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);


        final AlertDialog alertDialog2 = alertDialogBuilder.create();

        // show it
        alertDialog2.show();

        TextView report = (TextView) promptsView.findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog2.cancel();


            }
        });

        TextView share = (TextView) promptsView.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.cancel();
            }
        });


    }

    private void open_admin_menu(final String key, final String BlogText, final int position) {
        LayoutInflater li = LayoutInflater.from(BlogFeedActivity.this);
        View promptsView = li.inflate(R.layout.newsfeed_admin_options, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BlogFeedActivity.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);


        final AlertDialog alertDialog2 = alertDialogBuilder.create();

        // show it
        alertDialog2.show();

        TextView delete = (TextView) promptsView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteblog(key, position);
                alertDialog2.cancel();


            }
        });

        TextView edit = (TextView) promptsView.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editposts(key, BlogText, position);
                alertDialog2.cancel();
            }
        });

        TextView share = (TextView) promptsView.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.cancel();
            }
        });


    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        myPrefs = BlogFeedActivity.this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        int prefName2 = myPrefs.getInt("keyX", 0);
        String prefName1 = myPrefs.getString("check", "null");
        String send_like = myPrefs.getString("send_like", "null");
        String send_dislike = myPrefs.getString("send_dislike", "null");
        int LikeCountS = myPrefs.getInt("LikeCountS", 0);
        int CommentsCount = myPrefs.getInt("CommentsCount", -2);
        int DislikeCountS = myPrefs.getInt("DislikeCountS", 0);
        String EditCheck=myPrefs.getString("EditCheck","null");
        Toast.makeText(BlogFeedActivity.this, "Mii " + prefName2, Toast.LENGTH_SHORT).show();
        if (prefName2 != -2 && prefName1.equals("true")) {
            if (posts.size() > prefName2) {
                posts.remove(prefName2);
                mAdapter.notifyDataSetChanged();
            }

        }

        if (prefName2 != -2) {
            if (posts.size() > prefName2) {
                if (!send_like.equals("hex")) {
                    posts.get(prefName2).setLikeS(send_like);
                }
                if (!send_dislike.equals("hex")) {
                    posts.get(prefName2).setDislikeS(send_dislike);
                }
                if (LikeCountS != -2) {
                    posts.get(prefName2).setNumberOfLikes(LikeCountS);
                }
                if (DislikeCountS != -2) {
                    posts.get(prefName2).setNumberOfDislikes(DislikeCountS);
                }
                if (CommentsCount != -2) {
                    posts.get(prefName2).setComments_count(CommentsCount);
                }
                if(EditCheck.equals("true")){
                    posts.get(prefName2).setEdited("true");
                }


                mAdapter.notifyDataSetChanged();

            }
        }
       /* if(prefName!=-2){

        }*/

        // t.setText(prefName);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            finish();

        } else if (id == R.id.nav_history) {
            startActivity(new Intent(getApplicationContext(), EventsHistoryActivity.class));
            finish();

        } else if (id == R.id.nav_create) {
            startActivity(new Intent(getApplicationContext(), NewEventProcessDescription.class));
            finish();

        } else if (id == R.id.nav_notif) {



        } else if (id == R.id.nav_payment) {

        } else if (id == R.id.nav_feedback) {
            startActivity(new Intent(getApplicationContext(), FeedbackActivity.class));

        } else if (id == R.id.nav_faq) {
            startActivity(new Intent(getApplicationContext(), AppFAQsActivity.class));

        } else if (id == R.id.nav_contactus) {
            startActivity(new Intent(getApplicationContext(), ContactUsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }







    private void fetch() {

        // Toast.makeText(getActivity(),""+database,Toast.LENGTH_SHORT).show();
       /* Log.e("database1",""+database);*/
        /*mRecyclerView.setVisibility(View.VISIBLE);
        tv_noresults.setVisibility(View.INVISIBLE);
*/


        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {

                UserName = snapshot.child("UserName").getValue(String.class);

                ProfileImage = snapshot.child("ProfileImage").getValue(String.class);
                BlogText = snapshot.child("BlogText").getValue(String.class);
                updated_day = snapshot.child("UpdatedDay").getValue(String.class);
                updated_time = snapshot.child("UpdatedTime").getValue(String.class);
                uploadedBy = snapshot.child("UploadedBy").getValue(String.class);
                likeS = snapshot.child("LikeStatus").child(user.getUid()).getValue(String.class);
                dislikeS = snapshot.child("DislikeStatus").child(user.getUid()).getValue(String.class);
                Key = snapshot.child("Key").getValue(String.class);
                if (snapshot.hasChild("NumberLikes")) {
                    NumberOfLikes = snapshot.child("NumberLikes").getValue(Integer.class);
                }
                if (snapshot.hasChild("NumberDislikes")) {
                    NumberOfDislikes = snapshot.child("NumberDislikes").getValue(Integer.class);
                }




                // NumberOfDislikes = snapshot.child("NumberDislikes").getValue(Integer.class);
                String BlogImage = snapshot.child("BlogPic").getValue(String.class);
                ClubLocationX = snapshot.child("ClubLocation").getValue(String.class);
                String Edited=snapshot.child("Edited").getValue(String.class);
                int comments_count = 0;
                if (snapshot.hasChild("CommentsCount")) {
                    comments_count = snapshot.child("CommentsCount").getValue(Integer.class);
                }

                Log.e("ClubLocation",""+ClubLocationX);

                if(Objects.equals(BlogFeedType, "MyBlogFeed")){
                    if(user.getUid().equals(uploadedBy)){
                        posts.add(new BlogsObject(UserName, ClubLocationX, BlogImage, ProfileImage, BlogText, uploadedBy, updated_time, updated_day, likeS, dislikeS, Key, NumberOfLikes, NumberOfDislikes, comments_count,Edited));
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount()-1);
                    }

                }
                else{
                    posts.add(new BlogsObject(UserName, ClubLocationX, BlogImage, ProfileImage, BlogText, uploadedBy, updated_time, updated_day, likeS, dislikeS, Key, NumberOfLikes, NumberOfDislikes, comments_count,Edited));
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount()-1);
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

        blog_reference.addChildEventListener(childEventListener);

        blog_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (posts.size() == 0) {

                    tv_noresults.setVisibility(View.VISIBLE);


                    //   Toast.makeText(getActivity(),"No content available",Toast.LENGTH_SHORT).show();
                } else {

                    tv_noresults.setVisibility(View.INVISIBLE);


                }


                progressBar.setVisibility(View.INVISIBLE);
                if (progressBar.getVisibility() == View.VISIBLE) {

                    Log.e("Visible", "Visible");
                }
                if (progressBar.getVisibility() == View.INVISIBLE) {

                    Log.e("Visible", "Invisible");

                }

                blog_reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    protected void onDestroy() {
        Log.e("OnDestroyBlogFeed","hguyuyyu");
        blog_reference.removeEventListener(childEventListener);
        super.onDestroy();
    }










    /* @Override
    public boolean onLongClick(View view) {
        return false;
    }*/

    class BlogAdapter extends RecyclerView.Adapter<BlogFeedActivity.BlogAdapter.MyViewHolder> {

        private List<BlogsObject> posts;
        private Context context;


        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case

            TextView UserName;
            TextView UserSkill;
            CircleImageView ProfileImage;
            ImageView BlogImage;
            TextView ClubLocation;

            TextView BlogText;

            LinearLayout likeButton;
            LinearLayout dislikeButton;
            TextView like_count;
            TextView dislike_count;
            ImageView like_icon;
            ImageView dislike_icon;
            FrameLayout parentView;
            LinearLayout views;
            TextView comments_count;
            TextView time;
            TextView edited_check;

            CardView user_uploader;
            LinearLayout user_uploader2;
            LinearLayout user_uploader3;

            ProgressBar progressBar;

            public MyViewHolder(View v) {
                super(v);
                parentView = (FrameLayout) v.findViewById(R.id.parent_view);
                edited_check=(TextView)v.findViewById(R.id.edited_check);


                UserName = (TextView) v.findViewById(R.id.user_name);
                likeButton = v.findViewById(R.id.like_button);
                dislikeButton =v.findViewById(R.id.dislike_button);
                BlogImage = (ImageView) v.findViewById(R.id.blog_image);
                comments_count = (TextView) v.findViewById(R.id.comment_count);

                BlogText = (TextView) v.findViewById(R.id.blog_text);
                views = (LinearLayout) v.findViewById(R.id.views);
              //  UserSkill = (TextView) v.findViewById(R.id.user_skill);
                ProfileImage = (CircleImageView) v.findViewById(R.id.profile_image);

                ClubLocation = (TextView) v.findViewById(R.id.skill_location);
                time=(TextView)v.findViewById(R.id.time);
                like_count=v.findViewById(R.id.like_count);
                dislike_count=v.findViewById(R.id.dislike_count);
                like_icon=v.findViewById(R.id.like_icon);
                dislike_icon=v.findViewById(R.id.dislike_icon);

                user_uploader=v.findViewById(R.id.user_uploader1);
                user_uploader2=v.findViewById(R.id.user_uploader2);
                user_uploader3=v.findViewById(R.id.user_uploader3);



            }

        }

        // Provide a suitable constructor (depends on the kind of dataset)

        public BlogAdapter(Context context, List<BlogsObject> posts) {
            this.context = context;
            this.posts = posts;

        }


        @Override
        public BlogFeedActivity.BlogAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                            int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog2, parent, false);
            BlogFeedActivity.BlogAdapter.MyViewHolder vh = new BlogFeedActivity.BlogAdapter.MyViewHolder(v);
            return vh;
        }


        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(@NonNull final BlogFeedActivity.BlogAdapter.MyViewHolder holder, final int position) {

            final BlogsObject c = posts.get(position);
            holder.parentView.setVisibility(View.INVISIBLE);

            holder.ClubLocation.setText(c.getClubLocation());
            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(BlogFeedActivity.this, BlogActivity.class);

                    intent.putExtra("Key", c.getKey());
                    intent.putExtra("DeletePosition", position);
                    startActivity(intent);
                }
            });


            holder.UserName.setText(c.getUserName());
            if(c.getEdited()!=null&&c.getEdited().equals("true")){
                holder.edited_check.setVisibility(View.VISIBLE);
            }
            else{
                holder.edited_check.setVisibility(View.GONE);
            }


            holder.user_uploader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            holder.user_uploader2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            holder.user_uploader3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            holder.UserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });



            //  Toast.makeText(getActivity(),""+posts.getNumberOfLikes(),Toast.LENGTH_SHORT).show();
            holder.like_count.setText(""+c.getNumberOfLikes());
            holder.dislike_count.setText(""+c.getNumberOfDislikes());
            holder.comments_count.setText("" + c.getComments_count());

            Transformation transformation = new Transformation() {

                @Override
                public Bitmap transform(Bitmap source) {
                    int targetWidth;
                    int targetHeight;
                    int phoneHeight;

                    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    DisplayMetrics metrics = new DisplayMetrics();
                    display.getMetrics(metrics);

                    targetWidth = metrics.widthPixels / 2;
                    // phoneHeight=metrics.heightPixels;
                    // targetWidth = holder.EventBanner.getWidth();
                    // if(targetWidth==0){

                    // }
                    Log.e("Width", "" + targetWidth);


                    double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                    targetHeight = (int) (targetWidth * aspectRatio);
                    Log.e("Height", "" + targetHeight);


                    Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                    if (result != source) {
                        // Same bitmap is returned if sizes are the same
                        source.recycle();
                    }
                    return result;


                }

                @Override
                public String key() {
                    return "transformation" + " desiredWidth";
                }
            };

            if (c.getBlogImage() == null) {


                Resources r = BlogFeedActivity.this.getResources();
                int px = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        15,
                        r.getDisplayMetrics()
                );

                int px2 = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        10,
                        r.getDisplayMetrics()
                );

                int px3 = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        5,
                        r.getDisplayMetrics()
                );

                holder.parentView.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.views.getLayoutParams();
                params.setMargins(-px3, px3, -px3, -px2);
                holder.views.setLayoutParams(params);
            } else {

                Picasso.get()
                        .load(c.getBlogImage())
                        .error(android.R.drawable.stat_notify_error)

                        .transform(transformation)
                        .into(holder.BlogImage, new Callback() {
                            @Override
                            public void onSuccess() {

                                Resources r = BlogFeedActivity.this.getResources();
                                int px = (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP,
                                        15,
                                        r.getDisplayMetrics()
                                );

                                int px2 = (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP,
                                        10,
                                        r.getDisplayMetrics()
                                );

                                int px3 = (int) TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP,
                                        5,
                                        r.getDisplayMetrics()
                                );

                                holder.parentView.setVisibility(View.VISIBLE);
                                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.views.getLayoutParams();
                                params.setMargins(-px3, px2, -px3, -px2);
                                holder.views.setLayoutParams(params);

                                //  holder.parentView.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onError(Exception e) {
                                e.printStackTrace();
                                Toast.makeText(BlogFeedActivity.this, "", Toast.LENGTH_SHORT).show();
                            }
                        });


            }


            Zoomy.Builder builder = new Zoomy.Builder(BlogFeedActivity.this).target(holder.BlogImage);
            builder.register();

            holder.dislikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    int dislikes = c.getNumberOfDislikes();

                    if (c.getDislikeS() == null || c.getDislikeS().equals("false")) {


                        c.setDislikeS("true");
                        c.setLikeS("false");

                        dislikes = dislikes + 1;
                        c.setNumberOfDislikes(dislikes);
                        mAdapter.notifyDataSetChanged();
                        blog_reference.child(c.getKey()).child("LikeStatus").child(user.getUid()).setValue("false");
                        blog_reference.child(c.getKey()).child("DislikeStatus").child(user.getUid()).setValue("true");
                        blog_reference.child(c.getKey()).child("Participated").child(user.getUid()).child("2").setValue("Disliked");
                        blog_reference.child(c.getKey()).child("NumberDislikes").setValue(dislikes);


                    } else {
                        c.setDislikeS("false");
                        c.setLikeS("false");
                        dislikes = dislikes - 1;
                        c.setNumberOfDislikes(dislikes);
                        mAdapter.notifyDataSetChanged();
                        blog_reference.child(c.getKey()).child("NumberDislikes").setValue(dislikes);
                        blog_reference.child(c.getKey()).child("Participated").child(user.getUid()).child("2").removeValue();
                        blog_reference.child(c.getKey()).child("LikeStatus").child(user.getUid()).setValue("false");

                        blog_reference.child(c.getKey()).child("DislikeStatus").child(user.getUid()).setValue("false");


                    }

                }
            });


            holder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int likes = c.getNumberOfLikes();

                    if (c.getLikeS() == null || c.getLikeS().equals("false")) {


                        likes = likes + 1;
                        c.setDislikeS("false");
                        c.setLikeS("true");
                        c.setNumberOfLikes(likes);
                        mAdapter.notifyDataSetChanged();


                        blog_reference.child(c.getKey()).child("LikeStatus").child(user.getUid()).setValue("true");
                        blog_reference.child(c.getKey()).child("Participated").child(user.getUid()).child("2").setValue("Liked");
                        blog_reference.child(c.getKey()).child("DislikeStatus").child(user.getUid()).setValue("false");
                        blog_reference.child(c.getKey()).child("NumberLikes").setValue(likes);


                    } else {


                        likes = likes - 1;
                        c.setLikeS("false");
                        c.setNumberOfLikes(likes);
                        c.setDislikeS("false");

                        mAdapter.notifyDataSetChanged();
                        blog_reference.child(c.getKey()).child("LikeStatus").child(user.getUid()).setValue("false");
                        blog_reference.child(c.getKey()).child("Participated").child(user.getUid()).child("2").removeValue();
                        blog_reference.child(c.getKey()).child("DislikeStatus").child(user.getUid()).setValue("false");
                        blog_reference.child(c.getKey()).child("NumberLikes").setValue(likes);


                    }

                }
            });

            if (c.getLikeS() != null && c.getLikeS().equals("true")) {
                holder.dislikeButton.setClickable(false);
                int tintColor = ContextCompat.getColor(BlogFeedActivity.this, android.R.color.holo_blue_dark);
                holder.like_icon.setBackgroundTintList(ColorStateList.valueOf(tintColor));
                holder.dislike_icon.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(BlogFeedActivity.this, android.R.color.darker_gray)));

            } else {
                int tintColor = ContextCompat.getColor(BlogFeedActivity.this, android.R.color.darker_gray);
                holder.dislikeButton.setClickable(true);
                //holder.like_icon.setBackgroundTintList(ColorStateList.valueOf(tintColor));


            }
            if (c.getDislikeS() != null && c.getDislikeS().equals("true")) {
                holder.likeButton.setClickable(false);
                int tintColor = ContextCompat.getColor(BlogFeedActivity.this, android.R.color.holo_red_dark);
                holder.dislike_icon.setBackgroundTintList(ColorStateList.valueOf(tintColor));
                holder.like_icon.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(BlogFeedActivity.this, android.R.color.darker_gray)));


            } else {
                int tintColor = ContextCompat.getColor(BlogFeedActivity.this, android.R.color.darker_gray);
                holder.likeButton.setClickable(true);
                //holder.dislike_icon.setBackgroundTintList(ColorStateList.valueOf(tintColor));

            }


            //  setOnItemLongClickLister(mListener);

            holder.parentView.setOnLongClickListener(new View.OnLongClickListener() {


                @Override
                public boolean onLongClick(View view) {


                    if (user.getUid().equals(c.getUploadedBy())) {
                        open_admin_menu(c.getKey(), c.getBlogText(), position);


                    } else {
                        open_allusers_menu();

                    }


                    return true;

                }


        });


            String time=c.getUpdatedTime();
            String date=c.getUpdatedDay();
            if(todays_date.equals(date)){
                if(todays_time.equals(c.getUpdatedTime())){
                    holder.time.setText("Right Now");
                }
                else{
                    holder.time.setText(time);
                }

            }
            else{
                holder.time.setText(date);
            }



            // holder.update_day.setText(c.getUpdatedDay());

            //  holder.updated_time.setText(c.getUpdatedTime());
            holder.BlogText.setText(c.getBlogText());


            Picasso.get()
                    .load(c.getImageUrl())
                    .error(android.R.drawable.stat_notify_error)
                    .resize(400, 400)
                    .centerCrop()
                    // .transform(transformation)
                    .into(holder.ProfileImage, new Callback() {
                        @Override
                        public void onSuccess() {


                        }

                        @Override
                        public void onError(Exception e) {

                            Toast.makeText(BlogFeedActivity.this, "Failed to load new posts", Toast.LENGTH_SHORT).show();

                        }


                    });


        }


        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {


            return posts.size();
        }

        public int getCount() {

            return posts.size();

        }


    }
/*

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.clear();

        inflater.inflate(R.menu.fragment_blogs_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.filter:
                call_filters();
                return true;
            case R.id.chat:
                Intent intent=new Intent(getActivity(), HomeMessage.class);
                startActivity(intent);
                return true;
        }


        return true;
    }
*/

    public void getFollowingCount(final firebaseXXX2 FirebaseXXX2){

        parentReference.child(user.getUid()).child("Following").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long hex=dataSnapshot.getChildrenCount();
                Log.e("hexX",""+hex);
                FirebaseXXX2.onCallBack1(hex);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void getFollowingPeople(final FirebaseCallBackX2 firebaseCallBackX2){

        final ArrayList<String> FollowingPeopleS=new ArrayList<>();


        parentReference.child(user.getUid()).child("Following").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FollowingPeopleS.add(dataSnapshot.getKey());
                Log.e("FP2",""+ChildrenCountS[0]);
                Log.e("FP",""+dataSnapshot.getKey());

                if(ChildrenCountS[0]==FollowingPeopleS.size()){
                    firebaseCallBackX2.onCallBack(FollowingPeopleS);
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


        } );





    }

   /* public void changeToOrganizers(){

        Organizers.setTextColor(ContextCompat.getColor(BlogFeedActivity.this,R.color.mainOrange));
        Students.setTextColor(ContextCompat.getColor(BlogFeedActivity.this,R.color.colorGrey));

        fetch("Organizers","null");




    }
    public void changeToStudents(){

        Organizers.setTextColor(ContextCompat.getColor(BlogFeedActivity.this,R.color.colorGrey));
        Students.setTextColor(ContextCompat.getColor(BlogFeedActivity.this,R.color.mainOrange));

        fetch("Students","null");




    }*/

    /*public void call_filters(){


        final Dialog dialog = new Dialog(new ContextThemeWrapper(BlogFeedActivity.this, R.style.DialogSlideAnim));
        dialog.setContentView(R.layout.call_filters);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogSlideAnim;
        dialog.getWindow().setAttributes(lp);
        Spinner spinner = (Spinner) dialog.findViewById(R.id.sort_spinner);
        Button ApplySort = (Button) dialog.findViewById(R.id.sort_apply);
        ApplySort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner_position = m;
                posts.clear();
                fetch(sort_category);
                dialog.cancel();
            }
        });
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(BlogFeedActivity.this,
                R.array.blogs_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setSelection(spinner_position);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //   ((TextView) view).setTextColor(Color.RED);


                sort_category = adapterView.getItemAtPosition(i).toString();
                m = i;
                //  Toast.makeText(getActivity(), "" + sort_category, Toast.LENGTH_SHORT).show();


                //   final NewsFeedAdapter jadapter=new NewsFeedAdapter(getActivity(),posts);


//

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });


        // show it
        dialog.show();




    }*/


}






