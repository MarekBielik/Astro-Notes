package com.marek.astronotes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {

    public static final int CURRENT_OBJECTS_SCREEN = 0;
    public static final int MY_OBJECTS_SCREEN = 1;
    public static final int NUMBER_OF_SCREENS = 2;
    public static final int mSECOND = 1;
    private ViewPager pager;
    private MainPagerAdapter pagerAdapter;
    private int previousPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        pager = (ViewPager) findViewById(R.id.mainPager);
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        if(previousPosition != -1)
            pager.setCurrentItem(previousPosition);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(pagerAdapter != null) {
            pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
            pager.setAdapter(pagerAdapter);
        }
        pager.setCurrentItem(previousPosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        previousPosition = pager.getCurrentItem();
    }

    private class MainPagerAdapter extends FragmentPagerAdapter {

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case CURRENT_OBJECTS_SCREEN: return CurrentObjectsFragment.newInstance();
                case MY_OBJECTS_SCREEN: return MyTrophiesFragment.newInstance();
                default: return CurrentObjectsFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return NUMBER_OF_SCREENS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {

                case CURRENT_OBJECTS_SCREEN: return getString(R.string.current_objects_title);
                case MY_OBJECTS_SCREEN: return getString(R.string.my_objects_title);
                default: return null;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_current_objects, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.switch_to_my_trophies) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
