package com.example.hotelloadmin.CommonPackage;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReportBugActivity extends AppCompatActivity {

    EditText name;
    EditText phone;
    EditText email;
    EditText address;
    EditText subject;
    EditText feedback;
    Button upload;
    Button send;
    ImageView ss;

    RelativeLayout ss_lay;
    AlertDialog.Builder builder;
    ProgressBar progressBar;
    Uri feedback_image_url;
    Bitmap original;
    String downloadUriS;

    FirebaseDatabase feedbackDB;
    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference storage_reference;
    FirebaseUser user;


    private static int RESULT_LOAD_IMAGE = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 7;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 9;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug_report);

        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        email=findViewById(R.id.email);
        address=findViewById(R.id.address);
        subject=findViewById(R.id.subject);
        feedback=findViewById(R.id.feedback);
        upload=findViewById(R.id.upload_ss);
        ss=findViewById(R.id.ss);

        progressBar=findViewById(R.id.progressBar);
        ss_lay=findViewById(R.id.ss_lay);
        send=findViewById(R.id.send_feedback);

        user = FirebaseAuth.getInstance().getCurrentUser();
        feedbackDB=FirebaseDatabase.getInstance();
        reference=feedbackDB.getReference();
        reference=reference.child("Feedback");
        storage=FirebaseStorage.getInstance();
        storage_reference = storage.getReference();
        storage_reference=storage_reference.child("admin_app/");
        storage_reference=storage_reference.child("feedback_images/");


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadScreenshot();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameS=name.getText().toString();
                String phoneS=phone.getText().toString();
                String emailS=email.getText().toString();
                String addressS=address.getText().toString();
                String subjectS=subject.getText().toString();
                String feedbackS=feedback.getText().toString();

                if(TextUtils.isEmpty(nameS)||TextUtils.isEmpty(phoneS)||TextUtils.isEmpty(emailS)
                        ||TextUtils.isEmpty(addressS)||TextUtils.isEmpty(subjectS)||TextUtils.isEmpty(feedbackS)
                            ||TextUtils.isEmpty(downloadUriS)){
                    Toast.makeText(ReportBugActivity.this, "Please fill in the empty fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {


                    Map<String, Object> newEvents = new HashMap<>();
                    //   newEvents.put("UploadedDate",todays_date);
                    newEvents.put("Name", nameS);
                    newEvents.put("Phone", phoneS);
                    newEvents.put("Email", emailS);
                    newEvents.put("Address", addressS);
                    newEvents.put("Subject", subjectS);
                    newEvents.put("Feedback", feedbackS);
                    newEvents.put("FeedbackImageUrl", downloadUriS);
                    newEvents.put("UploadedBy", user.getUid());

                    reference.push().updateChildren(newEvents);

                    Toast.makeText(ReportBugActivity.this, "Booyah! You have successfully hired an assassin to kill this bug.", Toast.LENGTH_SHORT).show();
                    finish();


                }




            }
        });





        if (ContextCompat.checkSelfPermission(ReportBugActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(ReportBugActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(ReportBugActivity.this, "This is very necessary", Toast.LENGTH_SHORT).show();

            } else {

                ActivityCompat.requestPermissions(ReportBugActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


            }
        }





    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //h

                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery

                    ActivityCompat.requestPermissions(ReportBugActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                break;
        }
    }


    public void UploadScreenshot(){

        builder = new AlertDialog.Builder(ReportBugActivity.this, R.style.AlertDialogTheme);

        builder.setTitle("Add photo")
                .setMessage("Choose photo from")
                .setNegativeButton(R.string.GALLERY, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (ContextCompat.checkSelfPermission(ReportBugActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)

                                != PackageManager.PERMISSION_GRANTED) {

                            if (ActivityCompat.shouldShowRequestPermissionRationale(ReportBugActivity.this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                                Toast.makeText(ReportBugActivity.this, "This is very necessary", Toast.LENGTH_SHORT).show();

                            } else {

                                ActivityCompat.requestPermissions(ReportBugActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);


                            }
                        } else {

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "whatever i want"), 1);


                        }
                    }
                })
                .show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);


        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    String selectedImageS = selectedImage.toString();


                    new ReportBugActivity.CompressionAsyncTask(selectedImage, selectedImageS).execute();


                }
                break;
        }
    }



    public void uploadimageinbytes(Bitmap bitmap, int quality, Uri uri) {
        final StorageReference mountainsRef = storage_reference.child(user.getUid()).child("feedback_image");
        // final StorageReference mountainsRef = storage_reference.child(user.getUid()).child("profile_images/"+uri.getLastPathSegment());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ReportBugActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.INVISIBLE);


                Toast.makeText(ReportBugActivity.this, "Successful", Toast.LENGTH_SHORT).show();

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
                    feedback_image_url = downloadUri;
                    //reference.child(user.getUid()).child("FeedbackImageUrl").setValue(downloadUriS);
                    //Toast.makeText(MainActivity.this, "" + downloadUriS, Toast.LENGTH_SHORT).show();
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
                original = MediaStore.Images.Media.getBitmap(ReportBugActivity.this.getContentResolver(), selectedImage);
            } catch (IOException e) {
            }
            uploadimageinbytes(original, 40, selectedImage);


            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Picasso.get()
                    .load(Uri.parse(selectedImageS)) // mCategory.icon is a string
                    .noFade()
                    .into(ss);

            ss_lay.setVisibility(View.VISIBLE);


        }
    }


}
