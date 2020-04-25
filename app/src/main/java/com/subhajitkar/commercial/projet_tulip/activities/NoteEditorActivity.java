package com.subhajitkar.commercial.projet_tulip.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;


import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Type;
import com.google.android.material.snackbar.Snackbar;
import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.fragments.SimpleNoteFragment;
import com.subhajitkar.commercial.projet_tulip.utils.PortableContent;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteEditorActivity extends AppCompatActivity {
    private static final String TAG = "NoteEditorActivity";

    private LinearLayout root;
    private Cursor c;
    private String intentFlag, UniqueId, createdDateAndTime, table;
    private int position, idIndex, titleIndex, contentIndex,createdDateIndex, updatedDateIndex, editorTypeIndex, starIndex, tagIndex;
    private String sendNoteTitle, sendNoteContent, sendCreatedDateTime, editorType, star, tag;

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
        root = findViewById(R.id.note_editor_linear);

        table = getIntent().getStringExtra(StaticFields.KEY_INTENT_TABLEID);
        try {
            //initializing database primitives
            c = StaticFields.dbHelper.getData(table);
            idIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_ID);
            titleIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_TITLE);
            contentIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_CONTENT);
            createdDateIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_CREATED_DATE);
            updatedDateIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_UPDATED_DATE);
            editorTypeIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_EDITOR_TYPE);
            starIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_STAR);
            tagIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_TAG);
        }catch(Exception e){
            new PortableContent().showSnackBar(this, Type.ERROR, "Some error occurred. ("+e.getMessage()+")",
                    Duration.SHORT);
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
            sendNoteTitle = c.getString(titleIndex);
            sendNoteContent = c.getString(contentIndex);
            editorType = c.getString(editorTypeIndex);
            star = c.getString(starIndex);
            tag = c.getString(tagIndex);
            createdDateAndTime = sendCreatedDateTime = c.getString(createdDateIndex);
        }else{      //if new
            if (getIntent()!=null){
                editorType = getIntent().getStringExtra(StaticFields.KEY_EDITOR_ID);
            }
            getSupportActionBar().setTitle("Edit new note");
        }
        launchFragment();
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
                if (!c.isClosed()) {
                    String noteTitle = c.getString(titleIndex);
                    String noteContent = c.getString(contentIndex);

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, noteTitle + "\n\n" +
                            noteContent + "\n\n================\nShared from Pocket Notes, a truly simple notepad app.\n\nDownload now:)\n"
                            + StaticFields.Store_URL);
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "Share note with..."));
                }else{
                    new PortableContent().showSnackBar(this, Type.ERROR, "Some error occurred",
                            Duration.SHORT);
                }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (editorType.equals("simple")){
            final SimpleNoteFragment fragment = (SimpleNoteFragment) getSupportFragmentManager().findFragmentByTag("TAG_SIMPLE_FRAGMENT");
            if (fragment.backPressedSimple()) {
                super.onBackPressed();
                supportFinishAfterTransition();
            }
        }else{
            super.onBackPressed();
            supportFinishAfterTransition();
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: actual saving mechanism is here");

        boolean AlreadyThere = false;
        //getting the created date and time
        String currentDateAndTime = getDateTime(StaticFields.visibleDateFormat);
        String noteUniqueId = getDateTime(StaticFields.UniqueIdDateFormat);

        try {
            //checking if note already exists in the database or not
            if (intentFlag.equals("existing")) {  //note already exists
                if (!StaticFields.noteTitle.isEmpty()) {
                    Log.d(TAG, "onPause: updating note");
                    ContentValues contentValues = StaticFields.dbHelper.createDBContentValue(table, UniqueId,
                            StaticFields.noteTitle, StaticFields.noteContent, createdDateAndTime, currentDateAndTime, editorType
                    ,star, tag);
                    StaticFields.dbHelper.updateNote(table, UniqueId, contentValues);
                }
                c = StaticFields.dbHelper.getData(table);
                Log.d(TAG, "onPause: note updated successfully");

            } else {          //indicates note doesn't exist or first element
                Log.d(TAG, "onPause: new note. NumberRows: "+StaticFields.dbHelper.numberOfRows(table));
                for (int i = 0; i < StaticFields.dbHelper.numberOfRows(table); i++) {
                    c.moveToPosition(i);
                    if (c.getString(titleIndex).equals(StaticFields.noteTitle)) {  //if title matches to existing
                        AlreadyThere = true;
                        break;
                    }
                    Log.d(TAG, "onPause: checking note exists in the database or not");
                }
                if (!AlreadyThere && !StaticFields.noteTitle.isEmpty()) {   //if new then insert
                    Log.d(TAG, "onPause: new note saving into database");
                    ContentValues contentValues = StaticFields.dbHelper.createDBContentValue(table, noteUniqueId,
                            StaticFields.noteTitle, StaticFields.noteContent, currentDateAndTime, currentDateAndTime,
                            editorType, "false", null);
                    StaticFields.dbHelper.insertNote(table,contentValues);
                    Log.d(TAG, "onPause: new note created");
                }
            }
            Log.d(TAG, "onPause: content:"+StaticFields.noteContent);
            c.close();
        }catch(Exception e){
            new PortableContent().showSnackBar(this, Type.ERROR, "Some error occured. ("+e.getMessage()+")",
                    Duration.SHORT);
            Log.d(TAG, "onPause: error occurred. Message: "+e.getMessage());
        }
        super.onPause();
    }

    public String getDateTime(String format){
        Log.d(TAG, "getDateTime: getting current date, time in specified format");
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date());
    }

    public void launchFragment(){
        Log.d(TAG, "launchFragment: launching editor fragment");
        Bundle bundle = new Bundle();
        if (editorType.equals("simple")){
            //launch simpleNoteFragment
            SimpleNoteFragment fragment = new SimpleNoteFragment();
            bundle.putString(StaticFields.KEY_EDITOR_TITLE, sendNoteTitle);
            bundle.putString(StaticFields.KEY_EDITOR_CONTENT, sendNoteContent);
            bundle.putString(StaticFields.KEY_EDITOR_DATECREATED, sendCreatedDateTime);
            bundle.putString(StaticFields.KEY_EDITOR_INTENTFLAG, intentFlag);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.editor_frag_container, fragment, "TAG_SIMPLE_FRAGMENT")
                    .addToBackStack(null)
                    .commit();
        }
    }
}
