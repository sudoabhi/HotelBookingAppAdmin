package com.example.hotelloadmin.NewEventPackage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelloadmin.R;

import java.util.ArrayList;
import java.util.List;

public class NewEventActivity4 extends AppCompatActivity {







    public String event_description;
    public String todays_date;
    public String event_name;
    public String location;
    public String event_address;
    ArrayList<String> event_banner=new ArrayList<String>();
    ArrayList<String> event_banner_key=new ArrayList<String>();

    String key;
    String event_typeX;


    public ArrayList<String> question=new ArrayList<String>();
    public ArrayList<String> answer=new ArrayList<String>();
    public ArrayList<String> organiser_name=new ArrayList<String>();
    public ArrayList<String> organiser_role=new ArrayList<String>();
    public ArrayList<String> organiser_email=new ArrayList<String>();
    public ArrayList<String> organiser_phone=new ArrayList<String>();
    public ArrayList<String> organiser_pic=new ArrayList<String>();
    public ArrayList<String> orgKeyS = new ArrayList<String>();


    Button add_ticket;
    TextInputEditText name;


    TextInputEditText category;
    TextInputEditText description;
    EditText price;






    ArrayList<String> nameS=new ArrayList<String>();
    ArrayList<String> categoryS=new ArrayList<String>();
    ArrayList<String> descriptionS=new ArrayList<String>();
    ArrayList<String> priceS=new ArrayList<String>();
    ArrayList<String> typeS=new ArrayList<String>();
    ArrayList<String> min_membersS=new ArrayList<String>();
    ArrayList<String> membersS=new ArrayList<String>();
    ArrayList<String> settingS=new ArrayList<String>();



    //TextInputEditText team_sizeE;

    LinearLayout big_layout;

    ImageView next;



    String ticket_fees_setting;

    RadioGroup radioGroup1;


