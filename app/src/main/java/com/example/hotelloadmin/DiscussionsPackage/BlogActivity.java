package com.example.hotelloadmin.DiscussionsPackage;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.NestedScrollView;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hotelloadmin.R;
import com.example.hotelloadmin.StudentsList.PeopleClick;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
//import com.google.firestore.v1beta1.StructuredQuery;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import de.hdodenhof.circleimageview.CircleImageView;

public class BlogActivity extends AppCompatActivity {

    ImageView IV_BlogImage;
    TextView TV_UserName;
    TextView TV_ClubLocation;
    TextView TV_BlogText;
    String liked;
    CircleImageView CIV_ProfileImage;
    LinearLayout views;
    LinearLayoutManager mLayoutManager;
    CardView cardView;
    CardView cardView2;
    Toolbar toolBar;
    int LikeCount;
    NestedScrollView scrollView;
    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    List<DiscussionsObject> comments;
    EditText comments_et;
    Button send_button;
    DatabaseReference blogReference;
    DatabaseReference parentReference;
    DatabaseReference adminReference;
    FirebaseUser user;
    String UserNameX;
    String ClubImageX;
    ProgressBar progressBar;
    ProgressBar progressBar2;
    LinearLayout noComments;
    String key;

    String commentsX;
    String usernamesX;
    String KeysX;

    RelativeLayout ReplyIdentify;
    TextView ReplyIdentify_RepliersName;
    TextView ReplyIdentify_RepliersText;
    ImageView CancelReply;
    // CollapsingToolbarLayout layout;

    TextView CommentCount;
    LinearLayout CommentSection;

    String dayS;
    String yearS;
    String am_pm;
    String todays_time;
    String monthS;
    String todays_date;
    String EventKeyM;





    Boolean reply=false;
    String BlogText;
    String BlogImage;
    String ProfileImage;
    String UserName;
    String ClubLocation;
    int CommentsCountX;
    TextView participant_count;
    TextView TimeX;
    TextView like_thumb;
    TextView dislike_thumb;


    CircleImageView P1;
    CircleImageView P2;
    CircleImageView P3;
    CircleImageView P4;
    CircleImageView P5;

    String check_like;
    String check_dislike;

    CircleImageView[] images;
    String[] usersIDS;
    DatabaseReference parentRef;
    DatabaseReference adminRef;

    int participant_countX=0;
    String UploadedBy;

    int l=0;

    RelativeLayout parent_layout;
    TextView no_topics;
    int LikeCountS;
    int DislikeCountS;


    ProgressBar progress_edit_delete;

    SharedPreferences myPrefs;
    SharedPreferences.Editor prefsEditor;

    int delete_position=-2;
    String deleted_check=null;
    Menu menu;

    TextView edited;
    String editedS;

    String X11=null;
    String X22=null;


    String updatedDay;
    String updatedTime;
    String LikeStatus;
    String DislikeStatus;
    String send_like;
    String send_dislike;
    String EditCheck="null";
    String URLChecker;

    LinearLayout author_intro;
    LinearLayout author_intro2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        myPrefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        prefsEditor = myPrefs.edit();

        Intent i = getIntent();
        key = i.getStringExtra("Key");
        EventKeyM=i.getStringExtra("EventKey");

        if(EventKeyM==null){
            blogReference = FirebaseDatabase.getInstance().getReference();
            blogReference=blogReference.child("HotelierBlogs");
        }
        else{
            blogReference = FirebaseDatabase.getInstance().getReference();
            blogReference=blogReference.child("EventBlogs").child(EventKeyM);
        }




        author_intro=findViewById(R.id.author_intro);
        author_intro2=findViewById(R.id.author_intro1);

        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitleTextColor(Color.BLACK);

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setSubtitle("");


        TV_ClubLocation = (TextView) findViewById(R.id.skill_location);
        TimeX=(TextView) findViewById(R.id.timeX);
        TV_UserName = (TextView) findViewById(R.id.user_name);
        TV_BlogText = (TextView) findViewById(R.id.blog_text);
        CommentCount=(TextView) findViewById(R.id.comment_count);
        CIV_ProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        IV_BlogImage = (ImageView) findViewById(R.id.blog_image);

        views = (LinearLayout) findViewById(R.id.views);
        like_thumb=(TextView) findViewById(R.id.like_thumb);
        dislike_thumb=(TextView) findViewById(R.id.dislike_thumb);
        user = FirebaseAuth.getInstance().getCurrentUser();
        edited=(TextView) findViewById(R.id.edited);


