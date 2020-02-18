package com.subhajitkar.commercial.projet_tulip.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteEditorActivity extends AppCompatActivity {
    private static final String TAG = "NoteEditorActivity";

    private EditText noteTitle, noteContent;
    private LinearLayout root;
    private Cursor c;
    private String intentFlag, UniqueId, createdDateAndTime;
    private int position, idIndex, titleIndex, contentIndex,createdDateIndex, updatedDateIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        //customizing the actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //initializing views
        noteTitle = findViewById(R.id.et_note_title);
        noteContent = findViewById(R.id.et_note_content);
        root = findViewById(R.id.note_editor_linear);

        try {
            //initializing database primitives
            c = StaticFields.database.rawQuery("SELECT * FROM notes", null);
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
        }else{
            getSupportActionBar().setTitle("Edit new note");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Log.d(TAG, "onOptionsItemSelected: back button clicked");
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
