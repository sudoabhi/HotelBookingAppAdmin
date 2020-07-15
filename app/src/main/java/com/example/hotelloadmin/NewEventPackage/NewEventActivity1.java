package com.example.hotelloadmin.NewEventPackage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelloadmin.R;

import java.util.ArrayList;

public class NewEventActivity1 extends AppCompatActivity {






    public String event_description;
    public String todays_date;
    public String event_name;
    public String location;
    public String event_address;

    String event_typeX;



    Button add_faq;
    EditText EventType;
    TextInputEditText question;
    TextInputEditText answer;

    ArrayList<String> questionS =new ArrayList<String>();
    ArrayList<String> answerS =new ArrayList<String>();


    LinearLayout big_layout;
    ImageView next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event__frag1);


        Intent i=getIntent();
        event_name=i.getStringExtra("EVENT_NAME");
        event_description=i.getStringExtra("EVENT_DESCRIPTION");
        location=i.getStringExtra("EVENT_LOCATION");
        event_address=i.getStringExtra("EVENT_ADDRESS");


        event_typeX=i.getStringExtra("EVENT_TYPEX");



        todays_date=i.getStringExtra("TODAYS_DATE");



        next=(ImageView) findViewById(R.id.next);



        add_faq=(Button)findViewById(R.id.add_faq);
        // EventType=(EditText) view.findViewById(R.id.event_type);
        big_layout=(LinearLayout) findViewById(R.id.big_container);

        question=(TextInputEditText) findViewById(R.id.question);
        answer=(TextInputEditText) findViewById(R.id.answer);



        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ((AppCompatActivity) NewEventActivity1.this).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });


        add_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(question.getText().toString().equals("")||answer.getText().toString().equals("")){
                    Toast.makeText(NewEventActivity1.this, "Please fill in the empty fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                else {
                    questionS.add(question.getText().toString());
                    answerS.add(answer.getText().toString());
                    final TextView qt=new TextView(NewEventActivity1.this);
                    qt.setText(question.getText().toString());
                    final TextView at=new TextView(NewEventActivity1.this);
                    at.setText(answer.getText().toString());

                    question.setText("");
                    answer.setText("");
                    question.requestFocus();


                    final CardView cv = new CardView(NewEventActivity1.this);
                    CardView.LayoutParams paramcv=new CardView.LayoutParams
                            (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    cv.setCardElevation(8);
                    cv.setRadius(16);
                    cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                    paramcv.setMargins(30,5,30,20);
                    cv.setLayoutParams(paramcv);
                    big_layout.addView(cv);


                    LinearLayout lay = new LinearLayout(NewEventActivity1.this);
                    lay.setOrientation(LinearLayout.VERTICAL);
                    //lay.setBackgroundColor(Color.parseColor("#ffffff"));
                    LinearLayout.LayoutParams paramlay = new LinearLayout.LayoutParams
                            (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    paramlay.setMargins(30, 10, 30, 0);
                    lay.setLayoutParams(paramlay);
                    cv.addView(lay);




                    final TextView tv2 = new TextView(NewEventActivity1.this);
                    final TextView tv3 = new TextView(NewEventActivity1.this);



                    tv2.setText(questionS.get(questionS.size()-1));
                    tv2.setTextSize(18);
                    tv2.setTypeface(Typeface.DEFAULT_BOLD);
                    tv2.setTextColor(Color.parseColor("#000000"));
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams
                            (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    param.setMargins(10, 10, 0, 0);
                    tv2.setLayoutParams(param);
                    lay.addView(tv2);


                    tv3.setText(answerS.get(answerS.size()-1));
                    tv3.setTextSize(17);
                    tv3.setTextColor(Color.parseColor("#000000"));
                    param.setMargins(10, 10, 0, 0);
                    tv3.setLayoutParams(param);
                    lay.addView(tv3);



                    Button delete=new Button(NewEventActivity1.this);
                    LinearLayout.LayoutParams delete_param = new LinearLayout.LayoutParams
                            (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    delete_param.setMargins(0,0,0,-20);
                    delete_param.gravity= Gravity.CENTER_HORIZONTAL;
                    delete.setLayoutParams(delete_param);
                    delete.setBackgroundColor(Color.TRANSPARENT);
                    delete.setText("Remove this FAQ");
                    delete.setAllCaps(false);

                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            big_layout.removeView(cv);
                            String q=qt.getText().toString();
                            String a=at.getText().toString();
                            Log.e("To delete ",""+q);
                            Log.e("To delete ",""+a);
                            questionS.remove(q);
                            answerS.remove(a);
                            Log.e("Questions ",""+questionS);
                            Log.e("Answers ",""+answerS);
                        }
                    });
                    lay.addView(delete);



                    if(questionS.size()>50){
                        add_faq.setEnabled(false);
                        add_faq.setClickable(false);
                        add_faq.setTextColor(Color.parseColor("#70000000"));
                    }
                    else{
                        add_faq.setEnabled(true);
                        add_faq.setClickable(true);
                        add_faq.setTextColor(Color.parseColor("#000000"));
                    }


                }


            }
        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(questionS.isEmpty()) {
                    Toast.makeText(NewEventActivity1.this, "Please add at least one faq", Toast.LENGTH_SHORT).show();

                }

                else {
                    sendData();



                }

            }
        });


    }

    private void sendData()
    {
        //INTENT OBJ
        Intent i = new Intent(NewEventActivity1.this, NewEventActivity2.class);

        //PACK DATA
        i.putExtra("SENDER_KEY", "New_Event_Frag1");
        i.putExtra("QUESTIONS", questionS);
        i.putExtra("ANSWERS", answerS);

        i.putExtra("SENDER_KEY", "New_Event_Frag0");
        i.putExtra("TODAYS_DATE", todays_date);
        i.putExtra("EVENT_NAME", event_name);
        i.putExtra("EVENT_DESCRIPTION",event_description);





        i.putExtra("EVENT_LOCATION", location);

        i.putExtra("EVENT_ADDRESS", event_address);
        i.putExtra("EVENT_TYPEX",event_typeX);


        startActivity(i);

    }



}
