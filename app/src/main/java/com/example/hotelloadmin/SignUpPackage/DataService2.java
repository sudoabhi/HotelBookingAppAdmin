package com.example.hotelloadmin.SignUpPackage;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DataService2 extends Service {

    public String todays_date;
    public String club_name;
    public String club_bio;
    public String club_type;
    public String location;
    public String profile_image;
    String club_address;

    String uid;


    String domain;



    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference newdbRef;
    FirebaseUser user;




    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //Log.e("Data onbind","Running");
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Log.e("Data onstart","Running");

        user = FirebaseAuth.getInstance().getCurrentUser();

        database = FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/");
        reference = database.getReference();


        if (intent.getExtras() != null) {

            String sender = intent.getExtras().getString("SENDER_KEY");

            //IF ITS THE FRAGMENT THEN RECEIVE DATA
            if (sender != null) {
                if (sender.equals("Frag21")) {
                    this.receiveData0(intent);
                    //Toast.makeText(this, "Received from frag0", Toast.LENGTH_SHORT).show();
                }

                if (sender.equals("Frag22")) {
                    this.receiveData1(intent);
                    //Toast.makeText(this, "Received from frag1", Toast.LENGTH_SHORT).show();
                }

                if (sender.equals("Frag23")) {
                    this.receiveData2(intent);
                    //Toast.makeText(this, "Received from frag2", Toast.LENGTH_SHORT).show();
                }
                if (sender.equals("Frag24")) {
                    this.receiveData3(intent);
                    //Toast.makeText(this, "Received from frag2", Toast.LENGTH_SHORT).show();
                }




            }
        }



        return START_STICKY;
    }


    private void receiveData0(Intent i)
    {
        //RECEIVE DATA VIA INTENT

        todays_date=i.getStringExtra("TODAYS_DATE");
        profile_image=i.getStringExtra("PROFILE_URL");

        //Log.e("eventname received0",""+event_name);


    }

    private void receiveData1(Intent i) {
        //RECEIVE DATA VIA INTENT
        club_name = i.getStringExtra("CLUB_NAME");
        club_bio = i.getStringExtra("CLUB_BIO");

    }


    private void receiveData2(Intent i)
    {
        //RECEIVE DATA VIA INTENT
        club_address=i.getStringExtra("ClubAddress");
        location = i.getStringExtra("ClubLocation");

    }

    private void receiveData3(Intent i)
    {
        //RECEIVE DATA VIA INTENT
        uid=i.getStringExtra("UserUID");
        club_type = i.getStringExtra("CLUB_TYPE");
        //reg=i.getStringExtra("REGISTERED");
        domain = i.getStringExtra("CLUB_DOMAIN");


        uploadData();

    }



    public void uploadData(){

        newdbRef=reference.child(user.getUid());
        Map<String, Object> newData = new HashMap<>();
        newData.put("UploadedDate",todays_date);
        newData.put("ClubName", club_name);
        newData.put("ClubBio", club_bio);
        newData.put("ClubType", club_type);
        newData.put("ProfileImageUrl", profile_image);
        newData.put("ClubAddress", club_address);
        newData.put("UserUID", uid);
        newData.put("ClubLocation", location);
        newData.put("Registered","True");
        newData.put("UserType","Organiser");
        //newData.put("Registered", reg);
        //newData.put("UploadedBy", user.getUid());
        newData.put("ClubDomain", domain);


        newdbRef.updateChildren(newData);

        /*Intent intent = new Intent(this, MainUserActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        stopSelf();
*/

    }


}
