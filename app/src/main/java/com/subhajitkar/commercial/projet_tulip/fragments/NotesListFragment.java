package com.subhajitkar.commercial.projet_tulip.fragments;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.activities.NoteEditorActivity;
import com.subhajitkar.commercial.projet_tulip.fragments.EmptyNotesFragment;
import com.subhajitkar.commercial.projet_tulip.utils.ObjectNote;
import com.subhajitkar.commercial.projet_tulip.utils.RecyclerAdapter;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;

import java.util.ArrayList;

public class NotesListFragment extends Fragment implements RecyclerAdapter.OnItemClickListener, RecyclerAdapter.OnItemLongClickListener {
    private static final String TAG = "NotesListFragment";

    private RecyclerView notesRecycler;
    private String table;
    private RecyclerAdapter adapter;
    private LinearLayout root;
    private Cursor c;
    private ArrayList<ObjectNote> dataList;
    private int idIndex, titleIndex, contentIndex,createdDateIndex, updateDateIndex;

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
        //getting bundle data
        if (getArguments()!=null){
            table = getArguments().getString(StaticFields.KEY_FRAGMENT_MAINACTIVITY);
        }
        dataList = new ArrayList<>(); //initializing arraylist
        retrieveDB(); //retrieving database and getting items ino arrayList

        //preparing recyclerListView
        notesRecycler = view.findViewById(R.id.notes_recycler);
        notesRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        notesRecycler.setLayoutManager(mLayoutManager);
        notesRecycler.setItemAnimator(new DefaultItemAnimator());

