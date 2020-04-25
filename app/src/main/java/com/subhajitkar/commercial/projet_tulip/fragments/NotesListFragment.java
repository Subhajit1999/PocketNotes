package com.subhajitkar.commercial.projet_tulip.fragments;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Type;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.activities.NoteEditorActivity;
import com.subhajitkar.commercial.projet_tulip.utils.BottomSheetRecyclerAdapter;
import com.subhajitkar.commercial.projet_tulip.utils.DialogListAdapter;
import com.subhajitkar.commercial.projet_tulip.utils.ListObject;
import com.subhajitkar.commercial.projet_tulip.utils.ObjectNote;
import com.subhajitkar.commercial.projet_tulip.utils.OnStorage;
import com.subhajitkar.commercial.projet_tulip.utils.PortableContent;
import com.subhajitkar.commercial.projet_tulip.utils.RecyclerAdapter;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesListFragment extends Fragment implements RecyclerAdapter.OnItemClickListener, RecyclerAdapter.OnItemLongClickListener, BottomSheetRecyclerAdapter.OnBottomSheetItemClickListener {
    private static final String TAG = "NotesListFragment";
    private RecyclerView notesRecycler;
    private String table;
    private RecyclerAdapter adapter;
    private FrameLayout root;
    private Cursor c;
    private ArrayList<ObjectNote> dataList;
    private int idIndex, titleIndex, contentIndex,createdDateIndex, updateDateIndex, editorTypeIndex,
            starIndex, tagIndex, tableIdIndex, mNotePosition;
    private static List<String> listPermissionsNeeded;
    private LinearLayout sortLayout;
    private FloatingActionButton addNoteMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: creating view");
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: fragment view created");
        super.onViewCreated(view, savedInstanceState);

        root = view.findViewById(R.id.root_listFragment);
        sortLayout = view.findViewById(R.id.layout_list_sort);
        //getting bundle data
        if (getArguments()!=null){
            table = getArguments().getString(StaticFields.KEY_FRAGMENT_MAINACTIVITY);
        }
        dataList = new ArrayList<>(); //initializing arraylist
        retrieveDB(); //retrieving database and getting items ino arrayList

        //preparing recyclerListView
        notesRecycler = view.findViewById(R.id.notes_recycler);
        notesRecycler.setHasFixedSize(true);
        notesRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        notesRecycler.setItemAnimator(new DefaultItemAnimator());

        //setting up the adapter
        if (!dataList.isEmpty()) {
            adapter = new RecyclerAdapter(getContext(),dataList);
//            if (table.equals(StaticFields.dbHelper.TABLE_NOTES)||table.equals(StaticFields.dbHelper.TABLE_ARCHIVE)){
                adapter.setOnItemClickListener(this);
                adapter.setOnItemLongClickListener(this);
//            }
            notesRecycler.setAdapter(adapter);
        }else{
            emptyNotesScreen();
        }
        configureFAB(view);
    }

    @Override
    public void onItemClick(int position) {
        Log.d(TAG, "onItemClick: recycler list item click action, position: "+position);
        Intent i = new Intent(getContext(), NoteEditorActivity.class);
        i.putExtra(StaticFields.KEY_INTENT_EDITORACTIVITY,"existing");
        i.putExtra(StaticFields.KEY_INTENT_TABLEID,table);
        i.putExtra(StaticFields.KEY_INTENT_LISTPOSITION,position);
        startActivity(i);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: gets called.");
        if (!c.isClosed()){
            c.close();
        }
    }

    @Override
    public void onResume() {
        retrieveDB();
        if (!dataList.isEmpty()) {
            adapter.notifyDataSetChanged();
        }
        Log.d(TAG, "onPause: updating dataList");
        super.onResume();
    }

    public void configureFAB(View view){
        Log.d(TAG, "configureFAB: creating fab button");
        addNoteMenu = view.findViewById(R.id.fab_new_note);

        //creating new note menu
        StaticFields.newNoteListInit();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppDialogTheme);
        builder.setAdapter(new DialogListAdapter(getContext(), StaticFields.listNewNote), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getActivity(), NoteEditorActivity.class);
                switch(which){
                    case 0:
                        i.putExtra(StaticFields.KEY_INTENT_EDITORACTIVITY,"new");
                        i.putExtra(StaticFields.KEY_INTENT_TABLEID,table);
                        i.putExtra(StaticFields.KEY_EDITOR_ID,"simple");
                        break;
                }
                startActivity(i);
                dialog.dismiss();
            }
        });
        addNoteMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });
    }

    @Override
    public void onItemLongClick(int position) {
        Log.d(TAG, "onItemLongClick: gets called");
        // using BottomSheetDialog
        View dialogView = getLayoutInflater().inflate(R.layout.recycler_menu_bottomsheet, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        //handle item click events
        RecyclerView bottomsheetRecycler = dialogView.findViewById(R.id.bottomSheet_recycler);
        //preparing recyclerListView
        bottomsheetRecycler.setHasFixedSize(true);
        bottomsheetRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        bottomsheetRecycler.setItemAnimator(new DefaultItemAnimator());
        View viewBSheet = dialogView.findViewById(R.id.view);;
        mNotePosition = position;
        //random view color
        int randInt = new Random().nextInt(7);
        viewBSheet.setBackgroundColor(getResources().getColor(StaticFields.colorLists[randInt]));
        //preparing data
        ArrayList<ListObject> itemsList = setUpbottomSheetItems(table, position);
        //setting up adapter
        BottomSheetRecyclerAdapter adapter = new BottomSheetRecyclerAdapter(getContext(), itemsList, dialog);
        bottomsheetRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        dialog.setContentView(dialogView);
        dialog.show();
    }
    private ArrayList<ListObject> setUpbottomSheetItems(String table, int position){
        Log.d(TAG, "setUpbottomSheetItems: setting up bottomsheet items");
        ArrayList<ListObject> itemsList = new ArrayList<>();

        if (table.equals(StaticFields.dbHelper.TABLE_NOTES)){  //for notes table
            itemsList.add(new ListObject("Archive", R.drawable.ic_archive, R.drawable.ic_archive_dark,
                    StaticFields.darkThemeSet?android.R.color.white:android.R.color.black));
        }else if (table.equals(StaticFields.dbHelper.TABLE_ARCHIVE)){  //for archives table
            itemsList.add(new ListObject("Unarchive", R.drawable.ic_unarchive, R.drawable.ic_unarchive_dark,
                    StaticFields.darkThemeSet?android.R.color.white:android.R.color.black));
        }
        if (table.equals(StaticFields.dbHelper.TABLE_NOTES)||table.equals(StaticFields.dbHelper.TABLE_ARCHIVE)) {
            if (!dataList.get(position).getIsStarred()) {
                // star/unStar check
                itemsList.add(new ListObject("Add to star", R.drawable.ic_star, R.drawable.ic_star_dark,
                        StaticFields.darkThemeSet ? android.R.color.white : android.R.color.black));
            } else {
                itemsList.add(new ListObject("remove star", R.drawable.ic_star, R.drawable.ic_star_dark,
                        StaticFields.darkThemeSet ? android.R.color.white : android.R.color.black));
            }
        }
        if (table.equals(StaticFields.dbHelper.TABLE_STAR)){
            itemsList.add(new ListObject("remove star", R.drawable.ic_star, R.drawable.ic_star_dark,
                    StaticFields.darkThemeSet ? android.R.color.white : android.R.color.black));
        }
        if (table.equals(StaticFields.dbHelper.TABLE_NOTES)||table.equals(StaticFields.dbHelper.TABLE_ARCHIVE)||
        table.equals(StaticFields.dbHelper.TABLE_TRASH)){
            itemsList.add(new ListObject("Delete", R.drawable.ic_delete, R.drawable.ic_delete,
                    android.R.color.holo_red_dark));
        }
        if (table.equals(StaticFields.dbHelper.TABLE_NOTES)||table.equals(StaticFields.dbHelper.TABLE_ARCHIVE)||
                table.equals(StaticFields.dbHelper.TABLE_STAR)) {
            itemsList.add(new ListObject("Save", R.drawable.ic_file, R.drawable.ic_file_dark,
                    StaticFields.darkThemeSet ? android.R.color.white : android.R.color.black));
            itemsList.add(new ListObject("Share", R.drawable.ic_share, R.drawable.ic_share_dark,
                    StaticFields.darkThemeSet ? android.R.color.white : android.R.color.black));
        }
        if (table.equals(StaticFields.dbHelper.TABLE_TRASH)){
            itemsList.add(new ListObject("Restore", R.drawable.ic_restore, R.drawable.ic_restore_dark,
                    StaticFields.darkThemeSet ? android.R.color.white : android.R.color.black));
        }
        return itemsList;
    }

    private void manageSave(int position){
        Log.d(TAG, "manageSave: saving to external storage");
        if (!permissionsGranted(getContext())){ //if permission not granted
            grantPermissionBottomSheet();
        }else{
            OnStorage.createFile(dataList.get(position).getmNoteTitle(),
                    dataList.get(position).getmNoteContent(), root, getContext(), ".txt");
        }
    }

    private void manageArchive(int position){
        Log.d(TAG, "manageArchive: archiving note.");

        //getting the data from the table and preparing in the contentValues
        ObjectNote itemNote = dataList.get(position);
        ContentValues contentValues = StaticFields.dbHelper.createDBContentValue(table, itemNote.getmNoteId(),
                itemNote.getmNoteTitle(), itemNote.getmNoteContent(), itemNote.getmDateCreated(),
                itemNote.getMdatedUpdated(), itemNote.getmEditorType(), String.valueOf(itemNote.getIsStarred()),itemNote.getmTag());

        String message="";
        String tableId = "";
        if (table.equals(StaticFields.dbHelper.TABLE_NOTES)){
        //inserting data into archives table from notes table
        StaticFields.dbHelper.insertNote(StaticFields.dbHelper.TABLE_ARCHIVE,contentValues);
        StaticFields.dbHelper.deleteNote(StaticFields.dbHelper.TABLE_NOTES, c.getString(idIndex));
        dataList.remove(position);
        message = "Note archived successfully.";
        tableId = StaticFields.dbHelper.TABLE_ARCHIVE;
        }else{
            //inserting data into notes table from archives table
            StaticFields.dbHelper.insertNote(StaticFields.dbHelper.TABLE_NOTES,contentValues);
            StaticFields.dbHelper.deleteNote(StaticFields.dbHelper.TABLE_ARCHIVE, c.getString(idIndex));
            dataList.remove(position);
            message = "Note unarchived successfully.";
            tableId = StaticFields.dbHelper.TABLE_NOTES;
        }
        if (itemNote.getIsStarred()){
            ContentValues contentValuesStar = StaticFields.dbHelper.createDBContentValue(StaticFields.dbHelper.TABLE_STAR,
                    itemNote.getmNoteId(), itemNote.getmNoteTitle(), itemNote.getmNoteContent(), itemNote.getmDateCreated(),
                    itemNote.getMdatedUpdated(), itemNote.getmEditorType(), tableId, itemNote.getmTag());

            StaticFields.dbHelper.updateNote(StaticFields.dbHelper.TABLE_STAR,itemNote.getmNoteId(),contentValuesStar);
        }
        adapter.notifyDataSetChanged();
        new PortableContent().showSnackBar(getContext(), Type.SUCCESS, message, Duration.SHORT);
        emptyNotesScreen();  //showing empty notes screen if list empty
    }

    private void manageDelete(final int position){
        Log.d(TAG, "manageDelete: deleting note");
        c.moveToPosition(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AppDialogTheme);
        builder.setTitle("Warning:");
        if (StaticFields.darkThemeSet) {
            builder.setIcon(R.drawable.dialog_warning_dark);
        }else{
            builder.setIcon(R.drawable.dialog_warning);
        }
        builder.setMessage("Are you sure you want to delete the note from the "+table+" list?");
        //preparing data
        ObjectNote itemNote = dataList.get(position);
        final ContentValues contentValues = StaticFields.dbHelper.createDBContentValue(StaticFields.dbHelper.TABLE_TRASH,
                itemNote.getmNoteId(), itemNote.getmNoteTitle(), itemNote.getmNoteContent(), itemNote.getmDateCreated(),
                itemNote.getMdatedUpdated(), itemNote.getmEditorType(), String.valueOf(itemNote.getIsStarred()), itemNote.getmTag());

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //re-requesting permissions
                StaticFields.dbHelper.insertNote(StaticFields.dbHelper.TABLE_TRASH, contentValues);
                StaticFields.dbHelper.deleteNote(table,c.getString(idIndex));
                dataList.remove(position);
                adapter.notifyDataSetChanged();
                new PortableContent().showSnackBar(getContext(),Type.SUCCESS,"Note moved to trash successfully.", Duration.SHORT);
                emptyNotesScreen();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void manageShare(int position, int shareType){
        Log.d(TAG, "manageShare: handling share action");
        String noteTitle = dataList.get(position).getmNoteTitle();
        String noteContent = dataList.get(position).getmNoteContent();

        Intent sendIntent;
        if (shareType==0){  //simple text format (applicable only for simple note)
            sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, noteTitle + "\n\n" +
                    noteContent + "\n\n================\nShared from Pocket Notes, a truly simple notepad app.\n\nDownload now:)\n"
                    + StaticFields.Store_URL);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share note with..."));

        }else {  //file format
            if (permissionsGranted(getContext())) {
                File file = OnStorage.createFile(noteTitle, noteContent, root, getContext(), ".txt");  //getting created file
                Uri fileUri = FileProvider.getUriForFile(getContext(), getActivity().getPackageName(), file);
                sendIntent = ShareCompat.IntentBuilder.from(getActivity())
                        .setType(getContext().getContentResolver().getType(fileUri))
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
            } else {
                grantPermissionBottomSheet();
            }
        }
    }

    private void manageStar(int position){
        Log.d(TAG, "manageStar: manage star/bookmarking notes");
        ObjectNote itemNote = dataList.get(position);
        ContentValues contentValuesStar = StaticFields.dbHelper.createDBContentValue(StaticFields.dbHelper.TABLE_STAR,
                itemNote.getmNoteId(), itemNote.getmNoteTitle(), itemNote.getmNoteContent(), itemNote.getmDateCreated(),
                itemNote.getMdatedUpdated(), itemNote.getmEditorType(), table, itemNote.getmTag());

        ContentValues contentValuesTable = StaticFields.dbHelper.createDBContentValue(table,
                itemNote.getmNoteId(), itemNote.getmNoteTitle(), itemNote.getmNoteContent(), itemNote.getmDateCreated(),
                itemNote.getMdatedUpdated(), itemNote.getmEditorType(), "true", itemNote.getmTag());

        StaticFields.dbHelper.insertNote(StaticFields.dbHelper.TABLE_STAR,contentValuesStar);
        StaticFields.dbHelper.updateNote(table,itemNote.getmNoteId(),contentValuesTable);
        //updating element of that position in the temp arrayList
        dataList.set(position, new ObjectNote(itemNote.getmNoteId(), itemNote.getmNoteTitle(),
                itemNote.getmNoteContent(), itemNote.getmDateCreated(), itemNote.getMdatedUpdated(),
                itemNote.getmEditorType(),true, itemNote.getmTag()));
        adapter.notifyDataSetChanged();
    }

    private void retrieveDB(){
        Log.d(TAG, "updateCursor: updating database cursor.");
        if (!dataList.isEmpty()){
            dataList.clear();
        }
        try {
            //initializing database primitives
            c = StaticFields.dbHelper.getData(table);
            idIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_ID);
            titleIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_TITLE);
            contentIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_CONTENT);
            createdDateIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_CREATED_DATE);
            updateDateIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_UPDATED_DATE);
            editorTypeIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_EDITOR_TYPE);
            if (table.equals(StaticFields.dbHelper.TABLE_STAR)) {
                tableIdIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_TABLE_ID);
            }else{
                starIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_STAR);
            }
            tagIndex = c.getColumnIndex(StaticFields.dbHelper.ITEM_TAG);

            do{
             c.moveToNext();
             String id = c.getString(idIndex);
             String title = c.getString(titleIndex);
             String content = c.getString(contentIndex);
             String dateCreated = c.getString(createdDateIndex);
             String dateUpdated = c.getString(updateDateIndex);
             String editorType = c.getString(editorTypeIndex);
             String tag = c.getString(tagIndex);
                if (table.equals(StaticFields.dbHelper.TABLE_STAR)) {
                    String tableId = c.getString(tableIdIndex);
                    dataList.add(new ObjectNote(id,title,content,dateCreated,dateUpdated,editorType,tableId,tag));
                }else{
                    boolean star = Boolean.parseBoolean(c.getString(starIndex));
                    dataList.add(new ObjectNote(id,title,content,dateCreated,dateUpdated,editorType,star,tag));
                }
            }while(!c.isLast());

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void emptyNotesScreen(){
        Log.d(TAG, "emptyNotesScreen: for displaying empty notes fragment.");

        if (StaticFields.dbHelper.numberOfRows(table)<=0) {
            //if table is empty or null
            EmptyNotesFragment emptyNotesFragment = new EmptyNotesFragment();
            Bundle bundle = new Bundle();
            bundle.putString(StaticFields.KEY_INTENT_EMPTYNOTES, table);
            emptyNotesFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, emptyNotesFragment)
                    .commit();
        }
    }

    public void grantPermissionBottomSheet(){
        View dialogView = getLayoutInflater().inflate(R.layout.layout_permission_bottomsheet,null);
        final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        Button ok = dialogView.findViewById(R.id.bt_bottomsheet);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: bottomSheet button clicked.");
                requestPermissions(getActivity());
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialogView);
        dialog.show();
    }

    public static boolean permissionsGranted(Context context) {
        Log.d(TAG, "checkPermissions: checking if permissions granted.");
        int result;
        listPermissionsNeeded = new ArrayList<>();
        for (String p:StaticFields.permissions) {
            result = ContextCompat.checkSelfPermission(context,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        //if all/some permissions not granted
        return listPermissionsNeeded.isEmpty();
    }

    private static void requestPermissions(Activity activity){
        Log.d(TAG, "requestPermissions: requesting permissions.");
        ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new
                String[listPermissionsNeeded.size()]), StaticFields.PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: action when permission granted or denied");
        if (requestCode == StaticFields.PERMISSION_CODE) {
            if (grantResults.length <= 0) {
                // no permissions granted.
                showPermissionDialog();
            }
        }
    }

    private void showPermissionDialog(){
        Log.d(TAG, "showPermissionDialog: requesting permissions");

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("Are you sure?");
        builder.setMessage("You'll not be able to use this app properly without these permissions.");
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //re-requesting permissions
                requestPermissions(getActivity());
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void manageShareChooser(final int itemPosition) {
        Log.d(TAG, "manageShareChooser: managing share type choose dialog");
        int dialogIcon;
        if (StaticFields.darkThemeSet) {
            dialogIcon = R.drawable.ic_share_dark;
        } else {
            dialogIcon = R.drawable.ic_share;
        }

        final ArrayList<ListObject> list = new ArrayList<>();
        list.add(new ListObject("Simple text", R.drawable.dialog_text, R.drawable.dialog_text_dark));
        list.add(new ListObject("Text file", R.drawable.ic_file, R.drawable.ic_file_dark));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppDialogTheme);
        builder.setTitle("Share note as:");
        builder.setIcon(dialogIcon);

        builder.setAdapter(new DialogListAdapter(getContext(), list), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                manageShare(itemPosition, which);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onBottomSheetItemClick(int position) {
        //bottomSheet item click action
        if (table.equals(StaticFields.dbHelper.TABLE_NOTES)||table.equals(StaticFields.dbHelper.TABLE_ARCHIVE)){
            switch(position){
                case 0: //archive/unarchive
                    manageArchive(mNotePosition);
                    break;
                case 1: //star/unstar
                    manageStar(mNotePosition);
                    break;
                case 2:  //Delete
                    manageDelete(mNotePosition);
                    break;
                case 3: //save
                    manageSave(mNotePosition);
                    break;
                case 4: //share
                    manageShareChooser(mNotePosition);
                    break;
            }
        }
    }
}
