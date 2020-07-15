package com.example.hotelloadmin;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 1/24/2020.
 */

public class MyApp extends Application {

    public static final String CHANNEL_ONE_ID="channel_one_id";
    public static final String CHANNEL_TWO_ID="channel_two_id";
    DatabaseReference parentReference;

    FirebaseUser user;



    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);




        user= FirebaseAuth.getInstance().getCurrentUser();


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel Channel_One=new NotificationChannel(CHANNEL_ONE_ID,"Channel One", NotificationManager.IMPORTANCE_HIGH);
            Channel_One.setDescription("This is for video notifications");

            NotificationChannel Channel_Two=new NotificationChannel(CHANNEL_TWO_ID,"Channel Two", NotificationManager.IMPORTANCE_HIGH);
            Channel_Two.setDescription("This is for audio notifications");

            NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            List<NotificationChannel> channels=new ArrayList<>();
            channels.add(Channel_One);
            channels.add(Channel_Two);
            manager.createNotificationChannel(channels.get(0));
        }
    }




}

