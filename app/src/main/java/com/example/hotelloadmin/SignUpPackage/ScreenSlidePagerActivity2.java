package com.example.hotelloadmin.SignUpPackage;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.example.hotelloadmin.CustomViewPager;
import com.example.hotelloadmin.R;

/**
 * Created by Asus on 6/25/2019.
 */

public class ScreenSlidePagerActivity2 extends FragmentActivity {

    private static final int NUM_PAGES = 4;




    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    public CustomViewPager mPager;
    TabLayout tabLayout;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);








        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (CustomViewPager) findViewById(R.id.pager);
        //tabLayout.setupWithViewPager(mPager,true);
        mPager.setOffscreenPageLimit(3);



        mPager.setPagingEnabled(false);
        mPagerAdapter =  new ScreenSlidePagerActivity2.ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /*@Override
    public void onClick(View view) {
        mPager = (CustomViewPager)this.findViewById(R.id.pager);
        mPager.setCurrentItem(mPager.getCurrentItem()+1);

    }*/

    /**
     * A simple pager adapter that represents 5 Fragment11 objects, in
     * sequence.
     */
    public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){

                case 0:
                    return new Fragment21();
                case 1:
                    return new Fragment22();
                case 2:
                    return new Fragment23();
                case 3:
                    return new Fragment24();

                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public CustomViewPager getmPager(){
        if (null == mPager) {
            mPager = (CustomViewPager) findViewById(R.id.pager);
        }
        return mPager;

    }



}



