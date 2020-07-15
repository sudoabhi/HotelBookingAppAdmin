package com.example.hotelloadmin.EventsPackage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hotelloadmin.NewEventPackage.EventTicketsRecyclerData;
import com.example.hotelloadmin.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditEventTickets extends AppCompatActivity {


        Button add_ticket;
        TextInputEditText name;


        TextInputEditText category;
        TextInputEditText description;
        EditText price;


        ProgressBar progressBar;


        ArrayList<String> nameS=new ArrayList<String>();
        ArrayList<String> categoryS=new ArrayList<String>();
        ArrayList<String> descriptionS=new ArrayList<String>();
        ArrayList<String> priceS=new ArrayList<String>();
        ArrayList<String> typeS=new ArrayList<String>();


        ArrayList<String> settingS=new ArrayList<String>();
        ArrayList<String> TicketKey=new ArrayList<String>();

        LinearLayout fees_settings;
        LinearLayout big_layout;

        TextView next;

        LinearLayout inner_layout;

    FirebaseUser user;
    FirebaseDatabase eventsDatabase;
    DatabaseReference eventsReference;
    DatabaseReference newEventsReference;
    FirebaseDatabase adminDatabase;
    DatabaseReference adminReference;

    String EventKey;


    ArrayList<String> ticket_key2=new ArrayList<String>();

    ChildEventListener cel;
    long[] ticketnum=new long[1];


        String ticket_type;
        String ticket_fees_setting;

        RadioGroup radioGroup1;


        List<EventTicketsRecyclerData> eventsList=new ArrayList<>();
    List<EventTicketsRecyclerData> eventsListOld=new ArrayList<>();

        RecyclerView rvTicketsOld;
        EditEventTicketsRecyclerViewAdapter adapterOld;
        RecyclerView.LayoutManager layoutManagerOld;

    RecyclerView rvTickets;
    EditEventTicketsRecyclerViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit_event_tickets);

            next=findViewById(R.id.next);

            radioGroup1=(RadioGroup) findViewById(R.id.radio_group1);

            add_ticket=(Button) findViewById(R.id.button2);






            progressBar=findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);


            big_layout=(LinearLayout) findViewById(R.id.big_container);
            inner_layout=findViewById(R.id.inner_layout);
            //team_check=(RadioButton) view.findViewById(R.id.individual_ticket)

            name=(TextInputEditText) findViewById(R.id.ticket_name);
            category=(TextInputEditText) findViewById(R.id.ticket_category);
            description=(TextInputEditText) findViewById(R.id.ticket_description);
            price=(EditText) findViewById(R.id.ticket_price);
            //team_sizeE=(TextInputEditText) view.findViewById(R.id.team_size);
            //team_sizeS=team_sizeE.getText().toString();

            rvTicketsOld=findViewById(R.id.rvTicketsOld);
            adapterOld=new EditEventTicketsRecyclerViewAdapter(this,eventsListOld);
            layoutManagerOld = new LinearLayoutManager(getApplicationContext());
            rvTicketsOld.setLayoutManager(layoutManagerOld);
            rvTicketsOld.setItemAnimator(new DefaultItemAnimator());
            rvTicketsOld.setAdapter(adapterOld);
            rvTicketsOld.setNestedScrollingEnabled(false);


            rvTickets=findViewById(R.id.rvTickets);
            adapter=new EditEventTicketsRecyclerViewAdapter(this,eventsList);
            layoutManager = new LinearLayoutManager(getApplicationContext());
            rvTickets.setLayoutManager(layoutManager);
            rvTickets.setItemAnimator(new DefaultItemAnimator());
            rvTickets.setAdapter(adapter);
            rvTickets.setNestedScrollingEnabled(false);


            android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
            ((AppCompatActivity)EditEventTickets.this).setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onBackPressed();

                }
            });

            user = FirebaseAuth.getInstance().getCurrentUser();

            adminDatabase = FirebaseDatabase.getInstance();
            adminReference = adminDatabase.getReference().child("HotelLoAdmin");


            eventsDatabase=FirebaseDatabase.getInstance();
            eventsReference=eventsDatabase.getReference().child("Hotels");


            Intent i = getIntent();
            EventKey = i.getStringExtra("EventKey");


            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                    new IntentFilter("TicketKey"));



            eventsReference.child(EventKey).child("Tickets").addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ticketnum[0] =dataSnapshot.getChildrenCount();//total tickets count
                    fetch();
                    eventsReference.child(EventKey).child("Tickets").removeEventListener(this);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            //adding new tickets

            ticket_type="Individual";
            ticket_fees_setting="Team"; //by default ye dono rakhenge

            radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    switch(checkedId) {
                        case R.id.individual_ticket:


                            fees_settings.setVisibility(View.GONE);
                            price.setHint("Room Price(in ₹) *");
                            ticket_type="Individual";
                            ticket_fees_setting="Individual";
                            break;


                        case R.id.team_ticket:
                            fees_settings.setVisibility(View.VISIBLE);
                            ticket_type="Team";
                            break;
                    }
                }
            });



            add_ticket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(name.getText().toString().equals("")||category.getText().toString().equals("")||
                            description.getText().toString().equals("")||price.getText().toString().equals("")||
                            radioGroup1.getCheckedRadioButtonId()==-1){

                        Toast.makeText(getApplicationContext(), "Please select/fill the required details.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {





                        EventTicketsRecyclerData data=new EventTicketsRecyclerData(null,ticket_type,null,null,
                                name.getText().toString(),category.getText().toString(),description.getText().toString(),
                                ticket_fees_setting, price.getText().toString());
                        eventsList.add(data);
                        adapter.notifyDataSetChanged();


                        name.setText("");
                        category.setText("");
                        description.setText("");
                        price.setText("");
                        radioGroup1.clearCheck();



                        price.clearFocus();
                        radioGroup1.requestFocus();


                        Toast.makeText(getApplicationContext(), "New Ticket Added.", Toast.LENGTH_SHORT).show();


                        if(nameS.size()>15){
                            add_ticket.setEnabled(false);
                            add_ticket.setClickable(false);
                            add_ticket.setTextColor(Color.parseColor("#70000000"));
                        }
                        else{
                            add_ticket.setEnabled(true);
                            add_ticket.setClickable(true);
                            add_ticket.setTextColor(Color.parseColor("#000000"));
                        }

                    }


                }
            });



            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(eventsList.size()==0&&eventsListOld.size()==0) {
                        Toast.makeText(getApplicationContext(), "Please add at least one ticket", Toast.LENGTH_SHORT).show();

                    }

                    else {
                        //eventsReference.child(EventKey).child("Tickets").removeValue();
                        sendData();


                        finish();

                    }

                }
            });




        }


        private void sendData()
        {


            for(int i=0;i<ticket_key2.size();i++){
                eventsReference.child(EventKey).child("Tickets").child(ticket_key2.get(i)).removeValue();
            }

            newEventsReference=eventsReference.child(EventKey).child("Tickets");

            if(eventsList.size()!=0) {

                for (int i = 0; i < eventsList.size(); i++) {

                    String name = eventsList.get(i).getTicket_name();
                    nameS.add(name);
                    String category = eventsList.get(i).getTicket_cat();
                    categoryS.add(category);
                    String desc = eventsList.get(i).getTicket_desc();
                    descriptionS.add(desc);
                    String price = eventsList.get(i).getTicket_price();
                    priceS.add(price);
                    String type = eventsList.get(i).getTicket_type();
                    typeS.add(type);

                    String setting = eventsList.get(i).getFees_setting();
                    settingS.add(setting);

                }


                final Map<String, Object> newEvents4 = new HashMap<>();

                for (int i = 0; i < nameS.size(); i++) {
                    newEvents4.put("Name", nameS.get(i));
                    newEvents4.put("Category", categoryS.get(i));
                    newEvents4.put("Description", descriptionS.get(i));
                    newEvents4.put("Price", priceS.get(i));
                    newEvents4.put("TEAM_CHECK", typeS.get(i));
                    newEvents4.put("PRICE_CHECK", settingS.get(i));


                    DatabaseReference aa = newEventsReference.push();
                    String keyx = aa.getKey();
                    newEvents4.put("TicketKey", keyx);
                    newEvents4.put("EventKey", EventKey);
                    aa.updateChildren(newEvents4);
                }

            }


                ArrayList<Integer> TicketPrices = new ArrayList<Integer>();

                for (int i = 0; i < eventsListOld.size(); i++) {
                    String price = eventsListOld.get(i).getTicket_price();
                    priceS.add(price);
                }

                for(int i=0;i<priceS.size();i++){
                    TicketPrices.add(Integer.parseInt(priceS.get(i)));
                }

                int startingprice = Collections.min(TicketPrices);
                int endingprices = Collections.max(TicketPrices);

                final Map<String, Object> newEvents = new HashMap<>();
                newEvents.put("StartingPrice", startingprice);
                newEvents.put("EndingPrice", endingprices);
                eventsReference.child(EventKey).updateChildren(newEvents);



            Toast.makeText(getApplicationContext(),"Event Tickets Edited Successfully",Toast.LENGTH_LONG).show();


        }

    private void fetch(){
        cel=new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                nameS.add(dataSnapshot.child("Name").getValue(String.class));
                categoryS.add(dataSnapshot.child("Category").getValue(String.class));
                descriptionS.add(dataSnapshot.child("Description").getValue(String.class));
                priceS.add(dataSnapshot.child("Price").getValue(String.class));
                typeS.add(dataSnapshot.child("TEAM_CHECK").getValue(String.class));
                settingS.add(dataSnapshot.child("PRICE_CHECK").getValue(String.class));


                TicketKey.add(dataSnapshot.child("TicketKey").getValue(String.class));

                ticketnum[0]--;

                if(ticketnum[0]==0){
                    remove();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }

        };

        eventsReference.child(EventKey).child("Tickets").addChildEventListener(cel);
    }


    public void remove(){
        eventsReference.child(EventKey).child("Tickets").removeEventListener(cel);

        int size=nameS.size();
        progressBar.setVisibility(View.GONE);

        for(int i=0;i<size;i++) {

            EventTicketsRecyclerData data=new EventTicketsRecyclerData(TicketKey.get(i),typeS.get(i),null,null,
                    nameS.get(i),categoryS.get(i),descriptionS.get(i), settingS.get(i), priceS.get(i));
            eventsListOld.add(data);
            adapterOld.notifyDataSetChanged();

            
        }

        nameS.clear();
        categoryS.clear();
        descriptionS.clear();
        priceS.clear();
        typeS.clear();


        settingS.clear();
        TicketKey.clear();

    }

    
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            ticket_key2.add(intent.getStringExtra("TicketKey"));

        }
    };

}