        comments_et = (EditText) findViewById(R.id.edittext_chatbox);
        progress_edit_delete=(ProgressBar) findViewById(R.id.progress_edit_delete);
        no_topics=(TextView) findViewById(R.id.no_topics);
        parent_layout=(RelativeLayout) findViewById(R.id.parent_layout);
        send_button = (Button) findViewById(R.id.button_chatbox_send);
        participant_count=(TextView) findViewById(R.id.participant_count);
        comments = new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        ReplyIdentify=(RelativeLayout) findViewById(R.id.reply_identify_LL);
        ReplyIdentify_RepliersName=(TextView) findViewById(R.id.reply_identify_RepliersName);
        ReplyIdentify_RepliersText=(TextView) findViewById(R.id.reply_identify_RepliersText);
        CancelReply=(ImageView) findViewById(R.id.cancel_reply);
        progressBar2 = (ProgressBar) findViewById(R.id.progress_bar2);
        noComments = (LinearLayout) findViewById(R.id.no_comments_tv);
        CommentSection=(LinearLayout) findViewById(R.id.comment_section);



        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mAdapter = new BlogActivity.RecycleAdapter(comments);
        mLayoutManager = new LinearLayoutManager(BlogActivity.this);

        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        recyclerView.setNestedScrollingEnabled(false
        );
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);

        recyclerView.setAdapter(mAdapter);


        cardView = (CardView) findViewById(R.id.cv);


        P1=(CircleImageView) findViewById(R.id.p1);
        P2=(CircleImageView) findViewById(R.id.p2);
        P3=(CircleImageView) findViewById(R.id.p3);
        P4=(CircleImageView) findViewById(R.id.p4);
        P5=(CircleImageView) findViewById(R.id.p5);


        images=new CircleImageView[]{P1,P2,P3,P4,P5};
        usersIDS = new String[5];



        author_intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        author_intro2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });



        if(key!=null){
            URLChecker="false";
            delete_position=i.getIntExtra("DeletePosition",-2);
            send_dislike="hex";
            send_like="hex";
            LikeCountS=-2;
            DislikeCountS=-2;

            fetch(key,null,null, 0);

            getLikeDetails(new FirebaseCallbackXXXX() {
                @Override
                public void onCallBack1(String string) {
                    LikeStatus=string;
                    if(LikeStatus!=null&&LikeStatus.equals("true")){
                        check_like="true";
                    }
                    else{
                        check_like="false";
                    }
                    if(check_like!=null&&check_like.equals("true")){
                        X22="Disabled";
                  /*  dislike_thumb.setClickable(false);
                    Log.e("work","true");

                    dislike_thumb.setEnabled(false);*/

                    } else{
                        X22="Enabled";
                   /* Log.e("work","false");
                    dislike_thumb.setClickable(true);
                    dislike_thumb.setEnabled(true);*/

                    }


                }

                @Override
                public void onCallBack2(String string) {
                    DislikeStatus=string;
                    if(DislikeStatus!=null&&DislikeStatus.equals("true")){
                        check_dislike="true";
                    }
                    else{
                        check_dislike="false";
                    }
                    if(check_dislike!=null&&check_dislike.equals("true")){

                        X11="Disabled";
                   /* like_thumb.setClickable(false);
                    Log.e("work","true");

                    like_thumb.setEnabled(false);*/

                    } else{
                        X11="Enabled";
                   /* Log.e("work","false");
                    like_thumb.setClickable(true);
                    like_thumb.setEnabled(true);*/

                    }


                }
            });


            Log.e("X11",""+X11);
            Log.e("X22",""+X22);








            blogReference.child(key).child("Participated").child(user.getUid()).child("1").setValue("Seen");

            ParticipantCount(new firebaseXXX() {
                @Override
                public void onCallBack1(int string) {
                    participant_countX=string;
                    if(participant_countX==1){
                        participant_count.setText(participant_countX+" Participant");
                    }
                    else
                        participant_count.setText(participant_countX+" Participants");
                }
            });
            getParticipants(key);

        }
        Log.i("KEYSS2",""+key);

        if(key==null){
            Log.i("KEYSS4",""+key);
            URLChecker="true";




            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(getIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                        @Override
                        public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {

                            Log.i("KEYSS3",""+key);

                            Uri deepLink = null;
                            if (pendingDynamicLinkData != null) {
                                deepLink = pendingDynamicLinkData.getLink();

                                Log.i("Link2", "1 "+deepLink.toString());

                            }
                            key=deepLink.getQueryParameter("Key");
                            String userX=deepLink.getQueryParameter("User");
                            String userComment=deepLink.getQueryParameter("Comment");

                            getLikeDetails(new FirebaseCallbackXXXX() {
                                @Override
                                public void onCallBack1(String string) {
                                    LikeStatus=string;
                                    if(LikeStatus!=null&&LikeStatus.equals("true")){
                                        check_like="true";
                                    }
                                    else{
                                        check_like="false";
                                    }
                                    if(check_like!=null&&check_like.equals("true")){
                                        X22="Disabled";
                                        /*dislike_thumb.setClickable(false);
                                        Log.e("work","true");

                                        dislike_thumb.setEnabled(false);*/

                                    } else{
                                        X22="Enabled";
                                       /* Log.e("work","false");
                                        dislike_thumb.setClickable(true);
                                        dislike_thumb.setEnabled(true);*/

                                    }

                                }

                                @Override
                                public void onCallBack2(String string) {
                                    DislikeStatus=string;
                                    if(DislikeStatus!=null&&DislikeStatus.equals("true")){
                                        check_dislike="true";
                                    }
                                    else{
                                        check_dislike="false";
                                    }
                                    if(check_dislike!=null&&check_dislike.equals("true")){
                                        X11="Disabled";
                                       /* like_thumb.setClickable(false);
                                        Log.e("work","true");

                                        like_thumb.setEnabled(false);*/

                                    } else{
                                        X11="Enabled";
                                       /* Log.e("work","false");
                                        like_thumb.setClickable(true);
                                        like_thumb.setEnabled(true);*/

                                    }


                                }
                            });

                            if(key!=null){

                                blogReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.hasChild(key)){
                                            blogReference.child(key).child("Participated").child(user.getUid()).child("1").setValue("Seen");

                                        }
                                        else{
                                            parent_layout.setVisibility(View.INVISIBLE);
                                            no_topics.setVisibility(View.VISIBLE);
                                            // deleted_check="true";
                                            menu.clear();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                ParticipantCount(new firebaseXXX() {
                                    @Override
                                    public void onCallBack1(int string) {
                                        participant_countX=string;
                                        if(participant_countX==1){
                                            participant_count.setText(participant_countX+" Participant");
                                        }
                                        else
                                            participant_count.setText(participant_countX+" Participants");
                                    }
                                });
                            }

                            Toast.makeText(BlogActivity.this,"Key is "+key,Toast.LENGTH_SHORT).show();
                            Log.i("KEYSS",key);

                            if(userX!=null&&userComment!=null){
                                fetch(key,userX,userComment,2);
                            }
                            else{
                                fetch(key,null,null, 0);
                            }
                            getParticipants(key);




                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Heyya", "getDynamicLink:onFailure", e);
                        }
                    });


        }

        parentRef=FirebaseDatabase.getInstance().getReference();
        parentRef=parentRef.child("HotelLo");
        adminRef=FirebaseDatabase.getInstance().getReference();
        adminRef=adminRef.child("HotelLoAdmin");



        parentReference=FirebaseDatabase.getInstance().getReference();
        parentReference=parentReference.child("HotelLo");
        adminReference=FirebaseDatabase.getInstance().getReference();
        adminReference=adminReference.child("HotelLoAdmin");





       /* parentRef.child(usersIDS[0]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                images[0].setVisibility(View.VISIBLE);
                String ImageUrl=  dataSnapshot2.child("ImageUrl").getValue(String.class);
                Log.e("string","lxx"+l+ImageUrl);
                Picasso.get()
                        .load(Uri.parse(ImageUrl))
                        .into(images[0]);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/










/*

        for( int j=0;j<5;j++){




        }
*/









        getBlogDetails(new FirebaseCallbackXX() {
            @Override
            public void onCallBack1(String string) {
                BlogText=string;
                TV_BlogText.setText(BlogText);

            }

            @Override
            public void onCallBack2(String string) {

                BlogImage=string;
                Transformation transformation = new Transformation() {

                    @Override
                    public Bitmap transform(Bitmap source) {
                        int targetWidth;
                        int targetHeight;
                        int phoneHeight;

                        WindowManager wm = (WindowManager) BlogActivity.this.getSystemService(Context.WINDOW_SERVICE);
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

                if (BlogImage == null) {

                    Resources r = BlogActivity.this.getResources();
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


                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) views.getLayoutParams();
                    params.setMargins(0, 0, 0, -px2);
                    views.setLayoutParams(params);

                } else {

                    Picasso.get()
                            .load(BlogImage)
                            .error(android.R.drawable.stat_notify_error)

                            .transform(transformation)
                            .into(IV_BlogImage, new Callback() {
                                @Override
                                public void onSuccess() {



                                }

                                @Override
                                public void onError(Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(BlogActivity.this, "", Toast.LENGTH_SHORT).show();
                                }
                            });

                }


            }

            @Override
            public void onCallBack3(String string) {

                UserName=string;
                TV_UserName.setText(UserName);


            }

            @Override
            public void onCallBack4(String string) {

                ProfileImage=string;
                Picasso.get()
                        .load(ProfileImage)
                        .error(android.R.drawable.stat_notify_error)
                        .resize(400, 400)
                        .centerCrop()
                        // .transform(transformation)
                        .into(CIV_ProfileImage, new Callback() {
                            @Override
                            public void onSuccess() {


                            }

                            @Override
                            public void onError(Exception e) {

                                Toast.makeText(BlogActivity.this, "Failed to load new posts", Toast.LENGTH_SHORT).show();

                            }


                        });


            }

            @Override
            public void onCallBack5(String string) {

                ClubLocation=string;
                TV_ClubLocation.setText(ClubLocation);

            }

            @Override
            public void onCallBack6(int count) {

                CommentsCountX=count;

            }

            @Override
            public void onCallBack7(String string) {

                UploadedBy=string;

            }

            @Override
            public void onCallBack8(String string) {

                editedS=string;
                if(editedS.equals("true")){
                    edited.setVisibility(View.VISIBLE);
                }
                else{
                    edited.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCallBack10(String string){

                updatedTime=string;

            }

            @Override
            public void onCallBack9(String string){
                getTodaysTime();
                updatedDay=string;
                Log.e("S1",""+updatedTime);
                if(updatedDay.equals(todays_date)){
                    TimeX.setText(updatedTime);
                }
                else{
                    TimeX.setText(updatedDay);
                }



            }

            @Override
            public void onCallBack11(int string){
                Log.e("llike",""+string);

                like_thumb.setText(""+string);



            }

            @Override
            public void onCallBack12(int string){
                Log.e("ldislike",""+string);

                dislike_thumb.setText(""+string);
            }






        });
















        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int cv_height = cardView.getHeight();
                int scrollY = scrollView.getScrollY();
                if (scrollY >= cv_height) {
                    getSupportActionBar().setTitle(BlogText);
                    getSupportActionBar().setSubtitle(UserName);
                } else {
                    getSupportActionBar().setTitle("Comments");
                    getSupportActionBar().setSubtitle("");
                }
                Log.i("ScrollY", "" + scrollY);
                Log.i("ScrollCV", "" + cv_height);
            }
        });



       /* if(check_like!=null&&check_like.equals("true")){
            dislike_thumb.setClickable(false);
            dislike_thumb.setEnabled(false);
            dislike_thumb.setFocusable(false);
        }
        else{
            dislike_thumb.setClickable(true);
            dislike_thumb.setEnabled(true);
            dislike_thumb.setFocusable(true);
        }
*/




        like_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(X11.equals("Enabled")){

                    if (check_like != null && check_like.equals("true")) {
                        blogReference.child(key).child("LikeStatus").child(user.getUid()).setValue("false");
                        blogReference.child(key).child("Participated").child(user.getUid()).child("2").removeValue();
                        int new_number = Integer.parseInt(like_thumb.getText().toString()) - 1;
                        like_thumb.setText("" + new_number);
                        X22 = "Enabled";
                        DontLikeThings();
                        send_like="false";
                        LikeCountS=new_number;

                        blogReference.child(key).child("NumberLikes").setValue(new_number);


                    } else {


                        int new_number = Integer.parseInt(like_thumb.getText().toString()) + 1;
                        X22 = "Disabled";
                        blogReference.child(key).child("LikeStatus").child(user.getUid()).setValue("true");
                        blogReference.child(key).child("Participated").child(user.getUid()).child("2").setValue("Liked");
                        blogReference.child(key).child("NumberLikes").setValue(new_number);
                        like_thumb.setText("" + new_number);
                        DoLikeThings();
                        send_like="true";
                        LikeCountS=new_number;


                    }



                }



            }
        });




        dislike_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(X22!=null&&X22.equals("Enabled")){

                    if(check_dislike!=null&&check_dislike.equals("true")){
                        X11="Enabled";
                        int new_number=Integer.parseInt(dislike_thumb.getText().toString())-1;
                        blogReference.child(key).child("DislikeStatus").child(user.getUid()).setValue("false");
                        blogReference.child(key).child("Participated").child(user.getUid()).child("2").removeValue();
                        blogReference.child(key).child("NumberDislikes").setValue(new_number);
                        dislike_thumb.setText(""+ new_number);
                        DontDislikeThings();
                        send_dislike="false";
                        DislikeCountS=new_number;
                    }
                    else{
                        X11="Disabled";
                        int new_number=Integer.parseInt(dislike_thumb.getText().toString())+1;
                        blogReference.child(key).child("DislikeStatus").child(user.getUid()).setValue("true");
                        blogReference.child(key).child("Participated").child(user.getUid()).child("2").setValue("Disliked");
                        blogReference.child(key).child("NumberDislikes").setValue(new_number);
                        dislike_thumb.setText(""+ new_number);
                        DoDislikeThings();
                        send_dislike="true";
                        DislikeCountS=new_number;
                    }

                }




                  /* if(check_dislike!=null&&check_dislike.equals("true")){
                       like_thumb.setClickable(false);
                       Log.e("work","true");
                       X22="Disabled";
                       like_thumb.setEnabled(false);
                       like_thumb.setFocusable(false);
                   } else{
                       Log.e("work","false");
                       like_thumb.setClickable(true);
                       like_thumb.setEnabled(true);
                       like_thumb.setFocusable(true);
                   }*/



            }
        });





        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!reply)
                    customOnClick(send_button,null,null,null);
            }
        });

        CancelReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ReplyIdentify.getVisibility()==View.VISIBLE)
                    ReplyIdentify.setVisibility(View.GONE);
            }
        });











        participant_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BlogActivity.this,ParticipantsList.class);
                intent.putExtra("Count",participant_countX);
                intent.putExtra("EventKey",EventKeyM);
                intent.putExtra("Key",key);
                startActivity(intent);
            }
        });





    }

    public void DontLikeThings(){

        // like_thumb.setClickable(false);
        int tintColor = ContextCompat.getColor(this,R.color.lightGrey);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_like_thumb);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable.mutate(), tintColor);
        check_like="false";

        drawable.setBounds( 0, 0, 60, 60);

        like_thumb.setCompoundDrawables(drawable, null, null, null);
    }




    public void DoLikeThings(){

        //  like_thumb.setClickable(false);
        int tintColor = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_like_thumb);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable.mutate(), tintColor);
        check_like="true";

        drawable.setBounds( 0, 0, 60, 60);

        like_thumb.setCompoundDrawables(drawable, null, null, null);
    }

    public void DoDislikeThings(){

        check_dislike="true";

        //  dislike_thumb.setClickable(false);
        int tintColor = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_dislike_thumb);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable.mutate(), tintColor);

        drawable.setBounds( 0, 0, 60, 60);

        dislike_thumb.setCompoundDrawables(drawable, null, null, null);
    }

    public void DontDislikeThings(){

        check_dislike="false";

        //  dislike_thumb.setClickable(false);
        int tintColor = ContextCompat.getColor(this,R.color.lightGrey);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_dislike_thumb);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable.mutate(), tintColor);

        drawable.setBounds( 0, 0, 60, 60);

        dislike_thumb.setCompoundDrawables(drawable, null, null, null);
    }







    private void getTodaysTime(){

        Calendar calendar = Calendar.getInstance();


        int hourS=calendar.get(Calendar.HOUR_OF_DAY);
        yearS = Integer.toString(calendar.get(Calendar.YEAR)) ;
        int monthofYear = calendar.get(Calendar.MONTH);
        dayS = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        int minuteS=calendar.get(Calendar.MINUTE);
        //    Toast.makeText(getActivity(),hourS+minutesS,Toast.LENGTH_SHORT).show();

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourS);
        datetime.set(Calendar.MINUTE, minuteS);

        String minute;
        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        if(datetime.get(Calendar.MINUTE)<10){

            minute= "0"+datetime.get(Calendar.MINUTE);
        }
        else minute=""+datetime.get(Calendar.MINUTE);

        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";
        todays_time=strHrsToShow + ":" + minute + " " + am_pm;

        switch(monthofYear+1) {
            case 1:
                monthS = "Jan";
                break;
            case 2:
                monthS = "Feb";
                break;
            case 3:
                monthS= "March";
                break;
            case 4:
                monthS= "April";
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

        todays_date=""+dayS+" "+monthS+", "+yearS;





    }

    public void fetch(final String key,final String User,final String Comment, final int scroll) {


        blogReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //     liked="False";
                if (dataSnapshot.hasChild("Comments")) {
                    dataSnapshot.child("Comments").getRef().addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            final String UserName = dataSnapshot.child("UserName").getValue(String.class);
                            final String ImageUrl = dataSnapshot.child("UserImage").getValue(String.class);
                            final String Comment = dataSnapshot.child("Comment").getValue(String.class);
                            final String Time = dataSnapshot.child("Time").getValue(String.class);
                            final String Date = dataSnapshot.child("Date").getValue(String.class);
                            final String Key = dataSnapshot.child("Key").getValue(String.class);
                            final String UserUID=dataSnapshot.child("UserUID").getValue(String.class);
                            final String RepliersText=dataSnapshot.child("RepliersText").getValue(String.class);
                            final String RepliersName=dataSnapshot.child("RepliersName").getValue(String.class);


                            Log.i("xx", "" + Key);
                            String liked2 = dataSnapshot.child("Likes").child(user.getUid()).getValue(String.class);
                            Log.i("xx", "hii" + "" + Key);
                            Log.i("xx", "hii2" + "" + dataSnapshot.hasChild("Likes"));

                            if (dataSnapshot.hasChild("LikeCount")) {
                                LikeCount = dataSnapshot.child("LikeCount").getValue(Integer.class);
                            } else LikeCount = 0;

                            Log.i("liked2", "" + liked2);

                            if (liked2 == null) {
                                liked2 = "False";
                            }


                            comments.add(new DiscussionsObject(ImageUrl, UserName, Time, Comment, Key, liked2, LikeCount,UserUID,RepliersText,RepliersName,Date));
                            mAdapter.notifyDataSetChanged();
                            //  reply=false;


                            Log.i("Status", "2" + liked);


                            // recyclerView.scrollToPosition(comments.size()-1);


                            Log.i("Hii", "Hii");


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
                    });

                    blogReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.i("Gye", "Gye");

                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(BlogActivity.this, "size " + comments.size(), Toast.LENGTH_SHORT).show();
                            if (comments.size() == 0) {
                                CommentSection.setVisibility(View.GONE);
                                CommentSection.setVisibility(View.INVISIBLE);
                                Log.i("Sye", "Sye");
                                noComments.setVisibility(View.VISIBLE);

                            } else {

                                CommentSection.setVisibility(View.VISIBLE);
                                if(comments.size()==1){
                                    CommentCount.setText(comments.size()+" Comment");
                                }
                                else{
                                    CommentCount.setText(comments.size()+" Comments");
                                }


                                noComments.setVisibility(View.INVISIBLE);
                                if(scroll==2){


                                    recyclerView.post(new Runnable() {
                                        @Override
                                        public void run() {


                                            for (int i = 0; i<comments.size(); i++) {
                                                Log.i("Check9",""+i);


                                                final RecyclerView.ViewHolder holder2= recyclerView.findViewHolderForLayoutPosition(i);

                                                TextView view2=holder2.itemView.findViewById(R.id.user_comment);
                                                Log.i("Check7",""+view2.getText().toString());
                                                TextView view1=holder2.itemView.findViewById(R.id.user_name);




                                                //   Log.i("Height",""+holder2.itemView.findViewById(R.id.cv).getLayoutParams().height )  ;
                                                if(view1.getText().toString().equals(User)&&view2.getText().toString().equals(Comment)){

                                                    final   int result_position=i;
                                                    Log.i("Check8",""+result_position);
                                                    final RecyclerView.ViewHolder holder7= recyclerView.findViewHolderForLayoutPosition(result_position);
                                                    final int height=      holder7.itemView.findViewById(R.id.cv).getHeight() ;
                                                    Log.i("Height2",""+holder7.itemView.findViewById(R.id.cv).getHeight() )  ;

                                                    recyclerView.post(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            float y = recyclerView.getY() + recyclerView.getLayoutManager().getChildAt(result_position).getY();

                                                            scrollView.scrollTo(0, (int) y);
                                                            Resources r = BlogActivity.this.getResources();
                                                            int px2 = (int) TypedValue.applyDimension(
                                                                    TypedValue.COMPLEX_UNIT_DIP,
                                                                    20,
                                                                    r.getDisplayMetrics()
                                                            );


                                                            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder7.itemView.findViewById(R.id.background_view).getLayoutParams();
                                                            params.height=height+px2;
                                                            holder7.itemView.findViewById(R.id.background_view).setLayoutParams(params);
                                                            holder7.itemView.findViewById(R.id.background_view).setVisibility(View.VISIBLE);

                                                            Handler mHandler=new Handler();
                                                            mHandler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    holder7.itemView.findViewById(R.id.background_view).setVisibility(View.GONE);

                                                                }
                                                            },1000);





                                                        }
                                                    });

                                                    break;


                                                }



                                            }

                                        }
                                    });



                                }
                                if (scroll == 1) {
                                    dataSnapshot.child(key).child("CommentsCount").getRef().setValue(comments.size());
                                    recyclerView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (recyclerView != null && recyclerView.getLayoutManager().getChildAt(comments.size() - 1) != null) {
                                                float y = recyclerView.getY() + recyclerView.getLayoutManager().getChildAt(comments.size() - 1).getY();
                                                scrollView.scrollTo(0, (int) y);
                                                final RecyclerView.ViewHolder holder7 = recyclerView.findViewHolderForLayoutPosition(comments.size()-1);
                                                final int height = holder7.itemView.findViewById(R.id.cv).getHeight();
                                                Resources r = BlogActivity.this.getResources();
                                                int px2 = (int) TypedValue.applyDimension(
                                                        TypedValue.COMPLEX_UNIT_DIP,
                                                        20,
                                                        r.getDisplayMetrics()
                                                );
                                                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder7.itemView.findViewById(R.id.background_view).getLayoutParams();
                                                params.height = height + px2;
                                                holder7.itemView.findViewById(R.id.background_view).setLayoutParams(params);
                                                holder7.itemView.findViewById(R.id.background_view).setVisibility(View.VISIBLE);

                                                Handler mHandler = new Handler();
                                                mHandler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        holder7.itemView.findViewById(R.id.background_view).setVisibility(View.GONE);

                                                    }
                                                }, 1000);
                                            }

                                        }
                                    });


                                }
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {


                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(BlogActivity.this, "size " + comments.size(), Toast.LENGTH_SHORT).show();

                    if (comments.size() == 0) {
                        Log.i("Sye", "Sye");
                        noComments.setVisibility(View.VISIBLE);

                    } else {
                        noComments.setVisibility(View.INVISIBLE);

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // recyclerView.scrollToPosition(comments.size()-1);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(UploadedBy!=null){

            if(UploadedBy.equals(user.getUid())){
                getMenuInflater().inflate(R.menu.blog_activity_admin, menu);
            }
            else{
                getMenuInflater().inflate(R.menu.blog_activity, menu);
            }

        }



        this.menu=menu;

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
          /*  case R.id.copy_topic_link:
                progress_edit_delete.setVisibility(View.VISIBLE);
                CopyTopicLink();
                break;

            case R.id.copy_user_profile_link:
                progress_edit_delete.setVisibility(View.VISIBLE);
                CopyUserProfileLink(UploadedBy);
                break;
*/
            case R.id.edit:

                EditTopic();
                break;

            case R.id.delete:

                DeleteTopic();
                break;

            default:
                return false;
        }





        return true;
    }

    public void EditTopic(){
        LayoutInflater li = LayoutInflater.from(BlogActivity.this);
        View promptsView = li.inflate(R.layout.edit_posts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BlogActivity.this);

        alertDialogBuilder.setView(promptsView);

        final EditText subject = (EditText) promptsView
                .findViewById(R.id.subject);

        blogReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String text=dataSnapshot.child("BlogText").getValue(String.class);

                subject.setText(text);
                subject.setSelection(text.length());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                progress_edit_delete.setVisibility(View.VISIBLE);
                                InputMethodManager imm = (InputMethodManager)BlogActivity.this. getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(subject.getWindowToken(), 0);
                                blogReference.child(key).child("BlogText").setValue(subject.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        blogReference.child(key).child("Edited").setValue("true");
                                        TV_BlogText.setText(subject.getText().toString());
                                        progress_edit_delete.setVisibility(View.GONE);
                                        edited.setVisibility(View.VISIBLE);
                                        EditCheck="true";

                                        Toast.makeText(BlogActivity.this, "Topic Edited", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                InputMethodManager imm = (InputMethodManager)BlogActivity.this. getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(subject.getWindowToken(), 0);
                                dialog.cancel();
                            }
                        });


        AlertDialog alertDialog = alertDialogBuilder.create();


        alertDialog.show();
        subject.requestFocus();
        InputMethodManager imm = (InputMethodManager)BlogActivity.this. getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);






    }

    public void DeleteTopic(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BlogActivity.this);
        alertDialogBuilder.setTitle("Attention !")
                .setMessage("Are you sure you want to delete this topic?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        progress_edit_delete.setVisibility(View.VISIBLE);

                        blogReference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(BlogActivity.this,"Topic Deleted",Toast.LENGTH_SHORT).show();
                                progress_edit_delete.setVisibility(View.GONE);
                                parent_layout.setVisibility(View.INVISIBLE);
                                no_topics.setVisibility(View.VISIBLE);
                                deleted_check="true";
                                menu.clear();







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


                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();



    }

    @Override
    public void onBackPressed() {


       /* if(URLChecker.equals("false")){*/
        prefsEditor.putInt("keyX", delete_position);
        prefsEditor.putString("check",deleted_check);
        prefsEditor.putString("send_like",send_like);
        prefsEditor.putString("send_dislike",send_dislike);
        prefsEditor.putInt("LikeCountS",LikeCountS);
        prefsEditor.putInt("DislikeCountS",DislikeCountS);
        prefsEditor.putInt("CommentsCount",CommentsCountX);
        prefsEditor.putString("EditCheck",EditCheck);
        prefsEditor.commit();


        super.onBackPressed();







    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }












    class RecycleAdapter extends RecyclerView.Adapter<BlogActivity.RecycleAdapter.MyViewHolder> {

        private List<DiscussionsObject> comments;
        private Context context;

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.blog_discussions, parent, false);
            com.example.hotelloadmin.DiscussionsPackage.BlogActivity.RecycleAdapter.MyViewHolder vh =
                    new com.example.hotelloadmin.DiscussionsPackage.BlogActivity.RecycleAdapter.MyViewHolder(v);
            return vh;
        }



        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

            final DiscussionsObject c = comments.get(position);


            holder.ProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    parentReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(c.getUserUID())){
                                Intent i=new Intent(BlogActivity.this,PeopleClick.class);
                                i.putExtra("UserUID",c.getUserUID());
                                startActivity(i);

                            }else{

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });

            //chose wisely
          /*  if(c.getUserUID().equals(user.getUid())){
                holder.cv.setCardBackgroundColor(ContextCompat.getColor(BlogActivity.this,R.color.mainColor));
                holder.UserName.setTextColor(ContextCompat.getColor(BlogActivity.this,R.color.white));
                if(holder.likeCount1!=null){
                    holder.likeCount1.setTextColor(ContextCompat.getColor(BlogActivity.this,R.color.white));
                }
                if(holder.likeCount2!=null){
                    holder.likeCount2.setTextColor(ContextCompat.getColor(BlogActivity.this,R.color.white));
                }

                holder.UserComment.setTextColor(ContextCompat.getColor(BlogActivity.this,R.color.white));
                holder.ReplyCV.setCardBackgroundColor(ContextCompat.getColor(BlogActivity.this,R.color.white));

            }*/
            if(c.getRepliersText()!=null){
                Log.i("mmm","mmm");
                holder.ReplyCV.setVisibility(View.VISIBLE);
                holder.RepliersText.setText(c.getRepliersText());
                holder.RepliersName.setText(c.getRepliersName());

            }
            else{
                holder.ReplyCV.setVisibility(View.GONE);
            }
            holder.UserComment.setText(c.getComment());


            holder.cv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    PopupMenu popupMenu=new PopupMenu(BlogActivity.this,holder.ProfileImage);
                    if(c.getUserUID().equals(user.getUid())){
                        popupMenu.getMenuInflater().inflate(R.menu.reply_comment_admin, popupMenu.getMenu());
                    }
                    else{
                        popupMenu.getMenuInflater().inflate(R.menu.reply_comment_user, popupMenu.getMenu());
                    }

                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            switch (menuItem.getItemId()){
                                case R.id.reply:
                                    commentsX=c.getComment();
                                    usernamesX=c.getUserName();
                                    KeysX=c.getKey();


                                    ReplyIdentify_RepliersName.setText(usernamesX);
                                    ReplyIdentify_RepliersText.setText(commentsX);

                                    ReplyIdentify.setVisibility(View.VISIBLE);
                                    comments_et.requestFocus();
                                    addReply(commentsX,usernamesX,KeysX);



                                    return true;
                                case R.id.delete:
                                    deleteComment(c.getKey(),position);
                                    return true;
                              /*  case R.id.userprofile_link:
                                    progress_edit_delete.setVisibility(View.VISIBLE);
                                    CopyUserProfileLink(c.getUserUID());

                                    return true;*/

                               /* case R.id.comment_link:
                                    progress_edit_delete.setVisibility(View.VISIBLE);
                                    copyCommentLink(c.getUserName(),c.getComment());*/
                            }
                            return true;
                        }
                    });

                    return true;
                }
            });
            if(holder.ReplyCV.getVisibility()==View.VISIBLE){

                holder.ReplyCV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String check_text=holder.RepliersText.getText().toString();
                        String check_name=holder.RepliersName.getText().toString();
                        final int check_position= holder.getAdapterPosition();

                        Log.i("Check5",""+check_text);
                        Log.i("Check6",""+check_position);


                        for (int i = check_position-1; i >=0; i--) {
                            Log.i("Check9",""+i);
                            final RecyclerView.ViewHolder holder2= recyclerView.findViewHolderForLayoutPosition(i);
                            TextView view2=holder2.itemView.findViewById(R.id.user_comment);
                            Log.i("Check7",""+view2.getText().toString());
                            TextView view1=holder2.itemView.findViewById(R.id.user_name);
                            //   Log.i("Height",""+holder2.itemView.findViewById(R.id.cv).getLayoutParams().height )  ;
                            if(view1.getText().toString().equals(check_name)&&view2.getText().toString().equals(check_text)){

                                final   int result_position=i;
                                Log.i("Check8",""+result_position);
                                final RecyclerView.ViewHolder holder7= recyclerView.findViewHolderForLayoutPosition(result_position);
                                final int height=      holder7.itemView.findViewById(R.id.cv).getHeight() ;
                                Log.i("Height2",""+holder7.itemView.findViewById(R.id.cv).getHeight() )  ;

                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        float y = recyclerView.getY() + recyclerView.getLayoutManager().getChildAt(result_position).getY();
                                        //scrollView.scrollTo();
                                        scrollView.scrollTo(0, (int) y);







                                      /* CardView.LayoutParams params=(CardView.LayoutParams)holder.cv.getLayoutParams();
                                       params.get



                                       Resources r = BlogActivity.this.getResources();
                                       int px = (int) TypedValue.applyDimension(
                                               TypedValue.COMPLEX_UNIT_DIP,
                                               15,
                                               r.getDisplayMetrics()
                                       );*/
                                        Resources r = BlogActivity.this.getResources();
                                        int px2 = (int) TypedValue.applyDimension(
                                                TypedValue.COMPLEX_UNIT_DIP,
                                                20,
                                                r.getDisplayMetrics()
                                        );


                                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder7.itemView.findViewById(R.id.background_view).getLayoutParams();
                                        params.height=height+px2;
                                        holder7.itemView.findViewById(R.id.background_view).setLayoutParams(params);
                                        holder7.itemView.findViewById(R.id.background_view).setVisibility(View.VISIBLE);

                                        Handler mHandler=new Handler();
                                        mHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                holder7.itemView.findViewById(R.id.background_view).setVisibility(View.GONE);

                                            }
                                        },1000);





                                    }
                                });

                                break;


                            }

                        }



                    }
                });

            }

            if(holder.likeCount1!=null&&c.getLikeCount()!=0){

                holder.likeCount1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent =new Intent(BlogActivity.this,LikeCommentParticipant.class);
                        intent.putExtra("BlogKey",key);
                        intent.putExtra("CommentKey",c.getKey());
                        startActivity(intent);

                    }
                });


            }

            if(holder.likeCount2!=null&&c.getLikeCount()!=0){

                holder.likeCount2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent =new Intent(BlogActivity.this,LikeCommentParticipant.class);
                        intent.putExtra("BlogKey",key);
                        intent.putExtra("CommentKey",c.getKey());
                        startActivity(intent);

                    }
                });


            }

            holder.UserName.setText(c.getUserName());
            holder.UserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    parentReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(c.getUserUID())){
                                Intent intent=new Intent(BlogActivity.this, PeopleClick.class);
                                intent.putExtra("UserUID",c.getUserUID());
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    adminReference.addListenerForSingleValueEvent(new ValueEventListener() {
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


            if(todays_date.equals(c.getDate())){
                holder.Time.setText(c.getTime());
            }
            else{
                holder.Time.setText(c.getDate());
            }

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

                            Toast.makeText(BlogActivity.this, "Failed to load new posts", Toast.LENGTH_SHORT).show();

                        }


                    });

            if (c.getLiked() == null || c.getLiked().equals("False")) {

                Log.i("Status", "False");
                Log.i("Status", "0" + c.getComment());
                holder.Dislike.setVisibility(View.INVISIBLE);
                holder.Like.setVisibility(View.VISIBLE);
            } else {
                Log.i("Status", "True");
                Log.i("Status", "0" + c.getComment());
                holder.Dislike.setVisibility(View.VISIBLE);
                holder.Like.setVisibility(View.INVISIBLE);
            }

            if (holder.likeCount1 != null) {
                holder.likeCount1.setText(" " + c.getLikeCount() + " liked");
            }

            if (holder.likeCount2 != null) {
                holder.likeCount2.setText(" " + c.getLikeCount() + " liked");
            }


            if(holder.likee!=null) {

                holder.likee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int i = 1;


                        if ((c.getLiked() == null || c.getLiked().equals("False"))) {

                            i = 0;

                            Log.i("Check", "Dislike");


                            c.setLiked("True");

                            blogReference.child(key).child("Comments").child(c.getKey()).child("Likes").child(user.getUid()).setValue("True");
                            blogReference.child(key).child("Comments").child(c.getKey()).child("LikeCount").setValue(c.getLikeCount() + 1);
                            c.setLikeCount(c.getLikeCount() + 1);
                            mAdapter.notifyDataSetChanged();


                            if (holder.likeCount1 != null) {

                                Log.i("Yes1", "Yes2");
                                holder.likeCount1.setText(" " + c.getLikeCount() + " liked");
                            }

                            if (holder.likeCount2 != null) {
                                Log.i("Yes2", "Yes1");
                                holder.likeCount2.setText(" " + c.getLikeCount() + " liked");
                            }


                        }}


                });

            }


            if(holder.dislikee!=null){

                holder.dislikee.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        c.setLiked("False");

                        blogReference.child(key).child("Comments").child(c.getKey()).child("Likes").child(user.getUid()).setValue("False");
                        blogReference.child(key).child("Comments").child(c.getKey()).child("LikeCount").setValue((c.getLikeCount() - 1));
                        c.setLikeCount(c.getLikeCount() - 1);
                        mAdapter.notifyDataSetChanged();

                        if (holder.likeCount1 != null) {
                            holder.likeCount1.setText(" " + c.getLikeCount() + " liked");
                            Log.i("Yes3", "Yes4");
                        }

                        if (holder.likeCount2 != null) {
                            Log.i("Yes4", "Yes3");
                            holder.likeCount2.setText(" " + c.getLikeCount() + " liked");
                        }

                    }
                });
            }








