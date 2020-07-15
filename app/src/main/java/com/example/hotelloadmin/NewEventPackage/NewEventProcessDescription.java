package com.example.hotelloadmin.NewEventPackage;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.hotelloadmin.R;

public class NewEventProcessDescription extends AppCompatActivity {

    Button proceed;
    Handler handler = new Handler();
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_process_description);

        proceed=(Button)findViewById(R.id.proceed);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setMax(6000);

        final Boolean[] flag = {false};
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(NewEventProcessDescription.this,NewEventActivity0.class);
                startActivity(intent);
                flag[0] =true;
                finish();
            }
        });


        new CountDownTimer(7000, 1) {

            public void onTick(long millisUntilFinished) {
                int progress = (int) (millisUntilFinished);
                progressBar.setProgress(progressBar.getMax()-progress);
            }

            public void onFinish() {
                if (flag[0]==false) {
                    Intent intent = new Intent(NewEventProcessDescription.this, NewEventActivity0.class);
                    startActivity(intent);
                    flag[0]=true;
                    finish();
                }
            }
        }.start();


        handler.postDelayed(new Runnable() {
            public void run() {

            }
        }, 7000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacksAndMessages(null);
    }
}
