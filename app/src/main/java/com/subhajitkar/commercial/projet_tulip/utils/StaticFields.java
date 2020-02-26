package com.subhajitkar.commercial.projet_tulip.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class StaticFields {
    private static final String TAG = "StaticFields";

    public static SQLiteDatabase database;
    public static ContentValues contentValues;
    public static String KEY_INTENT_EDITORACTIVITY = "KEY_INTENT_EDITORACTIVITY#264289";
    public static String KEY_INTENT_LISTPOSITION = "KEY_INTENT_LISTPOSITION#327964";
    public static String visibleDateFormat = "MMM dd,yyyy | H:mm a";
    public static String UniqueIdDateFormat = "MMMddyyyy_HH:mm:ss";
    public static String KEY_INTENT_EMPTYNOTES = "KEY_INTENT_EMPTYNOTES#317964";
    public static String KEY_FRAGMENT_MAINACTIVITY = "KEY_FRAGMENT_MAINACTIVITY#319648";
    public static String KEY_BUNDLE_GENERAL = "KEY_BUNDLE_GENERAL#978610";
    public static boolean darkThemeSet = false;


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
