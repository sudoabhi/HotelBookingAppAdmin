package com.example.hotelloadmin.EventsPackage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelloadmin.NewEventPackage.EventPostersRecyclerData;
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

public class EditEventBanner extends AppCompatActivity{


    FirebaseStorage storage;
    StorageReference storage_reference;
    FirebaseDatabase eventsDatabase;
    DatabaseReference eventsReference;


    ProgressBar progressBar;
    Bitmap original;
    FirebaseUser user;
    TextView next;

    ChildEventListener cel;

    FirebaseDatabase adminDatabase;
    DatabaseReference adminReference;
    DatabaseReference newAdminReference;
    String EventKey;

    ArrayList<String> downloadUriS = new ArrayList<String>();
    ArrayList<String> bannerKey = new ArrayList<String>();

    ArrayList<String> bannerKey2 = new ArrayList<String>();

    String banner_key;

    Button event_banner;

    long[] poster_num=new long[1];

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private GridLayoutManager mLayoutManager;
    List<EventPostersRecyclerData> list;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event_banner);

        Intent i = getIntent();
        EventKey = i.getStringExtra("EventKey");


        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storage_reference = storage.getReference();
        storage_reference=storage_reference.child("admin_app/");
        adminDatabase = FirebaseDatabase.getInstance();
        adminReference = adminDatabase.getReference();
        adminReference=adminReference.child("HotelLoAdmin");
        newAdminReference=adminReference.push();

        eventsDatabase=FirebaseDatabase.getInstance();
        eventsReference=eventsDatabase.getReference().child("Hotels");

        next=findViewById(R.id.save);

        progressBar = (ProgressBar) findViewById(R.id.top_progress_bar);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        ((AppCompatActivity)EditEventBanner.this).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });


        list = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.event_banner_rv);

        mLayoutManager = new GridLayoutManager(EditEventBanner.this, 2);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        // mLayoutManager.setReverseLayout(true);
        //  mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);
        mAdapter = new EditEventBannerAdapter(EditEventBanner.this, list);
        mRecyclerView.setAdapter(mAdapter);





        eventsReference.child(EventKey).child("Posters").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                poster_num[0] =dataSnapshot.getChildrenCount();

                fetch();
                eventsReference.child(EventKey).child("Posters").removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        progressBar.setVisibility(View.VISIBLE);
        event_banner = findViewById(R.id.add_event_banner);
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
                    Toast.makeText(EditEventBanner.this, "Please upload atleast one image", Toast.LENGTH_SHORT).show();
                } else {
                    sendData();
                }

            }
        });



    }

    private void fetch(){
        cel=new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                downloadUriS.add(dataSnapshot.child("EventBannerURL").getValue(String.class));
                bannerKey.add(dataSnapshot.child("EventBannerKey").getValue(String.class));
                poster_num[0]--;

                if(poster_num[0]==0){
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

        eventsReference.child(EventKey).child("Posters").addChildEventListener(cel);
    }

    public void remove(){
        eventsReference.child(EventKey).child("Posters").removeEventListener(cel);
        progressBar.setVisibility(View.GONE);

        for(int i=0;i<downloadUriS.size();i++) {

            EventPostersRecyclerData data=new EventPostersRecyclerData(downloadUriS.get(i),bannerKey.get(i));
            list.add(data);
            mAdapter.notifyDataSetChanged();


        }

        downloadUriS.clear();
        bannerKey.clear();
    }






    private void sendData()
    {

        for(int i=0;i<bannerKey2.size();i++){
            eventsReference.child(EventKey).child("Posters").child(bannerKey2.get(i)).removeValue();
            adminReference.child(user.getUid()).child("Events").child(EventKey).child("Posters")
                    .child(bannerKey2.get(i)).removeValue();
        }



        for(int i=0;i<list.size();i++) {

            String pic = list.get(i).getposter_pic();
            downloadUriS.add(pic);
            bannerKey.add(list.get(i).getposter_pic_key());
        }


        eventsReference.child(EventKey).child("EventThumbnailURL").setValue(downloadUriS.get(0));
        adminReference.child(user.getUid()).child("Events").child(EventKey).child("EventThumbnailURL").setValue(downloadUriS.get(0));

        DatabaseReference newPosterNodes=eventsReference.child(EventKey).child("Posters");
        DatabaseReference newPosterRefer=adminReference.child(user.getUid()).child("Events").child(EventKey).child("Posters");
        Map<String, Object> newEvents= new HashMap<>();
        for(int i=0;i<downloadUriS.size();i++){
            newEvents.put("EventBannerURL",downloadUriS.get(i));
            newEvents.put("EventBannerKey",bannerKey.get(i));
            newPosterRefer.child(bannerKey.get(i)).updateChildren(newEvents);
            newPosterNodes.child(bannerKey.get(i)).updateChildren(newEvents);


        }

        Toast.makeText(getApplicationContext(),"Event Banner Changed Successfully",Toast.LENGTH_LONG).show();

        finish();


    }




    public void upload_image() {


        Intent intent = new Intent(EditEventBanner.this, PhotoSelector.class);
        intent.putExtra("Activity", "EditEventPic");
        startActivityForResult(intent,1);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String resultUriS = data.getStringExtra("EditEventPicStringUri");
                Uri resultUri=Uri.parse(resultUriS);

                if(resultUriS!=null){
                    event_banner.setEnabled(false);
                    new EditEventBanner.CompressionAsyncTask(resultUri,resultUriS).execute();
                }
            }
        }
    }







    public void uploadimageinbytes(Bitmap bitmap, int quality, Uri uri) {

        banner_key=eventsReference.child(EventKey).child("Posters").push().getKey();
        final StorageReference mountainsRef = storage_reference.child(user.getUid()).child("event_images/")
                .child(EventKey).child("posters/").child( "-_banner_-"+banner_key);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(EditEventBanner.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(EditEventBanner.this, "Successful", Toast.LENGTH_SHORT).show();

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
                original = MediaStore.Images.Media.getBitmap(EditEventBanner.this.getContentResolver(), selectedImage);
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












    class EditEventBannerAdapter extends RecyclerView.Adapter<EditEventBanner.EditEventBannerAdapter.MyViewHolder> {

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


        public EditEventBannerAdapter(Context context, List<EventPostersRecyclerData> postersList) {
            this.postersList = postersList;
            this.context = context;
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_row_new_event_banners, parent, false);

            return new EditEventBanner.EditEventBannerAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, final int position) {

            EventPostersRecyclerData data = postersList.get(position);
            final String key=data.getposter_pic_key();
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

                    bannerKey2.add(key);

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
