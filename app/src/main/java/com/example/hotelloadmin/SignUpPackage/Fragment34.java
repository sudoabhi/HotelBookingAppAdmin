package com.example.hotelloadmin.SignUpPackage;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.hotelloadmin.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

//import com.example.hotelloadmin.ddxr.UserFragments.MainUserActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment34 extends Fragment {


    ArrayAdapter adapter;

    ArrayList<String> domain;

    TextInputEditText org_type;

    String club_domain;


    Button next;


    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseDatabase fetchDatabase;
    DatabaseReference fetchDatabaseReference;
    FirebaseUser user;


    public Fragment34() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment34, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        next = (Button) view.findViewById(R.id.next_viewpager);
        org_type=view.findViewById(R.id.et_org_type);



        database = FirebaseDatabase.getInstance("https://ddrx-admin-172d3.firebaseio.com/");
        databaseReference = database.getReference();
        fetchDatabase=FirebaseDatabase.getInstance("https://ddrx-college-list.firebaseio.com/");
        fetchDatabaseReference=fetchDatabase.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();


        domain = new ArrayList<>(Arrays.asList("Sports", "Cultural", "Managerial", "Entrepreneurship", "Dance", "Music",
                "Drama", "Robotics", "Education", "Literary", "Media", "Science", "Social", "Technical","Religious",
                "Political","Charity"));
        Collections.sort(domain);



        Spinner show_domain = (Spinner) view.findViewById(R.id.spn1);



        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, domain);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        show_domain.setAdapter(adapter);


        show_domain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                club_domain = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });







        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String org_typeS=org_type.getText().toString();


                if(TextUtils.isEmpty(club_domain)||TextUtils.isEmpty(org_typeS)){

                    Toast.makeText(getContext(), "Please fill in the empty fields", Toast.LENGTH_SHORT).show();
                    return;

                }

                Intent i = new Intent(getActivity().getBaseContext(),
                        DataService3.class);

                //PACK DATA
                i.putExtra("SENDER_KEY", "Frag34");
                //i.putExtra("REGISTERED", "True");
                //i.putExtra("USER_TYPE", "Organiser");
                i.putExtra("UserUID", user.getUid());



                i.putExtra("CLUB_TYPE", org_typeS);
                i.putExtra("CLUB_DOMAIN", club_domain);


                getActivity().startService(i);


                //databaseReference.child(user.getUid()).child("ClubDomain").setValue(club_domain);
                //databaseReference.child(user.getUid()).child("ClubType").setValue(org_typeS);

                databaseReference.child(user.getUid()).child("UserType").setValue("Organiser");
                databaseReference.child(user.getUid()).child("Registered").setValue("True");
                Toast.makeText(getContext(), "Registring...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), WelcomeActivity.class));


            }
        });


    }



}







