package com.example.hotelloadmin.HomePackage;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hotelloadmin.CommonPackage.AboutActivity;
import com.example.hotelloadmin.CommonPackage.AppFAQsActivity;
import com.example.hotelloadmin.CommonPackage.ContactUsActivity;
import com.example.hotelloadmin.CommonPackage.FeedbackActivity;
import com.example.hotelloadmin.CommonPackage.ReportBugActivity;
import com.example.hotelloadmin.EventsPackage.EventsHistoryActivity;
import com.example.hotelloadmin.DiscussionsPackage.BlogFeedActivity;
import com.example.hotelloadmin.IntroPackage.ScreenActivity;
import com.example.hotelloadmin.NewEventPackage.NewEventProcessDescription;

import com.example.hotelloadmin.PaymentsActivity;
import com.example.hotelloadmin.R;
import com.example.hotelloadmin.ScannerPackage.ScannerActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView num_post;
    TextView num_attendee;
    TextView num_event;
    TextView domain;
    TextView college;
    TextView club_name;
    TextView location;
    TextView formation_year;
    TextView club_type;
    TextView club_bio;
    ImageView profile_image;
    ImageButton edit_profile_image;
    FloatingActionButton fab;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    FirebaseUser user;
    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference storage_reference;

    ProgressBar progressBar;
    Uri profile_image_url;
    Bitmap original;
    String collegeS;
    LinearLayout lay;
    LinearLayout posts;
    LinearLayout events;
    LinearLayout attendees;
    LinearLayout bio;
    EditText userInputBio;

    FirebaseDatabase blogDb;
    DatabaseReference blogReference;

    int postCount=0;
    String EventKey;
    String TicketKey;
    String AllotedKey;
    String UserUID;
    IntentIntegrator qrScan;
    String yearS;
    String dayS;
    String am_pm;
    String todays_time;
    String todays_date;
    String monthS;




    private FirebaseAuth.AuthStateListener authStateListener;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 7;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        qrScan=new IntentIntegrator(this);

        profile_image=(ImageView) findViewById(R.id.profile_image);
        num_attendee = (TextView) findViewById(R.id.num_attendees);
        num_event = (TextView) findViewById(R.id.num_events);
        num_post = (TextView) findViewById(R.id.num_posts);
        domain = (TextView) findViewById(R.id.domain_name);
        college= findViewById(R.id.college_name);
        club_name = (TextView) findViewById(R.id.club_name);
        location = (TextView) findViewById(R.id.club_location);
        edit_profile_image=(ImageButton) findViewById(R.id.profile_change);
        formation_year=(TextView) findViewById(R.id.formation_year);
        club_type=(TextView) findViewById(R.id.type);
        club_bio=(TextView) findViewById(R.id.club_bio);

        lay=findViewById(R.id.llll);
        posts=findViewById(R.id.posts);
        events=findViewById(R.id.events);
        attendees=findViewById(R.id.attendees);
        bio=findViewById(R.id.bio);



        progressBar=(ProgressBar) findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("HotelLoAdmin");
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storage_reference = storage.getReference();
        storage_reference=storage_reference.child("admin_app/");


        blogDb=FirebaseDatabase.getInstance();
        blogReference=blogDb.getReference().child("HotelierBlogs");


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                    startActivity(new Intent(getApplicationContext(), ScreenActivity.class),options.toBundle());
                    finish();
                }
            }
        };

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                club_name.setText(dataSnapshot.child(user.getUid()).child("ClubName").getValue(String.class));

                domain.setText(dataSnapshot.child(user.getUid()).child("ClubDomain").getValue(String.class));

                formation_year.setText(dataSnapshot.child(user.getUid()).child("ClubFormationYear").getValue(String.class));
                club_bio.setText(dataSnapshot.child(user.getUid()).child("ClubBio").getValue(String.class));
                String club_typeS=dataSnapshot.child(user.getUid()).child("ClubType").getValue(String.class);
                club_type.setText(club_typeS);

                int event_count=(int)dataSnapshot.child(user.getUid()).child("Events").getChildrenCount();
                num_event.setText(""+event_count);

                int attendees_count=(int)dataSnapshot.child(user.getUid()).child("Attendees").getChildrenCount();
                num_attendee.setText(""+attendees_count);
                location.setText(dataSnapshot.child(user.getUid()).child("ClubAddress").getValue(String.class));

                if(club_typeS!=null){

                }




                Picasso.get()
                        .load(dataSnapshot.child(user.getUid()).child("ProfileImageUrl").getValue(String.class))
                        .resize(800, 800)
                        .centerCrop()
                        .noFade()
                        .error(R.drawable.addphoto) // default image to load
                        .into(profile_image);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        blogReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String uid=dataSnapshot.child("UploadedBy").getValue(String.class);
                if(user.getUid().equals(uid)){
                    postCount++;
                }
                num_post.setText(""+postCount);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,BlogFeedActivity.class);
                intent.putExtra("BlogType","MyBlogFeed");
                startActivity(intent);
            }
        });

        attendees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AttendeesActivity.class));
            }
        });

        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EventsHistoryActivity.class));
            }
        });


       bio.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toast.makeText(MainActivity.this,"Press and Hold to edit your bio.",Toast.LENGTH_SHORT).show();

           }
       });

        bio.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                LayoutInflater li = LayoutInflater.from(getApplicationContext());
                View promptsView = li.inflate(R.layout.edit_bio_prompt, null);
                userInputBio = (EditText)promptsView.findViewById(R.id.edit_bio);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder
                        (MainActivity.this,R.style.AlertDialogTheme);

                alertDialogBuilder.setView(promptsView);


                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userInputBio.setText(dataSnapshot.child(user.getUid()).child("ClubBio").getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                alertDialogBuilder
                        .setTitle("Edit Bio")
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(!userInputBio.getText().toString().equals("")) {
                                    club_bio.setText(userInputBio.getText().toString());
                                    reference.child(user.getUid()).child("ClubBio").setValue(club_bio.getText().toString());
                                    Toast.makeText(getApplicationContext(),"Changes to your About saved successfully",Toast.LENGTH_SHORT).show();

                                }
                                else
                                    Toast.makeText(getApplicationContext(),"About Cannot be empty",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel",null);

                final AlertDialog alertDialog = alertDialogBuilder.create();
                //alertDialog.getWindow().setLayout();
                alertDialog.show();

                (alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                (alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(MainActivity.this,R.color.lightGrey));

                userInputBio.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (TextUtils.isEmpty(s)) {
                            (alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            //(alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#50000000"));
                            (alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(MainActivity.this,R.color.lightGrey));

                        } else {
                            (alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                            (alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(MainActivity.this,R.color.black));
                        }

                    }
                });




                return false;
            }
        });


        edit_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfileImage();
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfileImage();
            }
        });




        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanQRCode();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id==R.id.action_message){



            return true;
        }
        if(id==R.id.action_notification){
            Toast.makeText(MainActivity.this, "Will be added soon",
                    Toast.LENGTH_SHORT).show();
            return true;
        }
       else  if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, "Not Available",
                    Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            finish();
            return true;
        } else if (id == R.id.action_about) {
            startActivity(new Intent(getApplicationContext(), AboutActivity.class));
            return true;
        } else if (id == R.id.action_bug) {
            startActivity(new Intent(getApplicationContext(), ReportBugActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history) {
            startActivity(new Intent(getApplicationContext(), EventsHistoryActivity.class));

        }else if(id==R.id.posts){
            startActivity(new Intent(getApplicationContext(), BlogFeedActivity.class));
        }


        else if (id == R.id.nav_create) {

            startActivity(new Intent(getApplicationContext(), NewEventProcessDescription.class));

        } else if (id == R.id.nav_notif) {



        } else if (id == R.id.nav_payment) {
            startActivity(new Intent(getApplicationContext(), PaymentsActivity.class));

        } else if (id == R.id.nav_feedback) {
            startActivity(new Intent(getApplicationContext(), FeedbackActivity.class));

        } else if (id == R.id.nav_faq) {
            startActivity(new Intent(getApplicationContext(), AppFAQsActivity.class));

        } else if (id == R.id.nav_contactus) {
            startActivity(new Intent(getApplicationContext(), ContactUsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }



    public void EditProfileImage() {


        Intent intent=new Intent(MainActivity.this, PhotoSelector.class);
        intent.putExtra("Activity","ProfilePic");
        startActivityForResult(intent,1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String resultUriS = imageReturnedIntent.getStringExtra("ProfileStringUri");
                Uri resultUri=Uri.parse(resultUriS);

                if(resultUriS!=null){
                    Picasso.get()
                            .load(resultUriS) // mCategory.icon is a string
                            .noFade()
                            .error(R.drawable.addphoto2) // default image to load
                            .into(profile_image);
                    new MainActivity.CompressionAsyncTask(resultUri,resultUriS).execute();
                }
            }
        }



        //Scanner
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(MainActivity.this,"Result Not Found",Toast.LENGTH_SHORT).show();
            }
            else{

                Log.e("Boys", "h"+result.getContents().toString());
                //  textView.setText(""+result.getContents().toString());
                try {

                    JSONObject obj = new JSONObject(result.getContents());
                    EventKey=obj.getString("EventKey");
                    TicketKey=obj.getString("TicketKey");
                    if(obj.has("AllotedKey")){
                        AllotedKey=obj.getString("AllotedKey");
                    }
                    // Toast.makeText(MainActivity.this,""+AllotedKey,Toast.LENGTH_SHORT).show();
                    UserUID=obj.getString("UserUID");

                    Intent intent=new Intent(MainActivity.this, ScannerActivity.class);
                    intent.putExtra("EventKey",EventKey);
                    intent.putExtra("TicketKey",TicketKey);
                    if(AllotedKey!=null){
                        intent.putExtra("AllotedKey",AllotedKey);
                    }

                    intent.putExtra("UserUID",UserUID);
                    startActivity(intent);
                    //  Toast.makeText(MainActivity.this,"id="+obj.getString("AllotedKey")+" name="+obj.getString("name"),Toast.LENGTH_SHORT).show();

                } catch (Throwable t) {
                    Log.d("My App", " "+t.getMessage()+result.getContents());
                    Toast.makeText(MainActivity.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }

        }
    }



    public void uploadimageinbytes(Bitmap bitmap, int quality, Uri uri) {
        final StorageReference mountainsRef = storage_reference.child(user.getUid()).child("profile_image");
        // final StorageReference mountainsRef = storage_reference.child(user.getUid()).child("profile_images/"+uri.getLastPathSegment());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);


                Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();

            }
        });


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }


                return mountainsRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String downloadUriS = downloadUri.toString();
                    profile_image_url = downloadUri;
                    reference.child(user.getUid()).child("ProfileImageUrl").setValue(downloadUriS);
                    //Toast.makeText(MainActivity.this, "" + downloadUriS, Toast.LENGTH_SHORT).show();
                    Log.d("Fragment 7", "" + downloadUriS);
                } else {

                }
            }

        });


    }


    private class CompressionAsyncTask extends AsyncTask<Uri, Integer, Void> {

        Uri selectedImage;
        String selectedImageS;

        public CompressionAsyncTask(Uri selectedImage, String selectedImageS) {

            this.selectedImage = selectedImage;
            this.selectedImageS = selectedImageS;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        protected Void doInBackground(Uri... params) {
            try {
                original = MediaStore.Images.Media.getBitmap(MainActivity.this.getContentResolver(), selectedImage);
            } catch (IOException e) {
            }
            uploadimageinbytes(original, 40, selectedImage);


            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);




        }
    }


    public void ScanQRCode(){

        qrScan.initiateScan();
    }

    private void getTodaysTime() {

        Calendar calendar = Calendar.getInstance();


        int hourS = calendar.get(Calendar.HOUR_OF_DAY);
        yearS = Integer.toString(calendar.get(Calendar.YEAR));
        int monthofYear = calendar.get(Calendar.MONTH);
        dayS = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        int minuteS = calendar.get(Calendar.MINUTE);
        //    Toast.makeText(getActivity(),hourS+minutesS,Toast.LENGTH_SHORT).show();

        Calendar datetime = Calendar.getInstance();
        datetime.set(Calendar.HOUR_OF_DAY, hourS);
        datetime.set(Calendar.MINUTE, minuteS);

        String minute;
        if (datetime.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        if (datetime.get(Calendar.MINUTE) < 10) {

            minute = "0" + datetime.get(Calendar.MINUTE);
        } else minute = "" + datetime.get(Calendar.MINUTE);

        String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";
        todays_time = strHrsToShow + ":" + minute + " " + am_pm;

        switch (monthofYear + 1) {
            case 1:
                monthS = "Jan";
                break;
            case 2:
                monthS = "Feb";
                break;
            case 3:
                monthS = "March";
                break;
            case 4:
                monthS = "April";
                break;
            case 5:
                monthS = "May";
                break;
            case 6:
                monthS = "June";
                break;
            case 7:
                monthS = "July";
                break;
            case 8:
                monthS = "August";
                break;
            case 9:
                monthS = "Sept";
                break;
            case 10:
                monthS = "Oct";
                break;
            case 11:
                monthS = "Nov";
                break;
            case 12:
                monthS = "Dec";
                break;
        }

        todays_date = "" + dayS + " " + monthS + ", " + yearS;


    }




}
