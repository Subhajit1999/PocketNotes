package com.subhajitkar.commercial.projet_tulip;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class NotesListFragment extends Fragment implements RecyclerAdapter.OnItemClickListener{
    private static final String TAG = "NotesListFragment";

    private static RecyclerView notesRecycler;
    @SuppressLint("StaticFieldLeak")
    private static RecyclerAdapter adapter;
    private Cursor c;
    private int titleIndex, contentIndex,createdDateIndex;

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

        //preparing recyclerListView
        notesRecycler = view.findViewById(R.id.notes_recycler);
        notesRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        notesRecycler.setLayoutManager(mLayoutManager);
        notesRecycler.setItemAnimator(new DefaultItemAnimator());
        registerForContextMenu(notesRecycler);

        try {
            //initializing database primitives
            c = StaticFields.database.rawQuery("SELECT * FROM notes", null);
            titleIndex = c.getColumnIndex("title");
            contentIndex = c.getColumnIndex("content");
            createdDateIndex = c.getColumnIndex("dateCreated");
        }catch(Exception e){
            Toast.makeText(getContext(),"Some error occurred. ("+e.getMessage()+")",Toast.LENGTH_SHORT).show();
        }

        //setting up the adapter
        adapter = new RecyclerAdapter(getContext(),c,titleIndex,contentIndex,createdDateIndex);
        adapter.setOnItemClickListener(this);
//        adapter.setOnItemLongClickListener(this);
        notesRecycler.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        Log.d(TAG, "onItemClick: recycler list item click action, position: "+position);
        Intent i = new Intent(getContext(),NoteEditorActivity.class);
        i.putExtra(StaticFields.KEY_INTENT_EDITORACTIVITY,"existing");
        i.putExtra(StaticFields.KEY_INTENT_LISTPOSITION,position);
        startActivity(i);
    }

//    public static void updateAdapter(Context context){
//        Log.d(TAG, "updateAdapter: updating the list");
//        adapter = null;
//        adapter = new RecyclerAdapter(context, StaticFields.NotesList);
//        if (!StaticFields.NotesList.isEmpty()){
//            notesRecycler.setAdapter(adapter);
//        }
//    }


    @Override
    public void onPause() {
        super.onPause();
        if (!c.isClosed()){
            c.close();
        }
    }

//    @Override
//    public void onItemLongClick(int position) {
//        Log.d(TAG, "onItemLongClick: gets called");
//        Toast.makeText(getContext(),"Item long clicked", Toast.LENGTH_SHORT).show();
//    }
}
