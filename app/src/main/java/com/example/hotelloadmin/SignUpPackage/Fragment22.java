package com.example.hotelloadmin.SignUpPackage;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.hotelloadmin.R;

import static com.google.firebase.database.DatabaseReference.CompletionListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment22 extends Fragment {

    FirebaseDatabase db;
    DatabaseReference dbRef;
    TextInputEditText CompanyName_ET;
    TextInputEditText CompanyBio_ET;
    Button next_viewpager;
    FirebaseUser currentUser;


    public Fragment22() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment22, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        db=FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/");
        dbRef=db.getReference();
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        CompanyName_ET=(TextInputEditText) view.findViewById(R.id.et_club_name);
        CompanyBio_ET=(TextInputEditText) view.findViewById(R.id.et_club_bio);

        next_viewpager=(Button)view.findViewById(R.id.next_viewpager);

        final CompletionListener completionListener = new CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        if (databaseError != null) {

                            Toast.makeText(getActivity(),""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                };

        next_viewpager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String  CompanyName_ETs= CompanyName_ET.getText().toString();
                String  CompanyBio_ETs= CompanyBio_ET.getText().toString();
                if(TextUtils.isEmpty(CompanyName_ETs)||TextUtils.isEmpty(CompanyBio_ETs)){
                    Toast.makeText(getContext(), "Please fill in the empty fields", Toast.LENGTH_SHORT).show();

                    return;
                }else {
                    Intent i = new Intent(getActivity().getBaseContext(), DataService2.class);

                    //PACK DATA
                    i.putExtra("SENDER_KEY", "Frag22");
                    i.putExtra("CLUB_NAME", CompanyName_ETs);
                    i.putExtra("CLUB_BIO", CompanyBio_ETs);


                    getActivity().startService(i);
                }

                //dbRef.child(currentUser.getUid()).child("ClubName").setValue(""+CompanyName_ETs,completionListener);
                //dbRef.child(currentUser.getUid()).child("ClubBio").setValue(""+CompanyBio_ETs,completionListener);


                if(((ScreenSlidePagerActivity2) getActivity()).getmPager()!=null){
                    ((ScreenSlidePagerActivity2) getActivity()).getmPager().setCurrentItem(2);
                }
                else{
                    Log.e("Hii","hii");
                }
            }
        });

    }

}
