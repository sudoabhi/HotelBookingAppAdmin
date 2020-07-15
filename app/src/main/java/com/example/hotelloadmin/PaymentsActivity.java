package com.example.hotelloadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.hotelloadmin.CommonPackage.AppFAQsActivity;
import com.example.hotelloadmin.CommonPackage.ContactUsActivity;
import com.example.hotelloadmin.CommonPackage.FeedbackActivity;
import com.example.hotelloadmin.EventsPackage.EventsHistoryActivity;
import com.example.hotelloadmin.DiscussionsPackage.BlogFeedActivity;
import com.example.hotelloadmin.NewEventPackage.NewEventProcessDescription;


public class PaymentsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            finish();

        } else if (id == R.id.posts) {
            startActivity(new Intent(getApplicationContext(), BlogFeedActivity.class));
            finish();

        } else if (id == R.id.nav_create) {
            startActivity(new Intent(getApplicationContext(), NewEventProcessDescription.class));
            finish();

        } else if (id == R.id.nav_notif) {



        } else if (id == R.id.nav_history) {
            startActivity(new Intent(getApplicationContext(), EventsHistoryActivity.class));
            finish();

        } else if (id == R.id.nav_feedback) {
            startActivity(new Intent(getApplicationContext(), FeedbackActivity.class));

        } else if (id == R.id.nav_faq) {
            startActivity(new Intent(getApplicationContext(), AppFAQsActivity.class));

        } else if (id == R.id.nav_contactus) {
            startActivity(new Intent(getApplicationContext(), ContactUsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

