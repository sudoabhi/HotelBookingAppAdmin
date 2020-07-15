package com.example.hotelloadmin.SignUpPackage;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;
import com.example.hotelloadmin.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class Fragment31 extends Fragment {

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storage_reference;
    Bitmap original;

    Uri profile_image_url;
    String downloadUriS;


    ProgressBar progressBar;

    Button next_viewpager;



    ImageView profile_image;
    FirebaseUser user;
    AlertDialog.Builder builder;


    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 9;


    public Fragment31() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment31, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar=(ProgressBar) view.findViewById(R.id.top_progress_bar);
        user= FirebaseAuth.getInstance().getCurrentUser();

       // upload_photo=(Button)view.findViewById(R.id.upload_photo);

        database=FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/");
        databaseReference =database.getReference();
        storage=FirebaseStorage.getInstance();
        storage_reference = storage.getReference();
        storage_reference=storage_reference.child("admin_app/");

        databaseReference.child(user.getUid()).child("UserType").setValue("Organiser");

        next_viewpager=(Button)view.findViewById(R.id.next_viewpager);
        next_viewpager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(downloadUriS)){
                    Toast.makeText(getContext(), "Please upload a club profile photo", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(((ScreenSlidePagerActivity3) getActivity()).getmPager()!=null){
                        ((ScreenSlidePagerActivity3) getActivity()).getmPager().setCurrentItem(1);
                        sendData();
                    }
                    else{
                        Log.e("Hii","hii");
                    }
                }

            }
        });
        profile_image = (ImageView) view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                } else {
                    builder = new AlertDialog.Builder(getActivity());
                }
                builder.setTitle("Add photo")
                        .setMessage("Choose photo from")
                        .setNegativeButton(R.string.GALLERY, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)

                                        != PackageManager.PERMISSION_GRANTED) {

                                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                            Manifest.permission.READ_EXTERNAL_STORAGE)) {

                                        Toast.makeText(getActivity(), "This is very necessary", Toast.LENGTH_SHORT).show();

                                    } else {

                                        ActivityCompat.requestPermissions(getActivity(),
                                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                                    }
                                } else {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    intent.setType("image/*");
                                    startActivityForResult(Intent.createChooser(intent,"whatever i want"),1);
                                }
                            }
                        })
                        .show();

            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        if(requestCode==1){
                //Toast.makeText(getActivity(),""+resultCode,Toast.LENGTH_SHORT).show();
                if (resultCode == RESULT_OK) {
                    //Toast.makeText(getActivity(),"Hii",Toast.LENGTH_SHORT).show();
                    Uri selectedImage = imageReturnedIntent.getData();
                    String selectedImageS = selectedImage.toString();
                    //Toast.makeText(getActivity(),""+selectedImageS,Toast.LENGTH_LONG).show();

                    new CompressionAsyncTask(selectedImage,selectedImageS).execute();


                    Log.d("Fragment11", "" + selectedImageS);

                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else {

                }
                return;
            }

        }
    }

    public void uploadimageinbytes(Bitmap bitmap,int quality,Uri uri){
        final StorageReference mountainsRef = storage_reference.child(user.getUid()).child("profile_image");
       // final StorageReference mountainsRef = storage_reference.child(user.getUid()).child("profile_images/"+uri.getLastPathSegment());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
       bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
       byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainsRef.putBytes(data);
       uploadTask.addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception exception) {
               Toast.makeText(getActivity(),"Failed",Toast.LENGTH_SHORT).show();
           }
       }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               progressBar.setVisibility(View.INVISIBLE);




               Toast.makeText(getActivity(),"Successful",Toast.LENGTH_SHORT).show();

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
                    downloadUriS=downloadUri.toString();
                    profile_image_url=downloadUri;
                    //databaseReference.child(user.getUid()).child("ProfileImageUrl").setValue(downloadUriS);
                    //Toast.makeText(getActivity(),""+downloadUriS,Toast.LENGTH_SHORT).show();
                    Log.d("Fragment 7",""+downloadUriS);
                } else {

                }
            }
        });







    }


   private class CompressionAsyncTask extends AsyncTask<Uri,Integer,Void>{

        Uri selectedImage;
        String selectedImageS;

        public CompressionAsyncTask(Uri selectedImage,String selectedImageS){

            this.selectedImage=selectedImage;
            this.selectedImageS=selectedImageS;

        }


       @Override
       protected void onPreExecute() {
           super.onPreExecute();

           progressBar.setVisibility(View.VISIBLE);
       }




         @Override
       protected Void doInBackground(Uri... params) {
           try{
               original = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),selectedImage);
           }
           catch(IOException e)
           {
           }
           uploadimageinbytes(original,40,selectedImage);


             return null;

       }

       @Override
       protected void onPostExecute(Void aVoid) {
           super.onPostExecute(aVoid);

           Picasso.get()
                   .load(Uri.parse(selectedImageS)) // mCategory.icon is a string
                   .resize(800, 800)
                   .centerCrop()
                   .noFade()
                   .error(R.drawable.addphoto) // default image to load
                   .into(profile_image);



       }
   }

    private void sendData()
    {
        //INTENT OBJ
        Intent i = new Intent(getActivity().getBaseContext(),
                DataService3.class);


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        //PACK DATA
        i.putExtra("SENDER_KEY", "Frag31");
        i.putExtra("TODAYS_DATE", formattedDate);
        i.putExtra("PROFILE_URL", downloadUriS);

        getActivity().startService(i);


    }


}