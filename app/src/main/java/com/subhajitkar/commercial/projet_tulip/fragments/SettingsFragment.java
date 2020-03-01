package com.subhajitkar.commercial.projet_tulip.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.activities.GeneralActivity;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;

import java.lang.reflect.Type;

public class SettingsFragment extends PreferenceFragment {
    private static final String TAG = "SettingsFragment";

    private SwitchPreference themeSwitch;
    private Preference email,rate,share,toc,privacy,copyright;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (StaticFields.darkThemeSet){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            getActivity().setTheme(R.style.DarkTheme);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            getActivity().setTheme(R.style.AppTheme);
        }
        addPreferencesFromResource(R.xml.preferences);

        //switch for theme change
        themeSwitch = (SwitchPreference) findPreference("key_change_theme");
        themeSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().equals("true")){
                    StaticFields.darkThemeSet = true;
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    restartApp();
                }else{
                    StaticFields.darkThemeSet = false;
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    restartApp();
                }
                return true;
            }
        });
        //email preference
        email = findPreference("key_contact_email");
        if (StaticFields.darkThemeSet){
            email.setIcon(R.drawable.settings_help_dark);
        }else{
            email.setIcon(R.drawable.settings_help);
        }
        email.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //email intent
                mailSupport();
                return true;
            }
        });
        //rate preference
        rate = findPreference("key_rate_review");
        if (StaticFields.darkThemeSet){
            rate.setIcon(R.drawable.settings_rate_dark);
        }else{
            rate.setIcon(R.drawable.settings_rate);
        }
        rate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //rate and review intent
                rateApp();
                return true;
            }
        });
        //share preference
        share = findPreference("key_app_share");
        if (StaticFields.darkThemeSet){
            share.setIcon(R.drawable.ic_share_dark);
        }else{
            share.setIcon(R.drawable.ic_share);
        }
        share.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //share app intent
                shareApp();
                return true;
            }
        });
        //terms and conditions web intent
        toc = findPreference("key_terms_conditions");
        toc.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //toc web intent
                getActivity().finish();
                Intent i = new Intent(getActivity(),GeneralActivity.class);
                i.putExtra(StaticFields.KEY_BUNDLE_GENERAL,"webView");
                i.putExtra(StaticFields.KEY_INTENT_WEBTITLE,"Terms and conditions");
                i.putExtra(StaticFields.KEY_INTENT_WEBVIEW,"file:///android_asset/terms_and_conditions.html");
                startActivity(i);
                return true;
            }
        });
        //privacy policy web intent
        privacy = findPreference("key_privacy_policy");
        privacy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //privacy policy web intent
                getActivity().finish();   //first finishes the GeneralActivity instance
                Intent i = new Intent(getActivity(),GeneralActivity.class);  //taking back to the GeneralActivity through intent
                i.putExtra(StaticFields.KEY_BUNDLE_GENERAL,"webView");  //with new fragment
                i.putExtra(StaticFields.KEY_INTENT_WEBTITLE,"Privacy policy");
                i.putExtra(StaticFields.KEY_INTENT_WEBVIEW,"file:///android_asset/privacy_policy.html");
                startActivity(i);
                return true;
            }
        });

        copyright = findPreference("key_copyright");
        if (StaticFields.darkThemeSet){
            copyright.setIcon(R.drawable.settings_copyright_dark);
        }else{
            copyright.setIcon(R.drawable.settings_copyright);
        }
    }

    private void shareApp(){
        Log.d(TAG, "shareApp: sharing app");
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi! Download and use this awesome, simple note app, "
                +getResources().getString(R.string.app_name)+".\n Download from PlayStore:\n"+StaticFields.Store_URL);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share app:"));
    }

    private void rateApp(){
        Log.d(TAG, "rateApp: taking to the play store for rating");
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
        }
    }

    private void mailSupport(){
        Log.d(TAG, "mailSupport: sending to the mail client");
        Intent feedback = new Intent(Intent.ACTION_SEND);
        feedback.setData(Uri.parse("mailto:"));
        String[] emailTo = {"developercontact.subhajitkar@gmail.com"};
        feedback.putExtra(Intent.EXTRA_EMAIL,emailTo);
        feedback.putExtra(Intent.EXTRA_SUBJECT,"feedback/query related "+getString(R.string.app_name)+" app");
        feedback.putExtra(Intent.EXTRA_TEXT,"Write what you want...");
        feedback.setType("message/rfc822");
        startActivity(Intent.createChooser(feedback,"Send Email with..."));
    }
}
