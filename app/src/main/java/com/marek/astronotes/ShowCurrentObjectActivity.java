package com.marek.astronotes;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.marek.astronotes.service.MessierObjectManager;

public class ShowCurrentObjectActivity extends AppCompatActivity {
    private static final int INITIAL_POSITION = 1;

    ShowCurrentObjectPagerAdapter showCurrentObjectPagerAdapter;
    ViewPager viewPager;
    MessierObjectManager messierObjectManager = MessierObjectManager.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_current_object_activity);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.currentMessierToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showCurrentObjectPagerAdapter = new ShowCurrentObjectPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.showCurrentObjectPager);
        Intent intent = getIntent();
        final int position = intent.getIntExtra(CurrentObjectsFragment.ITEM_POSITION, INITIAL_POSITION);
        viewPager.setAdapter(showCurrentObjectPagerAdapter);
        viewPager.setCurrentItem(position);
    }

    private class ShowCurrentObjectPagerAdapter extends FragmentStatePagerAdapter {

        public ShowCurrentObjectPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return messierObjectManager.getNumberOfCurrentObjects();
        }

        @Override
        public Fragment getItem(int position) {
                 return ShowCurrentObjectFragment.newInstance(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_show_current_object, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
