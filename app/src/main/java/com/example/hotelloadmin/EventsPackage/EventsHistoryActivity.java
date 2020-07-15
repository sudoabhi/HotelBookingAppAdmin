package com.example.hotelloadmin.EventsPackage;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hotelloadmin.CommonPackage.AppFAQsActivity;
import com.example.hotelloadmin.CommonPackage.ContactUsActivity;
import com.example.hotelloadmin.CommonPackage.FeedbackActivity;
import com.example.hotelloadmin.DiscussionsPackage.BlogFeedActivity;

import com.example.hotelloadmin.NewEventPackage.NewEventProcessDescription;

import com.example.hotelloadmin.PaymentsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.hotelloadmin.R;

import java.util.ArrayList;
import java.util.List;

public class EventsHistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView mRecyclerView;
    private EventsHistoryAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    FirebaseDatabase admin_database;
    DatabaseReference admin_reference;
    FirebaseUser user;

    List<EventsHistoryData> posts;

    String EventName;

    String EventKey;
    String EventBannerUrl;
    String uploadedUserUid;
    String EventType;

    private SearchView searchView;

    TextView no_event;
    Button create_new;
    TextView or_tv;
    Button contact_us;
    TextView awesome_event;

    ChildEventListener childEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        no_event=findViewById(R.id.no_event_found);
        create_new=findViewById(R.id.create_new);
        awesome_event=findViewById(R.id.awesome_event_name);
        or_tv=findViewById(R.id.or_tv);
        contact_us=findViewById(R.id.contact_us);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //
        user = FirebaseAuth.getInstance().getCurrentUser();
        //  FirebaseDatabase.getInstance("https://ddrx-172d3-64f25.firebaseio.com/").setPersistenceEnabled(true);
        admin_database = FirebaseDatabase.getInstance();

        admin_reference = admin_database.getReference().child("HotelLoAdmin");
        admin_reference= admin_reference.child(user.getUid()).child("Events");

        /*event_node_reference = admin_reference.push();
        key = event_node_reference.getKey();*/

        posts = new ArrayList<>();
        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        // mLayoutManager.setReverseLayout(true);
        //  mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        mAdapter = new EventsHistoryAdapter(this, posts);
        mRecyclerView.setAdapter(mAdapter);

        /*if(mAdapter.getItemCount()!=0){
            no_event.setVisibility(View.GONE);
            create_new.setVisibility(View.GONE);
            awesome_event.setVisibility(View.GONE);
            or_tv.setVisibility(View.GONE);
            contact_us.setVisibility(View.GONE);
        }
        else{
            no_event.setVisibility(View.VISIBLE);
            create_new.setVisibility(View.VISIBLE);
            awesome_event.setVisibility(View.VISIBLE);
            or_tv.setVisibility(View.VISIBLE);
            contact_us.setVisibility(View.VISIBLE);
        }*/


        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                EventName = dataSnapshot.child("EventName").getValue(String.class);

                EventBannerUrl = dataSnapshot.child("EventThumbnailURL").getValue(String.class);
                uploadedUserUid=dataSnapshot.child("UploadedBy").getValue(String.class);


                int StartingPrice;
                int EndingPrice;
                String PriceSegment=null;

                String uploadDate=dataSnapshot.child("UploadedDate").getValue(String.class);

                String end_date = dataSnapshot.child("EndDate").getValue(String.class);

                String start_date = dataSnapshot.child("StartDate").getValue(String.class);

                final String dates = start_date + " - " + end_date;


                final String EventTypeX=dataSnapshot.child("EventTypeX").getValue(String.class);

                final String EventLocation=dataSnapshot.child("EventLocation").getValue(String.class);

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
                    PriceSegment=String.valueOf(StartingPrice)+" - "+String.valueOf(EndingPrice);
                }
                else if(StartingPrice==0&&EndingPrice!=0){
                    PriceSegment="Upto "+String.valueOf(EndingPrice);
                }
                else if(StartingPrice==EndingPrice&&StartingPrice!=0){
                    PriceSegment=String.valueOf(StartingPrice);
                }






                EventType = dataSnapshot.child("EventType").getValue(String.class);

                EventKey = dataSnapshot.child("EventKey").getValue(String.class);

                if (EventBannerUrl != null&&uploadedUserUid.equals(user.getUid())) {

                    posts.add(0,new EventsHistoryData( EventName, EventBannerUrl,null,start_date, end_date,
                            uploadDate,uploadedUserUid,EventKey,EventTypeX,EventLocation,EventType, PriceSegment));

                    mAdapter.notifyDataSetChanged();
                    if(posts.size()!=0){
                        no_event.setVisibility(View.GONE);
                        create_new.setVisibility(View.GONE);
                        awesome_event.setVisibility(View.GONE);
                        or_tv.setVisibility(View.GONE);
                        contact_us.setVisibility(View.GONE);
                    }
                    else{
                        no_event.setVisibility(View.VISIBLE);
                        create_new.setVisibility(View.VISIBLE);
                        awesome_event.setVisibility(View.VISIBLE);
                        or_tv.setVisibility(View.VISIBLE);
                        contact_us.setVisibility(View.VISIBLE);
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
        };

        admin_reference.addChildEventListener(childEventListener);




        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("Flag"));

        if(posts.size()==0){
            awesome_event.setText("Jeez man! No event listed yet!");
            no_event.setText("You're boring. Meh!\nOr be awesome and create your first event!");

            no_event.setVisibility(View.VISIBLE);
            create_new.setVisibility(View.VISIBLE);
            awesome_event.setVisibility(View.VISIBLE);
            or_tv.setVisibility(View.VISIBLE);
            contact_us.setVisibility(View.VISIBLE);
        }
        else{
            no_event.setVisibility(View.GONE);
            create_new.setVisibility(View.GONE);
            awesome_event.setVisibility(View.GONE);
            or_tv.setVisibility(View.GONE);
            contact_us.setVisibility(View.GONE);

        }

        create_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventsHistoryActivity.this, NewEventProcessDescription.class));
                finish();
            }
        });

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do
                startActivity(new Intent(getApplicationContext(), ContactUsActivity.class));
            }
        });


    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            int flag = intent.getIntExtra("FlagValue",0);
            Log.e("flagvv",""+flag);
            if(flag!=0){
                no_event.setVisibility(View.GONE);
                create_new.setVisibility(View.GONE);
                awesome_event.setVisibility(View.GONE);
                or_tv.setVisibility(View.GONE);
                contact_us.setVisibility(View.GONE);
            }
            else{
                awesome_event.setText("It's an awesome event name!");
                no_event.setText("But we can't seem to find one in your list. :(\nCreate Now!");
                no_event.setVisibility(View.VISIBLE);
                create_new.setVisibility(View.VISIBLE);
                awesome_event.setVisibility(View.VISIBLE);
                or_tv.setVisibility(View.VISIBLE);
                contact_us.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(posts.size()==0) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

        if(posts.size()!=0) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            else if (searchView!=null&& !searchView.isIconified()) {
                searchView.setIconified(true);
            }else if(searchView!=null&& !searchView.isIconified()&&drawer.isDrawerOpen(GravityCompat.START)){
                drawer.closeDrawer(GravityCompat.START);
            }
            else {
                super.onBackPressed();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(posts.size()!=0) {
            getMenuInflater().inflate(R.menu.search, menu);

            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);

            // listening to search query text change
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // filter recycler view when query submitted
                    //mAdapter.getFilter().filter(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    // filter recycler view when text is changed
                    mAdapter.getFilter().filter(query);


                    return true;
                }
            });

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            finish();

        } else if (id == R.id.posts) {
            startActivity(new Intent(getApplicationContext(), BlogFeedActivity.class));
            finish();

        } else if (id == R.id.nav_create) {
            startActivity(new Intent(getApplicationContext(), NewEventProcessDescription.class));
            //finish();

        } else if (id == R.id.nav_notif) {



        } else if (id == R.id.nav_payment) {
            startActivity(new Intent(getApplicationContext(), PaymentsActivity.class));
            finish();

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


    /*private void readData(final FirebaseCallback firebasecallback) {
        ValueEventListener singleValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String  CollegeNameS = dataSnapshot.child("CollegeName").getValue(String.class);
                String ClubNameS = dataSnapshot.child("ClubName").getValue(String.class);
                String ClubImageUrlS=dataSnapshot.child("ClubImageUrl").getValue(String.class);

                firebasecallback.onCallBack(CollegeNameS);
                firebasecallback.onCallBacktwo(ClubNameS);
                firebasecallback.onCallBackthree(ClubImageUrlS);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        admin_reference.addListenerForSingleValueEvent(singleValueEventListener);
    }*/

    /*  private void readData(final FirebaseCallback firebasecallback) {
        final CountDownLatch done = new CountDownLatch(1);
        final String message[] = {null};

        ValueEventListener singleValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String  CollegeNameS = dataSnapshot.child("CollegeName").getValue(String.class);
                String ClubNameS = dataSnapshot.child("ClubName").getValue(String.class);
                String ClubImageUrlS=dataSnapshot.child("ClubImageUrl").getValue(String.class);

                firebasecallback.onCallBack(CollegeNameS);
                firebasecallback.onCallBacktwo(ClubNameS);
                firebasecallback.onCallBackthree(ClubImageUrlS);


                System.out.println("worked");
                done.countDown();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        try{
            done.await();
        }
        catch(InterruptedException e)
        {

        }
         //it will wait till the response is received from firebase.


        admin_reference.addListenerForSingleValueEvent(singleValueEventListener);
    }*/





 /* private void loadEvent()
  {

      admin_reference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              CollegeName = dataSnapshot.child("EventName").getValue(String.class);
              ClubName = dataSnapshot.child("ClubName").getValue(String.class);
               Club_Image_Url=dataSnapshot.child("ClubImageUrl").getValue(String.class);


              // Toast.makeText(getActivity(),""+CollegeName,Toast.LENGTH_SHORT).show();
              MyObject o = new MyObject();
              //CALL FOR METHOD nextMethod()
              bus.post(o);


          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });


  }
  */


    @Override
    protected void onStop() {
        Log.e("Onstop","Run");
        admin_reference.removeEventListener(childEventListener);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e("Ondestroy","Run");
        admin_reference.removeEventListener(childEventListener);
        super.onDestroy();
    }
}
