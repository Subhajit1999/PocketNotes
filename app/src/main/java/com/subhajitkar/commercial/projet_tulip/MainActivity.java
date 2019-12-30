package com.subhajitkar.commercial.projet_tulip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: activity created");
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: resuming activity");
        super.onResume();
        try {
            //initializing database
            StaticFields.database = this.openOrCreateDatabase("PocketNotes", MODE_PRIVATE, null);
            StaticFields.database.execSQL("CREATE TABLE IF NOT EXISTS notes (id VARCHAR, title VARCHAR, content VARCHAR, dateCreated VARCHAR)");

            if (StaticFields.database == null || StaticFields.getProfilesCount("notes")<=0) {
                //if list is empty or null
                EmptyNotesFragment emptyNotesFragment = new EmptyNotesFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(TAG, 1);
                emptyNotesFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, emptyNotesFragment)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new NotesListFragment())
                        .commit();
            }
        }catch(SQLException e){
            Log.d(TAG, "onResume: Exception: "+e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: creating menu in the actionbar");
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: menu items click action");
        switch(item.getItemId()){
            case R.id.menu_add_note:
                Intent i = new Intent(getApplicationContext(),NoteEditorActivity.class);
                i.putExtra(StaticFields.KEY_INTENT_EDITORACTIVITY,"new");
                startActivity(i);
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (StaticFields.database.isOpen()){
            StaticFields.database.close();
        }
    }
}
