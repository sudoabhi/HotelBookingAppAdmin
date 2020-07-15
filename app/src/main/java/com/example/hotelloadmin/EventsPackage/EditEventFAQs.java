package com.example.hotelloadmin.EventsPackage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Map;

public class EditEventFAQs extends AppCompatActivity {





        Button add_faq;

        TextInputEditText question;
        TextInputEditText answer;
        ProgressBar progressBar;

        ArrayList<String> questionSarray =new ArrayList<String>();
        ArrayList<String> answerSarray =new ArrayList<String>();


        LinearLayout big_layout;
        LinearLayout inner_layout;
        TextView next;

        FirebaseUser user;
        FirebaseDatabase eventsDatabase;
        DatabaseReference eventsReference;
        DatabaseReference newEventsReference;
        FirebaseDatabase adminDatabase;
        DatabaseReference adminReference;
        DatabaseReference newAdminReference;
        String EventKey;

        ChildEventListener cel;
        long[] faqnum;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit_event_faqs);
            next=findViewById(R.id.next);



            add_faq=(Button) findViewById(R.id.add_faq);
            big_layout=(LinearLayout) findViewById(R.id.big_container);
            inner_layout=findViewById(R.id.innerlayout);
            progressBar=findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);

            question=(TextInputEditText) findViewById(R.id.question);
            answer=(TextInputEditText) findViewById(R.id.answer);



            android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
            ((AppCompatActivity)EditEventFAQs.this).setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onBackPressed();

                }
            });

            /*View.OnTouchListener listener= new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {

                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    return false;
                }

            };

            question.setOnTouchListener(listener);
            answer.setOnTouchListener(listener);*/


            user = FirebaseAuth.getInstance().getCurrentUser();

            adminDatabase = FirebaseDatabase.getInstance();
            adminReference = adminDatabase.getReference().child("HotelLoAdmin");


            eventsDatabase=FirebaseDatabase.getInstance();
            eventsReference=eventsDatabase.getReference().child("Hotels");


            Intent i = getIntent();
            EventKey = i.getStringExtra("EventKey");

            faqnum = new long[1];

            eventsReference.child(EventKey).child("FAQs").addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    faqnum[0] =dataSnapshot.getChildrenCount();
                    Log.e("total faqnum",""+faqnum[0]);
                    fetch();
                    eventsReference.child(EventKey).child("FAQs").removeEventListener(this);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });












            add_faq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(question.getText().toString().equals("")||answer.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Please fill in the empty fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    else {
                        questionSarray.add(question.getText().toString());
                        answerSarray.add(answer.getText().toString());
                        final TextView qt=new TextView(getApplicationContext());
                        qt.setText(question.getText().toString());
                        final TextView at=new TextView(getApplicationContext());
                        at.setText(answer.getText().toString());

                        question.setText("");
                        answer.setText("");
                        question.requestFocus();


                        final CardView cv = new CardView(getApplicationContext());
                        CardView.LayoutParams paramcv=new CardView.LayoutParams
                                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        cv.setCardElevation(8);
                        cv.setRadius(16);
                        cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                        paramcv.setMargins(30,5,30,20);
                        cv.setLayoutParams(paramcv);
                        inner_layout.addView(cv);


                        LinearLayout lay = new LinearLayout(getApplicationContext());
                        lay.setOrientation(LinearLayout.VERTICAL);
                        //lay.setBackgroundColor(Color.parseColor("#ffffff"));
                        LinearLayout.LayoutParams paramlay = new LinearLayout.LayoutParams
                                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        paramlay.setMargins(30, 10, 30, 0);
                        lay.setLayoutParams(paramlay);
                        cv.addView(lay);




                        final TextView tv2 = new TextView(getApplicationContext());
                        final TextView tv3 = new TextView(getApplicationContext());



                        tv2.setText(questionSarray.get(questionSarray.size()-1));
                        tv2.setTextSize(18);
                        tv2.setTypeface(Typeface.DEFAULT_BOLD);
                        tv2.setTextColor(Color.parseColor("#000000"));
                        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams
                                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        param.setMargins(10, 10, 0, 0);
                        tv2.setLayoutParams(param);
                        lay.addView(tv2);


                        tv3.setText(answerSarray.get(answerSarray.size()-1));
                        tv3.setTextSize(17);
                        tv3.setTextColor(Color.parseColor("#000000"));
                        param.setMargins(10, 10, 0, 0);
                        tv3.setLayoutParams(param);
                        lay.addView(tv3);

                        Log.e("ques",""+questionSarray);

                        TypedValue outValue = new TypedValue();
                        getApplicationContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);


                        Button delete=new Button(getApplicationContext());
                        LinearLayout.LayoutParams delete_param = new LinearLayout.LayoutParams
                                (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        delete_param.setMargins(0,0,0,-20);
                        delete_param.gravity= Gravity.CENTER_HORIZONTAL;
                        delete.setLayoutParams(delete_param);
                        delete.setBackgroundColor(Color.TRANSPARENT);
                        delete.setBackgroundResource(outValue.resourceId);
                        delete.setText("Remove this FAQ");
                        delete.setAllCaps(false);

                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                inner_layout.removeView(cv);
                                String q=qt.getText().toString();
                                String a=at.getText().toString();
                                Log.e("To delete ",""+q);
                                Log.e("To delete ",""+a);
                                questionSarray.remove(q);
                                answerSarray.remove(a);
                                Log.e("Questions ",""+questionSarray);
                                Log.e("Answers ",""+answerSarray);
                            }
                        });
                        lay.addView(delete);



                        if(questionSarray.size()>50){
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

                    if(questionSarray.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please add at least one faq", Toast.LENGTH_SHORT).show();

                    }

                    else {
                        eventsReference.child(EventKey).child("FAQs").removeValue();
                        adminReference.child(user.getUid()).child("Events").child(EventKey).child("FAQs").removeValue();
                        sendData();

                        finish();
                    }

                }
            });

        }


        private void sendData()
        {


            newAdminReference=adminReference.child(user.getUid()).child("Events").child(EventKey).child("FAQs");
            newEventsReference=eventsReference.child(EventKey).child("FAQs");

            Map<String, Object> newEvents2= new HashMap<>();

            for(int i=0;i<questionSarray.size();i++){
                newEvents2.put("Ques",questionSarray.get(i));
                newEvents2.put("Ans",answerSarray.get(i));

                newAdminReference.push().updateChildren(newEvents2);
                newEventsReference.push().updateChildren(newEvents2);


            }

            Toast.makeText(getApplicationContext(),"Event FAQs Edited Successfully",Toast.LENGTH_LONG).show();


        }



        private void fetch(){
            cel=new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    questionSarray.add(dataSnapshot.child("Ques").getValue(String.class));
                    answerSarray.add(dataSnapshot.child("Ans").getValue(String.class));
                    faqnum[0]--;

                    if(faqnum[0]==0){
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

            eventsReference.child(EventKey).child("FAQs").addChildEventListener(cel);
        }

        public void remove(){
            eventsReference.child(EventKey).child("FAQs").removeEventListener(cel);

            int size=questionSarray.size();
            progressBar.setVisibility(View.GONE);
            for(int x=0;x<size;x++) {

                final String[] q = {questionSarray.get(x)};
                final String[] a = {answerSarray.get(x)};


                final CardView cv = new CardView(getApplicationContext());
                CardView.LayoutParams paramcv = new CardView.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                cv.setCardElevation(8);
                cv.setCardBackgroundColor(Color.parseColor("#ffffff"));
                paramcv.setMargins(0, 5, 0, 20);
                cv.setLayoutParams(paramcv);
                inner_layout.addView(cv);


                LinearLayout lay = new LinearLayout(getApplicationContext());
                lay.setOrientation(LinearLayout.VERTICAL);
                //lay.setBackgroundColor(Color.parseColor("#ffffff"));
                LinearLayout.LayoutParams paramlay = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramlay.setMargins(30, 10, 30, 0);
                lay.setLayoutParams(paramlay);
                cv.addView(lay);


                final EditText tv2 = new EditText(getApplicationContext());
                final EditText tv3 = new EditText(getApplicationContext());


                tv2.setText(questionSarray.get(x));
                tv2.setTextSize(18);
                tv2.setHint("Add Question");
                tv2.setTypeface(Typeface.DEFAULT_BOLD);
                tv2.setTextColor(Color.parseColor("#000000"));
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                param.setMargins(10, 10, 0, 0);
                tv2.setLayoutParams(param);
                lay.addView(tv2);


                tv3.setText(answerSarray.get(x));
                tv3.setTextSize(17);
                tv3.setHint("Add Answer");
                tv3.setTextColor(Color.parseColor("#000000"));
                param.setMargins(10, 10, 0, 0);
                tv3.setLayoutParams(param);
                lay.addView(tv3);


                LinearLayout butt_lay = new LinearLayout(getApplicationContext());
                butt_lay.setOrientation(LinearLayout.HORIZONTAL);
                butt_lay.setGravity(Gravity.CENTER);
                //lay.setBackgroundColor(Color.parseColor("#ffffff"));
                LinearLayout.LayoutParams butt_lay_param = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                butt_lay_param.setMargins(0, 0, 0, 0);

                butt_lay.setLayoutParams(butt_lay_param);
                lay.addView(butt_lay);

                TypedValue outValue = new TypedValue();
                getApplicationContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);


                Button delete = new Button(getApplicationContext());
                LinearLayout.LayoutParams delete_param = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                delete_param.setMargins(0, 0, 0, -20);
                delete_param.gravity = Gravity.CENTER_HORIZONTAL;
                delete.setLayoutParams(delete_param);
                delete.setBackgroundColor(Color.TRANSPARENT);
                delete.setBackgroundResource(outValue.resourceId);
                delete.setText("Remove");
                delete.setAllCaps(false);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        inner_layout.removeView(cv);
                        Log.e("To delete ",""+ q[0]);
                        Log.e("To delete ",""+ a[0]);
                        questionSarray.remove(q[0]);
                        answerSarray.remove(a[0]);
                        Log.e("Questions ",""+questionSarray);
                        Log.e("Answers ",""+answerSarray);

                    }
                });
                butt_lay.addView(delete);


                final Button save = new Button(getApplicationContext());
                LinearLayout.LayoutParams save_param = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                save_param.setMargins(0, 0, 0, -20);
                save_param.gravity = Gravity.CENTER_HORIZONTAL;
                save.setLayoutParams(save_param);
                save.setBackgroundColor(Color.TRANSPARENT);
                save.setBackgroundResource(outValue.resourceId);
                save.setText("Save");
                save.setAllCaps(false);
                save.setVisibility(View.GONE);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int y=questionSarray.indexOf(q[0]);
                        if(tv2.getText().toString().equals("")||tv3.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(), "Please fill in the empty fields", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.e("Index ",""+ y);
                        Log.e("To save ",""+tv2.getText().toString());
                        Log.e("To save ",""+tv3.getText().toString());
                        questionSarray.set(y,tv2.getText().toString());
                        answerSarray.set(y,tv3.getText().toString());
                        q[0] =questionSarray.get(y);
                        a[0] =answerSarray.get(y);
                        Log.e("Questions ",""+questionSarray);
                        Log.e("Answers ",""+answerSarray);
                        save.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();


                    }
                });
                butt_lay.addView(save);

                tv2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        save.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                tv3.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        save.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


            }


        }


    @Override
    public void onBackPressed() {

            super.onBackPressed();




    }
}