    List<EventTicketsRecyclerData> eventsList=new ArrayList<>();
    RecyclerView rvTickets;
    EventTicketsRecyclerViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event__frag4);


        Intent i=getIntent();
        event_name=i.getStringExtra("EVENT_NAME");
        event_description=i.getStringExtra("EVENT_DESCRIPTION");
        location=i.getStringExtra("EVENT_LOCATION");
        event_address=i.getStringExtra("EVENT_ADDRESS");


        event_typeX=i.getStringExtra("EVENT_TYPEX");



        todays_date=i.getStringExtra("TODAYS_DATE");



        question=i.getStringArrayListExtra("QUESTIONS");
        answer=i.getStringArrayListExtra("ANSWERS");

        organiser_name=i.getStringArrayListExtra("ORG_NAME");
        organiser_role=i.getStringArrayListExtra("ORG_ROLE");
        organiser_email=i.getStringArrayListExtra("ORG_EMAIL");
        organiser_phone=i.getStringArrayListExtra("ORG_PHONE");
        organiser_pic=i.getStringArrayListExtra("ORG_PIC");
        orgKeyS = i.getStringArrayListExtra("ORG_KEY");

        event_banner=i.getStringArrayListExtra("EVENT_BANNER_URL");
        event_banner_key=i.getStringArrayListExtra("EVENT_BANNER_KEY");

        key=i.getStringExtra("EVENT_KEY");







        next=(ImageView) findViewById(R.id.next);

        radioGroup1=(RadioGroup)findViewById(R.id.radio_group1);

        add_ticket=(Button) findViewById(R.id.button2);








        big_layout=(LinearLayout) findViewById(R.id.big_container);
        //team_check=(RadioButton) view.findViewById(R.id.individual_ticket)

        name=(TextInputEditText) findViewById(R.id.ticket_name);
        category=(TextInputEditText) findViewById(R.id.ticket_category);
        description=(TextInputEditText)findViewById(R.id.ticket_description);
        price=(EditText) findViewById(R.id.ticket_price);
        //team_sizeE=(TextInputEditText) view.findViewById(R.id.team_size);
        //team_sizeS=team_sizeE.getText().toString();

        rvTickets=findViewById(R.id.rvTickets);
        adapter=new EventTicketsRecyclerViewAdapter(eventsList);
        layoutManager = new LinearLayoutManager(NewEventActivity4.this);
        rvTickets.setLayoutManager(layoutManager);
        rvTickets.setItemAnimator(new DefaultItemAnimator());
        rvTickets.setAdapter(adapter);
        rvTickets.setNestedScrollingEnabled(false);



        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ((AppCompatActivity) NewEventActivity4.this).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        //by default ye rakhenge

        ticket_fees_setting="Pay and Book";


        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch(checkedId) {
                    case R.id.individual_ticket:


                        price.setHint("Room Price(in ₹) *");

                        ticket_fees_setting="Pay and Book";



                        break;
                    case R.id.team_ticket:


                        //max_membersI.setVisibility(View.VISIBLE);

                        ticket_fees_setting="Book and pay at hotel";
                        //team_sizeS=max_members.getText().toString();
                        //MaxMembers=team_sizeS;
                        break;
                }
            }
        });




        add_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(price.getText().toString().equals("1")){
                    Toast.makeText(NewEventActivity4.this, "Room price cannot be equal to Rs.1", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(name.getText().toString().equals("")||category.getText().toString().equals("")||
                        description.getText().toString().equals("")||price.getText().toString().equals("")||
                        radioGroup1.getCheckedRadioButtonId()==-1
                        ){

                    Toast.makeText(NewEventActivity4.this, "Please select/fill the required details.", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getContext(), ""+max_members.getText().toString(), Toast.LENGTH_SHORT).show();

                    //  Toast.makeText(getContext(), name.getText().toString()+" "+category.getText().toString()+" "+description.getText().toString()+" "+price.getText().toString()+" "+team_check+" "+price_check+" "+team_sizeS, Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    /*nameS.add(name.getText().toString());
                    categoryS.add(category.getText().toString());
                    descriptionS.add(description.getText().toString());
                    priceS.add(price.getText().toString());
                    typeS.add(ticket_type);
                    membersS.add(max_members.getText().toString());
                    settingS.add(ticket_fees_setting);*/







                    EventTicketsRecyclerData data=new EventTicketsRecyclerData(null,ticket_fees_setting,null,null,
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




                    Toast.makeText(NewEventActivity4.this, "New Room Added.", Toast.LENGTH_SHORT).show();




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

                if(eventsList.size()==0) {
                    Toast.makeText(NewEventActivity4.this, "Please add at least one Room", Toast.LENGTH_SHORT).show();

                }

                else {
                    sendData();

                    //startActivity(new Intent(NewEventActivity4.this, EventsHistoryActivity.class));


                }

            }
        });




    }


    private void sendData()
    {
        //INTENT OBJ
        Intent intent = new Intent(NewEventActivity4.this, NewEventActivity5.class);

        for(int i=0;i<eventsList.size();i++) {
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

        //PACK DATA
        intent.putExtra("SENDER_KEY", "New_Event_Frag4");
        intent.putExtra("TICK_NAME", nameS);
        intent.putExtra("TICK_CATEGORY", categoryS);
        intent.putExtra("TICK_DESCRIPTION", descriptionS);
        intent.putExtra("TICK_PRICE", priceS);
        intent.putExtra("TICKET_TYPE", typeS);


        intent.putExtra("FEES_SETTING", settingS);

        intent.putExtra("EVENT_BANNER_URL",event_banner );
        intent.putExtra("EVENT_BANNER_KEY",event_banner_key );

        intent.putExtra("EVENT_KEY",key);


        intent.putExtra("ORG_NAME", organiser_name);
        intent.putExtra("ORG_ROLE",organiser_role);
        intent.putExtra("ORG_EMAIL", organiser_email);
        intent.putExtra("ORG_PHONE", organiser_phone);
        intent.putExtra("ORG_PIC", organiser_pic);
        intent.putExtra("ORG_KEY", orgKeyS);

        intent.putExtra("QUESTIONS", question);
        intent.putExtra("ANSWERS", answer);


        intent.putExtra("TODAYS_DATE", todays_date);
        intent.putExtra("EVENT_NAME", event_name);
        intent.putExtra("EVENT_DESCRIPTION",event_description);





        intent.putExtra("EVENT_LOCATION", location);

        intent.putExtra("EVENT_ADDRESS", event_address);
        intent.putExtra("EVENT_TYPEX",event_typeX);



        startActivity(intent);


    }


}
