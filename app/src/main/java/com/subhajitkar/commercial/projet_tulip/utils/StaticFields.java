package com.subhajitkar.commercial.projet_tulip.utils;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.objects.DataModel;
import com.subhajitkar.commercial.projet_tulip.objects.ObjectNote;

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
  public static final String KEY_INTENT_LISTOBJECT = "KEY_INTENT_LISTOBJECT#327964";
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
  public static final String KEY_CLICKED_NOTE_OBJ = "KEY_CLICKED_NOTE_OBJ#397194";
  public static boolean darkThemeSet = false;
  public static int canvasStrokeWidth, canvasStrokeColor, canvasStrokeStyle;
  public static String colorHex;
  public static Point colorPickerPosition;
  public static String canvasTitleTemplate = "Title_canvas_drawing_note@";
  public static String[] permissions = new String[] {
          Manifest.permission.WRITE_EXTERNAL_STORAGE
  };
  public static final int PERMISSION_CODE = 10;
  public static String noteTitle, noteContent, shareNoteTitle, shareNoteContent;
  public static ObjectNote itemClickNoteObject;  //temp for now, should remove in future
  public static Bitmap shareCanvasBitmap;
  public static int[] colorLists = new int[]{android.R.color.holo_blue_bright,android.R.color.holo_green_light,android.R.color.holo_blue_light,
          android.R.color.holo_orange_light,android.R.color.holo_purple,android.R.color.holo_red_light
          , R.color.darkestGray};

  public static ArrayList<DataModel> listNewNote;
  public static void newNoteListInit() {
    Log.d(TAG, "newNoteListInit: initializing new note options list");
    listNewNote = new ArrayList<>();
    listNewNote.add(new DataModel("Simple note", R.drawable.ic_file, R.drawable.ic_file_dark));
    listNewNote.add(new DataModel("Drawing note", R.drawable.ic_fab_drawing, R.drawable.ic_fab_drawing_dark));
    listNewNote.add(new DataModel("Todo list", R.drawable.ic_fab_todo, R.drawable.ic_fab_todo_dark));
  }

  public static String getDateTime(String format){
    Log.d(TAG, "getDateTime: getting current date, time in specified format");
    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
    return sdf.format(new Date());
  }
}