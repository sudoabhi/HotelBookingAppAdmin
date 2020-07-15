package com.example.hotelloadmin.IntroPackage;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.hotelloadmin.HomePackage.MainActivity;
import com.example.hotelloadmin.R;

public class MailActivity extends AppCompatActivity {

    private static final String TAG = "MailActivity.this";

    private EditText email;
    private EditText password;
    Button register;
    Button login;
    Button cross;
    TextView forgot;
    private String emailS;
    private String passwordS;
    TextInputLayout pass;

    RelativeLayout inner1;
    RelativeLayout inner2;
    ProgressBar progressBar;
    Boolean emailVerified;

    private FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser currentUser;
    String user_type=null;
    String str="";
    ValueEventListener listener;
    FirebaseDatabase userDatabase;
    DatabaseReference userReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;

        email = (EditText)findViewById(R.id.et_email);
        password=(EditText) findViewById(R.id.et_password);
        register=(Button)findViewById(R.id.btn_register);
        login=(Button)findViewById( R.id.btn_login);
        forgot=(TextView)findViewById(R.id.textresetpassword);
        cross = (Button)findViewById(R.id.cross_button);
        pass=findViewById(R.id.input2);
        inner1=findViewById(R.id.inner1);
        inner2=findViewById(R.id.inner2);

        ViewGroup.LayoutParams params=inner1.getLayoutParams();
        params.height=(deviceHeight/2);
        inner1.setLayoutParams(params);
        ViewGroup.LayoutParams params2=inner2.getLayoutParams();
        params2.height=(deviceHeight/2);
        inner2.setLayoutParams(params2);


        progressBar=findViewById(R.id.my_progressBar);
        progressBar.setVisibility(View.GONE);


        login.setEnabled(false);


        SharedPreferences preferencesGet = getSharedPreferences("StoreEmail", MODE_PRIVATE);
        String email_get = preferencesGet.getString("email", "");
        email.setText(email_get);

        email.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