/*

            holder.frame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int i = 1;


                    if ((c.getLiked() == null || c.getLiked().equals("False"))) {

                        i = 0;

                        Log.i("Check", "Dislike");


                        c.setLiked("True");

                        blogReference.child(key).child("Comments").child(c.getKey()).child("Likes").child(user.getUid()).setValue("True");
                        blogReference.child(key).child("Comments").child(c.getKey()).child("LikeCount").setValue(c.getLikeCount() + 1);
                        c.setLikeCount(c.getLikeCount() + 1);
                        mAdapter.notifyDataSetChanged();



                        if (holder.likeCount1 != null) {

                            Log.i("Yes1", "Yes2");
                            holder.likeCount1.setText(" " + c.getLikeCount() + " liked");
                        }

                        if (holder.likeCount2 != null) {
                            Log.i("Yes2", "Yes1");
                            holder.likeCount2.setText(" " + c.getLikeCount() + " liked");
                        }

                    } else {

                        c.setLiked("False");

                        blogReference.child(key).child("Comments").child(c.getKey()).child("Likes").child(user.getUid()).setValue("False");
                        blogReference.child(key).child("Comments").child(c.getKey()).child("LikeCount").setValue((c.getLikeCount() - 1));
                        c.setLikeCount(c.getLikeCount() - 1);
                        mAdapter.notifyDataSetChanged();

                        if (holder.likeCount1 != null) {
                            holder.likeCount1.setText(" " + c.getLikeCount() + " liked");
                            Log.i("Yes3", "Yes4");
                        }

                        if (holder.likeCount2 != null) {
                            Log.i("Yes4", "Yes3");
                            holder.likeCount2.setText(" " + c.getLikeCount() + " liked");
                        }

                    }

                }
            });
*/


        }

        @Override
        public int getItemCount() {
            Log.i("Size", "Hii " + comments.size());
            return comments.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {


            TextView UserComment;

            CircleImageView ProfileImage;
            TextView Time;
            TextView UserName;
            LinearLayout Like;
            LinearLayout Dislike;
            FrameLayout frame;
            TextView likeCount1;
            TextView likeCount2;
            CardView cv;
            CardView ReplyCV;
            TextView RepliersName;
            TextView RepliersText;
            View BackGroundView;
            LinearLayout likee;
            ImageView dislikee;



            public MyViewHolder(View v) {
                super(v);

                UserComment = (TextView) v.findViewById(R.id.user_comment);
                ReplyCV=(CardView) v.findViewById(R.id.reply_cv);
                Like = (LinearLayout) v.findViewById(R.id.like_comment);
                Dislike = (LinearLayout) v.findViewById(R.id.dis_like_comment);
                ProfileImage = (CircleImageView) v.findViewById(R.id.profile_image);
                Time = (TextView) v.findViewById(R.id.time);
                BackGroundView=(View)findViewById(R.id.background_view);
                UserName = (TextView) v.findViewById(R.id.user_name);
                frame = (FrameLayout) v.findViewById(R.id.frame);
                likee=(LinearLayout)v.findViewById(R.id.like);
                dislikee=(ImageView)v.findViewById(R.id.dislike);
                likeCount1 = (TextView) v.findViewById(R.id.likeCount1);
                likeCount2 = (TextView) v.findViewById(R.id.likeCount2);
                cv=(CardView)v.findViewById(R.id.cv);
                RepliersName=(TextView)v.findViewById(R.id.comment_admin_name);
                RepliersText=(TextView)v.findViewById(R.id.comment_admin_text);


            }

        }

        public RecycleAdapter(List<DiscussionsObject> comments) {

            this.comments = comments;
        }


    }

    public void addReply(String comment,String username,String key){
        // Toast.makeText(BlogActivity.this,"Reply",Toast.LENGTH_SHORT).show();


        //  ReplyIdentify.setVisibility(View.VISIBLE);
        customOnClick(send_button,comment,username,key);













    }

    public void deleteComment(final String KeyXX, final int positionX){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                BlogActivity.this);
        alertDialogBuilder.setTitle("Attention !")
                .setMessage("Are you sure you want to delete this comment?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        blogReference.child(key).child("Comments").child(KeyXX).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(BlogActivity.this,"Comment Deleted",Toast.LENGTH_SHORT).show();
                                CommentsCountX=CommentsCountX-1;
                                blogReference.child(key).child("CommentsCount").setValue(CommentsCountX);
                                if(CommentsCountX==0){
                                    noComments.setVisibility(View.VISIBLE);
                                    CommentSection.setVisibility(View.GONE);

                                }
                                if(CommentsCountX==1){
                                    CommentCount.setText(CommentsCountX+" Comment");
                                    noComments.setVisibility(View.INVISIBLE);
                                    CommentSection.setVisibility(View.VISIBLE);
                                }
                                if(CommentsCountX>1){
                                    CommentCount.setText(CommentsCountX+" Comments");
                                    noComments.setVisibility(View.INVISIBLE);
                                    CommentSection.setVisibility(View.VISIBLE);
                                }

                                comments.remove(positionX);
                                mAdapter.notifyDataSetChanged();

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


                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();




    }

    public void CopyTopicLink(){

        String uri="http://hungryhunter96.github.io/post?Key="+key;


        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(uri))
                .setDomainUriPrefix("https://ddxr.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())

                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            progress_edit_delete.setVisibility(View.GONE);

                            Toast.makeText(BlogActivity.this,"Link Copied",Toast.LENGTH_SHORT).show();

                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            ClipboardManager clipboard = (ClipboardManager) BlogActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText(null, shortLink.toString());
                            if (clipboard == null) return;
                            clipboard.setPrimaryClip(clip);

                        } else {

                            Toast.makeText(BlogActivity.this,"Link Copy Failure",Toast.LENGTH_SHORT).show();

                        }
                    }
                });






    }

    public void CopyUserProfileLink(String key8){

        String uri="http://hungryhunter96.github.io/profile?Key="+key8;


        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(uri))
                .setDomainUriPrefix("https://ddxr.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())

                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            progress_edit_delete.setVisibility(View.GONE);

                            Toast.makeText(BlogActivity.this,"Link Copied",Toast.LENGTH_SHORT).show();

                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            ClipboardManager clipboard = (ClipboardManager) BlogActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText(null, shortLink.toString());
                            if (clipboard == null) return;
                            clipboard.setPrimaryClip(clip);

                        } else {

                            Toast.makeText(BlogActivity.this,"Link Copy Failure",Toast.LENGTH_SHORT).show();

                        }
                    }
                });






    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void copyCommentLink(String user_name, String comment){

        // Toast.makeText(BlogActivity.this,"Copy",Toast.LENGTH_SHORT).show();

        String uri="http://hungryhunter96.github.io/post?Key="+key+"&User="+user_name+"&Comment="+comment;


        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(uri))
                .setDomainUriPrefix("https://ddxr.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())

                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            progress_edit_delete.setVisibility(View.GONE);

                            Toast.makeText(BlogActivity.this,"Link Copied",Toast.LENGTH_SHORT).show();
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            ClipboardManager clipboard = (ClipboardManager) BlogActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText(null, shortLink.toString());
                            if (clipboard == null) return;
                            clipboard.setPrimaryClip(clip);

                        } else {

                            Toast.makeText(BlogActivity.this,"Link Copy Failure",Toast.LENGTH_SHORT).show();

                        }
                    }
                });












    }

    private void customOnClick(Button b1, final String comment, final String username, final String key2){


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String comments_text = comments_et.getText().toString();
                if (comments_text.length() == 0) {
                    Toast.makeText(BlogActivity.this, "Comments cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    comments_et.setText("");
                    progressBar2.setVisibility(View.VISIBLE);
                    send_button.setVisibility(View.INVISIBLE);

                    hideKeyboard(BlogActivity.this);


                    progressBar.setVisibility(View.VISIBLE);





                    parentReference = FirebaseDatabase.getInstance().getReference();

                    parentReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChild(user.getUid())) {

                                UserNameX = dataSnapshot.child(user.getUid()).child("UserName").getValue(String.class);
                                ClubImageX = dataSnapshot.child(user.getUid()).child("ImageUrl").getValue(String.class);
                                final Map<String, Object> commented_user = new HashMap<>();
                                commented_user.put("UserImage", ClubImageX);
                                commented_user.put("UserName", UserNameX);
                                commented_user.put("UserUID", user.getUid());
                                commented_user.put("Comment", comments_text);
                                commented_user.put("Time", todays_time);
                                commented_user.put("Date", todays_date);
                                DatabaseReference blognodeReference = blogReference.child(key).child("Comments").push();
                                final String keyX = blognodeReference.getKey();
                                commented_user.put("Key", keyX);

                                if(ReplyIdentify.getVisibility()==View.VISIBLE){

                                    commented_user.put("RepliersName",username);
                                    commented_user.put("RepliersText",comment);


                                }
                                blogReference.child(key).child("Participated").child(user.getUid()).child("3").setValue("Commented");
                                blogReference.child(key).child("Comments").child(keyX).updateChildren(commented_user, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable final DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                            Toast.makeText(BlogActivity.this, "Comment cannot be added", Toast.LENGTH_SHORT).show();
                                            progressBar2.setVisibility(View.INVISIBLE);
                                            if(ReplyIdentify.getVisibility()==View.VISIBLE){
                                                ReplyIdentify.setVisibility(View.GONE);
                                            }

                                            send_button.setVisibility(View.VISIBLE);
                                            // CommentsCountX=CommentsCountX+1;

                                            progressBar.setVisibility(View.INVISIBLE);
                                            noComments.setVisibility(View.INVISIBLE);
                                        } else {

                                            if(ReplyIdentify.getVisibility()==View.VISIBLE){
                                                ReplyIdentify.setVisibility(View.GONE);
                                            }

                                            progressBar2.setVisibility(View.INVISIBLE);
                                            send_button.setVisibility(View.VISIBLE);

                                            progressBar.setVisibility(View.INVISIBLE);
                                            noComments.setVisibility(View.INVISIBLE);
                                            Log.e("SizeX",""+comments.size());
                                            mAdapter.notifyDataSetChanged();
                                            Log.e("SizeX2",""+comments.size());



                                            // Toast.makeText(BlogActivity.this,"Size is "+comments.size(),Toast.LENGTH_SHORT).show();

                                   /*     recyclerView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (recyclerView != null && recyclerView.getLayoutManager().getChildAt(comments.size() - 1) != null) {

                                                    float y = recyclerView.getY() + recyclerView.getLayoutManager().getChildAt(comments.size() - 1).getY();
                                                    scrollView.smoothScrollTo(0, (int) y);
                                                    Toast.makeText(BlogActivity.this, "workingfine", Toast.LENGTH_SHORT).show();
                                                    Log.i("VV", "WorkingFine");
                                                }

                                                if (recyclerView.getChildAt(0) == null) {
                                                    Log.i("VV", "findViewHolder");

                                                }
                                            }
                                        });*/






                                            //uncomment it if any problem exists with fetching
                                            if(comments.size()==0) {

                                                Log.e("SizeX3",""+comments.size()+"Going Inside");

                                                comments.clear();
                                                CommentsCountX=CommentsCountX+1;
                                                fetch(key,null,null,1);

                                            }




                                            else{
                                                Log.e("SizeX4",""+comments.size()+"Going Outside");
                                                mAdapter.notifyDataSetChanged();

                                                blogReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if(dataSnapshot.child(key).hasChild("CommentsCount")){
                                                            CommentsCountX=dataSnapshot.child(key).child("CommentsCount").getValue(Integer.class);
                                                            CommentsCountX=CommentsCountX+1;
                                                            dataSnapshot.child(key).child("CommentsCount").getRef().setValue(CommentsCountX);
                                                            CommentCount.setText(CommentsCountX+" Comments");
                                                        }

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });



                                                recyclerView.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        float y = recyclerView.getY() + recyclerView.getLayoutManager().getChildAt(comments.size() - 1).getY();

                                                        scrollView.smoothScrollTo(0, (int) y);
                                                        final RecyclerView.ViewHolder holder7 = recyclerView.findViewHolderForLayoutPosition(comments.size()-1);
                                                        final int height = holder7.itemView.findViewById(R.id.cv).getHeight();
                                                        Resources r = BlogActivity.this.getResources();
                                                        int px2 = (int) TypedValue.applyDimension(
                                                                TypedValue.COMPLEX_UNIT_DIP,
                                                                20,
                                                                r.getDisplayMetrics()
                                                        );


                                                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder7.itemView.findViewById(R.id.background_view).getLayoutParams();
                                                        params.height=height+px2;
                                                        holder7.itemView.findViewById(R.id.background_view).setLayoutParams(params);
                                                        holder7.itemView.findViewById(R.id.background_view).setVisibility(View.VISIBLE);

                                                        Handler mHandler=new Handler();
                                                        mHandler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                holder7.itemView.findViewById(R.id.background_view).setVisibility(View.GONE);

                                                            }
                                                        },1000);





                                                    }
                                                });


                                            }







                                            Toast.makeText(BlogActivity.this, "Comment added successfully", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });


                            } else {
                                adminReference = FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/").getReference();

                                adminReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        UserNameX = dataSnapshot.child(user.getUid()).child("ClubName").getValue(String.class);
                                        ClubImageX = dataSnapshot.child(user.getUid()).child("ProfileImageUrl").getValue(String.class);
                                        final Map<String, Object> commented_user = new HashMap<>();
                                        commented_user.put("UserImage", ClubImageX);
                                        commented_user.put("UserName", UserNameX);
                                        commented_user.put("UserUID", user.getUid());
                                        commented_user.put("Comment", comments_text);
                                        commented_user.put("Time", todays_time);
                                        commented_user.put("Date", todays_date);
                                        DatabaseReference blognodeReference = blogReference.child(key).child("Comments").push();
                                        final String keyX = blognodeReference.getKey();
                                        commented_user.put("Key", keyX);

                                        if(ReplyIdentify.getVisibility()==View.VISIBLE){

                                            commented_user.put("RepliersName",username);
                                            commented_user.put("RepliersText",comment);


                                        }
                                        blogReference.child(key).child("Participated").child(user.getUid()).child("3").setValue("Commented");
                                        blogReference.child(key).child("Comments").child(keyX).updateChildren(commented_user, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable final DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                if (databaseError != null) {
                                                    Toast.makeText(BlogActivity.this, "Comment cannot be added", Toast.LENGTH_SHORT).show();
                                                    progressBar2.setVisibility(View.INVISIBLE);
                                                    if(ReplyIdentify.getVisibility()==View.VISIBLE){
                                                        ReplyIdentify.setVisibility(View.GONE);
                                                    }

                                                    send_button.setVisibility(View.VISIBLE);
                                                    // CommentsCountX=CommentsCountX+1;

                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    noComments.setVisibility(View.INVISIBLE);
                                                } else {

                                                    if(ReplyIdentify.getVisibility()==View.VISIBLE){
                                                        ReplyIdentify.setVisibility(View.GONE);
                                                    }

                                                    progressBar2.setVisibility(View.INVISIBLE);
                                                    send_button.setVisibility(View.VISIBLE);

                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    noComments.setVisibility(View.INVISIBLE);
                                                    Log.e("SizeX",""+comments.size());
                                                    mAdapter.notifyDataSetChanged();
                                                    Log.e("SizeX2",""+comments.size());



                                                    // Toast.makeText(BlogActivity.this,"Size is "+comments.size(),Toast.LENGTH_SHORT).show();

                                   /*     recyclerView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (recyclerView != null && recyclerView.getLayoutManager().getChildAt(comments.size() - 1) != null) {

                                                    float y = recyclerView.getY() + recyclerView.getLayoutManager().getChildAt(comments.size() - 1).getY();
                                                    scrollView.smoothScrollTo(0, (int) y);
                                                    Toast.makeText(BlogActivity.this, "workingfine", Toast.LENGTH_SHORT).show();
                                                    Log.i("VV", "WorkingFine");
                                                }

                                                if (recyclerView.getChildAt(0) == null) {
                                                    Log.i("VV", "findViewHolder");

                                                }
                                            }
                                        });*/






                                                    //uncomment it if any problem exists with fetching
                                                    if(comments.size()==0) {

                                                        Log.e("SizeX3",""+comments.size()+"Going Inside");

                                                        comments.clear();
                                                        CommentsCountX=CommentsCountX+1;
                                                        fetch(key,null,null,1);

                                                    }




                                                    else{
                                                        Log.e("SizeX4",""+comments.size()+"Going Outside");
                                                        mAdapter.notifyDataSetChanged();

                                                        blogReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if(dataSnapshot.child(key).hasChild("CommentsCount")){
                                                                    CommentsCountX=dataSnapshot.child(key).child("CommentsCount").getValue(Integer.class);
                                                                    CommentsCountX=CommentsCountX+1;
                                                                    dataSnapshot.child(key).child("CommentsCount").getRef().setValue(CommentsCountX);
                                                                    CommentCount.setText(CommentsCountX+" Comments");
                                                                }

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });



                                                        recyclerView.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                float y = recyclerView.getY() + recyclerView.getLayoutManager().getChildAt(comments.size() - 1).getY();

                                                                scrollView.smoothScrollTo(0, (int) y);
                                                                final RecyclerView.ViewHolder holder7 = recyclerView.findViewHolderForLayoutPosition(comments.size()-1);
                                                                final int height = holder7.itemView.findViewById(R.id.cv).getHeight();
                                                                Resources r = BlogActivity.this.getResources();
                                                                int px2 = (int) TypedValue.applyDimension(
                                                                        TypedValue.COMPLEX_UNIT_DIP,
                                                                        20,
                                                                        r.getDisplayMetrics()
                                                                );


                                                                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder7.itemView.findViewById(R.id.background_view).getLayoutParams();
                                                                params.height=height+px2;
                                                                holder7.itemView.findViewById(R.id.background_view).setLayoutParams(params);
                                                                holder7.itemView.findViewById(R.id.background_view).setVisibility(View.VISIBLE);

                                                                Handler mHandler=new Handler();
                                                                mHandler.postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        holder7.itemView.findViewById(R.id.background_view).setVisibility(View.GONE);

                                                                    }
                                                                },1000);





                                                            }
                                                        });


                                                    }







                                                    Toast.makeText(BlogActivity.this, "Comment added successfully", Toast.LENGTH_SHORT).show();

                                                }

                                            }
                                        });

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

            }
        });




    }

    public void getLikeDetails(final FirebaseCallbackXXXX firebaseCallbackXXXX){

        blogReference.child(key).child("LikeStatus").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String LikeStatusX=dataSnapshot.child(user.getUid()).getValue(String.class);

                if(LikeStatusX!=null&&LikeStatusX.equals("true")){
                    DoLikeThings();
                }
                else{

                }

                firebaseCallbackXXXX.onCallBack1(LikeStatusX);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        blogReference.child(key).child("DislikeStatus").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String DislikeStatusX=dataSnapshot.child(user.getUid()).getValue(String.class);
                if(DislikeStatusX!=null&&DislikeStatusX.equals("true")){
                    DoDislikeThings();
                }
                else{

                }
                firebaseCallbackXXXX.onCallBack2(DislikeStatusX);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void getBlogDetails(final FirebaseCallbackXX  firebaseCallbackXX){

        blogReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final String  BlogTextW=dataSnapshot.child(key).child("BlogText").getValue(String.class);
                final String  BlogImageW=dataSnapshot.child(key).child("BlogPic").getValue(String.class);
                final String  UserNameW=dataSnapshot.child(key).child("UserName").getValue(String.class);
                final String  ProfileImageW=dataSnapshot.child(key).child("ProfileImage").getValue(String.class);
                final String  ClubLocationW=dataSnapshot.child(key).child("ClubLocation").getValue(String.class);
                final String UpdatedDayX=dataSnapshot.child(key).child("UpdatedDay").getValue(String.class);
                final String UploadedByX=dataSnapshot.child(key).child("UploadedBy").getValue(String.class);
                final String UpdatedTimeX=dataSnapshot.child(key).child("UpdatedTime").getValue(String.class);
                int like_count;
                if(dataSnapshot.child(key).hasChild("NumberLikes")){
                    like_count=dataSnapshot.child(key).child("NumberLikes").getValue(Integer.class);
                }
                else like_count=0;
                int dislike_count;
                if(dataSnapshot.child(key).hasChild("NumberDislikes")){
                    dislike_count=dataSnapshot.child(key).child("NumberDislikes").getValue(Integer.class);
                }
                else dislike_count=0;


                int comments_countX;
                if(dataSnapshot.child(key).hasChild("CommentsCount")){
                    comments_countX=dataSnapshot.child(key).child("CommentsCount").getValue(Integer.class);
                }
                else comments_countX=0;
                String editedX;
                if(dataSnapshot.child(key).hasChild("Edited")){
                    editedX=dataSnapshot.child(key).child("Edited").getValue(String.class);
                }
                else{
                    editedX="false";
                }

                firebaseCallbackXX.onCallBack1(BlogTextW);
                firebaseCallbackXX.onCallBack2(BlogImageW);
                firebaseCallbackXX.onCallBack3(UserNameW);
                firebaseCallbackXX.onCallBack4(ProfileImageW);
                firebaseCallbackXX.onCallBack5(ClubLocationW);
                firebaseCallbackXX.onCallBack6(comments_countX);
                firebaseCallbackXX.onCallBack7(UploadedByX);
                firebaseCallbackXX.onCallBack8(editedX);

                firebaseCallbackXX.onCallBack10(UpdatedTimeX);
                firebaseCallbackXX.onCallBack9(UpdatedDayX);
                firebaseCallbackXX.onCallBack11(like_count);
                firebaseCallbackXX.onCallBack12(dislike_count);






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void getParticipants(String key7){


        Query query=blogReference.child(key7).child("Participated").limitToLast(5);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot1, @Nullable String s) {
                usersIDS[l]=dataSnapshot1.getKey();
                Log.i("mml",""+usersIDS[l]);
                l=l+1;
                for (int h = 0; h < 5; h++) {
                    final int k=h;
                    if(usersIDS[k]!=null){
                        parentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(usersIDS[k])){
                                    parentRef.child(usersIDS[k]).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                            images[k].setVisibility(View.VISIBLE);
                                            String ImageUrl = dataSnapshot2.child("ImageUrl").getValue(String.class);
                                            Log.i("mml2", "lxx" + l + ImageUrl);
                                            Picasso.get()
                                                    .load(Uri.parse(ImageUrl))

                                                    .into(images[k]);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }
                                else{

                                    adminRef.child(usersIDS[k]).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                            images[k].setVisibility(View.VISIBLE);
                                            String ImageUrl = dataSnapshot2.child("ProfileImageUrl").getValue(String.class);
                                            Log.i("mml2", "lxx" + l + ImageUrl);
                                            Picasso.get()
                                                    .load(Uri.parse(ImageUrl))

                                                    .into(images[k]);
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




        });
    }

    public void ParticipantCount(final firebaseXXX  firebaseXXX2){

        blogReference.child(key).child("Participated").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                participant_countX=participant_countX+1;

                blogReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        firebaseXXX2.onCallBack1(participant_countX);
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
        });





    }



}



