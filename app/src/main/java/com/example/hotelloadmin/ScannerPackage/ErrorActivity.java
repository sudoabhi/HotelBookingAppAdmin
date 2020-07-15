package com.example.hotelloadmin.ScannerPackage;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.hotelloadmin.R;

public class ErrorActivity extends AppCompatActivity {

    TextView ErrorText;
    Toolbar toolbar;
    CardView cardView;
    String EventKey;
    String TicketKey;
    String AllotedKey;
    String UserUID;
    String Purpose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        ErrorText=(TextView) findViewById(R.id.error_text);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        cardView=(CardView) findViewById(R.id.gotoprofile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Error");

        toolbar.setTitleTextColor(ContextCompat.getColor(ErrorActivity.this,android.R.color.black));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i=getIntent();
        final String error=i.getStringExtra("Error");
        if(i.getStringExtra("Use")!=null&&i.getStringExtra("Use").equals("Refetch")){

            cardView.setVisibility(View.VISIBLE);

            EventKey = i.getStringExtra("EventKey");
            TicketKey = i.getStringExtra("TicketKey");
            AllotedKey = i.getStringExtra("AllotedKey");
            UserUID = i.getStringExtra("UserUID");
            Purpose = i.getStringExtra("Purpose");


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(ErrorActivity.this,ScannerActivity.class);
                    intent.putExtra("Error",error);
                    intent.putExtra("Use","CASE");
                    intent.putExtra("EventKey",EventKey);
                    intent.putExtra("TicketKey",TicketKey);
                    intent.putExtra("AllotedKey",AllotedKey);
                    intent.putExtra("UserUID",UserUID);
                    intent.putExtra("Purpose","BookedPeople");



                    ErrorActivity.this.finish();
                    startActivity(intent);
                }
            });



        }
        else{
            cardView.setVisibility(View.GONE);
        }

        ErrorText.setText(error);




    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
