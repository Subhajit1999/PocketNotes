package com.subhajitkar.commercial.projet_tulip.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.activities.NoteEditorActivity;
import com.subhajitkar.commercial.projet_tulip.utils.DialogListAdapter;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;

public class EmptyNotesFragment extends Fragment {
    private static final String TAG = "EmptyNotesFragment";

    private ImageView errorImage;
    private TextView errorMsg;
    private String tableId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: creating fragment view");
        return inflater.inflate(R.layout.fragment_empty_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: fragment view created");

        errorImage = view.findViewById(R.id.iv_empty_notes);
        errorMsg = view.findViewById(R.id.tv_empty_notes);
        //get arg
        if (getArguments()!=null){
            tableId = getArguments().getString(StaticFields.KEY_INTENT_EMPTYNOTES);
        }
        manageNewNoteEntry(view);
        //Animations
        Animation topAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_slide_bottom);
        Animation bottomAnim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_slide_top);
        errorImage.setAnimation(topAnim);
        errorMsg.setAnimation(bottomAnim);
    }

    private void manageNewNoteEntry(View view){
        Log.d(TAG, "manageNewNoteEntry: handling new note fab button");
        FrameLayout fabLayout = null;
        fabLayout = view.findViewById(R.id.new_note_fab_layout);
        if (tableId.equals(StaticFields.dbHelper.TABLE_NOTES)){
            fabLayout.setVisibility(View.VISIBLE);

         //do the job, when layout visible
            FloatingActionButton fabBtn = view.findViewById(R.id.fab_new_note_empty);
            //creating new note menu
            StaticFields.newNoteListInit();
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppDialogTheme);
            builder.setAdapter(new DialogListAdapter(getContext(), StaticFields.listNewNote), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //add item click action here
                    Intent i = new Intent(getActivity(), NoteEditorActivity.class);
                    switch(which){
                        case 0:
                            i.putExtra(StaticFields.KEY_INTENT_EDITORACTIVITY,"new");
                            i.putExtra(StaticFields.KEY_INTENT_TABLEID,tableId);
                            i.putExtra(StaticFields.KEY_EDITOR_ID,"simple");
                            break;
                    }
                    startActivity(i);
                    dialog.dismiss();
                }
            });
            fabBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //fab click action, showing dialog
                    builder.show();
                }
            });
        }else{
            fabLayout.setVisibility(View.GONE);
        }
    }
}
