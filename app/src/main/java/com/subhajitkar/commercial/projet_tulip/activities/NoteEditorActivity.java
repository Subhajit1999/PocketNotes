package com.subhajitkar.commercial.projet_tulip.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.EditText;


import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Type;
import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.fragments.CanvasFragment;
import com.subhajitkar.commercial.projet_tulip.fragments.SimpleNoteFragment;
import com.subhajitkar.commercial.projet_tulip.fragments.ToDoNoteFragment;
import com.subhajitkar.commercial.projet_tulip.objects.ObjectNote;
import com.subhajitkar.commercial.projet_tulip.utils.OnStorage;
import com.subhajitkar.commercial.projet_tulip.utils.PortableContent;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;
import com.werdpressed.partisan.rundo.RunDo;

import org.parceler.Parcels;

import java.io.File;

public class NoteEditorActivity extends AppCompatActivity implements RunDo.TextLink {
    private static final String TAG = "NoteEditorActivity";

    private Cursor c;
    private String intentFlag, UniqueId, createdDateAndTime, table;
    private int idIndex, titleIndex, contentIndex,createdDateIndex, updatedDateIndex, editorTypeIndex, starIndex, tagIndex;
    private String sendNoteTitle, sendNoteContent, sendCreatedDateTime, editorType, star, tag;
    private RunDo mRunDo;
    private SimpleNoteFragment fragment;
    private ObjectNote objectNote;
    private PortableContent portableContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (StaticFields.darkThemeSet){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.DarkTheme);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_note_editor);

        //customizing the actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        portableContent = new PortableContent(this);
        table = getIntent().getStringExtra(StaticFields.KEY_INTENT_TABLEID);
        try {
            //initializing database primitives
            c = StaticFields.dbHelper.getData(table);
            idIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_ID);
            titleIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_TITLE);
            contentIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_CONTENT);
            createdDateIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_CREATED_DATE);
            updatedDateIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_UPDATED_DATE);
            editorTypeIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_EDITOR_TYPE);
            starIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_STAR);
            tagIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_TAG);
        }catch(Exception e){
            portableContent.showSnackBar(Type.ERROR, "Some error occurred. ("+e.getMessage()+")",
                    Duration.SHORT);
        }

        //getting the intent data
        intentFlag = getIntent().getStringExtra(StaticFields.KEY_INTENT_EDITORACTIVITY);
        Log.d(TAG, "onCreate: intentFlag: "+intentFlag);

        if (intentFlag.equals("existing")){
            Bundle bundle = getIntent().getBundleExtra(StaticFields.KEY_INTENT_LISTOBJECT);
            objectNote = Parcels.unwrap(getIntent().getParcelableExtra(StaticFields.KEY_CLICKED_NOTE_OBJ));
            getSupportActionBar().setTitle("Edit note");
            UniqueId = objectNote.getmNoteId();
            sendNoteTitle = objectNote.getmNoteTitle();
            sendNoteContent = objectNote.getmNoteContent();
            editorType = objectNote.getmEditorType();
            if (!table.equals(StaticFields.dbHelper.TABLE_STAR)) {
                star = String.valueOf(objectNote.getIsStarred());
            }
            tag = objectNote.getmTag();
            createdDateAndTime = sendCreatedDateTime = objectNote.getmDateCreated();
        }else{      //if new
            if (getIntent()!=null){
                editorType = getIntent().getStringExtra(StaticFields.KEY_EDITOR_ID);
            }
            getSupportActionBar().setTitle("New note");
        }
        launchFragment();
        if (editorType.equals("simple")) {
            //undo, redo library init
            mRunDo = RunDo.Factory.getInstance(getFragmentManager());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: creating menu");
        getMenuInflater().inflate(R.menu.menu_editor,menu);
        if (!editorType.equals("simple")){
            menu.findItem(R.id.editor_undo).setVisible(false);
            menu.findItem(R.id.editor_redo).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: back button clicked");
                onBackPressed();
                break;
            case R.id. editor_undo:
                Log.d(TAG, "onOptionsItemSelected: undo pressed");
                mRunDo.undo();
                break;
            case R.id.editor_redo:
                Log.d(TAG, "onOptionsItemSelected: redo pressed");
                mRunDo.redo();
                break;
            case R.id.editor_share:
                Log.d(TAG, "onOptionsItemSelected: editor share option");
                boolean flag = false;
                if (!editorType.equals("drawing")) {
                    if (editorType.equals("simple")) {
                        SimpleNoteFragment fragment = (SimpleNoteFragment) getSupportFragmentManager().findFragmentByTag("TAG_SIMPLE_FRAGMENT");
                        flag = fragment.onSharing();
                    } else if (editorType.equals("todoList")) {
                        ToDoNoteFragment fragment = (ToDoNoteFragment) getSupportFragmentManager().findFragmentByTag("TAG_TODO_FRAGMENT");
                        flag = fragment.onSharing();
                    }
                    if (flag) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, StaticFields.shareNoteTitle + "\n\n" +
                                StaticFields.shareNoteContent + "\n\n================\nShared from Pocket Notes, a truly simple notepad app.\n\nDownload now:)\n"
                                + StaticFields.Store_URL);
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent, "Share note with..."));
                    }
                }else{
                    CanvasFragment fragmentCanvas = (CanvasFragment) getSupportFragmentManager().findFragmentByTag("TAG_CANVAS_FRAGMENT");
                    if (fragmentCanvas.onSharing()){

                        File file = OnStorage.createImageFile(StaticFields.shareNoteTitle,StaticFields.shareCanvasBitmap,this);
                        Uri fileUri = FileProvider.getUriForFile(this, this.getPackageName(), file);
                        Intent sendIntent = ShareCompat.IntentBuilder.from(this)
                            .setType(this.getContentResolver().getType(fileUri))
                            .setStream(fileUri)
                            .getIntent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setData(fileUri);
                    //setting proper mime type
                    MimeTypeMap map = MimeTypeMap.getSingleton();
                    String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
                    String type = map.getMimeTypeFromExtension(ext);
                    if (type == null) {
                        type = "*/*";
                    }
                    sendIntent.setType(type);
                    sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    startActivity(Intent.createChooser(sendIntent, "Share file with..."));
                    }
                }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (editorType.equals("simple")){
            final SimpleNoteFragment fragment = (SimpleNoteFragment) getSupportFragmentManager().findFragmentByTag("TAG_SIMPLE_FRAGMENT");
            if (fragment.backPressedSimple()) {
                super.onBackPressed();
                supportFinishAfterTransition();
            }
        }else if (editorType.equals("todoList")) {
            ToDoNoteFragment fragment = (ToDoNoteFragment) getSupportFragmentManager().findFragmentByTag("TAG_TODO_FRAGMENT");
            if (fragment.backPressed()) {
                super.onBackPressed();
                supportFinishAfterTransition();
            }
        }else if (editorType.equals("drawing")) {
            CanvasFragment fragmentCanvas = (CanvasFragment) getSupportFragmentManager().findFragmentByTag("TAG_CANVAS_FRAGMENT");
            if (fragmentCanvas.backPressedCanvas()) {
                super.onBackPressed();
                supportFinishAfterTransition();
            }
        }else{
            super.onBackPressed();
            supportFinishAfterTransition();
        }
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: actual saving mechanism is here");

        boolean AlreadyThere = false;
        //getting the created date and time
        String currentDateAndTime = StaticFields.getDateTime(StaticFields.visibleDateFormat);
        String noteUniqueId = StaticFields.getDateTime(StaticFields.UniqueIdDateFormat);

        try {
            //checking if note already exists in the database or not
            if (intentFlag.equals("existing")) {  //note already exists
                if (!StaticFields.noteTitle.isEmpty()) {
                    Log.d(TAG, "onPause: updating note");
                    ContentValues contentValues = StaticFields.dbHelper.createDBContentValue(table, UniqueId,
                            StaticFields.noteTitle, StaticFields.noteContent, createdDateAndTime, currentDateAndTime, editorType
                            ,star, tag);
                    StaticFields.dbHelper.updateNote(table,UniqueId, contentValues);
                    if(Boolean.parseBoolean(star)){
                        //update in star table also
                        ContentValues starValues = StaticFields.dbHelper.createDBContentValue(StaticFields.dbHelper.TABLE_STAR,
                                UniqueId, StaticFields.noteTitle, StaticFields.noteContent, createdDateAndTime, currentDateAndTime,
                                editorType, table, tag);
                        StaticFields.dbHelper.updateNote(StaticFields.dbHelper.TABLE_STAR, UniqueId, starValues);
                    }
                }
                c = StaticFields.dbHelper.getData(table);

            } else {          //indicates note doesn't exist or first element
                Log.d(TAG, "onPause: new note. NumberRows: "+StaticFields.dbHelper.numberOfRows(table));
                for (int i = 0; i < StaticFields.dbHelper.numberOfRows(table); i++) {
                    c.moveToPosition(i);
                    if (c.getString(titleIndex).equals(StaticFields.noteTitle)) {  //if title matches to existing
                        AlreadyThere = true;
                        break;
                    }
                }
                if (!AlreadyThere && !StaticFields.noteTitle.isEmpty()) {   //if new then insert
                    Log.d(TAG, "onPause: new note saving into database");
                    ContentValues contentValues = StaticFields.dbHelper.createDBContentValue(table, noteUniqueId,
                            StaticFields.noteTitle, StaticFields.noteContent, currentDateAndTime, currentDateAndTime,
                            editorType, "false", null);
                    StaticFields.dbHelper.insertNote(table,contentValues);
                    Log.d(TAG, "onPause: new note created");
                }
            }
            Log.d(TAG, "onPause: content:"+StaticFields.noteContent);
            c.close();
        }catch(Exception e){
            portableContent.showSnackBar(Type.ERROR, "Some error occurred.",
                    Duration.SHORT);
            Log.d(TAG, "onPause: error occurred. Message: "+e.getMessage());
        }
        super.onPause();
    }

    public void launchFragment(){
        Log.d(TAG, "launchFragment: launching editor fragment");
        Bundle bundle = new Bundle();
        switch(editorType){
            case "simple":
                //launch simpleNoteFragment
                fragment = new SimpleNoteFragment();
                bundle.putString(StaticFields.KEY_EDITOR_TITLE, sendNoteTitle);
                bundle.putString(StaticFields.KEY_EDITOR_CONTENT, sendNoteContent);
                bundle.putString(StaticFields.KEY_EDITOR_DATECREATED, sendCreatedDateTime);
                bundle.putString(StaticFields.KEY_EDITOR_INTENTFLAG, intentFlag);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.editor_frag_container, fragment, "TAG_SIMPLE_FRAGMENT")
                        .addToBackStack(null)
                        .commit();
                break;
            case "drawing":
                CanvasFragment canvasFragment = new CanvasFragment();
                bundle.putString(StaticFields.KEY_EDITOR_TITLE, sendNoteTitle);
                bundle.putString(StaticFields.KEY_EDITOR_CONTENT, sendNoteContent);
                bundle.putString(StaticFields.KEY_EDITOR_DATECREATED, sendCreatedDateTime);
                bundle.putString(StaticFields.KEY_EDITOR_INTENTFLAG, intentFlag);
                canvasFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.editor_frag_container, canvasFragment, "TAG_CANVAS_FRAGMENT")
                        .addToBackStack(null)
                        .commit();
                break;
            case "todoList":
                ToDoNoteFragment fragmentToDo = new ToDoNoteFragment();
                bundle.putString(StaticFields.KEY_EDITOR_TITLE, sendNoteTitle);
                bundle.putString(StaticFields.KEY_EDITOR_CONTENT, sendNoteContent);
                bundle.putString(StaticFields.KEY_EDITOR_DATECREATED, sendCreatedDateTime);
                bundle.putString(StaticFields.KEY_EDITOR_INTENTFLAG, intentFlag);
                fragmentToDo.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.editor_frag_container, fragmentToDo, "TAG_TODO_FRAGMENT")
                        .addToBackStack(null)
                        .commit();
                break;

        }
    }

    @Override
    public EditText getEditTextForRunDo() {
        return fragment.getEditText();
    }
}

