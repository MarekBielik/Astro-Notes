package com.marek.astronotes;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.marek.astronotes.entity.MessierObject;
import com.marek.astronotes.service.MessierObjectManager;

import java.sql.SQLException;

public class ShowMyTrophyActivity extends AppCompatActivity {
    private static final int INITIAL_POSITION = 1;
    public static final String TROPHY = "com.fazula.marek.TROPHY";

    ViewPager viewPager;
    MessierObjectManager messierObjectManager = MessierObjectManager.getInstance(this);
    ShowMyTrophiesPagerAdapter showMyTrophiesPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_trophy);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.myTrophiesToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showMyTrophiesPagerAdapter = new ShowMyTrophiesPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.showMyTrophiesPager);
        Intent intent = getIntent();
        final int position = intent.getIntExtra(CurrentObjectsFragment.ITEM_POSITION, INITIAL_POSITION);
        viewPager.setAdapter(showMyTrophiesPagerAdapter);
        viewPager.setCurrentItem(position);
    }

    private class ShowMyTrophiesPagerAdapter extends FragmentStatePagerAdapter {

        public ShowMyTrophiesPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return messierObjectManager.getNumberOfMyTrophies();
        }

        @Override
        public Fragment getItem(int position) {
            return ShowMyTrophyFragment.newInstance(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_my_trophy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        int position = viewPager.getCurrentItem();
        messierObjectManager = MessierObjectManager.getInstance(this);
        MessierObject trophy = messierObjectManager.getMyTrophy(position);

        //user wants to delete currently displayed trophy
        if (id == R.id.deleteTrophy) {
            try {
                messierObjectManager.deleteTrophy(trophy);
                Toast.makeText(this, "M " + trophy.getMessierNumber() + " has been deleted",
                        Toast.LENGTH_SHORT).show();
                showMyTrophiesPagerAdapter.notifyDataSetChanged();
            } catch (SQLException e) {
                Toast.makeText(this, R.string.DATABASE_PROBLEM, Toast.LENGTH_SHORT).show();
            }
            finish();
            return true;
        }

        if (id == R.id.editNote) {
            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra(TROPHY, position);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
