package com.example.hotelloadmin.EventsPackage;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hotelloadmin.R;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
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
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventDetails extends AppCompatActivity {

    KenBurnsView EventBannerView;

    String EventKey;
    TextView EventName;
    TextView EventDetail;
    String EventBannerURL;
    TextView Location;
    ProgressBar progressBar2;
    ProgressBar progress_bar;


    //TextView InterestedCountT;
    TextView Edit;
    String EventBannerURLX;
    TextView PriceSegmentTV;
    //CircleImageView InterestedPhoto1;
    //CircleImageView InterestedPhoto2;
    ImageView FAQButton;
    DatabaseReference parentReference;
    DatabaseReference eventReference;
    DatabaseReference adminReference;
    ImageView TicketsButton;
    String thiseventurl;
    ImageView OrganiserButton;

    FirebaseUser user;


    SharedPreferences myPrefs2;
    SharedPreferences.Editor prefsEditor2;

    RelativeLayout parent_view;



    ImageView share;


    ScrollView scrollView;
    FrameLayout event_banner_container;
    Toolbar toolbar;
    Button interested;


    String[] MyImageURL;
    //long[] InterestedCount;
    String URLChecker;

    //String InterestedCountInit;
    //String InterestedCountFinal;
    int position=-2;

    //TextView InterestedTextView;
    //FrameLayout InterestedPhotosFL;
    String EventNameX;

    CardView cv_ticket;
    CardView cv_faq;
    CardView cv_organiser;




    String EventNameS;
    String EventDetailS;















    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);


        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        scrollView = findViewById(R.id.scrollView);
        EventBannerView =  findViewById(R.id.event_banner);

        RandomTransitionGenerator generator = new RandomTransitionGenerator(5000,new AccelerateDecelerateInterpolator());
        EventBannerView.setTransitionGenerator(generator);


        event_banner_container=findViewById(R.id.event_banner_container);
        parent_view=(RelativeLayout) findViewById(R.id.parent_view);

        progressBar2=(ProgressBar) findViewById(R.id.progress_bar2);
        PriceSegmentTV=(TextView) findViewById(R.id.price_segment);
        Edit=(TextView)findViewById(R.id.edit);
        interested=findViewById(R.id.interested);

        user = FirebaseAuth.getInstance().getCurrentUser();
        EventDetail = (TextView) findViewById(R.id.event_details);
        EventName = (TextView) findViewById(R.id.event_name);
        Location = (TextView) findViewById(R.id.location);


        //InterestedCountT = (TextView) findViewById(R.id.interested_count);
        //InterestedPhoto1 = (CircleImageView) findViewById(R.id.interested_photo1);
        //InterestedPhoto2 = (CircleImageView) findViewById(R.id.interested_photo2);

        FAQButton = (ImageView) findViewById(R.id.faq_button);
        TicketsButton = (ImageView) findViewById(R.id.tickets_button);
        OrganiserButton = (ImageView) findViewById(R.id.organisers_button);
        cv_faq=findViewById(R.id.faq_cardview);
        cv_ticket=findViewById(R.id.tickets_card_view);
        cv_organiser=findViewById(R.id.org_cardview);

        progress_bar=(ProgressBar) findViewById(R.id.progress_bar);


        share = (ImageView) findViewById(R.id.share_event);


        //InterestedTextView=(TextView)findViewById(R.id.interested_tv);
        //InterestedPhotosFL=(FrameLayout)findViewById(R.id.interested_photos);
        myPrefs2 = this.getSharedPreferences("myPrefs2", MODE_PRIVATE);
        prefsEditor2 = myPrefs2.edit();



        MyImageURL = new String[1];



        /*Resources r = EventDetails.this.getResources();
        px9 = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                30,
                r.getDisplayMetrics()
        );*/



        parentReference = FirebaseDatabase.getInstance().getReference().child("HotelLo");
        eventReference = FirebaseDatabase.getInstance().getReference().child("Hotels");
        adminReference = FirebaseDatabase.getInstance().getReference().child("HotelLoAdmin");



        Intent i = getIntent();
        EventKey = i.getStringExtra("Key");


        Log.e("EventKey",""+EventKey);


        URLChecker="false";
        position=i.getIntExtra("position",-2);
        //getEventURL();
        fetch();



        share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);


                    String uri="http://hungryhunter96.github.io/share_event?Key="+EventKey;


                    Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                            .setLink(Uri.parse(uri))
                            //.setDomainUriPrefix("https://ddxradmin.page.link")
                            .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())

                            .buildShortDynamicLink()
                            .addOnCompleteListener(EventDetails.this, new OnCompleteListener<ShortDynamicLink>() {
                                @Override
                                public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                    if (task.isSuccessful()) {
                                      //  progress_edit_delete.setVisibility(View.GONE);

                                       // Toast.makeText(EventDetails.this,"Link Copied",Toast.LENGTH_SHORT).show();

                                        Uri shortLink = task.getResult().getShortLink();
                                         thiseventurl=shortLink.toString();
                                        sendIntent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                                        sendIntent.setType("text/plain");
                                        startActivity(sendIntent);
                                        /*Uri flowchartLink = task.getResult().getPreviewLink();

                                        ClipboardManager clipboard = (ClipboardManager) EventDetails.this.getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText(null, shortLink.toString());
                                        if (clipboard == null) return;
                                        clipboard.setPrimaryClip(clip);*/

                                    } else {

                                        Toast.makeText(EventDetails.this,"Link Copy Failure",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });








            }
        });





        /*Edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //openOptionsMenu();
                PopupMenu popup = new PopupMenu(EventDetails.this, Edit);
                popup.getMenuInflater().inflate(R.menu.edit_event, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();


                        //noinspection SimplifiableIfStatement
                        if (id == R.id.action_edit) {
                            Intent i=new Intent(EventDetails.this,EditEventDetails.class);
                            i.putExtra("EventKey",EventKey);

                            startActivity(i);
                            finish();

                            return true;

                        } else if (id == R.id.action_edit_faq) {

                            Intent i=new Intent(EventDetails.this,EditEventFAQs.class);
                            i.putExtra("EventKey",EventKey);
                            startActivity(i);
                            finish();
                            //startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                            return true;
                        } else if (id == R.id.action_edit_org) {

                            Intent i=new Intent(EventDetails.this,EditEventOrganisers.class);
                            i.putExtra("EventKey",EventKey);
                            startActivity(i);
                            finish();
                            return true;
                        } else if (id == R.id.action_edit_ticket) {
                            Intent i=new Intent(EventDetails.this,EditEventTickets.class);
                            i.putExtra("EventKey",EventKey);
                            startActivity(i);
                            finish();
                            return true;
                        } else if (id == R.id.action_edit_banner) {
                            Intent i=new Intent(EventDetails.this,EditEventBanner.class);
                            i.putExtra("EventKey",EventKey);
                            startActivity(i);
                            finish();
                            return true;
                        }
                        return true;
                    }
                });

                popup.show();
            }

        });*/




        FAQButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(EventDetails.this,EventDetailsFAQsActivity.class);
                i.putExtra("EventKey",EventKey);
                startActivity(i);
            }
        });

        cv_faq.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(EventDetails.this,EventDetailsFAQsActivity.class);
                i.putExtra("EventKey",EventKey);
                startActivity(i);

            }
        });


        OrganiserButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(EventDetails.this,EventDetailsOrganisersActivity.class);
                i.putExtra("EventKey",EventKey);
                startActivity(i);
            }
        });

        cv_organiser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(EventDetails.this,EventDetailsOrganisersActivity.class);
                i.putExtra("EventKey",EventKey);
                startActivity(i);

            }
        });

        TicketsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(EventDetails.this,EventDetailsTicketsActivity.class);
                i.putExtra("EventKey",EventKey);
                startActivity(i);
            }
        });

        cv_ticket.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(EventDetails.this,EventDetailsTicketsActivity.class);
                i.putExtra("EventKey",EventKey);
                startActivity(i);

            }
        });


        /*// ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();*/

        interested.setEnabled(false);
        interested.setBackgroundColor(Color.parseColor("#E3E3E3"));
        interested.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });


        event_banner_container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(EventDetails.this,EventDetailsPostersActivity.class);
                i.putExtra("EventKey",EventKey);
                i.putExtra("EventName",EventNameS);
                startActivity(i);
            }
        });
        EventBannerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(EventDetails.this,EventDetailsPostersActivity.class);
                i.putExtra("EventKey",EventKey);
                i.putExtra("EventName",EventNameS);
                startActivity(i);
            }
        });




    }






    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_event, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            Intent i=new Intent(EventDetails.this,EditEventDetails.class);
            i.putExtra("EventKey",EventKey);
            i.putExtra("EventName",EventNameS);
            i.putExtra("EventBannerURL",EventBannerURL);
            i.putExtra("EventDescription",EventDetailS);
            i.putExtra("StartDate",start_date);
            i.putExtra("StartTime",start_time);
            i.putExtra("EndDate",end_date);
            i.putExtra("EndTime",end_time);
            i.putExtra("LastDate",last_date);
            startActivity(i);
            return true;

        } else if (id == R.id.action_edit_faq) {

            //startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        } else if (id == R.id.action_edit_org) {

            return true;
        } else if (id == R.id.action_edit_ticket) {

            return true;
        } else if (id == R.id.action_bug) {
            Toast.makeText(EventDetails.this, "Stay Tuned. Coming Soon",
                    Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/







/*    public void getInterestedDetails(final FirebaseCallbackXXXXX firebaseCallbackXXXX){

        eventReference.child(EventKey).child("Interested").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             String bookmarkS=   dataSnapshot.child(user.getUid()).child("Bookmark").getValue(String.class);
             String likeS=dataSnapshot.child(user.getUid()).child("Like").getValue(String.class);
             long interested_countS=dataSnapshot.getChildrenCount();

                Log.e("Like2",""+Like);
                Log.e("Bookmark2",""+Bookmark);

             firebaseCallbackXXXX.onCallBack1(bookmarkS);
             firebaseCallbackXXXX.onCallBack2(likeS);
                firebaseCallbackXXXX.onCallBack3(interested_countS);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getProfileImage(final FirebaseCallbackX firebaseCallbackX){

        final String[] MyImageURLS = new String[1];

        parentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user.getUid())){
                    MyImageURLS[0] =dataSnapshot.child(user.getUid()).child("ImageUrl").getValue(String.class);
                    Log.e("url2","hii"+MyImageURLS[0]);
                    firebaseCallbackX.onCallBack(MyImageURLS[0]);
                }
                else{

                    Log.e("url2","hii"+MyImageURLS[0]);

                    adminReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(user.getUid())){
                                MyImageURLS[0] =dataSnapshot.child(user.getUid()).child("ProfileImageUrl").getValue(String.class);
                                firebaseCallbackX.onCallBack(MyImageURLS[0]);
                            }

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








    }*/


    @Override
    public void onBackPressed() {

        /*if(URLChecker.equals("true")){}*/
        prefsEditor2.putString("Hii","Hii");
        prefsEditor2.putInt("EventPos",position);
        //prefsEditor2.putString("InterestedCountInit",InterestedCountInit);
        //prefsEditor2.putString("InterestedCountFinal",InterestedCountFinal);
        prefsEditor2.putString("MyImageURL",MyImageURL[0]);

        prefsEditor2.commit();

        /*Intent i=new Intent(EventDetails.this, EventsHistoryActivity.class);
        startActivity(i);*/
        super.onBackPressed();
    }


    public void fetch(){

        getEventImageURL(new FirebaseCallbackXXXX() {
            @Override
            public void onCallBack1(String string) {
                EventBannerURLX=string;


            }

            @Override
            public void onCallBack2(String string) {
                EventNameX=string;

            }


        });

        /*getProfileImage(new FirebaseCallbackX() {
            @Override
            public void onCallBack(String string) {

                MyImageURL[0] = string;
                Log.e("url", "" + MyImageURL[0]);

            }


        });

        getInterestedDetails(new FirebaseCallbackXXXXX() {
            @Override
            public void onCallBack1(String string) {

                Bookmark = string;


            }

            @Override
            public void onCallBack2(String string) {

                Like = string;

            }

            @Override
            public void onCallBack3(long string) {

                InterestedCount[0] = string;
                InterestedCountInit=String.valueOf(string);

            }
        });*/

        eventReference.child(EventKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                EventBannerURL = dataSnapshot.child("EventThumbnailURL").getValue(String.class);
                EventNameS = dataSnapshot.child("EventName").getValue(String.class);
                EventDetailS = dataSnapshot.child("EventDescription").getValue(String.class);



                String InterestedPhoto1S = dataSnapshot.child("InterestedPerson1").getValue(String.class);
                String InterestedPhoto2S = dataSnapshot.child("InterestedPerson2").getValue(String.class);




                long InterestedCount = dataSnapshot.child("Interested").getChildrenCount();
                int StartingPrice;
                int EndingPrice;
                String PriceSegment=null;
                if(dataSnapshot.hasChild("StartingPrice")){
                    StartingPrice=dataSnapshot.child("StartingPrice").getValue(Integer.class);
                }
                else{
                    StartingPrice=0;
                }
                if(dataSnapshot.hasChild("EndingPrice")){
                    EndingPrice=dataSnapshot.child("EndingPrice").getValue(Integer.class);
                }
                else{
                    EndingPrice=0;
                }

                if(StartingPrice==0&&EndingPrice==0){

                    PriceSegment="Free";

                }
                else if(StartingPrice!=0&&StartingPrice!=EndingPrice){
                    PriceSegment="₹ "+String.valueOf(StartingPrice)+" - "+String.valueOf(EndingPrice);
                }
                else if(StartingPrice==0&&EndingPrice!=0){
                    PriceSegment="Upto ₹ "+String.valueOf(EndingPrice);
                }
                else if(StartingPrice==EndingPrice&&StartingPrice!=0){
                    PriceSegment="₹ "+String.valueOf(StartingPrice);
                }

                PriceSegmentTV.setText(PriceSegment);



                Location.setText(dataSnapshot.child("EventAddress").getValue(String.class));









                EventDetail.setText(EventDetailS);
                //InterestedCountT.setText("" + InterestedCount);
                EventName.setText(EventNameS);
                /*final Transformation transformation = new Transformation() {


                    @Override
                    public Bitmap transform(Bitmap source) {
                        int targetWidth;
                        int targetHeight;



                        double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                        if (aspectRatio >= 1.0) {

                            Log.e("HEE", "HEE" + aspectRatio);
                            Resources r = EventDetails.this.getResources();
                            final int px = (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    60,
                                    r.getDisplayMetrics()
                            );

                            final int px2 = (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP,
                                    15,
                                    r.getDisplayMetrics()
                            );


                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    // Stuff that updates the UI
                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) EventBannerView.getLayoutParams();
                                    params.setMargins(px, px2, px, px2);
                                    EventBannerView.setLayoutParams(params);

                                }
                            });


                        }


                        targetWidth = EventBannerView.getWidth();


                        targetHeight = (int) (targetWidth * aspectRatio);
                        Log.e("Height1", "h " + targetHeight);


                        Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                        if (result != source) {

                            source.recycle();
                        }
                        return result;


                    }

                    @Override
                    public String key() {
                        return "transformation" + " desiredHeight";
                    }
                };*/


                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        Picasso.get()
                                .load(Uri.parse(EventBannerURL))
                                //.transform(transformation)
                                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                .into(EventBannerView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        progress_bar.setVisibility(View.GONE);
                                        parent_view.setVisibility(View.VISIBLE);
                                        share.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onError(Exception e) {

                                    }
                                });

                    }
                });

                /*if (InterestedPhoto1S != null) {
                    InterestedPhoto1.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(Uri.parse(InterestedPhoto1S))

                            .into(InterestedPhoto1);
                } else {
                    InterestedPhoto1.setVisibility(View.GONE);
                    InterestedPhoto2.setVisibility(View.GONE);
                }
                if (InterestedPhoto2S != null) {
                    InterestedPhoto1.setVisibility(View.VISIBLE);
                    InterestedPhoto2.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(Uri.parse(InterestedPhoto2S))

                            .into(InterestedPhoto2);
                } else {
                    InterestedPhoto2.setVisibility(View.GONE);
                }*/


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







    }




    private String generateString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }


    public void getEventURL(){


        String uri="http://hungryhunter96.github.io/share_event?Key="+EventKey;


        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(uri))
                //.setDomainUriPrefix("https://ddxr.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())

                .buildShortDynamicLink()
                .addOnCompleteListener(EventDetails.this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {


                            Uri shortLink = task.getResult().getShortLink();
                            thiseventurl=shortLink.toString();


                        } else {

                            Toast.makeText(EventDetails.this,"Event link cannot be created.",Toast.LENGTH_SHORT).show();

                        }
                    }
                });



    }



    public void getEventImageURL(final FirebaseCallbackXXXX firebaseCallbackX){

        adminReference.child(user.getUid()).child("Events").child(EventKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String EventBannerURL=dataSnapshot.child("EventThumbnailURL").getValue(String.class);
                String EventName=dataSnapshot.child("EventName").getValue(String.class);
                firebaseCallbackX.onCallBack1(EventBannerURL);
                firebaseCallbackX.onCallBack2(EventName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }












}
