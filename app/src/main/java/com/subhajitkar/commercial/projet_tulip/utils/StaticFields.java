package com.subhajitkar.commercial.projet_tulip.utils;

import android.Manifest;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.subhajitkar.commercial.projet_tulip.R;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StaticFields {
  private static final String TAG = "StaticFields";
  public static final String Store_URL ="http://play.google.com/store/apps/details?id=com.subhajitkar.commercial.projet_tulip";
  public static final String KEY_INTENT_WEBVIEW = "KEY_INTENT_WEBVIEW#937195";
  public static NotesDBHelper dbHelper;
  public static final String KEY_INTENT_EDITORACTIVITY = "KEY_INTENT_EDITORACTIVITY#264289";
  public static final String KEY_INTENT_LISTPOSITION = "KEY_INTENT_LISTPOSITION#327964";
  public static final String KEY_INTENT_TABLEID = "KEY_INTENT_TABLEID#985378";
  public static final String visibleDateFormat = "MMM dd,yyyy | H:mm a";
  public static final String UniqueIdDateFormat = "MMMddyyyy_HH:mm:ss";
  public static final String KEY_INTENT_EMPTYNOTES = "KEY_INTENT_EMPTYNOTES#317964";
  public static final String KEY_FRAGMENT_MAINACTIVITY = "KEY_FRAGMENT_MAINACTIVITY#319648";
  public static final String KEY_BUNDLE_GENERAL = "KEY_BUNDLE_GENERAL#978610";
  public static final String KEY_INTENT_WEBTITLE = "KEY_INTENT_WEBTITLE#379516";
  public static final String KEY_EDITOR_ID = "KEY_EDITOR_ID#307964";
  public static final String KEY_EDITOR_TITLE = "KEY_EDITOR_TITLE#307921";
  public static final String KEY_EDITOR_CONTENT = "KEY_EDITOR_CONTENT#521097";
  public static final String KEY_EDITOR_DATECREATED = "KEY_EDITOR_DATECREATED#462197";
  public static final String KEY_EDITOR_INTENTFLAG = "KEY_EDITOR_INTENTFLAG#379468";
  public static final String KEY_RICH_NOTE_EXT = "KEY_RICH_NOTE_EXT#397194";
  public static boolean darkThemeSet = false;
  public static String[] permissions = new String[] {
          Manifest.permission.WRITE_EXTERNAL_STORAGE
  };
  public static final int PERMISSION_CODE = 10;
  public static String noteTitle, noteContent, noteExtension;
  public static int[] colorLists = new int[]{android.R.color.holo_blue_bright,android.R.color.holo_green_light,android.R.color.holo_blue_light,
          android.R.color.holo_orange_light,android.R.color.holo_purple,android.R.color.holo_red_light
          , R.color.darkestGray};

  public static ArrayList<DataModel> listNewNote;
  public static void newNoteListInit() {
    Log.d(TAG, "newNoteListInit: initializing new note options list");
    listNewNote = new ArrayList<>();
    listNewNote.add(new DataModel("Simple note", R.drawable.ic_file, R.drawable.ic_file_dark));
    listNewNote.add(new DataModel("Image note", R.drawable.ic_fab_image, R.drawable.ic_fab_image_dark));
    listNewNote.add(new DataModel("Voice note", R.drawable.ic_fab_voice, R.drawable.ic_fab_voice_dark));
    listNewNote.add(new DataModel("Todo list", R.drawable.ic_fab_todo, R.drawable.ic_fab_todo_dark));
  }

  public static String stringListToJson(ArrayList<String> list) {
    Log.d(TAG, "listToJson: gets called");
    Gson gson = new Gson();
      return gson.toJson(list);
  }

  public static String objListToJson(ArrayList<DataModel> list) {
    Log.d(TAG, "listToJson: gets called");
    Gson gson = new Gson();
    return gson.toJson(list);
  }

  public static ArrayList<DataModel> jsonToObjList(String jsonString) {
    Log.d(TAG, "jsonToList: gets called");
    ArrayList<DataModel> list;
    Gson gson = new Gson();
    if (!jsonString.isEmpty()){
      Type type = new TypeToken<ArrayList<DataModel>>() {
      }.getType();

      list = gson.fromJson(jsonString, type);
    }else{
      list = new ArrayList<>();
    }
    return list;
  }

  public static ArrayList<String> jsonToStringList(String jsonString) {
    Log.d(TAG, "jsonToList: gets called");
    ArrayList<String> list;
    Gson gson = new Gson();
    if (!jsonString.isEmpty()){
      Type type = new TypeToken<ArrayList<String>>() {
      }.getType();

      list = gson.fromJson(jsonString, type);
    }else{
      list = new ArrayList<>();
    }
    return list;
  }

  public static String getDateTime(String format){
    Log.d(TAG, "getDateTime: getting current date, time in specified format");
    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
    return sdf.format(new Date());
  }
}