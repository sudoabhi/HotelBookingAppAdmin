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
public class Fragment32 extends Fragment {

    FirebaseDatabase db;
    DatabaseReference dbRef;
    TextInputEditText ClubName_ET;
    TextInputEditText Clubbio_ET;
    Button next_viewpager;
    FirebaseUser currentUser;


    public Fragment32() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment32, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        db=FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/");
        dbRef=db.getReference();
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        ClubName_ET=(TextInputEditText) view.findViewById(R.id.et_club_name);
        Clubbio_ET=(TextInputEditText) view.findViewById(R.id.et_club_bio);

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

                String  ClubName_ETs= ClubName_ET.getText().toString();
                String  ClubBio_ETs= Clubbio_ET.getText().toString();
                if(TextUtils.isEmpty(ClubName_ETs)||TextUtils.isEmpty(ClubBio_ETs)){
                    Toast.makeText(getContext(), "Please fill in the empty fields", Toast.LENGTH_SHORT).show();

                    return;
                }else {
                    Intent i = new Intent(getActivity().getBaseContext(), DataService3.class);

                    //PACK DATA
                    i.putExtra("SENDER_KEY", "Frag32");
                    i.putExtra("CLUB_NAME", ClubName_ETs);
                    i.putExtra("CLUB_BIO", ClubBio_ETs);


                    getActivity().startService(i);
                }

                //dbRef.child(currentUser.getUid()).child("ClubName").setValue(""+ClubName_ETs,completionListener);
                //dbRef.child(currentUser.getUid()).child("ClubBio").setValue(""+ClubBio_ETs,completionListener);


                if(((ScreenSlidePagerActivity3) getActivity()).getmPager()!=null){
                    ((ScreenSlidePagerActivity3) getActivity()).getmPager().setCurrentItem(2);
                }
                else{
                    Log.e("Hii","hii");
                }
            }
        });

    }

}
