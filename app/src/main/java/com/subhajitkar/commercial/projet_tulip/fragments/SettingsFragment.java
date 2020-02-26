package com.subhajitkar.commercial.projet_tulip.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.activities.GeneralActivity;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;

public class SettingsFragment extends PreferenceFragment {
    private static final String TAG = "SettingsFragment";

    private SwitchPreference themeSwitch;
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
    }

    public void restartApp() {
        Intent settingIntent = new Intent(getActivity(), GeneralActivity.class);
        settingIntent.putExtra(StaticFields.KEY_BUNDLE_GENERAL,"settings");
        startActivity(settingIntent);
//        getActivity().finish();
    }
}
