package com.example.hotelloadmin.StudentsList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ablanco.zoomy.Zoomy;
import com.example.hotelloadmin.IntroPackage.ScreenActivity;
import com.example.hotelloadmin.R;
import com.example.hotelloadmin.DiscussionsPackage.BlogActivity;
import com.example.hotelloadmin.DiscussionsPackage.BlogsObject;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PeopleClick extends AppCompatActivity {


    FirebaseDatabase blog_database;
    DatabaseReference blog_reference;
    ArrayList<BlogsObject> posts;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    FirebaseDatabase parentDatabase;
    DatabaseReference parentReference;
    CircleImageView profile_image;
    TextView user_name;
    TextView user_course;
    TextView college;
    TextView district;
    TextView tv_no_club;
    TextView state;
    ProgressBar progressBar2;
    TextView nothing;

    AuthUI mAuthUI;
    Button follow_bt;
    FirebaseUser user;
    TextView groupAdmin;
    ProgressBar progressBarX;
    String groupAdminS;
    ImageView edit_profile_image;

    View host_club_view;

    Bitmap original;
    CircleImageView club_profile_image;
    ProgressBar progressBar;
    Bitmap decoded;
    Uri profile_image_url;
    AlertDialog.Builder builder;
    FirebaseStorage storage;
    StorageReference storage_reference;
    ImageView host_setting;
    Boolean club_exists;


    String UserName;
    String key;
    String todays_date;
    String todays_time;
    String monthS;
    String yearS;
    String dayS;
    String am_pm;
    String UploadedBy;
    String ImageUrl;
    String UserSkill;
    String  ProfileImage ;
    String  BlogText;
    String  updated_day;
    String  updated_time;
    String uploadedBy;
    String sort_category;
    FloatingActionButton msg_button;
    String ReceiverName;

    String likeS;
    String ReceiverProfileImage;
    String dislikeS;
    String Key;
    int spinner_position;
    int no_likes;
    int no_dislikes;
    int NumberOfDislikes;
    int NumberOfLikes;
    String SenderProfileImage;
    String SenderName;

   // String SearchedUID;
    String SearchedUIDX;
    TextView FollowersCount;
    TextView FollowingCount;

    Toolbar toolbar;


    ChildEventListener childEventListener;
    ValueEventListener vel;



    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 7;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_click);



        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        toolbar.setTitleTextColor(ContextCompat.getColor(PeopleClick.this,R.color.black));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressBar = (ProgressBar) findViewById(R.id.top_progress_bar);
        host_setting = (ImageView) findViewById(R.id.host_setting);
        progressBarX=(ProgressBar) findViewById(R.id.progress_barX);
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storage_reference = storage.getReference();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        tv_no_club = (TextView) findViewById(R.id.tv_no_club);
        host_club_view = (LinearLayout) findViewById(R.id.host_club_view);
        msg_button = (FloatingActionButton) findViewById(R.id.message);
        FollowersCount=(TextView)findViewById(R.id.followers_count);
        FollowingCount=(TextView)findViewById(R.id.following_count);


        progressBar2=(ProgressBar) findViewById(R.id.progress_bar);
        nothing=(TextView) findViewById(R.id.nothing);
        parentDatabase=FirebaseDatabase.getInstance();
        parentReference = parentDatabase.getReference().child("HotelLo");
        follow_bt=(Button)findViewById(R.id.follow_bt);

        blog_database = FirebaseDatabase.getInstance();
        blog_reference = blog_database.getReference().child("Blogs");
        //  user = FirebaseAuth.getInstance().getCurrentUser();

        club_profile_image = (CircleImageView) findViewById(R.id.club_profile_image);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        posts = new ArrayList<>();

        mAdapter = new PeopleClick.BlogAdapter(PeopleClick.this, posts);

        mLayoutManager = new LinearLayoutManager(PeopleClick.this);
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

        Intent i = getIntent();
        SearchedUIDX = i.getStringExtra("UserUID");
        if(SearchedUIDX!=null){
            getProfile(SearchedUIDX);
            parentReference.child(SearchedUIDX).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if( dataSnapshot.child("Followers").hasChild(user.getUid())){
                        follow_bt.setText("Following");
                    }
                    else{
                        follow_bt.setText("Follow");
                    }
                    FollowersCount.setText(""+dataSnapshot.child("Followers").getChildrenCount()) ;
                    FollowingCount.setText(""+dataSnapshot.child("Following").getChildrenCount());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
        else{


            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(getIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                        @Override
                        public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {



                            Uri deepLink = null;
                            if (pendingDynamicLinkData != null) {
                                deepLink = pendingDynamicLinkData.getLink();

                                Log.i("Link2", "1 "+deepLink.toString());

                            }
                            SearchedUIDX=deepLink.getQueryParameter("Key");

                            getProfile(SearchedUIDX);

                            parentReference.child(SearchedUIDX).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if( dataSnapshot.child("Followers").hasChild(user.getUid())){
                                        follow_bt.setText("Following");
                                    }
                                    else{
                                        follow_bt.setText("Follow");
                                    }
                                    FollowersCount.setText(""+dataSnapshot.child("Followers").getChildrenCount()) ;
                                    FollowingCount.setText(""+dataSnapshot.child("Following").getChildrenCount());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });






                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Heyya", "getDynamicLink:onFailure", e);
                        }
                    });


        }

        getTodaysTime();

       /* parentReference.child(SearchedUIDX).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if( dataSnapshot.child("Followers").hasChild(user.getUid())){
                    follow_bt.setText("Following");
                }
                else{
                    follow_bt.setText("Follow");
                }
               FollowersCount.setText(""+dataSnapshot.child("Followers").getChildrenCount()) ;
                FollowingCount.setText(""+dataSnapshot.child("Following").getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/




        fetch();



        user_name = (TextView) findViewById(R.id.user_name);
        user_course = (TextView) findViewById(R.id.user_course);
        college = (TextView) findViewById(R.id.user_college);
        district = (TextView) findViewById(R.id.user_district);
        state = (TextView) findViewById(R.id.user_state);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        groupAdmin = (TextView) findViewById(R.id.user_club);







        /*msg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PeopleClick.this, FirstActivity.class);
                Toast.makeText(PeopleClick.this, "" + SenderName, Toast.LENGTH_SHORT).show();
                intent.putExtra("ReceiverName", ReceiverName);
                intent.putExtra("ReceiverUID", SearchedUIDX);
                intent.putExtra("ReceiverImageUrl", ReceiverProfileImage);
                intent.putExtra("SenderProfileImage", SenderProfileImage);
                intent.putExtra("SenderName", SenderName);
                intent.putExtra("ActivityName", "PeopleClick");
                startActivity(intent);

            }
        });*/




        //Implementing Listeners



        follow_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarX.setVisibility(View.VISIBLE);
                if(follow_bt.getText().equals("Follow")){
                    parentReference.child(SearchedUIDX).child("Followers").child(user.getUid()).setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            parentReference.child(user.getUid()).child("Following").child(SearchedUIDX).setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        follow_bt.setText("Following");
                                        progressBarX.setVisibility(View.GONE);
                                    }
                                    else{
                                        Toast.makeText(PeopleClick.this,"Can't process",Toast.LENGTH_SHORT).show();
                                        progressBarX.setVisibility(View.GONE);
                                    }


                                }
                            });

                        }
                    });



                }
                else{

                    parentReference.child(SearchedUIDX).child("Followers").child(user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            parentReference.child(user.getUid()).child("Following").child(SearchedUIDX).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        follow_bt.setText("Follow");
                                        progressBarX.setVisibility(View.GONE);
                                    }
                                    else{
                                        Toast.makeText(PeopleClick.this,"Can't process",Toast.LENGTH_SHORT).show();
                                        progressBarX.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }
                    });
                }
            }
        });





        // ATTENTION: This was auto-generated to handle app links.
        /*Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }




    public void open_allusers_menu(){

        LayoutInflater li = LayoutInflater.from(PeopleClick.this);
        View promptsView = li.inflate(R.layout.newsfeed_allusers_options, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                PeopleClick.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);


        final AlertDialog alertDialog2 = alertDialogBuilder.create();

        // show it
        alertDialog2.show();

        TextView report=(TextView)promptsView.findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog2.cancel();


            }
        });

        TextView share=(TextView)promptsView.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.cancel();
            }
        });


    }

    private void open_admin_menu(final String key,final String BlogText,final int position){
        LayoutInflater li = LayoutInflater.from(PeopleClick.this);
        View promptsView = li.inflate(R.layout.newsfeed_admin_options, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                PeopleClick.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);


        final AlertDialog alertDialog2 = alertDialogBuilder.create();

        // show it
        alertDialog2.show();

        TextView delete=(TextView)promptsView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteblog(key,position);
                alertDialog2.cancel();


            }
        });

        TextView edit=(TextView)promptsView.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editposts(key,BlogText,position);
                alertDialog2.cancel();
            }
        });

        TextView share=(TextView)promptsView.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.cancel();
            }
        });




    }

    private void editposts(final String key, final String blogtext, final int position){
        LayoutInflater li = LayoutInflater.from(PeopleClick.this);
        View promptsView = li.inflate(R.layout.edit_posts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                PeopleClick.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText subject = (EditText) promptsView
                .findViewById(R.id.subject);

        subject.setText(blogtext);
        subject.setSelection(subject.getText().length());



        //  final EditText link=(EditText) promptsView.findViewById(R.id.link);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text

                                InputMethodManager imm = (InputMethodManager)PeopleClick.this. getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(subject.getWindowToken(), 0);


                                blog_reference.child(key).child("BlogText").setValue(subject.getText().toString());
                                Map<String, Object> newAchievements = new HashMap<>();
                                posts.get(position).setBlogText(subject.getText().toString());



                                mAdapter.notifyDataSetChanged();
                                Toast.makeText(PeopleClick.this, "Post Edited", Toast.LENGTH_SHORT).show();

















                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                InputMethodManager imm = (InputMethodManager)PeopleClick.this. getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(subject.getWindowToken(), 0);
                                dialog.cancel();



                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

       /* mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.smoothScrollToPosition(
                                    position);
                        }
                    }, 100);
                }
            }
        });*/


        // show it
        alertDialog.show();
        subject.requestFocus();
        InputMethodManager imm = (InputMethodManager)PeopleClick.this. getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);







    }

    private void deleteblog(final String key,final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                PeopleClick.this);
        alertDialogBuilder.setTitle("Attention !")
                .setMessage("Are you sure you want to delete this post?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        blog_reference.child(key).removeValue();
                        posts.remove(position);
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(PeopleClick.this,"Post Deleted",Toast.LENGTH_LONG).show();

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


    /*public void open_hostclub_admin_noclub(){

        LayoutInflater li = LayoutInflater.from(PeopleClick.this);
        View promptsView = li.inflate(R.layout.profile_hostclub_admin_noclub, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                PeopleClick.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);


        final AlertDialog alertDialog2 = alertDialogBuilder.create();

        // show it
        alertDialog2.show();

        TextView add_new_club=(TextView)promptsView.findViewById(R.id.add_new_club);
        add_new_club.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent=new Intent(PeopleClick.this,AddClubActivity.class);
                startActivity(intent);
                alertDialog2.cancel();
            }

        });

    }*/






   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }*/




   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_club:
                addClubActivity();
                return true;

            case R.id.logoutbtn:
                Logout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }*/




    public void Logout() {
        mAuthUI = AuthUI.getInstance();

        mAuthUI.signOut(PeopleClick.this)


                .addOnCompleteListener(PeopleClick.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(
                                    PeopleClick.this,
                                    ScreenActivity.class));
                            PeopleClick.this.finish();

                        }
                    }
                });
    }



    private void fetch(){



        childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String s) {




                UserName= snapshot.child("UserName").getValue(String.class);
                UserSkill = snapshot.child("Skill").getValue(String.class);
                ProfileImage = snapshot.child("ProfileImage").getValue(String.class);
                BlogText=snapshot.child("BlogText").getValue(String.class);
                updated_day=snapshot.child("UpdatedDay").getValue(String.class);
                updated_time=snapshot.child("UpdatedTime").getValue(String.class);
                uploadedBy=snapshot.child("UploadedBy").getValue(String.class);
                likeS=snapshot.child("LikeStatus").child(user.getUid()).getValue(String.class);
                dislikeS=snapshot.child("DislikeStatus").child(user.getUid()).getValue(String.class);
                Key=snapshot.child("Key").getValue(String.class);
                NumberOfLikes=snapshot.child("NumberLikes").getValue(Integer.class);
                NumberOfDislikes=snapshot.child("NumberDislikes").getValue(Integer.class);
                String BlogImage=snapshot.child("BlogPic").getValue(String.class);
                String ClubLocation=snapshot.child("ClubLocation").getValue(String.class);
                String Edited=snapshot.child("Edited").getValue(String.class);
                int comments_count=0;
                if(snapshot.hasChild("CommentsCount")){
                    comments_count= snapshot.child("CommentsCount").getValue(Integer.class);
                }
                if (uploadedBy.equals(SearchedUIDX)) {
                    progressBar2.setVisibility(View.GONE);
                    posts.add(new BlogsObject(UserName,ClubLocation,BlogImage, ProfileImage, BlogText, uploadedBy, updated_time, updated_day, likeS, dislikeS, Key, NumberOfLikes, NumberOfDislikes,comments_count,Edited));
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

        blog_reference.addChildEventListener(childEventListener);

        blog_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(posts.size()==0){
                    //  progressBar2.setVisibility(View.GONE);
                    nothing.setVisibility(View.VISIBLE);
                    //  Toast.makeText(get,"No posts found",Toast.LENGTH_SHORT).show();

                }
                progressBar2.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    @Override
    protected void onDestroy() {
        blog_reference.removeEventListener(childEventListener);
        parentReference.removeEventListener(vel);
        super.onDestroy();
    }



    /*public void EditProfileImage() {


        builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);

        builder.setTitle("Add photo")
                .setMessage("Choose photo from")
                .setPositiveButton(R.string.CAMERA, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {


                            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                                Toast.makeText(getActivity(), "This is very necessary", Toast.LENGTH_SHORT).show();

                            } else {

                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


                            }
                        } else {


                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);

                        }


                    }
                })
                .setNegativeButton(R.string.GALLERY, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Log.d("Fragment 12", "" + MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);


                        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)

                                != PackageManager.PERMISSION_GRANTED) {

                            Log.d("Fragment 13", "" + MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);


                            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                                Toast.makeText(getActivity(), "This is very necessary", Toast.LENGTH_SHORT).show();

                            } else {

                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);


                            }
                        } else {

                            Log.d("Fragment 11", "" + MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image*//*");
                            startActivityForResult(Intent.createChooser(intent, "whatever i want"), 1);


                        }
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        switch (requestCode) {
            case 0:
                Toast.makeText(PeopleClick.this, "" + resultCode, Toast.LENGTH_SHORT).show();
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    String selectedImageS = selectedImage.toString();


                    new PeopleClick.CompressionAsyncTask(selectedImage, selectedImageS).execute();


                }
                break;
            case 1:
                Toast.makeText(PeopleClick.this, "" + resultCode, Toast.LENGTH_SHORT).show();
                if (resultCode == RESULT_OK) {
                    Toast.makeText(PeopleClick.this, "Hii", Toast.LENGTH_SHORT).show();
                    Uri selectedImage = imageReturnedIntent.getData();
                    String selectedImageS = selectedImage.toString();
                    Toast.makeText(PeopleClick.this, "" + selectedImageS, Toast.LENGTH_LONG).show();

                    new PeopleClick.CompressionAsyncTask(selectedImage, selectedImageS).execute();


                    Log.d("Fragment1", "" + selectedImageS);

                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else {

                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else {

                }
                return;
            }

        }
    }

    public void uploadimageinbytes(Bitmap bitmap, int quality, Uri uri) {
        final StorageReference mountainsRef = storage_reference.child(user.getUid()).child("profile_images");
        // final StorageReference mountainsRef = storage_reference.child(user.getUid()).child("profile_images/"+uri.getLastPathSegment());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(PeopleClick.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);


                Toast.makeText(PeopleClick.this, "Successful", Toast.LENGTH_SHORT).show();

            }
        });


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }


                return mountainsRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    final String downloadUriS = downloadUri.toString();
                    profile_image_url = downloadUri;
                    parentReference.child(user.getUid()).child("ImageUrl").setValue(downloadUriS);
                    FirebaseDatabase.getInstance("https://ddrx-172d3-2a5f7.firebaseio.com/").getReference()
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        if(snapshot.child("UploadedBy").getValue(String.class).equals(user.getUid())){
                                            String key=snapshot.child("Key").getValue(String.class);
                                            FirebaseDatabase.getInstance("https://ddrx-172d3-2a5f7.firebaseio.com/").getReference().child(key).child("ProfileImage").setValue(downloadUriS);

                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                    Toast.makeText(PeopleClick.this, "" + downloadUriS, Toast.LENGTH_SHORT).show();
                    Log.d("Fragment 7", "" + downloadUriS);
                } else {

                }
            }

        });


    }


    private class CompressionAsyncTask extends AsyncTask<Uri, Integer, Void> {

        Uri selectedImage;
        String selectedImageS;

        public CompressionAsyncTask(Uri selectedImage, String selectedImageS) {

            this.selectedImage = selectedImage;
            this.selectedImageS = selectedImageS;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected Void doInBackground(Uri... params) {
            try {
                original = MediaStore.Images.Media.getBitmap(PeopleClick.this.getContentResolver(), selectedImage);
            } catch (IOException e) {
            }
            uploadimageinbytes(original, 30, selectedImage);


            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Picasso.get()
                    .load(Uri.parse(selectedImageS)) // mCategory.icon is a string
                    .resize(800, 800)
                    .centerCrop()
                    .noFade()
                    .error(R.drawable.add_photo3) // default image to load
                    .into(profile_image);


        }
    }


    class BlogAdapter extends RecyclerView.Adapter<PeopleClick.BlogAdapter.MyViewHolder>   {

        private List<BlogsObject> posts;
        private Context context;




        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public  class MyViewHolder extends RecyclerView.ViewHolder {
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


            ProgressBar progressBar;
            public MyViewHolder(View v) {
                super(v);
              /*  parentView=(View)v.findViewById(R.id.parent_view) ;
                UserName = (TextView)v.findViewById(R.id.user_name);
                likeButton=(Button) v.findViewById(R.id.like_button);
                dislikeButton=(Button)v.findViewById(R.id.dislike_button);
                //  parentView.setVisibility(View.INVISIBLE);
                //  likeButton=(LikeButton) v.findViewById(R.id.star_button);
                //   progressBar=(ProgressBar) v.findViewById(R.id.post_progress_bar);
                BlogText=(TextView)v.findViewById(R.id.blog_text);
                UserSkill=( TextView) v.findViewById(R.id.user_skill);
                ProfileImage=(CircleImageView) v.findViewById(R.id.profile_image);
                update_day=(TextView)v.findViewById(R.id.date);
                //  update_date=(TextView)v.findViewById(R.id.update_day);
                time=(TextView)v.findViewById(R.id.time);*/


                parentView = (FrameLayout) v.findViewById(R.id.parent_view);
                edited_check=(TextView)v.findViewById(R.id.edited_check);


                UserName = (TextView) v.findViewById(R.id.user_name);
                likeButton = v.findViewById(R.id.like_button);
                dislikeButton = v.findViewById(R.id.dislike_button);
                BlogImage = (ImageView) v.findViewById(R.id.blog_image);
                comments_count = (TextView) v.findViewById(R.id.comment_count);

                BlogText = (TextView) v.findViewById(R.id.blog_text);
                views = (LinearLayout) v.findViewById(R.id.views);
                ProfileImage = (CircleImageView) v.findViewById(R.id.profile_image);

                ClubLocation = (TextView) v.findViewById(R.id.skill_location);
                time=(TextView)v.findViewById(R.id.time);

                like_count=v.findViewById(R.id.like_count);
                dislike_count=v.findViewById(R.id.dislike_count);
                like_icon=v.findViewById(R.id.like_icon);
                dislike_icon=v.findViewById(R.id.dislike_icon);

            }

        }

        // Provide a suitable constructor (depends on the kind of dataset)

        public BlogAdapter(Context context,List<BlogsObject> posts) {
            this.context=context;
            this.posts=posts;

        }

        // Create new views (invoked by the layout manager)
        @Override
        public PeopleClick.BlogAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
            // create a new view
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.blog2,parent,false);
            PeopleClick.BlogAdapter.MyViewHolder vh = new PeopleClick.BlogAdapter.MyViewHolder(v);
            return vh;
        }


        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(@NonNull final PeopleClick.BlogAdapter.MyViewHolder holder, final int position) {

            final BlogsObject c = posts.get(position);
            holder.parentView.setVisibility(View.INVISIBLE);


            holder.ClubLocation.setText(c.getClubLocation());
            holder.parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PeopleClick.this, BlogActivity.class);

                    intent.putExtra("Key", c.getKey());
                    intent.putExtra("DeletePosition", position);
                    intent.putExtra("State","Students");
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
            holder.UserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //   Intent intent=new Intent
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


                Resources r = getResources();
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

           /*     Picasso.get()
                        .load(c.getBlogImage())
                        .error(android.R.drawable.stat_notify_error)

                        .transform(transformation)
                        .into(holder.BlogImage, new Callback() {
                            @Override
                            public void onSuccess() {

                                Resources r = getActivity().getResources();
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
                                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                            }
                        });
*/

            }


            Zoomy.Builder builder = new Zoomy.Builder(PeopleClick.this).target(holder.BlogImage);
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

                        blog_reference.child(c.getKey()).child("DislikeStatus").child(user.getUid()).setValue("true");
                        blog_reference.child(c.getKey()).child("LikeStatus").child(user.getUid()).setValue("false");
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

                        blog_reference.child(c.getKey()).child("DislikeStatus").child(user.getUid()).setValue("false");
                        blog_reference.child(c.getKey()).child("LikeStatus").child(user.getUid()).setValue("true");
                        blog_reference.child(c.getKey()).child("Participated").child(user.getUid()).child("2").setValue("Liked");
                     /*   blog_reference.child(c.getKey()).child("Participated").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               if(dataSnapshot.child(user.getUid()).getValue(String.class).equals("Seen")) {

                                   dataSnapshot.child(user.getUid()).getRef().setValue("Seen and Liked");
                               }
                               else if(dataSnapshot.child(user.getUid()).getValue(String.class).equals("Seen and Commented")) {

                                    dataSnapshot.child(user.getUid()).getRef().setValue("Seen,Liked and Commented");
                                }
                                else{
                                    dataSnapshot.child(user.getUid()).getRef().setValue("Liked");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });*/


                        blog_reference.child(c.getKey()).child("NumberLikes").setValue(likes);


                    } else {


                        likes = likes - 1;
                        c.setLikeS("false");
                        c.setNumberOfLikes(likes);
                        c.setDislikeS("false");

                        mAdapter.notifyDataSetChanged();
                        blog_reference.child(c.getKey()).child("LikeStatus").child(user.getUid()).setValue("false");
                        blog_reference.child(c.getKey()).child("Participated").child(user.getUid()).child("2").removeValue();
                     /*   blog_reference.child(c.getKey()).child("Participated").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child(user.getUid()).getValue(String.class).equals("Liked")) {

                                    dataSnapshot.child(user.getUid()).getRef().removeValue();
                                }
                                else if(dataSnapshot.child(user.getUid()).getValue(String.class).equals("Seen,Liked and Commented")) {

                                    dataSnapshot.child(user.getUid()).getRef().setValue("Seen and Commented");
                                }
                                else if(dataSnapshot.child(user.getUid()).getValue(String.class).equals("Seen and Liked")) {

                                    dataSnapshot.child(user.getUid()).getRef().setValue("Seen");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });*/

                        blog_reference.child(c.getKey()).child("DislikeStatus").child(user.getUid()).setValue("false");
                        blog_reference.child(c.getKey()).child("NumberLikes").setValue(likes);


                    }

                }
            });

            if (c.getLikeS() != null && c.getLikeS().equals("true")) {
                holder.dislikeButton.setClickable(false);
                int tintColor = ContextCompat.getColor(PeopleClick.this, android.R.color.holo_blue_dark);
                holder.like_icon.setBackgroundTintList(ColorStateList.valueOf(tintColor));
                holder.dislike_icon.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(PeopleClick.this, android.R.color.darker_gray)));

            } if (c.getLikeS() != null && c.getLikeS().equals("false") && c.getDislikeS().equals("true")) {
                int tintColor = ContextCompat.getColor(PeopleClick.this, android.R.color.darker_gray);
                holder.dislikeButton.setClickable(true);
                holder.likeButton.setClickable(false);
                holder.like_icon.setBackgroundTintList(ColorStateList.valueOf(tintColor));

            }



            if (c.getDislikeS() != null && c.getDislikeS().equals("true")) {
                holder.likeButton.setClickable(false);
                int tintColor = ContextCompat.getColor(PeopleClick.this, android.R.color.holo_red_dark);
                holder.dislike_icon.setBackgroundTintList(ColorStateList.valueOf(tintColor));
                holder.like_icon.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(PeopleClick.this, android.R.color.darker_gray)));


            }if (c.getDislikeS() != null && c.getDislikeS().equals("false") && c.getLikeS().equals("true")) {
                int tintColor = ContextCompat.getColor(PeopleClick.this, android.R.color.darker_gray);
                holder.likeButton.setClickable(true);
                holder.dislikeButton.setClickable(false);
                holder.dislike_icon.setBackgroundTintList(ColorStateList.valueOf(tintColor));

            }


            //  setOnItemLongClickLister(mListener);

            holder.parentView.setOnLongClickListener(new View.OnLongClickListener() {
                                                         @Override
                                                         public boolean onLongClick(View view) {

                                                             //blog_et.setFocusable(false);
                                                             if (user.getUid().equals(c.getUploadedBy())) {
                                                                 open_admin_menu(c.getKey(), c.getBlogText(), position);


                                                             } else {
                                                                 open_allusers_menu();

                                                             }

                                        /*                     menu=new PopupMenu(getActivity(),holder.UserSkill);
                                                             //  menu.inflate(R.menu.blog_holdclick);
                                                             if(user.getUid().equals(c.getUploadedBy())){

                                                                 menu.getMenu().add(Menu.NONE, MENU_ITEM_EDIT, Menu.NONE, "EDIT").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                                                     @Override
                                                                     public boolean onMenuItemClick(MenuItem menuItem) {
                                                                         // Toast.makeText(getActivity(), " Hii"+posts.getKey(), Toast.LENGTH_SHORT).show();
                                                                         String blogtext=c.getBlogText();




                                                                         editposts(c.getKey(),blogtext,position);


                                                                         *//*InputMethodManager imm = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
                                                                         imm.hideSoftInputFromWindow(blog_et.getWindowToken(), 0);*//*

                                                                        // mLayoutManager.scrollToPositionWithOffset(position,10);









                                                                         return true;
                                                                     }
                                                                 });
                                                                 menu.getMenu().add(Menu.NONE, MENU_ITEM_DELETE, Menu.NONE, "DELETE").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                                                     @Override
                                                                     public boolean onMenuItemClick(MenuItem menuItem) {
                                                                         deleteblog(c.getKey(),position);
                                                                         return true;
                                                                     }
                                                                 });
                                                             }
                                                             else{
                                                                 menu.getMenu().add(Menu.NONE, MENU_ITEM_REPORT, Menu.NONE, "REPORT");
                                                                 //   menu.getMenu().add("Hide");
                                                             }

                                                             menu.show();*/
                                                             //blog_et.setFocusable(true);
                                                             return true;


                                                         }


                                                     }


            );


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

                            Toast.makeText(PeopleClick.this, "Failed to load new posts", Toast.LENGTH_SHORT).show();

                        }


                    });



  /*          final BlogsObject c = posts.get(position);

            holder.UserName.setText(c.getUserName());
            holder.UserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //   Intent intent=new Intent
                }
            });
            //  Toast.makeText(getActivity(),""+posts.getNumberOfLikes(),Toast.LENGTH_SHORT).show();
            holder.dislikeButton.setHint("("+c.getNumberOfDislikes()+")");
            holder.likeButton.setHint("("+c.getNumberOfLikes()+")");

            holder.dislikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    int dislikes= c.getNumberOfDislikes();

                    if( c.getDislikeS()==null||c.getDislikeS().equals("false")){


                        c.setDislikeS("true");


                        dislikes=dislikes+1;
                        c.setNumberOfDislikes(dislikes);
                        mAdapter.notifyDataSetChanged();

                        blog_reference.child(c.getKey()).child("DislikeStatus").child(user.getUid()).setValue("true");
                        blog_reference.child(c.getKey()).child("NumberDislikes").setValue(dislikes);








                    }
                    else{
                        c.setDislikeS("false");

                        dislikes=dislikes-1;
                        c.setNumberOfDislikes(dislikes);
                        mAdapter.notifyDataSetChanged();
                        blog_reference.child(c.getKey()).child("NumberDislikes").setValue(dislikes);


                        blog_reference.child(c.getKey()).child("DislikeStatus").child(user.getUid()).setValue("false");





                    }

                }
            });



            holder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int likes=c.getNumberOfLikes();

                    if(c.getLikeS()==null||c.getLikeS().equals("false")){


                        likes=likes+1;

                        c.setLikeS("true");
                        c.setNumberOfLikes(likes);
                        mAdapter.notifyDataSetChanged();


                        blog_reference.child(c.getKey()).child("LikeStatus").child(user.getUid()).setValue("true");






                        blog_reference.child(c.getKey()).child("NumberLikes").setValue(likes);








                    }
                    else{


                        likes=likes-1;
                        c.setLikeS("false");
                        c.setNumberOfLikes(likes);


                        mAdapter.notifyDataSetChanged();
                        blog_reference.child(c.getKey()).child("LikeStatus").child(user.getUid()).setValue("false");


                        blog_reference.child(c.getKey()).child("NumberLikes").setValue(likes);









                    }

                }
            });

            if(c.getLikeS()!=null&&c.getLikeS().equals("true")){
                holder.dislikeButton.setClickable(false);
                int tintColor = ContextCompat.getColor(PeopleClick.this, android.R.color.holo_blue_dark);
                Drawable drawable = ContextCompat.getDrawable(PeopleClick.this, R.drawable.ic_thumb_up_black_24dp);
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTint(drawable.mutate(), tintColor);

                drawable.setBounds( 0, 0, 90, 90);

                holder.likeButton.setCompoundDrawables(drawable, null, null, null);

            }
            else{
                int tintColor = ContextCompat.getColor(PeopleClick.this, android.R.color.darker_gray);
                holder.dislikeButton.setClickable(true);
                Drawable drawable = ContextCompat.getDrawable(PeopleClick.this, R.drawable.ic_thumb_up_black_24dp);
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTint(drawable.mutate(), tintColor);

                drawable.setBounds( 0, 0, 75, 75);

                holder.likeButton.setCompoundDrawables(drawable, null, null, null);


            }
            if(c.getDislikeS()!=null&&c.getDislikeS().equals("true")){
                holder.likeButton.setClickable(false);
                int tintColor = ContextCompat.getColor(PeopleClick.this, android.R.color.holo_blue_dark);
                Drawable drawable = ContextCompat.getDrawable(PeopleClick.this, R.drawable.ic_thumb_down_black_24dp);
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTint(drawable.mutate(), tintColor);

                drawable.setBounds( 0, 0, 90, 90);

                holder.dislikeButton.setCompoundDrawables(drawable, null, null, null);
            }
            else{
                int tintColor = ContextCompat.getColor(PeopleClick.this, android.R.color.darker_gray);
                holder.likeButton.setClickable(true);
                Drawable drawable = ContextCompat.getDrawable(PeopleClick.this, R.drawable.ic_thumb_down_black_24dp);
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTint(drawable.mutate(), tintColor);

                drawable.setBounds( 0, 0, 75, 75);

                holder.dislikeButton.setCompoundDrawables(drawable, null, null, null);


            }


            //  setOnItemLongClickLister(mListener);

            holder.parentView.setOnLongClickListener(new View.OnLongClickListener() {
                                                         @Override
                                                         public boolean onLongClick(View view)
                                                         {

                                                             //  blog_et.setFocusable(false);
                                                             if(user.getUid().equals(c.getUploadedBy())) {
                                                                 open_admin_menu(c.getKey(),c.getBlogText(),position);


                                                             }else{
                                                                 open_allusers_menu();

                                                             }

                                        *//*                     menu=new PopupMenu(getActivity(),holder.UserSkill);
                                                             //  menu.inflate(R.menu.blog_holdclick);
                                                             if(user.getUid().equals(c.getUploadedBy())){

                                                                 menu.getMenu().add(Menu.NONE, MENU_ITEM_EDIT, Menu.NONE, "EDIT").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                                                     @Override
                                                                     public boolean onMenuItemClick(MenuItem menuItem) {
                                                                         // Toast.makeText(getActivity(), " Hii"+posts.getKey(), Toast.LENGTH_SHORT).show();
                                                                         String blogtext=c.getBlogText();




                                                                         editposts(c.getKey(),blogtext,position);


                                                                         *//**//*InputMethodManager imm = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
                                                                         imm.hideSoftInputFromWindow(blog_et.getWindowToken(), 0);*//**//*

                                                                        // mLayoutManager.scrollToPositionWithOffset(position,10);









                                                                         return true;
                                                                     }
                                                                 });
                                                                 menu.getMenu().add(Menu.NONE, MENU_ITEM_DELETE, Menu.NONE, "DELETE").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                                                     @Override
                                                                     public boolean onMenuItemClick(MenuItem menuItem) {
                                                                         deleteblog(c.getKey(),position);
                                                                         return true;
                                                                     }
                                                                 });
                                                             }
                                                             else{
                                                                 menu.getMenu().add(Menu.NONE, MENU_ITEM_REPORT, Menu.NONE, "REPORT");
                                                                 //   menu.getMenu().add("Hide");
                                                             }

                                                             menu.show();*//*
                                                             // blog_et.setFocusable(true);
                                                             return true;


                                                         }




                                                     }








            );










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







//             holder.UserSkill.setText(c.getSkill());
//            holder.update_day.setText(c.getUpdatedDay());
            // holder.ClubProfileImage.setImageResource(c.getClubProfileImage());
  //          holder.updated_time.setText(c.getUpdatedTime());
            holder.BlogText.setText(c.getBlogText());




            Picasso.get()
                    .load(c.getImageUrl())
                    .error(android.R.drawable.stat_notify_error)
                    .resize(400,400)
                    .centerCrop()
                    // .transform(transformation)
                    .into(holder.ProfileImage, new Callback() {
                        @Override
                        public void onSuccess() {




                        }

                        @Override
                        public void onError(Exception e) {

                            Toast.makeText(PeopleClick.this, "Failed to load new posts", Toast.LENGTH_SHORT).show();

                        }


                    });



*/



        }


        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {



            return posts.size();
        }

        public  int getCount(){

            return  posts.size();

        }



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

    public void getProfile(final String SearchedUID){


      //  parentReference.child(SearchedUID).child("ProfileViewers").child(user.getUid()).setValue(todays_date);


        vel=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ReceiverName = dataSnapshot.child(SearchedUID).child("UserName").getValue().toString();
                Log.e("Ankur", "" + ReceiverName);
                user_name.setText(ReceiverName);
                String course = dataSnapshot.child(SearchedUID).child("Course").getValue().toString();
                String admission_year = dataSnapshot.child(SearchedUID).child("AdmissionYear").getValue().toString();
                String passing_year = dataSnapshot.child(SearchedUID).child("PassingYear").getValue().toString();
                String course_details = "" + course + " " + "(" + "" + admission_year + "-" + passing_year + ")";
                ReceiverProfileImage = dataSnapshot.child(SearchedUID).child("ImageUrl").getValue(String.class);
                SenderProfileImage = dataSnapshot.child(user.getUid()).child("ImageUrl").getValue(String.class);
                SenderName = dataSnapshot.child(user.getUid()).child("UserName").getValue(String.class);
               String ClubLocation = dataSnapshot.child(user.getUid()).child("ClubLocation").getValue(String.class);

               if((dataSnapshot.hasChild(user.getUid()))&&(!user.getUid().equals(SearchedUID))) {

                   parentReference.child(SearchedUID).child("ProfileViewers").child(user.getUid()).child("ProfileImage").setValue(SenderProfileImage);
                   parentReference.child(SearchedUID).child("ProfileViewers").child(user.getUid()).child("Name").setValue(SenderName);
                   parentReference.child(SearchedUID).child("ProfileViewers").child(user.getUid()).child("ClubLocation").setValue(ClubLocation);
                   parentReference.child(SearchedUID).child("ProfileViewers").child(user.getUid()).child("Date").setValue(todays_date);
                   parentReference.child(SearchedUID).child("ProfileViewers").child(user.getUid()).child("Time").setValue(todays_time);

               }

                user_course.setText(course_details);
                club_exists = false;
                college.setText(dataSnapshot.child(SearchedUID).child("CollegeName").getValue().toString());
                district.setText(dataSnapshot.child(SearchedUID).child("DistrictName").getValue().toString());
                state.setText(dataSnapshot.child(SearchedUID).child("StateName").getValue().toString());
                key = dataSnapshot.child(SearchedUID).child("UploadedBy").getValue(String.class);


                if (dataSnapshot.child(user.getUid()).hasChild("ClubName")) {
                    club_exists = true;
                    host_club_view.setVisibility(View.VISIBLE);
                    tv_no_club.setVisibility(View.INVISIBLE);

                    // Toast.makeText(getActivity(),"Hii",Toast.LENGTH_SHORT).show();

                    groupAdminS = dataSnapshot.child(user.getUid()).child("ClubName").getValue(String.class);
                    String s = " " + groupAdminS;


                    groupAdmin.setText(s);

                    // dataSnapshot.child(user.getUid()).child("ClubProfileImage").getValue(String.class);
                    groupAdmin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Intent intent = new Intent(PeopleClick.this, ClubMainActivity.class);
                            //startActivity(intent);
                        }
                    });

                } else {
                    tv_no_club.setVisibility(View.VISIBLE);
                    host_club_view.setVisibility(View.INVISIBLE);

                    groupAdmin.setOnClickListener(null);
                }


                Picasso.get()
                        .load(dataSnapshot.child(SearchedUID).child("ImageUrl").getValue().toString())
                        .resize(800, 800)
                        .centerCrop()
                        .noFade()
                        .error(R.drawable.add_photo3) // default image to load
                        .into(profile_image);

                Picasso.get()
                        .load(dataSnapshot.child(user.getUid()).child("ClubImageUrl").getValue(String.class))
                        .resize(800, 800)
                        .centerCrop()
                        .noFade()
                        .error(R.drawable.add_photo3) // default image to load
                        .into(club_profile_image);

                parentReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                         /*   if(!club_exists) {
                                if (!key.equals(user.getUid())) {
                                    host_setting.setVisibility(View.INVISIBLE);
                                } else {
                                    host_setting.setVisibility(View.VISIBLE);
                                }
                            }*/

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        parentReference.addListenerForSingleValueEvent(vel);

    }



}

























