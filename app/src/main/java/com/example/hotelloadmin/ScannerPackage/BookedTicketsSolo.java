package com.example.hotelloadmin.ScannerPackage;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hotelloadmin.R;
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

public class BookedTicketsSolo extends AppCompatActivity  {

    DatabaseReference  eventsReference;
    String TicketKey;
    String TicketName;
    String EventKey;
    LinearLayout LL_parent;
    LinearLayout LL_notickets;
    ArrayList<BookedTicketsObject> booked_tickets_solo;


    private SearchView searchView;

    Toolbar toolbar;

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    int count;

    String AllotedKey;

    int sizeFlag=0;
    String TeamNameC;
    TextView heading_count;
    LinearLayout headings;
    TextView heading_title;



    ChildEventListener cel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_tickets);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i=getIntent();
        TicketKey= i.getStringExtra("TicketKey");
        TicketName= i.getStringExtra("TicketName");
        EventKey= i.getStringExtra("EventKey");
        getSupportActionBar().setTitle(TicketName);

        AllotedKey=i.getStringExtra("AllotedKey");
        TeamNameC=i.getStringExtra("TeamNameC");



        heading_count =findViewById(R.id.heading_count);
        headings=findViewById(R.id.headings);
        heading_title=findViewById(R.id.heading_title);
        count=1;

        eventsReference = FirebaseDatabase.getInstance().getReference().child("Hotels");


        booked_tickets_solo=new ArrayList<BookedTicketsObject>();


        booked_tickets_solo.clear();



        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View promptsView = li.inflate(R.layout.verification_prompt, null);
        final CheckBox checkBox = promptsView.findViewById(R.id.checkBox);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder
                (BookedTicketsSolo.this,R.style.AlertDialogTheme);
        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder
                .setNegativeButton("Got it",null);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        //alertDialog.getWindow().setLayout();
        alertDialog.show();
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    storeDialogStatus(true);
                }else{
                    storeDialogStatus(false);
                }
            }
        });
        if(getDialogStatus()){
            alertDialog.hide();
        }else{
            alertDialog.show();
        }

        LL_parent=(LinearLayout) findViewById(R.id.LL_parent);
        LL_notickets=(LinearLayout) findViewById(R.id.LL_notickets);




        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mAdapter = new MyAdapter(BookedTicketsSolo.this, booked_tickets_solo);
        mLayoutManager = new LinearLayoutManager(BookedTicketsSolo.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);

        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);





      if(AllotedKey==null){



          eventsReference.child(EventKey).child("Tickets").child(TicketKey).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                  String TEAM_CHECK=dataSnapshot.child("TEAM_CHECK").getValue(String.class);

                  if(TEAM_CHECK.equals("Individual")){

                      mRecyclerView.setAdapter(mAdapter);

                      long bookings=dataSnapshot.child("BookedTickets").getChildrenCount();
                      heading_count.setText(""+bookings);
                      if( dataSnapshot.child("BookedTickets").getChildrenCount()==0){
                          ShowNoBookedTickets();
                          alertDialog.hide();
                      }
                      else{
                          RemoveNoBookedTickets();
                      }

                      dataSnapshot.child("BookedTickets").getRef().addChildEventListener(new ChildEventListener() {
                          @Override
                          public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                              String UserName= dataSnapshot.child("UserName").getValue(String.class);
                              String RollNo= dataSnapshot.child("RollNum").getValue(String.class);
                              String ClubLocation= dataSnapshot.child("ClubLocation").getValue(String.class);
                              String ProfileImage= dataSnapshot.child("UserImage").getValue(String.class);
                              String VerificationCheck=dataSnapshot.child("Verified").getValue(String.class);
                              String AllotedKey=dataSnapshot.getKey();
                              String UserUID=dataSnapshot.child("UserUID").getValue(String.class);



                              booked_tickets_solo.add(new BookedTicketsObject(UserName,ClubLocation,RollNo,VerificationCheck,ProfileImage,UserUID,AllotedKey));
                              mAdapter.notifyDataSetChanged();

                          }

                          @Override
                          public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                          }

                          @Override
                          public void onChildRemoved(DataSnapshot dataSnapshot) {

                          }

                          @Override
                          public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                          }

                          @Override
                          public void onCancelled(DatabaseError databaseError) {

                          }
                      });





                      mAdapter.notifyDataSetChanged();

                  }


              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });
      }

      else{

          alertDialog.hide();
          heading_title.setText("Team Members ");
          heading_count.setVisibility(View.INVISIBLE);


          getSupportActionBar().setTitle(TeamNameC);



          mRecyclerView.setAdapter(mAdapter);
          cel=new ChildEventListener() {
              @Override
              public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                  String UserName= dataSnapshot.child("UserName").getValue(String.class);
                  String RollNo= dataSnapshot.child("RollNo").getValue(String.class);
                  String ClubLocation= dataSnapshot.child("ClubLocation").getValue(String.class);
                  String ProfileImage= dataSnapshot.child("UserImage").getValue(String.class);
                  String VerificationCheck=dataSnapshot.child("Verified").getValue(String.class);

                  String UserUID=dataSnapshot.child("UserUID").getValue(String.class);


                  if(UserName!=null){
                      booked_tickets_solo.add(new BookedTicketsObject(UserName,ClubLocation,RollNo,VerificationCheck,ProfileImage,UserUID,AllotedKey));
                      mAdapter.notifyDataSetChanged();
                  }


              }

              @Override
              public void onChildChanged(DataSnapshot dataSnapshot, String s) {

              }

              @Override
              public void onChildRemoved(DataSnapshot dataSnapshot) {

              }

              @Override
              public void onChildMoved(DataSnapshot dataSnapshot, String s) {

              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          };
          eventsReference.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(AllotedKey)
                  .getRef().addChildEventListener(cel);


      }


    }


    @Override
    protected void onDestroy() {
        if(AllotedKey!=null) {
            eventsReference.child(EventKey).child("Tickets").child(TicketKey).child("BookedTickets").child(AllotedKey)
                    .getRef().removeEventListener(cel);
        }
        super.onDestroy();
    }

    private void storeDialogStatus(boolean isChecked){
        SharedPreferences mSharedPreferences = getSharedPreferences("CheckItem", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean("item", isChecked);
        mEditor.apply();
    }

    private boolean getDialogStatus(){
        SharedPreferences mSharedPreferences = getSharedPreferences("CheckItem", MODE_PRIVATE);
        return mSharedPreferences.getBoolean("item", false);
    }


    @Override
    public void onBackPressed() {
        if(LL_notickets.getVisibility()==View.INVISIBLE) {
            if (searchView!=null&& !searchView.isIconified()) {
                searchView.setIconified(true);
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if(LL_notickets.getVisibility()==View.INVISIBLE) {
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


















    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable{


        private List<BookedTicketsObject> participantsX;
        List<BookedTicketsObject> participantsXFiltered;
        private Context context;


        public MyAdapter(Context context, List<BookedTicketsObject> participantsX) {
            this.context=context;
            this.participantsX=participantsX;
            this.participantsXFiltered=participantsX;

        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.solo_buyers,parent,false);
            MyViewHolder vh = new  MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

          final  BookedTicketsObject c= participantsXFiltered.get(i);

            holder.UserName.setText(c.getUserName());
            holder.UserClubLocation.setText(c.getClubLocation());
            holder.LL_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(BookedTicketsSolo.this,ScannerActivity.class);
                    intent.putExtra("EventKey",EventKey);
                    intent.putExtra("TicketKey",TicketKey);
                    intent.putExtra("UserUID",c.getUserUID());
                    intent.putExtra("AllotedKey",c.getAllotedKey());
                    intent.putExtra("Purpose","BookedPeople");
                    startActivity(intent);

                }
            });

            Picasso.get()
                    .load(c.getProfileImage())
                    .into(holder.ProfileImage);
            holder.RollNum.setText(c.getRollNum());

            count++;
            if(c.getVerified()!=null&&c.getVerified().equals("true")){
                holder.isVerified.setText("✔");
            }
            else{
                holder.isVerified.setText("❌");
            }

        }

        @Override
        public int getItemCount() {
            return participantsXFiltered.size();
        }


        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        participantsXFiltered = participantsX;
                    }
                    else {
                        List<BookedTicketsObject> filteredList = new ArrayList<>();
                        for (BookedTicketsObject row : participantsX) {

                            if (row.getUserName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }

                        }
                        participantsXFiltered = filteredList;

                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = participantsXFiltered;
                    filterResults.count=participantsXFiltered.size();


                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    participantsXFiltered = (List<BookedTicketsObject>)filterResults.values;
                    if(filterResults.count==0){
                        sizeFlag=0;
                    }else{
                        sizeFlag=1;
                    }

                    notifyDataSetChanged();
                }

            };
        }


        public class MyViewHolder extends RecyclerView.ViewHolder{

            TextView UserName;
            TextView  UserClubLocation;
            CircleImageView ProfileImage;
            TextView RollNum;
            TextView isVerified;
            LinearLayout LL_parent;
            // TextView Activity;


            public MyViewHolder(@NonNull View itemView) {


                super(itemView);

                UserName=(TextView)itemView.findViewById(R.id.username);
                RollNum=(TextView) itemView.findViewById(R.id.rollnum);
                isVerified=(TextView) itemView.findViewById(R.id.check_verification);
                LL_parent=(LinearLayout) itemView.findViewById(R.id.LL_parent);
                UserClubLocation=(TextView)itemView.findViewById(R.id.club_location);
                ProfileImage=(CircleImageView) itemView.findViewById(R.id.profile_image);




            }
        }



    }




    public void ShowNoBookedTickets(){

        LL_parent.setVisibility(View.INVISIBLE);
        LL_notickets.setVisibility(View.VISIBLE);

    }

    public void RemoveNoBookedTickets(){
        LL_parent.setVisibility(View.VISIBLE);
        LL_notickets.setVisibility(View.INVISIBLE);

    }
}
