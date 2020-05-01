package com.subhajitkar.commercial.projet_tulip.activities;import androidx.annotation.NonNull;import androidx.appcompat.app.ActionBarDrawerToggle;import androidx.appcompat.app.AlertDialog;import androidx.appcompat.app.AppCompatActivity;import androidx.appcompat.app.AppCompatDelegate;import androidx.appcompat.widget.Toolbar;import androidx.core.view.GravityCompat;import androidx.drawerlayout.widget.DrawerLayout;import androidx.fragment.app.FragmentManager;import android.content.Context;import android.content.DialogInterface;import android.content.Intent;import android.content.SharedPreferences;import android.graphics.Color;import android.os.Bundle;import android.util.Log;import android.view.MenuItem;import com.google.android.material.navigation.NavigationView;import com.subhajitkar.commercial.projet_tulip.fragments.EmptyNotesFragment;import com.subhajitkar.commercial.projet_tulip.fragments.NotesListFragment;import com.subhajitkar.commercial.projet_tulip.R;import com.subhajitkar.commercial.projet_tulip.fragments.TagsDialogFragment;import com.subhajitkar.commercial.projet_tulip.utils.NotesDBHelper;import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{    private static final String TAG = "MainActivity";    private SharedPreferences themePreference;    private NavigationView navigationView;    private Toolbar toolbar;    private DrawerLayout drawer;    private static int navFragmentId=0;    private ActionBarDrawerToggle toggle;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        Log.d(TAG, "onCreate: activity created");        //getting preference data        Log.d(TAG, "onCreate: getting data");        themePreference = getApplicationContext().getSharedPreferences(TAG,Context.MODE_PRIVATE);        StaticFields.darkThemeSet = themePreference.getInt(TAG, 0) == 1;        Log.d(TAG, "onCreate: Dark theme: "+StaticFields.darkThemeSet);        if (StaticFields.darkThemeSet){            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);            setTheme(R.style.AppTheme_noActionBar_dark);        }else{            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);            setTheme(R.style.AppTheme_noActionBar);        }        setContentView(R.layout.activity_main);        //widgets init        navigationView = findViewById(R.id.nav_view_main);        toolbar = findViewById(R.id.toolbar);        //setting up toolbar        setSupportActionBar(toolbar);        getSupportActionBar().setDisplayHomeAsUpEnabled(true);        getSupportActionBar().setHomeButtonEnabled(true);        toolbar.setTitleTextColor(Color.WHITE);        //setting up navigation drawer        drawer = findViewById(R.id.drawerLayout);        toggle = new ActionBarDrawerToggle(                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);        drawer.addDrawerListener(toggle);        toggle.syncState();        navigationView.setNavigationItemSelectedListener(this);    }    @Override    protected void onResume() {        Log.d(TAG, "onResume: resuming activity");        super.onResume();        databaseInit();  //init database    }    @Override    protected void onDestroy() {        super.onDestroy();        //saving preference data        themePreference = getApplicationContext().getSharedPreferences(TAG,Context.MODE_PRIVATE);        SharedPreferences.Editor editor = themePreference.edit();        editor.putInt(TAG, StaticFields.darkThemeSet?1:0);        Log.d(TAG, "onDestroy: Preference data saved");        editor.commit();    }    @Override    public boolean onOptionsItemSelected(MenuItem item) {  //handles menu items click functions        Log.d(TAG, "onOptionsItemSelected: menu click handle in mainActivity");        if (toggle.onOptionsItemSelected(item)) {            return true;        }        return super.onOptionsItemSelected(item);    }    public void databaseInit(){        Log.d(TAG, "databaseInit: initializing database");        StaticFields.dbHelper = new NotesDBHelper(this);            if (navFragmentId==0) {                navigationView.setCheckedItem(R.id.menu_home);            }            launchTopNavigationFragments(navFragmentId);  //home fragment    }    @Override    public boolean onNavigationItemSelected(@NonNull MenuItem item) {        Log.d(TAG, "onNavigationItemSelected: navigation drawer item click action");        navigationView.setCheckedItem(item);        popFragmentBackStack(getSupportFragmentManager());        switch(item.getItemId()){            case R.id.menu_home:                //home navigation action                launchTopNavigationFragments(0);                break;            case R.id.menu_bookmark:                //bookmark navigation action                launchTopNavigationFragments(1);                break;            case R.id.menu_archive:                //archive navigation action                launchTopNavigationFragments(2);                break;            case R.id.menu_recycle_bin:                //trash bin navigation action                launchTopNavigationFragments(3);                break;            case R.id.menu_settings:                //settings navigation action                handleNavigationIntents("settings");                break;        }        drawer.closeDrawer(GravityCompat.START);        return true;    }    @Override    public void onBackPressed() {        Log.d(TAG, "onBackPressed: back button pressed");        if (drawer.isDrawerOpen(GravityCompat.START)) {            drawer.closeDrawer(GravityCompat.START);        }        if (isTaskRoot()) {    //if last activity, show message            Log.d(TAG, "onBackPressed: last activity");            String title = "Exit From App?";            String msg = "Are you sure you want to exit from app?";            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppDialogTheme);            builder.setTitle(title);            builder.setMessage(msg);            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {                @Override                public void onClick(DialogInterface dialog, int which) {                    finish();                    dialog.dismiss();                }            });            builder.setNegativeButton("No", null);            builder.show();        }    }    private void launchTopNavigationFragments(int FragIndex){        Log.d(TAG, "launchTopNavigationFragments: Handling top level navigation");        switch(FragIndex){            case 0:  //for home                navFragmentId = 0;                toolbar.setTitle(R.string.app_name);                NotesListFragment fragment = new NotesListFragment();                Bundle bundle = new Bundle();                bundle.putString(StaticFields.KEY_FRAGMENT_MAINACTIVITY,StaticFields.dbHelper.TABLE_NOTES);                fragment.setArguments(bundle);                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment)                        .addToBackStack("home")                        .commit();                break;            case 1:  //for bookmarked                navFragmentId = 1;                toolbar.setTitle("Starred");                NotesListFragment fragment1 = new NotesListFragment();                Bundle bundle1 = new Bundle();                bundle1.putString(StaticFields.KEY_FRAGMENT_MAINACTIVITY,StaticFields.dbHelper.TABLE_STAR);                fragment1.setArguments(bundle1);                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment1)                        .addToBackStack("starred")                        .commit();                break;            case 2:  //for archived                navFragmentId = 2;                toolbar.setTitle("Archived");                NotesListFragment fragment2 = new NotesListFragment();                Bundle bundle2 = new Bundle();                bundle2.putString(StaticFields.KEY_FRAGMENT_MAINACTIVITY,StaticFields.dbHelper.TABLE_ARCHIVE);                fragment2.setArguments(bundle2);                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment2)                        .addToBackStack("archive")                        .commit();                break;            case 3:  //for trash bin                navFragmentId = 3;                toolbar.setTitle("Trash bin");                NotesListFragment fragment3 = new NotesListFragment();                Bundle bundle3 = new Bundle();                bundle3.putString(StaticFields.KEY_FRAGMENT_MAINACTIVITY,StaticFields.dbHelper.TABLE_TRASH);                fragment3.setArguments(bundle3);                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment3)                        .addToBackStack("trash")                        .commit();                break;        }    }    private void handleNavigationIntents(String intentId){        Log.d(TAG, "handleNavigationIntents: handling navigation intents");        switch(intentId){            case "settings":                Intent i = new Intent(this, GeneralActivity.class);                i.putExtra(StaticFields.KEY_BUNDLE_GENERAL, "settings");                startActivity(i);                break;        }    }    private void popFragmentBackStack(FragmentManager manager){        for(int i=0; i<manager.getBackStackEntryCount(); i++){            Log.d(TAG, "popFragmentBackStack: popping out fragments from backStack. Count: "+manager.getBackStackEntryCount());            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(i);            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);        }    }}