package com.subhajitkar.commercial.projet_tulip.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.fragments.NotesListFragment;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;

public class GeneralActivity extends AppCompatActivity {
    private static final String TAG = "GeneralActivity";

    private String identifier;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
