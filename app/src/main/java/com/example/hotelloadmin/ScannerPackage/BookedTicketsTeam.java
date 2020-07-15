package com.example.hotelloadmin.ScannerPackage;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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

public class BookedTicketsTeam extends AppCompatActivity {

    DatabaseReference  eventsReference;
    String TicketKey;
    String TicketName;
    String EventKey;
    LinearLayout LL_parent;
    LinearLayout LL_notickets;

    ArrayList<BookedTicketsObjectTeam> booked_tickets_team;

    Toolbar toolbar;

    private RecyclerView mRecyclerView;

    private LinearLayoutManager mLayoutManager;


    private MyAdapter2 mAdapter2;

    int count;
    int sizeFlag=0;
    private SearchView searchView;


    TextView heading_count;
    LinearLayout headings;
    TextView heading_title;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_tickets2);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent i=getIntent();
        TicketKey= i.getStringExtra("TicketKey");
        EventKey= i.getStringExtra("EventKey");
        TicketName= i.getStringExtra("TicketName");

        getSupportActionBar().setTitle(TicketName);


        heading_count =findViewById(R.id.heading_count);
        headings=findViewById(R.id.headings);
        heading_title=findViewById(R.id.heading_title);
        count=1;

        eventsReference = FirebaseDatabase.getInstance().getReference().child("Hotels");



        booked_tickets_team=new ArrayList<BookedTicketsObjectTeam>();


        booked_tickets_team.clear();


        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View promptsView = li.inflate(R.layout.verification_prompt, null);
        final CheckBox checkBox = promptsView.findViewById(R.id.checkBox);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder
                (BookedTicketsTeam.this,R.style.AlertDialogTheme);
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






        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
         LL_parent=(LinearLayout) findViewById(R.id.LL_parent);
         LL_notickets=(LinearLayout) findViewById(R.id.LL_notickets);

        mAdapter2 = new MyAdapter2(BookedTicketsTeam.this, booked_tickets_team);
        mLayoutManager = new LinearLayoutManager(BookedTicketsTeam.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);

        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);








          eventsReference.child(EventKey).child("Tickets").child(TicketKey).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                  String TEAM_CHECK=dataSnapshot.child("TEAM_CHECK").getValue(String.class);

                  if(TEAM_CHECK.equals("Team")){

                      mRecyclerView.setAdapter(mAdapter2);
                      Log.e("xx","xx");

                      long bookings=dataSnapshot.child("BookedTickets").getChildrenCount();
                      heading_count.setText(""+bookings);

                      if( dataSnapshot.child("BookedTickets").getChildrenCount()==0){
                          ShowNoBookedTickets();

                      }
                      else{
                          RemoveNoBookedTickets();
                      }



                      dataSnapshot.child("BookedTickets").getRef().addChildEventListener(new ChildEventListener() {
                          @Override
                          public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                              String TeamName= dataSnapshot.child("TeamName").getValue(String.class);
                              String AllotedKey=dataSnapshot.getKey();
                              String Verified;
                              long TeamMembersCount=dataSnapshot.getChildrenCount();
                              if(dataSnapshot.hasChild("Verified")){
                                  TeamMembersCount=TeamMembersCount-2;
                              }
                              else{
                                  TeamMembersCount=TeamMembersCount-1;
                              }

                              if(dataSnapshot.hasChild("Verified")){
                                  Verified=dataSnapshot.child("Verified").getValue(String.class);
                              }
                              else{
                                  Verified="false";
                              }




                              booked_tickets_team.add(new BookedTicketsObjectTeam(TeamName,Verified,TeamMembersCount,AllotedKey));
                              mAdapter2.notifyDataSetChanged();


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
                      Log.e("xx","yy");
                      mAdapter2.notifyDataSetChanged();



                  }


              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });
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
                    mAdapter2.getFilter().filter(query);


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


















    class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder> implements Filterable {


        private List<BookedTicketsObjectTeam> participantsX;
        List<BookedTicketsObjectTeam> participantsXFiltered;
        private Context context;

        public MyAdapter2(Context context, List<BookedTicketsObjectTeam> participantsX) {
            this.context=context;
            this.participantsX=participantsX;
            this.participantsXFiltered=participantsX;
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.team_buyers,parent,false);
            MyViewHolder vh = new  MyViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

          final  BookedTicketsObjectTeam c= participantsXFiltered.get(i);

            holder.TeamName.setText(c.getTeamName());

            holder.TeamMembersCount.setText(""+c.getTeamMembersCount());
            count++;

            holder.parent_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(BookedTicketsTeam.this, BookedTicketsSolo.class);
                    intent.putExtra("AllotedKey",c.getAllotedKey());
                    intent.putExtra("EventKey",EventKey);
                    intent.putExtra("TicketKey",TicketKey);
                    intent.putExtra("TEAM_CHECK","Individual");
                    intent.putExtra("TeamNameC",c.getTeamName());
                    intent.putExtra("Purpose","BookedPeople");
                    startActivity(intent);

                }
            });





            if(c.getVerified().equals("true")){
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
                        List<BookedTicketsObjectTeam> filteredList = new ArrayList<>();
                        for (BookedTicketsObjectTeam row : participantsX) {

                            if (row.getTeamName().toLowerCase().contains(charString.toLowerCase())) {
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
                    participantsXFiltered = (List<BookedTicketsObjectTeam>)filterResults.values;
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

            TextView TeamName;
            TextView isVerified;

            TextView TeamMembersCount;
            LinearLayout parent_click;
            // TextView Activity;


            public MyViewHolder(@NonNull View itemView) {


                super(itemView);


                TeamName=(TextView)itemView.findViewById(R.id.teamname);
                isVerified=(TextView)itemView.findViewById(R.id.check_verification);

                 TeamMembersCount=(TextView)itemView.findViewById(R.id.team_count);
                 parent_click=(LinearLayout)itemView.findViewById(R.id.parent_click);

                //  Activity=(TextView)itemView.findViewById(R.id.activity);



            }
        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
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
