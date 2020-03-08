package com.subhajitkar.commercial.projet_tulip.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.subhajitkar.commercial.projet_tulip.fragments.EmptyNotesFragment;
import com.subhajitkar.commercial.projet_tulip.fragments.NotesListFragment;
import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private SharedPreferences themePreference;
    private FloatingActionMenu fab_menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: activity created");
        //getting preference data
        Log.d(TAG, "onCreate: getting data");
        themePreference = getApplicationContext().getSharedPreferences(TAG,Context.MODE_PRIVATE);
        StaticFields.darkThemeSet = themePreference.getInt(TAG, 0) == 1;

        Log.d(TAG, "onCreate: Dark theme: "+StaticFields.darkThemeSet);
        if (StaticFields.darkThemeSet){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.DarkTheme);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_main);
        configureFAB();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: resuming activity");
        super.onResume();
        databaseInit();  //init database
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
            case R.id.menu_archive:
                //archive menu action
                Intent intent = new Intent(this, GeneralActivity.class);
                intent.putExtra(StaticFields.KEY_BUNDLE_GENERAL,"archive");
                startActivity(intent);
                break;
            case R.id.menu_settings:
                //archive menu action
                Intent settingIntent = new Intent(this, GeneralActivity.class);
                settingIntent.putExtra(StaticFields.KEY_BUNDLE_GENERAL,"settings");
                startActivity(settingIntent);
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (StaticFields.database.isOpen()){
            Log.d(TAG, "onDestroy: Closing database");
            StaticFields.database.close();
        }
        //saving preference data
        themePreference = getApplicationContext().getSharedPreferences(TAG,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = themePreference.edit();
        editor.putInt(TAG, StaticFields.darkThemeSet?1:0);
        Log.d(TAG, "onDestroy: Preference data saved");
        editor.commit();

    }

    public void databaseInit(){
        Log.d(TAG, "databaseInit: initializing database");
        try {
            //initializing database
            StaticFields.database = this.openOrCreateDatabase("PocketNotes", MODE_PRIVATE, null);
            StaticFields.database.execSQL("CREATE TABLE IF NOT EXISTS notes (id VARCHAR, title VARCHAR, content VARCHAR, dateCreated VARCHAR, dateUpdated VARCHAR)");
            StaticFields.database.execSQL("CREATE TABLE IF NOT EXISTS archives (id VARCHAR, title VARCHAR, content VARCHAR, dateCreated VARCHAR, dateUpdated VARCHAR)");

            if (StaticFields.database == null || StaticFields.getProfilesCount("notes")<=0) {
                //if table is empty or null
                EmptyNotesFragment emptyNotesFragment = new EmptyNotesFragment();
                Bundle bundle = new Bundle();
                bundle.putString(StaticFields.KEY_INTENT_EMPTYNOTES,"notes");
                emptyNotesFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, emptyNotesFragment)
                        .commit();
            }else {
                NotesListFragment fragment = new NotesListFragment();
                Bundle bundle = new Bundle();
                bundle.putString(StaticFields.KEY_FRAGMENT_MAINACTIVITY,"notes");
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment)
                        .commit();
            }
        }catch(SQLException e){
            Log.d(TAG, "onResume: Exception: "+e.getMessage());
        }
    }

    public void configureFAB(){
        Log.d(TAG, "configureFAB: creating fab button");
        final FloatingActionButton fab_add_text = findViewById(R.id.fab_text_note);
        fab_menu = findViewById(R.id.fab_menu);
        fab_add_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), NoteEditorActivity.class);
                i.putExtra(StaticFields.KEY_INTENT_EDITORACTIVITY,"new");
                i.putExtra(StaticFields.KEY_INTENT_TABLEID,"notes");
                startActivity(i);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        fab_menu.close(true);
    }
}
