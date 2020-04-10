package com.subhajitkar.commercial.projet_tulip.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class NotesDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "NotesDBHelper";
    private static final String DATABASE_NAME = "PocketNotes";
    public final String TABLE_NOTES = "notes";
    public final String TABLE_ARCHIVE = "archives";
    public final String ITEM_ID = "id";
    public final String ITEM_TITLE = "title";
    public final String ITEM_CONTENT = "content";
    public final String ITEM_CREATED_DATE = "dateCreated";
    public final String ITEM_UPDATED_DATE = "dateUpdated";

    public NotesDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NOTES+" (id VARCHAR, title VARCHAR, content VARCHAR, dateCreated VARCHAR, dateUpdated VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_ARCHIVE+" (id VARCHAR, title VARCHAR, content VARCHAR, dateCreated VARCHAR, dateUpdated VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: gets called.");
    }

    public int numberOfRows(String table){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, table);
        return numRows;
    }

    public Cursor getData(String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+table, null );
        return res;
    }

    public ContentValues createDBContentValue(String id, String title, String content, String dateCreated,String dateUpdated){
        Log.d(TAG, "createDBContentValue: making content values");
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_ID, id);
        contentValues.put(ITEM_TITLE, title);
        contentValues.put(ITEM_CONTENT, content);
        contentValues.put(ITEM_CREATED_DATE, dateCreated);
        contentValues.put(ITEM_UPDATED_DATE, dateUpdated);
        return contentValues;
    }

    public void insertNote (String table, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(table, null, contentValues);
    }

    public void updateNote (String table, String id, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(table, contentValues, "id = ? ", new String[] { id } );
    }

    public Integer deleteNote (String table, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table,
                "id = ? ", new String[] {id});
    }
}
