package com.example.hotelloadmin.IntroPackage;

import android.content.Context;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.example.hotelloadmin.R;


public class PasswordActivity extends AppCompatActivity {

    EditText Email;
    Button btnNewPass;
    FirebaseAuth firebaseAuth;
    Button cross;

    RelativeLayout inner1;
    RelativeLayout inner2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;

        Email = (EditText) findViewById(R.id.email3);
        btnNewPass = (Button) findViewById(R.id.link);
        cross=findViewById(R.id.cross_button);

        inner1=findViewById(R.id.inner1);
        inner2=findViewById(R.id.inner2);

        ViewGroup.LayoutParams params=inner1.getLayoutParams();
        params.height=(deviceHeight/2);
        inner1.setLayoutParams(params);
        ViewGroup.LayoutParams params2=inner2.getLayoutParams();
        params2.height=(deviceHeight/2);
        inner2.setLayoutParams(params2);

        btnNewPass.setEnabled(false);


        Email.addTextChangedListener(new TextWatcher() {

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

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        firebaseAuth = FirebaseAuth.getInstance();

        btnNewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Please fill an e-mail",Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Password reset link was sent to your email address",Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                                else{
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

                                            case "ERROR_USER_NOT_FOUND":
                                                Toast.makeText(getApplicationContext(), "There is no user record corresponding to this email.", Toast.LENGTH_LONG).show();
                                                break;
                                        }

                                    } catch(Exception e) {
                                        Toast.makeText(getApplicationContext(),"Mail sending error",Toast.LENGTH_SHORT).show();
                                        Log.e("Error ", e.getMessage());
                                    }



                                }
                            }
                        });
            }
        });


    }

    public void buttonchange() {
        if (Email.getText().toString().equals("")) {
            btnNewPass.setEnabled(false);
            btnNewPass.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.button_disable));
            btnNewPass.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.disable_button_text));
        }
        else {
            btnNewPass.setEnabled(true);
            btnNewPass.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.button_enable));
            btnNewPass.setTextColor(Color.parseColor("#ffffff"));
        }
    }

}