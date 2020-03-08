package com.subhajitkar.commercial.projet_tulip.utils;

import android.Manifest;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.subhajitkar.commercial.projet_tulip.activities.MainActivity;

public class StaticFields {
    public static final String Store_URL ="http://play.google.com/store/apps/details?id=com.subhajitkar.commercial.projet_tulip";
    public static final String KEY_INTENT_WEBVIEW = "KEY_INTENT_WEBVIEW#937195";
    private static final String TAG = "StaticFields";

    public static SQLiteDatabase database;
    public static ContentValues contentValues;
    public static final String KEY_INTENT_EDITORACTIVITY = "KEY_INTENT_EDITORACTIVITY#264289";
    public static final String KEY_INTENT_LISTPOSITION = "KEY_INTENT_LISTPOSITION#327964";
    public static final String KEY_INTENT_TABLEID = "KEY_INTENT_TABLEID#985378";
    public static final String visibleDateFormat = "MMM dd,yyyy | H:mm a";
    public static final String UniqueIdDateFormat = "MMMddyyyy_HH:mm:ss";
    public static final String KEY_INTENT_EMPTYNOTES = "KEY_INTENT_EMPTYNOTES#317964";
    public static final String KEY_FRAGMENT_MAINACTIVITY = "KEY_FRAGMENT_MAINACTIVITY#319648";
    public static final String KEY_BUNDLE_GENERAL = "KEY_BUNDLE_GENERAL#978610";
    public static final String KEY_INTENT_WEBTITLE = "KEY_INTENT_WEBTITLE#379516";
    public static boolean darkThemeSet = false;
    public static String[] permissions = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final int PERMISSION_CODE = 10;


    public static int getProfilesCount(String table) {
        Log.d(TAG, "getProfilesCount: getting table length");
        String countQuery = "SELECT  * FROM " + table;
        int count=0;
        try {
            Cursor cursor = StaticFields.database.rawQuery(countQuery, null);
            count = cursor.getCount();
            Log.d(TAG, "getProfilesCount: item count: " + count);
            cursor.close();
        }catch(Exception e){
            Log.d(TAG, "getProfilesCount: Message: "+e.getMessage());
        }
        return count;
    }
}
