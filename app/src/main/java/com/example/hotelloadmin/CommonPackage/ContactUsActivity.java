package com.example.hotelloadmin.CommonPackage;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hotelloadmin.R;

public class ContactUsActivity extends AppCompatActivity {


    ImageView back;
    LinearLayout abhishek;

    Button fb1;
    Button insta1;

    LinearLayout abhi_lay;


    public static String FACEBOOK_URL = "https://www.facebook.com/sudo.abHi";
    public static String FACEBOOK_PAGE_ID = "sudo.abHi";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        back=findViewById(R.id.back);
        abhishek=findViewById(R.id.abhishek);

        fb1=findViewById(R.id.fb);

        insta1=findViewById(R.id.insta);

        abhi_lay=findViewById(R.id.abhi_lay);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url;
                PackageManager packageManager = getApplicationContext().getPackageManager();
                try {
                    int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                    if (versionCode >= 3002850) { //newer versions of fb app
                        url="fb://facewebmodal/f?href=" + "https://www.facebook.com/sudo.abHi";
                    } else { //older versions of fb app
                        url="fb://page/" + "sudo.abHi";
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    url="https://www.facebook.com/sudo.abHi"; //normal web url
                }

                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                facebookIntent.setData(Uri.parse(url));
                startActivity(facebookIntent);
            }
        });

        insta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/_u/sudo_abhi");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/sudo_abhi")));
                }
            }
        });






        abhishek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Uri uri = Uri.parse("https://www.facebook.com/sudo.abHi");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);*/
                if(abhi_lay.getVisibility()==View.VISIBLE){
                    abhi_lay.setVisibility(View.GONE);
                }else {
                    abhi_lay.setVisibility(View.VISIBLE);
                }


            }
        });











    }


}

