package com.example.hotelloadmin.IntroPackage;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelloadmin.CommonPackage.PrivacyTermsActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.hotelloadmin.HomePackage.MainActivity;
import com.example.hotelloadmin.R;

public class ScreenActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN = 9001;
    Button googleButton;
    Button emailbutton;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser currentUser;
    GoogleApiClient mGoogleApiClient;
    String user_type=null;
    String str="";
    ProgressBar progressBar;
    ValueEventListener listener;
    FirebaseDatabase userDatabase;
    DatabaseReference userReference;
    TextView privacy;
    TextView terms;

    RelativeLayout inner1;
    RelativeLayout inner2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;

        inner1=findViewById(R.id.inner1);
        inner2=findViewById(R.id.inner2);

        ViewGroup.LayoutParams params=inner1.getLayoutParams();
        params.height=(deviceHeight/2);
        inner1.setLayoutParams(params);
        ViewGroup.LayoutParams params2=inner2.getLayoutParams();
        params2.height=(deviceHeight/2);
        inner2.setLayoutParams(params2);


        googleButton = (Button) findViewById(R.id.google_button);
        emailbutton = (Button) findViewById(R.id.email_button);
        firebaseAuth = FirebaseAuth.getInstance();
        Log.e("Firebase Auth",""+firebaseAuth);
        currentUser=firebaseAuth.getCurrentUser();
        database= FirebaseDatabase.getInstance();
        reference = database.getReference().child("HotelLoAdmin");
        progressBar=findViewById(R.id.progress);
        terms=findViewById(R.id.terms);
        privacy=findViewById(R.id.privacy);

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ScreenActivity.this, PrivacyTermsActivity.class);
                i.putExtra("Type","Terms");
                startActivity(i);
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ScreenActivity.this, PrivacyTermsActivity.class);
                i.putExtra("Type","Privacy");
                startActivity(i);
            }
        });

        progressBar.setVisibility(View.INVISIBLE);

        userDatabase=FirebaseDatabase.getInstance();
        userReference=userDatabase.getReference().child("HotelLo");



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("264945336983-la2frvhuqfsjdh1vutmf4m2a08bv2dqf.apps.googleusercontent.com")
                .requestEmail()
                .build();

        //.requestIdToken("429539583672-qre0b4pbjg4j4inev5do78kk0lnmsb1j.apps.googleusercontent.com")

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(ScreenActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                signIn();
            }
        });

        emailbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ScreenActivity.this);
                startActivity(new Intent(getApplicationContext(), MailActivity.class),options.toBundle());
            }
        });
    }

        private void signIn() {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signIntent,RC_SIGN_IN);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==RC_SIGN_IN){
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if(result.isSuccess()){

                    GoogleSignInAccount account = result.getSignInAccount();
                    authWithGoogle(account);
                }
                else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Please select a valid Google ID to continue.",Toast.LENGTH_SHORT).show();

                }
            }
            else{
                progressBar.setVisibility(View.INVISIBLE);
            }
        }


        private void authWithGoogle(GoogleSignInAccount account) {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                        if(isNewUser) {
                            currentUser=firebaseAuth.getCurrentUser();
                            reference.child(currentUser.getUid()).child("Registered").setValue("False");
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ScreenActivity.this);
                            startActivity(new Intent(getApplicationContext(), SelectorActivity.class),options.toBundle());
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else {
                            currentUser=firebaseAuth.getCurrentUser();
                            listener= new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.hasChild(currentUser.getUid())&&dataSnapshot.child(currentUser.getUid()).hasChild("Registered")){


                                            if (dataSnapshot.child(currentUser.getUid()).hasChild("UserType")) {

                                                user_type = dataSnapshot.child(currentUser.getUid()).child("UserType").getValue(String.class);
                                                if (user_type.equals("Organiser")) {

                                                    str = dataSnapshot.child(currentUser.getUid()).child("Registered").getValue(String.class);

                                                    if (str.equals("False")) {
                                                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ScreenActivity.this);
                                                        //    startActivity(new Intent(getApplicationContext(), ScreenSlidePagerActivity.class),options.toBundle());
                                                        startActivity(new Intent(getApplicationContext(), SelectorActivity.class), options.toBundle());
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        remove();
                                                        finish();

                                                    } else {
                                                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ScreenActivity.this);
                                                        startActivity(new Intent(getApplicationContext(), MainActivity.class), options.toBundle());
                                                        remove();
                                                        finish();
                                                    }
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(), "The email entered is already registered by another user on ddxr.",
                                                            Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    remove();
                                                    return;
                                                }
                                            }
                                            else{

                                                reference.child(currentUser.getUid()).child("Registered").setValue("False");
                                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ScreenActivity.this);
                                                startActivity(new Intent(getApplicationContext(), SelectorActivity.class), options.toBundle());
                                                progressBar.setVisibility(View.INVISIBLE);
                                                remove();
                                                finish();



                                            }

                                        } else {
                                            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.hasChild(currentUser.getUid())&&dataSnapshot.child(currentUser.getUid()).hasChild("Registered")) {
                                                        String reg = dataSnapshot.child(currentUser.getUid()).child("Registered").getValue(String.class);
                                                        if (reg.equals("True")) {
                                                            Toast.makeText(getApplicationContext(), "This Google ID is already registered by another user on ddxr app.",
                                                                    Toast.LENGTH_SHORT).show();
                                                            firebaseAuth.signOut();
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            remove();
                                                            userReference.removeEventListener(this);
                                                            return;
                                                        } else {
                                                            reference.child(currentUser.getUid()).child("Registered").setValue("False");
                                                            Intent intent = new Intent(ScreenActivity.this, SelectorActivity.class);
                                                            startActivity(intent);
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            remove();
                                                            userReference.removeEventListener(this);
                                                            finishAffinity();
                                                        }
                                                    }else {
                                                        reference.child(currentUser.getUid()).child("Registered").setValue("False");
                                                        Intent intent = new Intent(ScreenActivity.this, SelectorActivity.class);
                                                        startActivity(intent);
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        remove();
                                                        userReference.removeEventListener(this);
                                                        finishAffinity();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };

                            reference.addValueEventListener(listener);



                        }
                    }

                    else{
                        Toast.makeText(getApplicationContext(),"Auth Error",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Toast.makeText(getApplicationContext(),"Connection Failed"+ connectionResult.toString(),Toast.LENGTH_SHORT).show();
            if (!connectionResult.hasResolution()) {
                // show the localized error dialog.
                GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
                return;
            }

        }

        public void remove(){
            reference.removeEventListener(listener);
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
