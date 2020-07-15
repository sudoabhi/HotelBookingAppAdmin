package com.example.hotelloadmin.IntroPackage;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.hotelloadmin.R;

public class RegsiterActivity extends AppCompatActivity {




        private EditText email;
        private EditText password;
        Button register;
        Button cross;
        TextView forgot;
        private String emailS;
        private String passwordS;
        TextInputLayout input2;
        TextInputLayout input;
        ProgressBar progressBar;
        TextView txt;
        boolean emailVerified;

        private FirebaseAuth firebaseAuth;
        FirebaseDatabase database;
        DatabaseReference reference;
        FirebaseUser currentUser;

    RelativeLayout inner1;
    RelativeLayout inner2;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
            int deviceWidth = displayMetrics.widthPixels;
            int deviceHeight = displayMetrics.heightPixels;

            email = (EditText)findViewById(R.id.et_email);
            password=(EditText) findViewById(R.id.et_password);
            register=(Button)findViewById(R.id.btn_register);
            cross = (Button)findViewById(R.id.cross_button);
            txt=findViewById(R.id.txt);
            input=findViewById(R.id.input);
            input2=findViewById(R.id.input2);
            progressBar=findViewById(R.id.progress);
            progressBar.setVisibility(View.INVISIBLE);

            inner1=findViewById(R.id.inner1);
            inner2=findViewById(R.id.inner2);

            ViewGroup.LayoutParams params=inner1.getLayoutParams();
            params.height=(deviceHeight/2);
            inner1.setLayoutParams(params);
            ViewGroup.LayoutParams params2=inner2.getLayoutParams();
            params2.height=(deviceHeight/2);
            inner2.setLayoutParams(params2);

            register.setEnabled(false);



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

                    buttonchange();
                    passwordToggle();

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




            //registers user
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    emailS= email.getText().toString();
                    passwordS=password.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    createUser(emailS,passwordS);

                }
            });


            cross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    //finish();
                }
            });

        }



        private void createUser(final String email, final String password){

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

            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                SharedPreferences preferencesPut = getSharedPreferences("StoreEmail", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferencesPut.edit();
                                editor.putString("email", emailS);
                                editor.apply();

                                currentUser=firebaseAuth.getCurrentUser();
                                reference.child(currentUser.getUid()).child("Registered").setValue("False");
                                currentUser.sendEmailVerification();
                                emailVerified = currentUser.isEmailVerified();

                                /*ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegsiterActivity.this);
                                Intent intent = new Intent(getApplicationContext(), SelectorActivity.class);
                                startActivity(intent,options.toBundle());
                                finishAffinity();*/

                                if(!emailVerified){
                                    input.setVisibility(View.GONE);
                                    input2.setVisibility(View.GONE);
                                    register.setText("Proceed to Login");
                                    txt.setText("A verification email has been sent to ur email ID.\n" + "Please Verify and Login Again. :)");
                                    txt.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);

                                    register.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            firebaseAuth.signOut();
                                            onBackPressed();
                                            /*ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegsiterActivity.this);
                                            Intent intent = new Intent(getApplicationContext(), MailActivity.class);
                                            startActivity(intent,options.toBundle());
                                            finishAffinity();*/
                                        }
                                    });

                                }
                                else{
                                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegsiterActivity.this);
                                    Intent intent = new Intent(getApplicationContext(), SelectorActivity.class);
                                    startActivity(intent,options.toBundle());
                                    finishAffinity();
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


                                        case "ERROR_INVALID_EMAIL":
                                            Toast.makeText(getApplicationContext(), "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                            break;


                                        case "ERROR_EMAIL_ALREADY_IN_USE":
                                            Toast.makeText(getApplicationContext(), "The email address is already in use by another account.", Toast.LENGTH_LONG).show();
                                            break;

                                    }


                                }
                                catch(Exception e) {
                                    Log.e("this", e.getMessage());
                                }
                                //Toast.makeText(getApplicationContext(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }



        public void buttonchange() {
            if (!(email.getText().toString().equals("") || password.getText().toString().equals(""))) {

                register.setEnabled(true);
                register.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.button_enable2));
                register.setTextColor(Color.parseColor("#ffffff"));


            }
            else {

                register.setEnabled(false);
                register.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.button_disable));
                register.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.disable_button_text));

            }
        }


        public void passwordToggle(){
            if (!password.getText().toString().equals("")) {
                input2.setPasswordVisibilityToggleEnabled(true);
            }
            else{
                input2.setPasswordVisibilityToggleEnabled(false);
            }
        }



}

