package com.example.hotelloadmin.NewEventPackage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelloadmin.EventsPackage.EventDetailsOrganisersData;
import com.example.hotelloadmin.HomePackage.PhotoSelector;
import com.example.hotelloadmin.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewEventActivity2 extends AppCompatActivity {






    public String event_description;
    public String todays_date;
    public String event_name;
    public String location;
    public String event_address;
    String event_typeX;


    public ArrayList<String> question=new ArrayList<String>();
    public ArrayList<String> answer=new ArrayList<String>();

    Button add_organiser;
    TextInputEditText name;
    TextInputEditText role;
    TextInputEditText email;
    TextInputEditText phone;
    ArrayList<String> nameS=new ArrayList<String>();
    ArrayList<String> roleS=new ArrayList<String>();
    ArrayList<String> emailS=new ArrayList<String>();
    ArrayList<String> phoneS=new ArrayList<String>();
    ArrayList<String> picS=new ArrayList<>();
    ArrayList<String> orgKeyS=new ArrayList<>();


    ImageView image_organiser;
    RelativeLayout pic_layout;
    LinearLayout big_layout;
    ImageView next;


    TextView aaaaa;

    String downloadUriS;
    String org_key;

    FirebaseStorage storage;
    StorageReference storage_reference;
    FirebaseDatabase eventsDatabase;
    DatabaseReference eventsReference;
    DatabaseReference newEventsReference;
    AlertDialog.Builder builder;
    Uri profile_image_url;
    ProgressBar progressBar;
    Bitmap original;
    FirebaseUser user;
    String key;


    List<EventDetailsOrganisersData> organisersList=new ArrayList<>();
    RecyclerView rvOrganisers;
    EventOrgainsersRecyclerViewAdapter adapter;
    RecyclerView.LayoutManager layoutManager;







    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 7;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 9;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event__frag2);


        Intent i=getIntent();
        event_name=i.getStringExtra("EVENT_NAME");
        event_description=i.getStringExtra("EVENT_DESCRIPTION");
        location=i.getStringExtra("EVENT_LOCATION");
        event_address=i.getStringExtra("EVENT_ADDRESS");


        event_typeX=i.getStringExtra("EVENT_TYPEX");



        todays_date=i.getStringExtra("TODAYS_DATE");



        question=i.getStringArrayListExtra("QUESTIONS");
        answer=i.getStringArrayListExtra("ANSWERS");





        next=(ImageView)findViewById(R.id.next);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        pic_layout=findViewById(R.id.pic_layout);
        add_organiser=(Button) findViewById(R.id.button2);
        big_layout=(LinearLayout) findViewById(R.id.big_container);

        name=(TextInputEditText) findViewById(R.id.organiser_name);
        role=(TextInputEditText) findViewById(R.id.organiser_role);
        email=(TextInputEditText) findViewById(R.id.organiser_email);
        phone=(TextInputEditText) findViewById(R.id.organiser_phone);
        image_organiser=findViewById(R.id.image_organiser);

        user= FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storage_reference = storage.getReference();
        storage_reference=storage_reference.child("admin_app/");
        eventsDatabase = FirebaseDatabase.getInstance();
        eventsReference = eventsDatabase.getReference().child("Hotels");
        newEventsReference=eventsReference.push();
        key =newEventsReference.getKey();


        rvOrganisers=findViewById(R.id.rvOrganisers);
        adapter=new EventOrgainsersRecyclerViewAdapter(NewEventActivity2.this,organisersList);
        layoutManager = new LinearLayoutManager(NewEventActivity2.this);
        rvOrganisers.setLayoutManager(layoutManager);
        rvOrganisers.setItemAnimator(new DefaultItemAnimator());
        rvOrganisers.setAdapter(adapter);
        rvOrganisers.setNestedScrollingEnabled(false);


        aaaaa=findViewById(R.id.aaaaa);


        image_organiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                upload_image();
            }
        });

        pic_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_image();
            }
        });


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ((AppCompatActivity) NewEventActivity2.this).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });


        add_organiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(phone.getText().toString().length()!=10 || !phone.getText().toString().matches("[0-9]+")
                        || !isEmailValid(email.getText().toString())
                        || !name.getText().toString().matches("[A-Za-z ]+")
                        || !role.getText().toString().matches("[A-Za-z ]+")){

                    Toast.makeText(NewEventActivity2.this, "Invalid details", Toast.LENGTH_SHORT).show();
                    return;
                }
                if( TextUtils.isEmpty(downloadUriS)){

                    Toast.makeText(NewEventActivity2.this, "Please upload a pic", Toast.LENGTH_SHORT).show();
                    return;
                }


                if(name.getText().toString().equals("")||role.getText().toString().equals("")||
                        email.getText().toString().equals("")||phone.getText().toString().equals("")
                        || TextUtils.isEmpty(downloadUriS)){

                    Toast.makeText(NewEventActivity2.this, "Please fill in the empty fields", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    EventDetailsOrganisersData data=new EventDetailsOrganisersData(name.getText().toString(),
                            role.getText().toString(),email.getText().toString(),phone.getText().toString(),
                            downloadUriS,org_key);
                    organisersList.add(data);
                    adapter.notifyDataSetChanged();

                    name.setText("");
                    role.setText("");
                    email.setText("");
                    phone.setText("");
                    downloadUriS=null;
                    name.requestFocus();

                    aaaaa.setVisibility(View.VISIBLE);
                    image_organiser.setImageResource(R.drawable.addphoto3);

                    Toast.makeText(NewEventActivity2.this, "New Manager Added.", Toast.LENGTH_SHORT).show();



                    if(nameS.size()>18){
                        add_organiser.setEnabled(false);
                        add_organiser.setClickable(false);
                        add_organiser.setTextColor(Color.parseColor("#70000000"));
                    }
                    else{
                        add_organiser.setEnabled(true);
                        add_organiser.setClickable(true);
                        add_organiser.setTextColor(Color.parseColor("#000000"));
                    }

                }
            }
        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(organisersList.size()==0) {
                    Toast.makeText(NewEventActivity2.this, "Please add at least one manager", Toast.LENGTH_SHORT).show();

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
        Intent intent = new Intent(NewEventActivity2.this, NewEventActivity3.class);


        for(int i=0;i<organisersList.size();i++) {
            String name = organisersList.get(i).getOrganiserName();
            nameS.add(name);
            String role = organisersList.get(i).getOrganiserRole();
            roleS.add(role);
            String email = organisersList.get(i).getOrganiserEmail();
            emailS.add(email);
            String phone = organisersList.get(i).getOrganiserPhone();
            phoneS.add(phone);
            String pic = organisersList.get(i).getOrganiserPic();
            picS.add(pic);

            orgKeyS.add(organisersList.get(i).getOrganiserKey());

        }

        //PACK DATA
        intent.putExtra("SENDER_KEY", "New_Event_Frag2");
        intent.putExtra("EventKey", key);
        intent.putExtra("ORG_NAME", nameS);
        intent.putExtra("ORG_ROLE", roleS);
        intent.putExtra("ORG_EMAIL", emailS);
        intent.putExtra("ORG_PHONE", phoneS);
        intent.putExtra("ORG_PIC", picS);
        intent.putExtra("ORG_KEY", orgKeyS);

        intent.putExtra("QUESTIONS", question);
        intent.putExtra("ANSWERS", answer);

        intent.putExtra("SENDER_KEY", "New_Event_Frag0");
        intent.putExtra("TODAYS_DATE", todays_date);
        intent.putExtra("EVENT_NAME", event_name);
        intent.putExtra("EVENT_DESCRIPTION",event_description);





        intent.putExtra("EVENT_LOCATION", location);

        intent.putExtra("EVENT_ADDRESS", event_address);
        intent.putExtra("EVENT_TYPEX",event_typeX);


        startActivity(intent);



    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }



    public void upload_image() {

        Intent intent=new Intent(NewEventActivity2.this, PhotoSelector.class);
        intent.putExtra("Activity","OrganiserPic");
        startActivityForResult(intent,1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String resultUriS = data.getStringExtra("OrganiserStringUri");
                Uri resultUri=Uri.parse(resultUriS);

                if(resultUriS!=null){
                    image_organiser.setImageURI(resultUri);
                    new NewEventActivity2.CompressionAsyncTask(resultUri,resultUriS).execute();
                }
            }
        }
    }









    public void uploadimageinbytes(Bitmap bitmap, int quality, Uri uri) {

        org_key=eventsReference.child(key).child("Organisers").push().getKey();
        final StorageReference mountainsRef = storage_reference.child(user.getUid()).child("event_images/").child(key)
                .child("organisers/").child("-_org_-"+org_key);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(NewEventActivity2.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(NewEventActivity2.this, "Successful", Toast.LENGTH_SHORT).show();

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
                    downloadUriS = downloadUri.toString();
                    profile_image_url = downloadUri;

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
                original = MediaStore.Images.Media.getBitmap(NewEventActivity2.this.getContentResolver(), selectedImage);
            } catch (IOException e) {
            }
            uploadimageinbytes(original, 40, selectedImage);


            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            aaaaa.setVisibility(View.GONE);


        }
    }


}