        //setting up the adapter
        if (!dataList.isEmpty()) {
            adapter = new RecyclerAdapter(getContext(),dataList);
            adapter.setOnItemClickListener(this);
            adapter.setOnItemLongClickListener(this);
            notesRecycler.setAdapter(adapter);
        }else{
            emptyNotesScreen();
        }
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
    public void onItemLongClick(int position) {
        Log.d(TAG, "onItemLongClick: gets called");
        // using BottomSheetDialog

        View dialogView = getLayoutInflater().inflate(R.layout.recycler_menu_bottomsheet, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        //handle item click events
        final ImageView archiveLogo,shareLogo;
        final TextView archiveText;
        LinearLayout archive, delete, share;
        final int mPosition = position;
        archive = dialogView.findViewById(R.id.bottomsheet_archive);
        archiveLogo = dialogView.findViewById(R.id.iv_bottomsheet_archive);
        archiveText = dialogView.findViewById(R.id.tv_bottomsheet_archive);
        shareLogo = dialogView.findViewById(R.id.iv_bottomsheet_share);
        if (table.equals("notes")){
            if(StaticFields.darkThemeSet){
                archiveLogo.setImageResource(R.drawable.ic_archive_dark);
            }else {
                archiveLogo.setImageResource(R.drawable.ic_archive);
            }
            archiveText.setText(getResources().getString(R.string.string_bottomsheet_archive));
        }else{
            if(StaticFields.darkThemeSet){
                archiveLogo.setImageResource(R.drawable.ic_unarchive_dark);
            }else {
                archiveLogo.setImageResource(R.drawable.ic_unarchive);
            }
            archiveText.setText(getResources().getString(R.string.string_bottomsheet_unarchive));
        }
        if(StaticFields.darkThemeSet){
            shareLogo.setImageResource(R.drawable.ic_share_dark);
        }else {
            shareLogo.setImageResource(R.drawable.ic_share);
        }
        archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //archive click event
                Log.d(TAG, "onClick: Archive option clicked");
                manageArchive(mPosition);
                dialog.dismiss();
            }
        });
        delete = dialogView.findViewById(R.id.bottomsheet_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete action click event
                Log.d(TAG, "onClick: Delete option clicked");
                manageDelete(mPosition);
                dialog.dismiss();
            }
        });
        share = dialogView.findViewById(R.id.bottomsheet_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //share click event
                Log.d(TAG, "onClick: Share option clicked");
                manageShare();
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialogView);
        dialog.show();
    }

    private void manageArchive(int position){
        Log.d(TAG, "manageArchive: archiving note.");

        //getting the data from the table and preparing in the contentValues
        c.moveToPosition(position);
        StaticFields.contentValues = new ContentValues();
        StaticFields.contentValues.put("id", c.getString(idIndex));
        StaticFields.contentValues.put("title", c.getString(titleIndex));
        StaticFields.contentValues.put("content", c.getString(contentIndex));
        StaticFields.contentValues.put("dateCreated", c.getString(createdDateIndex));
        StaticFields.contentValues.put("dateUpdated", c.getString(updateDateIndex));

        String message="";
        if (table.equals("notes")){
        //inserting data into archives table from notes table
        StaticFields.database.insert("archives", null, StaticFields.contentValues);
        StaticFields.database.delete("notes", "id = ?", new String[]{c.getString(idIndex)});
        dataList.remove(position);
        message = "Note archived successfully.";
        }else{
            //inserting data into notes table from archives table
            StaticFields.database.insert("notes", null, StaticFields.contentValues);
            StaticFields.database.delete("archives", "id = ?", new String[]{c.getString(idIndex)});
            dataList.remove(position);
            message = "Note unarchived.";
        }
        adapter.notifyDataSetChanged();
        Snackbar.make(root, Html.fromHtml("<font color=\""+getResources().getColor(R.color.colorAccent)+"\">"+message+"</font>"),Snackbar.LENGTH_SHORT).show();

        emptyNotesScreen();  //showing empty notes screen if list empty
    }

    private void manageDelete(final int position){
        Log.d(TAG, "manageDelete: deleting note");
        c.moveToPosition(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AppDialogTheme);
        builder.setTitle("Warning:");
        if (StaticFields.darkThemeSet) {
            builder.setIcon(R.drawable.dialog_warning);
        }else{
            builder.setIcon(R.drawable.dialog_warning_dark);
        }
        builder.setMessage("Are you sure you want to delete the note from the "+table+" list?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //re-requesting permissions
                StaticFields.database.delete(table,"id = ?", new String[]{c.getString(idIndex)});
                dataList.remove(position);
                adapter.notifyDataSetChanged();
                Snackbar.make(root,Html.fromHtml("<font color=\""+getResources().getColor(R.color.colorAccent)+"\">Note deleted successfully.</font>"),Snackbar.LENGTH_SHORT).show();
                emptyNotesScreen();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void manageShare(){
        Log.d(TAG, "manageShare: handling share action");
        String noteTitle = c.getString(titleIndex);
        String noteContent = c.getString(contentIndex);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, noteTitle+"\n\n"+
                noteContent+"\n\n================\nShared from Pocket Notes, a truly simple notepad app.\n\nDownload now:)\nhttp://play.google.com/store/apps/details?id="
                + getContext().getPackageName());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share note with..."));
    }

    private void retrieveDB(){
        Log.d(TAG, "updateCursor: updating database cursor.");
        if (!dataList.isEmpty()){
            dataList.clear();
        }
        try {
            //initializing database primitives
            c = StaticFields.database.rawQuery("SELECT * FROM "+table, null);
            idIndex = c.getColumnIndex("id");
            titleIndex = c.getColumnIndex("title");
            contentIndex = c.getColumnIndex("content");
            createdDateIndex = c.getColumnIndex("dateCreated");
            updateDateIndex = c.getColumnIndex("dateUpdated");

            do{
             c.moveToNext();
             String id = c.getString(idIndex);
             String title = c.getString(titleIndex);
             String content = c.getString(contentIndex);
             String dateCreated = c.getString(createdDateIndex);
             String dateUpdated = c.getString(updateDateIndex);
             dataList.add(new ObjectNote(id,title,content,dateCreated,dateUpdated));
            }while(!c.isLast());

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void emptyNotesScreen(){
        Log.d(TAG, "emptyNotesScreen: for displaying empty notes fragment.");

        if (StaticFields.database == null || StaticFields.getProfilesCount(table)<=0) {
            //if table is empty or null
            EmptyNotesFragment emptyNotesFragment = new EmptyNotesFragment();
            Bundle bundle = new Bundle();
            bundle.putString(StaticFields.KEY_INTENT_EMPTYNOTES, table);
            emptyNotesFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, emptyNotesFragment)
                    .commit();
        }
    }
}
