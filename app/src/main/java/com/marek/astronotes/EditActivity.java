package com.marek.astronotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.marek.astronotes.entity.MessierObject;
import com.marek.astronotes.service.MessierObjectManager;

import java.sql.SQLException;

public class EditActivity extends AppCompatActivity {

    private static final int DEFAULT_MESSIER_NUMBER = 0;

    MessierObject messierObject;
    MessierObjectManager messierObjectManager;
    EditText noteEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.editNoteToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //retrieve messier for editing
        messierObjectManager = MessierObjectManager.getInstance(this);
        Intent intent = getIntent();
        messierObject = messierObjectManager.getMyTrophy(
                intent.getIntExtra(ShowMyTrophyActivity.TROPHY, DEFAULT_MESSIER_NUMBER));

        noteEditText = (EditText) findViewById(R.id.editNoteEditText);

        //set the original note, if any
        if(!messierObject.getNote().equals(""))
            noteEditText.setText(messierObject.getNote());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        messierObjectManager = MessierObjectManager.getInstance(this);

        if (id == R.id.editDone) {
            String note = noteEditText.getText().toString();
            try {
                messierObjectManager.editMyTrophy(messierObject, note);
            } catch (SQLException e) {
                Toast.makeText(this, R.string.DATABASE_PROBLEM, Toast.LENGTH_SHORT).show();
            }
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
