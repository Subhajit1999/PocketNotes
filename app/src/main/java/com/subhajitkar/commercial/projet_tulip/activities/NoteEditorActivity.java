package com.subhajitkar.commercial.projet_tulip.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;
import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.fragments.NotesListFragment;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteEditorActivity extends AppCompatActivity {
    private static final String TAG = "NoteEditorActivity";

    private EditText noteTitle, noteContent;
    private LinearLayout root;
    private Cursor c;
    private String intentFlag, UniqueId, createdDateAndTime, table;
    private int position, idIndex, titleIndex, contentIndex,createdDateIndex, updatedDateIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (StaticFields.darkThemeSet){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.DarkTheme);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_note_editor);

        //customizing the actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //initializing views
        noteTitle = findViewById(R.id.et_note_title);
        noteContent = findViewById(R.id.et_note_content);
        root = findViewById(R.id.note_editor_linear);

        table = getIntent().getStringExtra(StaticFields.KEY_INTENT_TABLEID);
        try {
            //initializing database primitives
            c = StaticFields.database.rawQuery("SELECT * FROM "+table, null);
            idIndex = c.getColumnIndex("id");
            titleIndex = c.getColumnIndex("title");
            contentIndex = c.getColumnIndex("content");
            createdDateIndex = c.getColumnIndex("dateCreated");
            updatedDateIndex = c.getColumnIndex("dateUpdated");
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"Some error occurred. ("+e.getMessage()+")",Toast.LENGTH_SHORT).show();
        }

        //getting the intent data
        intentFlag = getIntent().getStringExtra(StaticFields.KEY_INTENT_EDITORACTIVITY);
        Log.d(TAG, "onCreate: intentFlag: "+intentFlag);

        if (intentFlag.equals("existing")){
            position = getIntent().getIntExtra(StaticFields.KEY_INTENT_LISTPOSITION,0);
            Log.d(TAG, "onCreate: position: "+position);
            getSupportActionBar().setTitle("Edit note");
            c.moveToPosition(position);
            UniqueId = c.getString(idIndex);
            noteTitle.setText(c.getString(titleIndex));
            noteContent.setText(c.getString(contentIndex));
            createdDateAndTime = c.getString(createdDateIndex);

            Snackbar.make(root, Html.fromHtml("<font color=\""+getResources().getColor(R.color.colorAccent)+"\">Created at: "+createdDateAndTime+"</font>"),Snackbar.LENGTH_INDEFINITE).show();
        }else{
            getSupportActionBar().setTitle("Edit new note");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: creating menu");
        getMenuInflater().inflate(R.menu.menu_editor,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            Log.d(TAG, "onOptionsItemSelected: back button clicked");
            onBackPressed();
                break;
            case R.id.editor_share:
                Log.d(TAG, "onOptionsItemSelected: editor share option");
                String noteTitle = c.getString(titleIndex);
                String noteContent = c.getString(contentIndex);

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, noteTitle+"\n\n"+
                        noteContent+"\n\n================\nShared from Pocket Notes, a truly simple notepad app.\n\nDownload now:)\nhttp://play.google.com/store/apps/details?id="
                        +getPackageName());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share note with..."));
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: actual saving mechanism is here");
        super.onPause();
        //extracting edit-texts data
        String mTitle = noteTitle.getText().toString();
        String mContent = noteContent.getText().toString();
        boolean AlreadyThere = false;

        //getting the created date and time
        String currentDateAndTime = getDateTime(StaticFields.visibleDateFormat);
        String noteUniqueId = getDateTime(StaticFields.UniqueIdDateFormat);

        //preparing the data to be saved
        StaticFields.contentValues = new ContentValues();
        StaticFields.contentValues.put("title", mTitle);
        StaticFields.contentValues.put("content", mContent);

        try {
            //checking if note already exists in the database or not
            if (intentFlag.equals("existing")) {  //note already exists
                StaticFields.contentValues.put("id", UniqueId);
                StaticFields.contentValues.put("dateCreated", createdDateAndTime);
                StaticFields.contentValues.put("dateUpdated", currentDateAndTime);
                StaticFields.database.update("notes", StaticFields.contentValues, "id = ?", new String[]{UniqueId});

            } else {          //indicates note doesn't exist or first element
                for (int i = 0; i < StaticFields.getProfilesCount("notes"); i++) {
                    c.moveToPosition(i);
                    if (c.getString(titleIndex).equals(mTitle)) {  //if title matches to existing
                        AlreadyThere = true;
                        break;
                    }
                }
                if (!AlreadyThere && !mTitle.isEmpty()) {   //if new then insert
                    StaticFields.contentValues.put("id", noteUniqueId);
                    StaticFields.contentValues.put("dateCreated", currentDateAndTime);
                    StaticFields.contentValues.put("dateUpdated", currentDateAndTime);
                    StaticFields.database.insert("notes", null, StaticFields.contentValues);
                }
            }
            c.close();
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"Some error occurred. ("+e.getMessage()+")",Toast.LENGTH_SHORT).show();
        }
    }

    public String getDateTime(String format){
        Log.d(TAG, "getDateTime: getting current date, time in specified format");
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date());
    }
}
