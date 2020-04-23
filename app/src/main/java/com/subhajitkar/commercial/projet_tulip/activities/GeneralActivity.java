package com.subhajitkar.commercial.projet_tulip.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewFragment;

import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.fragments.NotesListFragment;
import com.subhajitkar.commercial.projet_tulip.fragments.SettingsFragment;
import com.subhajitkar.commercial.projet_tulip.fragments.WebFragment;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;

public class GeneralActivity extends AppCompatActivity {
  private static final String TAG = "GeneralActivity";

  private String identifier, webUrl, title;
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
    if (identifier.equals("webView")){
      Log.d(TAG, "onCreate: loading web fragment");
      getSupportActionBar().setTitle(title);
      WebFragment fragment = new WebFragment();
      Bundle bundle = new Bundle();
      bundle.putString(StaticFields.KEY_INTENT_WEBVIEW,webUrl);
      fragment.setArguments(bundle);
      getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

    }else if (identifier.equals("settings")){
      Log.d(TAG, "onCreate: settings fragment");
      getSupportActionBar().setTitle("Settings");
      if (findViewById(R.id.fragment_container)!=null) {
        if (savedInstanceState!=null){
          return;
        }
        SettingsFragment fragment4 = new SettingsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment4)
                .addToBackStack("settings")
                .commit();
      }
    }
  }

  public void getIntentData(){
    Log.d(TAG, "getBundleData: getting bundle data");
    if (getIntent()!=null){
      identifier = getIntent().getStringExtra(StaticFields.KEY_BUNDLE_GENERAL);
      if (identifier.equals("webView")){
        webUrl = getIntent().getStringExtra(StaticFields.KEY_INTENT_WEBVIEW);
        title = getIntent().getStringExtra(StaticFields.KEY_INTENT_WEBTITLE);
      }
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
    if (identifier.equals("webView")) {  //when settings to webView
      finish();
      Intent i = new Intent(this, GeneralActivity.class);
      i.putExtra(StaticFields.KEY_BUNDLE_GENERAL, "settings");
      startActivity(i);
    } else {
      supportFinishAfterTransition();
    }
  }
}