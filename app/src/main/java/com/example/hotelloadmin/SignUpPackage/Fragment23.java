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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.hotelloadmin.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment23 extends Fragment {


    ArrayAdapter adapter2;


    TextInputEditText club_location1;
    TextInputEditText club_location2;
    String club_location;
    TextInputEditText club_location_detail;


    Button next_fragment;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser currentUser;


    public Fragment23() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment23, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        database=FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/");
        reference = database.getReference();
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        super.onViewCreated(view, savedInstanceState);
        next_fragment=(Button)view.findViewById(R.id.next_viewpager);
        club_location1=(TextInputEditText)view.findViewById(R.id.et_club_location);
        club_location2=(TextInputEditText) view.findViewById(R.id.et_club_location2);

        club_location_detail=view.findViewById(R.id.et_club_location_detail);








   next_fragment.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {

           String club_locs1= club_location1.getText().toString();
           String club_locs2= club_location2.getText().toString();
           String club_locs_detail=club_location_detail.getText().toString();
           club_location=club_locs1+","+club_locs2;
           if(TextUtils.isEmpty(club_locs1)||TextUtils.isEmpty(club_locs_detail)||TextUtils.isEmpty(club_locs2)){
               Toast.makeText(getContext(), "Please fill in the empty fields", Toast.LENGTH_SHORT).show();
               return;

           }else{
               Intent i = new Intent(getActivity().getBaseContext(), DataService2.class);

               //PACK DATA
               i.putExtra("SENDER_KEY", "Frag23");
               i.putExtra("ClubLocation", club_location);
               i.putExtra("ClubAddress", club_locs_detail);


               getActivity().startService(i);


           }


           //reference.child(currentUser.getUid()).child("ClubLocation").setValue(club_location);

           if(((ScreenSlidePagerActivity2) getActivity()).getmPager()!=null){
               ((ScreenSlidePagerActivity2) getActivity()).getmPager().setCurrentItem(3);
           }
           else{
               Log.e("Hii","hii");
           }
       }
   });




    }


}