package com.subhajitkar.commercial.projet_tulip.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.fragments.NotesListFragment;
import com.subhajitkar.commercial.projet_tulip.fragments.SettingsFragment;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;

public class GeneralActivity extends AppCompatActivity {
    private static final String TAG = "GeneralActivity";

    private String identifier;
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
        setContentView(R.layout.activity_general);

        //getting arguments
        getIntentData();

        //customizing the actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (identifier.equals("archive")) {
            getSupportActionBar().setTitle("Archives");

            //adding fragment
            NotesListFragment fragment = new NotesListFragment();
            Bundle bundle = new Bundle();
            bundle.putString(StaticFields.KEY_FRAGMENT_MAINACTIVITY,"archives");
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment)
                    .commit();
        }else if(identifier.equals("settings")){
            getSupportActionBar().setTitle("Settings");

            if (findViewById(R.id.fragment_container)!=null) {
                //adding settings fragment
                if (savedInstanceState!=null){
                    return;
                }
                //getSupportFragmentManager() is not working here
                getFragmentManager().beginTransaction().add(R.id.fragment_container, new SettingsFragment()).commit();
            }
        }
    }

    public void getIntentData(){
        Log.d(TAG, "getBundleData: getting bundle data");
        if (getIntent()!=null){
            identifier = getIntent().getStringExtra(StaticFields.KEY_BUNDLE_GENERAL);
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
}
