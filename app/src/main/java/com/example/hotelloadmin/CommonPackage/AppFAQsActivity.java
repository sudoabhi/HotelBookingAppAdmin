package com.example.hotelloadmin.CommonPackage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.hotelloadmin.R;

import java.util.ArrayList;
import java.util.List;

public class AppFAQsActivity extends AppCompatActivity {

    Button back;
    ArrayList<String> questions=new ArrayList<>();
    ArrayList<String> answers=new ArrayList<>();
    List<AppFAQsRecyclerData> faqList=new ArrayList<>();
    RecyclerView rv_app_faqs;
    AppFAQsRecyclerViewAdapter adapter;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs_app);
        back=findViewById(R.id.back_button);

        rv_app_faqs = (RecyclerView)findViewById(R.id.app_faqs_recycler);

        layoutManager = new LinearLayoutManager(AppFAQsActivity.this);
        rv_app_faqs.setNestedScrollingEnabled(false);
        rv_app_faqs.setHasFixedSize(true);
        rv_app_faqs.setLayoutManager(layoutManager);
        //rv_app_faqs.getRecycledViewPool().setMaxRecycledViews(0, 0);


        adapter = new AppFAQsRecyclerViewAdapter(faqList);
        rv_app_faqs.setItemAnimator(new DefaultItemAnimator());
        rv_app_faqs.setAdapter(adapter);


        questions.add("What is ddxr.in?");
        answers.add("ddxr.in is a small and diverse group of event lovers on a mission to help organizers by enabling them " +
                "to reach a wider audience and providing them with the right tools to conveniently sell their tickets " +
                "and track the sales!");

        questions.add("How can ddxr.in help an event organizer?");
        answers.add("We, at ddxr.in, know how much effort goes into making an event successful and we try to make it easier for you!\n" +
                "In a nutshell, you could outsource some of your work to us and we will take care of them for you!");
        
        questions.add("What services do we provide?");
        answers.add("Event Listing.\nTicketing.\n");

        //// Add more questions and answers



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int size=questions.size();
        for (int i=0;i<2;i++){
            faqList.add(new AppFAQsRecyclerData(questions.get(i),answers.get(i)));
            adapter.notifyDataSetChanged();
        }
        
        
    }
}
