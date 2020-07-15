package com.example.hotelloadmin.EventsPackage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hotelloadmin.HomePackage.PhotoSelector;
import com.example.hotelloadmin.R;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditEventOrganisers extends AppCompatActivity {



    Button add_organiser;
    TextInputEditText name;
    TextInputEditText role;
    TextInputEditText email;
    TextInputEditText phone;
    ImageView image_organiser;
    ArrayList<String> nameSarray=new ArrayList<String>();
    ArrayList<String> roleSarray=new ArrayList<String>();
    ArrayList<String> emailSarray=new ArrayList<String>();
    ArrayList<String> phoneSarray=new ArrayList<String>();
    ArrayList<String> picS=new ArrayList<>();
    ArrayList<String> organiserKey=new ArrayList<>();

    ArrayList<String> organiserKey2=new ArrayList<>();



    LinearLayout big_layout;
    RelativeLayout pic_layout;
    LinearLayout inner_layout;
    ProgressBar progressBar;
    TextView aaaaa;

    String downloadUriS;
    String org_key;
    ProgressBar progressBarCircle;
    Bitmap original;



    List<EventDetailsOrganisersData> organisersList=new ArrayList<>();
    RecyclerView rvOrganisers;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    List<EventDetailsOrganisersData> organisersListOld=new ArrayList<>();
    RecyclerView rvOrganisersOld;
    RecyclerView.Adapter adapterOld;
    RecyclerView.LayoutManager layoutManagerOld;





    TextView next;

    FirebaseUser user;
    FirebaseDatabase eventsDatabase;
    DatabaseReference eventsReference;
    DatabaseReference newEventsReference;
    FirebaseDatabase adminDatabase;
    DatabaseReference adminReference;
    DatabaseReference newAdminReference;
    String EventKey;
    FirebaseStorage storage;
    StorageReference storage_reference;

    ChildEventListener cel;
    long[] orgnum;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit_event_organisers);

            Intent i = getIntent();
            EventKey = i.getStringExtra("EventKey");

            next=findViewById(R.id.next);

            add_organiser=(Button)findViewById(R.id.button2);
            aaaaa=findViewById(R.id.aaaaa);
            name=(TextInputEditText) findViewById(R.id.organiser_name);
            role=(TextInputEditText) findViewById(R.id.organiser_role);
            email=(TextInputEditText) findViewById(R.id.organiser_email);
            phone=(TextInputEditText) findViewById(R.id.organiser_phone);
            image_organiser=findViewById(R.id.image_organiser);
            big_layout=(LinearLayout) findViewById(R.id.big_container);
            pic_layout=findViewById(R.id.pic_layout);
            inner_layout=findViewById(R.id.innerlayout);
            progressBar=findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);


            progressBarCircle=findViewById(R.id.progressBarCircle);
            progressBarCircle.setVisibility(View.GONE);


            android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

            ((AppCompatActivity)EditEventOrganisers.this).setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onBackPressed();

                }
            });



            user = FirebaseAuth.getInstance().getCurrentUser();

            adminDatabase = FirebaseDatabase.getInstance();
            adminReference = adminDatabase.getReference().child("HotelLoAdmin");


            eventsDatabase=FirebaseDatabase.getInstance();
            eventsReference=eventsDatabase.getReference().child("Hotels");

            storage = FirebaseStorage.getInstance();
            storage_reference = storage.getReference();
            storage_reference=storage_reference.child("admin_app/");



            rvOrganisersOld=findViewById(R.id.rvOrganisersOld);
            adapterOld=new EditEventOrganisersAdapter(EditEventOrganisers.this,organisersListOld);
            layoutManagerOld = new LinearLayoutManager(EditEventOrganisers.this);
            rvOrganisersOld.setLayoutManager(layoutManagerOld);
            rvOrganisersOld.setItemAnimator(new DefaultItemAnimator());
            rvOrganisersOld.setAdapter(adapterOld);
            rvOrganisersOld.setNestedScrollingEnabled(false);

            rvOrganisers=findViewById(R.id.rvOrganisers);
            adapter=new EditEventOrganisersAdapter(EditEventOrganisers.this,organisersList);
            layoutManager = new LinearLayoutManager(EditEventOrganisers.this);
            rvOrganisers.setLayoutManager(layoutManager);
            rvOrganisers.setItemAnimator(new DefaultItemAnimator());
            rvOrganisers.setAdapter(adapter);
            rvOrganisers.setNestedScrollingEnabled(false);






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




            orgnum = new long[1];

            eventsReference.child(EventKey).child("Organisers").addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    orgnum[0] =dataSnapshot.getChildrenCount();

                    Log.e("total orgnum",""+orgnum[0]);
                    fetch();
                    eventsReference.child(EventKey).child("Organisers").removeEventListener(this);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });





            add_organiser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(phone.getText().toString().length()!=10 || !phone.getText().toString().matches("[0-9]+")
                            || !isEmailValid(email.getText().toString())
                            || !name.getText().toString().matches("[A-Za-z ]+")
                            || !role.getText().toString().matches("[A-Za-z ]+")){
                        Toast.makeText(getApplicationContext(), "Invalid details", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if( TextUtils.isEmpty(downloadUriS)){

                        Toast.makeText(EditEventOrganisers.this, "Please upload a pic", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(name.getText().toString().equals("")||role.getText().toString().equals("")||
                            email.getText().toString().equals("")||phone.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Please fill in the empty fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    else {
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

                        Toast.makeText(EditEventOrganisers.this, "New Organiser Added.", Toast.LENGTH_SHORT).show();





                        if(nameSarray.size()>18){
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

                    if(organisersList.size()==0&&organisersListOld.size()==0) {
                        Toast.makeText(getApplicationContext(), "Please add at least one organiser", Toast.LENGTH_SHORT).show();

                    }

                    else {

                        sendData();
                    }
                }
            });

        }






        private void sendData()
        {
            for(int i=0;i<organiserKey2.size();i++){
                eventsReference.child(EventKey).child("Organisers").child(organiserKey2.get(i)).removeValue();
                adminReference.child(user.getUid()).child("Events").child(EventKey).child("Organisers")
                        .child(organiserKey2.get(i)).removeValue();
            }



            for(int i=0;i<organisersList.size();i++) {

                String name = organisersList.get(i).getOrganiserName();
                nameSarray.add(name);
                String role = organisersList.get(i).getOrganiserRole();
                roleSarray.add(role);
                String email = organisersList.get(i).getOrganiserEmail();
                emailSarray.add(email);
                String phone = organisersList.get(i).getOrganiserPhone();
                phoneSarray.add(phone);
                String pic = organisersList.get(i).getOrganiserPic();
                picS.add(pic);
                organiserKey.add(organisersList.get(i).getOrganiserKey());
            }

            newAdminReference=adminReference.child(user.getUid()).child("Events").child(EventKey).child("Organisers");
            newEventsReference=eventsReference.child(EventKey).child("Organisers");




            Map<String, Object> newEvents2= new HashMap<>();

            for(int i=0;i<nameSarray.size();i++){
                newEvents2.put("Name",nameSarray.get(i));
                newEvents2.put("Role",roleSarray.get(i));
                newEvents2.put("Email",emailSarray.get(i));
                newEvents2.put("Phone",phoneSarray.get(i));
                newEvents2.put("Pic",picS.get(i));
                newEvents2.put("OrganiserKey",organiserKey.get(i));

                newAdminReference.child(organiserKey.get(i)).updateChildren(newEvents2);
                newEventsReference.child(organiserKey.get(i)).updateChildren(newEvents2);

                //newAdminReference.push().updateChildren(newEvents2);
                //newEventsReference.push().updateChildren(newEvents2);
            }

            Toast.makeText(getApplicationContext(),"Event Organisers Edited Successfully",Toast.LENGTH_LONG).show();
            finish();
        }





        private void fetch(){
            cel=new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    nameSarray.add(dataSnapshot.child("Name").getValue(String.class));
                    roleSarray.add(dataSnapshot.child("Role").getValue(String.class));
                    emailSarray.add(dataSnapshot.child("Email").getValue(String.class));
                    phoneSarray.add(dataSnapshot.child("Phone").getValue(String.class));
                    picS.add(dataSnapshot.child("Pic").getValue(String.class));
                    organiserKey.add(dataSnapshot.child("OrganiserKey").getValue(String.class));
                    orgnum[0]--;

                    if(orgnum[0]==0){
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

            eventsReference.child(EventKey).child("Organisers").addChildEventListener(cel);
        }


    public void remove(){
        eventsReference.child(EventKey).child("Organisers").removeEventListener(cel);

        int size=nameSarray.size();
        progressBar.setVisibility(View.GONE);

        for(int i=0;i<size;i++) {

            EventDetailsOrganisersData data=new EventDetailsOrganisersData(nameSarray.get(i),roleSarray.get(i),
                    phoneSarray.get(i),emailSarray.get(i),picS.get(i),organiserKey.get(i));
            organisersListOld.add(data);
            adapterOld.notifyDataSetChanged();


        }

        nameSarray.clear();
        roleSarray.clear();
        phoneSarray.clear();
        emailSarray.clear();
        picS.clear();
        organiserKey.clear();


    }





    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void upload_image() {

        Intent intent=new Intent(EditEventOrganisers.this, PhotoSelector.class);
        intent.putExtra("Activity","EditOrganiser");
        startActivityForResult(intent,1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String resultUriS = data.getStringExtra("EditOrganiserStringUri");
                Uri resultUri=Uri.parse(resultUriS);

                if(resultUriS!=null){
                    image_organiser.setImageURI(resultUri);
                    new EditEventOrganisers.CompressionAsyncTask(resultUri,resultUriS).execute();
                }
            }
        }
    }



    public void uploadimageinbytes(Bitmap bitmap, int quality, Uri uri) {
        org_key=eventsReference.child(EventKey).child("Organisers").push().getKey();
        final StorageReference mountainsRef = storage_reference.child(user.getUid()).child("event_images/").child(EventKey)
                .child("organisers/").child("-_org_-"+org_key);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(EditEventOrganisers.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBarCircle.setVisibility(View.INVISIBLE);
                Toast.makeText(EditEventOrganisers.this, "Successful", Toast.LENGTH_SHORT).show();

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

            progressBarCircle.setVisibility(View.VISIBLE);
        }


        @Override
        protected Void doInBackground(Uri... params) {
            try {
                original = MediaStore.Images.Media.getBitmap(EditEventOrganisers.this.getContentResolver(), selectedImage);
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

















    class EditEventOrganisersAdapter extends RecyclerView.Adapter<EditEventOrganisers.EditEventOrganisersAdapter.MyViewHolder> {

        private List<EventDetailsOrganisersData> postersList;
        Context context;


        // stores and recycles views as they are scrolled off screen
        public class MyViewHolder extends RecyclerView.ViewHolder {


            TextView org_name;
            TextView org_role;
            TextView org_email;
            TextView org_phone;
            ImageView org_pic;
            Button remove_ticket;
            LinearLayout lay1;
            ProgressBar progressBar;

            public MyViewHolder(View itemView) {
                super(itemView);

                org_name = itemView.findViewById(R.id.org_name);
                org_email = itemView.findViewById(R.id.org_email);
                org_phone = itemView.findViewById(R.id.org_phone);
                org_pic = itemView.findViewById(R.id.org_pic);
                org_role = itemView.findViewById(R.id.org_role);
                lay1=itemView.findViewById(R.id.lay1);
                lay1.setVisibility(View.INVISIBLE);
                remove_ticket=itemView.findViewById(R.id.remove_tick_button);
                progressBar=itemView.findViewById(R.id.progressBar);
            }

        }


        public EditEventOrganisersAdapter(Context context, List<EventDetailsOrganisersData> postersList) {
            this.postersList = postersList;
            this.context = context;
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_row_new_event_organisers, parent, false);

            return new EditEventOrganisers.EditEventOrganisersAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, final int position) {

            EventDetailsOrganisersData data = postersList.get(position);
            final String key=data.getOrganiserKey();
            viewHolder.org_name.setText(data.getOrganiserName());
            viewHolder.org_role.setText(data.getOrganiserRole());
            viewHolder.org_email.setText(data.getOrganiserEmail());
            viewHolder.org_phone.setText("+(91)"+data.getOrganiserPhone());

            Picasso.get()
                    .load(data.getOrganiserPic())
                    .error(android.R.drawable.stat_notify_error)
                    //.resize(65,80)
                    //.centerCrop()
                    .into(viewHolder.org_pic, new Callback() {
                        @Override
                        public void onSuccess() {

                            viewHolder.progressBar.setVisibility(View.GONE);
                            viewHolder.lay1.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {

                            Toast.makeText(context, "Failed to load organisers", Toast.LENGTH_SHORT).show();

                        }


                    });


            viewHolder.remove_ticket.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    organiserKey2.add(key);

                    postersList.remove(position);  // remove the item from list
                    notifyItemRemoved(position);// notify the adapter about the removed item
                    notifyItemRangeChanged(position, postersList.size());

                }
            });

        }

        @Override
        public int getItemCount() {
            return postersList.size();
        }


    }




}
