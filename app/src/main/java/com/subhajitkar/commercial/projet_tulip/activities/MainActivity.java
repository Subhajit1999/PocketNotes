package com.subhajitkar.commercial.projet_tulip.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.subhajitkar.commercial.projet_tulip.fragments.EmptyNotesFragment;
import com.subhajitkar.commercial.projet_tulip.fragments.NotesListFragment;
import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;

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
            case R.id.menu_add_note:
                Intent i = new Intent(getApplicationContext(), NoteEditorActivity.class);
                i.putExtra(StaticFields.KEY_INTENT_EDITORACTIVITY,"new");
                startActivity(i);
                break;
            case R.id.menu_archive:
                //archive menu action
                Intent intent = new Intent(this, GeneralActivity.class);
                intent.putExtra(StaticFields.KEY_BUNDLE_GENERAL,"archive");
                startActivity(intent);
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
}