                buttonchange();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                buttonchange();
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });



        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

                passwordToggle();
                buttonchange();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

                passwordToggle();
                buttonchange();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/");
        reference = database.getReference();
        currentUser=firebaseAuth.getCurrentUser();

        userDatabase=FirebaseDatabase.getInstance("https://ddrx-172d3.firebaseio.com/");
        userReference=userDatabase.getReference();




        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                emailS= email.getText().toString();
                passwordS=password.getText().toString();
                authenicateuser(emailS,passwordS);
                progressBar.setVisibility(View.VISIBLE);

            }
        });

        //registers user
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MailActivity.this);
                startActivity(new Intent(getApplicationContext(),RegsiterActivity.class),options.toBundle());


            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MailActivity.this);
                startActivity(new Intent(getApplicationContext(),PasswordActivity.class),options.toBundle());
            }
        });

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferencesGet = getSharedPreferences("StoreEmail", MODE_PRIVATE);
        String email_get = preferencesGet.getString("email", "");
        email.setText(email_get);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferencesGet = getSharedPreferences("StoreEmail", MODE_PRIVATE);
        String email_get = preferencesGet.getString("email", "");
        email.setText(email_get);
    }





    private  void authenicateuser(String email,String password){

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please fill in the required fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please fill in the required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }


        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            SharedPreferences preferencesPut = getSharedPreferences("StoreEmail", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferencesPut.edit();
                            editor.putString("email", emailS);
                            editor.apply();

                            firebaseAuth = FirebaseAuth.getInstance();
                            currentUser=firebaseAuth.getCurrentUser();
                            emailVerified = currentUser.isEmailVerified();

                            if(!emailVerified) {
                                Toast.makeText(getApplicationContext(),"Your Email is not verified. " +
                                        "Please verify and Login again!",Toast.LENGTH_SHORT).show();
                                firebaseAuth.signOut();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            else {
                                listener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(currentUser.getUid())) {

                                            if (dataSnapshot.child(currentUser.getUid()).hasChild("Registered")) {

                                                if (dataSnapshot.child(currentUser.getUid()).hasChild("UserType")) {

                                                    user_type = dataSnapshot.child(currentUser.getUid()).child("UserType").getValue(String.class);

                                                    if (user_type.equals("Organiser")) {
                                                        str = dataSnapshot.child(currentUser.getUid()).child("Registered").getValue(String.class);

                                                        if (str.equals("False")) {
                                                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MailActivity.this);
                                                            startActivity(new Intent(getApplicationContext(), SelectorActivity.class), options.toBundle());
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            remove();
                                                            finishAffinity();
                                                        } else {
                                                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MailActivity.this);
                                                            Intent intent = new Intent(MailActivity.this, MainActivity.class);
                                                            startActivity(intent, options.toBundle());
                                                            remove();
                                                            finishAffinity();
                                                        }
                                                    }
                                                    else{
                                                        reference.child(currentUser.getUid()).child("Registered").setValue("False");
                                                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MailActivity.this);
                                                        startActivity(new Intent(getApplicationContext(), SelectorActivity.class), options.toBundle());
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        remove();
                                                        finishAffinity();
                                                    }
                                                }
                                                else{
                                                    reference.child(currentUser.getUid()).child("Registered").setValue("False");
                                                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MailActivity.this);
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
                                                                Intent intent = new Intent(MailActivity.this, SelectorActivity.class);
                                                                startActivity(intent);
                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                remove();
                                                                userReference.removeEventListener(this);
                                                                finishAffinity();
                                                            }
                                                        }else {
                                                            reference.child(currentUser.getUid()).child("Registered").setValue("False");
                                                            Intent intent = new Intent(MailActivity.this, SelectorActivity.class);
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
                                        else{
                                            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.hasChild(currentUser.getUid())&&dataSnapshot.child(currentUser.getUid()).hasChild("Registered")) {
                                                        String reg = dataSnapshot.child(currentUser.getUid()).child("Registered").getValue(String.class);
                                                        if (reg.equals("True")) {
                                                            Toast.makeText(getApplicationContext(), "This Email is already registered by another user on ddxr app.",
                                                                    Toast.LENGTH_SHORT).show();
                                                            firebaseAuth.signOut();
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            remove();
                                                            userReference.removeEventListener(this);
                                                            return;
                                                        } else {
                                                            reference.child(currentUser.getUid()).child("Registered").setValue("False");
                                                            Intent intent = new Intent(MailActivity.this, SelectorActivity.class);
                                                            startActivity(intent);
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            remove();
                                                            userReference.removeEventListener(this);
                                                            finishAffinity();
                                                        }
                                                    }else {
                                                        reference.child(currentUser.getUid()).child("Registered").setValue("False");
                                                        Intent intent = new Intent(MailActivity.this, SelectorActivity.class);
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


                        } else {

                            progressBar.setVisibility(View.INVISIBLE);
                            try {
                                if(task.getException()!=null) {
                                    throw task.getException();
                                }
                            } catch(FirebaseAuthException e) {
                                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                                String errorCode = e.getErrorCode();

                                switch (errorCode) {

                                    case "ERROR_INVALID_CREDENTIAL":
                                        Toast.makeText(getApplicationContext(), "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_INVALID_EMAIL":
                                        Toast.makeText(getApplicationContext(), "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_WRONG_PASSWORD":
                                        Toast.makeText(getApplicationContext(), "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_USER_MISMATCH":
                                        Toast.makeText(getApplicationContext(), "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_USER_DISABLED":
                                        Toast.makeText(getApplicationContext(), "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                                        break;

                                    case "ERROR_USER_NOT_FOUND":
                                        Toast.makeText(getApplicationContext(), "There is no user record corresponding to this email.", Toast.LENGTH_LONG).show();
                                        break;
                                }


                            }
                            catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }

                    }
                });
    }

    public void remove(){
        reference.removeEventListener(listener);
    }

    public void buttonchange() {
        if (email.getText().toString().equals("") || password.getText().toString().equals("")) {
            login.setEnabled(false);
            login.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.button_disable));
            login.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.disable_button_text));
        }
        else {
            login.setEnabled(true);
            login.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.button_enable));
            login.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    public void passwordToggle(){
        if (!password.getText().toString().equals("")) {
            pass.setPasswordVisibilityToggleEnabled(true);
        }
        else{
            pass.setPasswordVisibilityToggleEnabled(false);
        }
    }


    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
