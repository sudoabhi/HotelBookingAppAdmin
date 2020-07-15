package com.example.hotelloadmin.NewEventPackage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewEventActivity3 extends AppCompatActivity {






    public String event_description;
    public String todays_date;
    public String event_name;
    public String location;
    public String event_address;

    String event_typeX;


    public ArrayList<String> question = new ArrayList<String>();
    public ArrayList<String> answer = new ArrayList<String>();
    public ArrayList<String> organiser_name = new ArrayList<String>();
    public ArrayList<String> organiser_role = new ArrayList<String>();
    public ArrayList<String> organiser_email = new ArrayList<String>();
    public ArrayList<String> organiser_phone = new ArrayList<String>();
    public ArrayList<String> organiser_pic = new ArrayList<String>();
    public ArrayList<String> orgKeyS = new ArrayList<String>();

    ArrayList<String> downloadUriS = new ArrayList<String>();
    ArrayList<String> bannerKey = new ArrayList<String>();

    FirebaseStorage storage;
    StorageReference storage_reference;
    FirebaseDatabase eventsDatabase;
    DatabaseReference eventsReference;
    DatabaseReference newEventsReference;

    Button event_banner;
    AlertDialog.Builder builder;

    ProgressBar progressBar;
    Bitmap original;
    FirebaseUser user;
    String key;
    ImageView next;

    String banner_key;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private GridLayoutManager mLayoutManager;
    List<EventPostersRecyclerData> list;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event__frag3);


        Intent i = getIntent();
        event_name = i.getStringExtra("EVENT_NAME");
        event_description = i.getStringExtra("EVENT_DESCRIPTION");
        location = i.getStringExtra("EVENT_LOCATION");
        event_address = i.getStringExtra("EVENT_ADDRESS");


        event_typeX = i.getStringExtra("EVENT_TYPEX");



        todays_date = i.getStringExtra("TODAYS_DATE");



        question = i.getStringArrayListExtra("QUESTIONS");
        answer = i.getStringArrayListExtra("ANSWERS");

        organiser_name = i.getStringArrayListExtra("ORG_NAME");
        organiser_role = i.getStringArrayListExtra("ORG_ROLE");
        organiser_email = i.getStringArrayListExtra("ORG_EMAIL");
        organiser_phone = i.getStringArrayListExtra("ORG_PHONE");
        organiser_pic = i.getStringArrayListExtra("ORG_PIC");
        orgKeyS = i.getStringArrayListExtra("ORG_KEY");

        key = i.getStringExtra("EventKey");


        event_banner = findViewById(R.id.add_event_banner);
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storage_reference = storage.getReference();
        storage_reference = storage_reference.child("admin_app/");

        next = (ImageView) findViewById(R.id.next);
        progressBar = (ProgressBar) findViewById(R.id.top_progress_bar);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ((AppCompatActivity) NewEventActivity3.this).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        list = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.event_banner_rv);

        mLayoutManager = new GridLayoutManager(NewEventActivity3.this, 2);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        // mLayoutManager.setReverseLayout(true);
        //  mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        mAdapter = new NewEventActivity3Adapter(NewEventActivity3.this, list);
        mRecyclerView.setAdapter(mAdapter);

        eventsDatabase = FirebaseDatabase.getInstance();
        eventsReference = eventsDatabase.getReference().child("Hotels");


        event_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(list.size()>=4){
                    event_banner.setEnabled(false);
                    return;
                }
                upload_image();
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (list.isEmpty()) {
                    Toast.makeText(NewEventActivity3.this, "Please upload atleast one image", Toast.LENGTH_SHORT).show();
                } else {
                    sendData();
                }

            }
        });






    }

    private void sendData() {


        for(int i=0;i<list.size();i++) {

            String pic = list.get(i).getposter_pic();
            downloadUriS.add(pic);
            bannerKey.add(list.get(i).getposter_pic_key());
        }


        //INTENT OBJ
        Intent intent = new Intent(NewEventActivity3.this, NewEventActivity4.class);

        //PACK DATA
        intent.putExtra("SENDER_KEY", "New_Event_Frag3");
        intent.putExtra("EVENT_BANNER_URL", downloadUriS);
        intent.putExtra("EVENT_BANNER_KEY", bannerKey);

        intent.putExtra("EVENT_KEY", key);


        intent.putExtra("ORG_NAME", organiser_name);
        intent.putExtra("ORG_ROLE", organiser_role);
        intent.putExtra("ORG_EMAIL", organiser_email);
        intent.putExtra("ORG_PHONE", organiser_phone);
        intent.putExtra("ORG_PIC", organiser_pic);
        intent.putExtra("ORG_KEY", orgKeyS);

        intent.putExtra("QUESTIONS", question);
        intent.putExtra("ANSWERS", answer);

        intent.putExtra("SENDER_KEY", "New_Event_Frag0");
        intent.putExtra("TODAYS_DATE", todays_date);
        intent.putExtra("EVENT_NAME", event_name);
        intent.putExtra("EVENT_DESCRIPTION", event_description);





        intent.putExtra("EVENT_LOCATION", location);

        intent.putExtra("EVENT_ADDRESS", event_address);
        intent.putExtra("EVENT_TYPEX", event_typeX);


        startActivity(intent);

    }


    public void upload_image() {

        Intent intent = new Intent(NewEventActivity3.this, PhotoSelector.class);
        intent.putExtra("Activity", "EventPic");
        startActivityForResult(intent,1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String resultUriS = data.getStringExtra("EventStringUri");
                Uri resultUri=Uri.parse(resultUriS);

                if(resultUriS!=null){
                    event_banner.setEnabled(false);
                    new NewEventActivity3.CompressionAsyncTask(resultUri,resultUriS).execute();
                }
            }
        }
    }





    public void uploadimageinbytes(Bitmap bitmap, int quality, Uri uri) {

        banner_key=eventsReference.child(key).child("Posters").push().getKey();
        final StorageReference mountainsRef = storage_reference.child(user.getUid()).child("event_images/")
                .child(key).child("posters/").child("-_banner_-"+banner_key);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(NewEventActivity3.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(NewEventActivity3.this, "Successful", Toast.LENGTH_SHORT).show();

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
                    list.add(new EventPostersRecyclerData(downloadUri.toString(),banner_key));
                    mAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);

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
                original = MediaStore.Images.Media.getBitmap(NewEventActivity3.this.getContentResolver(), selectedImage);
            } catch (IOException e) {
            }
            uploadimageinbytes(original, 40, selectedImage);


            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            /*Picasso.get()
                    .load(Uri.parse(selectedImageS)) // mCategory.icon is a string
                    .noFade()
                    .error(R.drawable.addphoto) // default image to load
                    .into(event_banner);*/


        }


    }


    class NewEventActivity3Adapter extends RecyclerView.Adapter<NewEventActivity3.NewEventActivity3Adapter.MyViewHolder> {

        private List<EventPostersRecyclerData> postersList;
        Context context;


        // stores and recycles views as they are scrolled off screen
        public class MyViewHolder extends RecyclerView.ViewHolder {


            ImageView poster_pic;
            ImageView poster_num;
            Button remove_pic;
            ProgressBar progressBar;

            public MyViewHolder(View itemView) {
                super(itemView);

                poster_pic = itemView.findViewById(R.id.poster_pic);
                poster_num=itemView.findViewById(R.id.poster_num);
                remove_pic = itemView.findViewById(R.id.remove_pic_button);
                progressBar = itemView.findViewById(R.id.progressBar);
            }

        }


        public NewEventActivity3Adapter(Context context, List<EventPostersRecyclerData> postersList) {
            this.postersList = postersList;
            this.context = context;
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_row_new_event_banners, parent, false);

            return new NewEventActivity3.NewEventActivity3Adapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, final int position) {

            EventPostersRecyclerData data = postersList.get(position);

            Picasso.get()
                    .load(data.getposter_pic())
                    .error(R.drawable.photo_selector3)
                    .into(viewHolder.poster_pic, new Callback() {
                        @Override
                        public void onSuccess() {
                            event_banner.setEnabled(true);
                            viewHolder.progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError(Exception e) {

                            Toast.makeText(context, "Failed to load posters", Toast.LENGTH_SHORT).show();

                        }


                    });

            if(position==0){
                viewHolder.poster_num.setImageResource(R.drawable.ic_number_1);
            }
            if(position==1){
                viewHolder.poster_num.setImageResource(R.drawable.ic_number_2);
            }
            if(position==2){
                viewHolder.poster_num.setImageResource(R.drawable.ic_number_3);
            }
            if(position==3){
                viewHolder.poster_num.setImageResource(R.drawable.ic_number_4);
            }

            viewHolder.remove_pic.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

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





