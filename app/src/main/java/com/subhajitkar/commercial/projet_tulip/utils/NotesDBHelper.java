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
    public final String TABLE_STAR = "stars";
    public final String TABLE_TRASH = "trash";
    public final String ITEM_ID = "id";
    public final String ITEM_TITLE = "title";
    public final String ITEM_CONTENT = "content";
    public final String ITEM_CREATED_DATE = "dateCreated";
    public final String ITEM_UPDATED_DATE = "dateUpdated";
    public final String ITEM_EDITOR_TYPE = "editorType";
    public final String ITEM_STAR = "star";
    public final String ITEM_TAG = "tag";
    public final String ITEM_TABLE_ID = "tableId";

    public NotesDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NOTES+" (id VARCHAR, title VARCHAR, content VARCHAR, dateCreated VARCHAR, dateUpdated VARCHAR, editorType VARCHAR, star VARCHAR, tag VARACHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_ARCHIVE+" (id VARCHAR, title VARCHAR, content VARCHAR, dateCreated VARCHAR, dateUpdated VARCHAR, editorType VARCHAR, star VARCHAR, tag VARACHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_STAR+" (id VARCHAR, title VARCHAR, content VARCHAR, dateCreated VARCHAR, dateUpdated VARCHAR, editorType VARCHAR, tableId VARCHAR, tag VARACHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_TRASH+" (id VARCHAR, title VARCHAR, content VARCHAR, dateCreated VARCHAR, dateUpdated VARCHAR, editorType VARCHAR, star VARCHAR, tag VARACHAR)");
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

    public ContentValues createDBContentValue(String table, String id, String title, String content, String dateCreated,String dateUpdated, String editorType, String tableId_star, String tag){
        Log.d(TAG, "createDBContentValue: making content values");
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_ID, id);
        contentValues.put(ITEM_TITLE, title);
        contentValues.put(ITEM_CONTENT, content);
        contentValues.put(ITEM_CREATED_DATE, dateCreated);
        contentValues.put(ITEM_UPDATED_DATE, dateUpdated);
        contentValues.put(ITEM_EDITOR_TYPE, editorType);
        if (table.equals(TABLE_STAR)){
            contentValues.put(ITEM_TABLE_ID, tableId_star);
        }else{
            contentValues.put(ITEM_STAR, tableId_star);
        }
        contentValues.put(ITEM_TAG, tag);
        return contentValues;
    }

    public void insertNote (String table, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(table, null, contentValues);
    }

    public void updateNote (String table, String searchKey, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(table, contentValues, "id = ?", new String[] { searchKey } );
    }

    public Integer deleteNote (String table, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table,
                "id = ? ", new String[] {id});
    }
}
