package com.subhajitkar.commercial.projet_tulip.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefManager {
    private static final String TAG = "PrefManager";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    // shared pref mode
    private int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "com.subhajitkar.commercial.projet_tulip.utils";

    //keys for different preferences
    private static final String KEY_TAG_LIST = "tagsListFullScreenDialog";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
}
