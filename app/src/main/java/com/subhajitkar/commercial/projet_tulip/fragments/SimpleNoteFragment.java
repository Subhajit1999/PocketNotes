package com.subhajitkar.commercial.projet_tulip.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Type;
import com.google.android.material.snackbar.Snackbar;
import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.utils.PortableContent;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;

public class SimpleNoteFragment extends Fragment {
    private static final String TAG = "SimpleNoteFragment";

    private EditText noteTitle, noteContent;
    private String title, content, dateCreated, noteFlag = "";
    private LinearLayout root;
    private boolean exit = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simple_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        widgetsInit(view);
        //get bundle data
        if (getArguments()!=null){
            noteFlag = getArguments().getString(StaticFields.KEY_EDITOR_INTENTFLAG);
            if (noteFlag.equals("existing")) {
                //get the data
                title = getArguments().getString(StaticFields.KEY_EDITOR_TITLE);
                content = getArguments().getString(StaticFields.KEY_EDITOR_CONTENT);
                dateCreated = getArguments().getString(StaticFields.KEY_EDITOR_DATECREATED);
                //set the data
                noteTitle.setText(title);
                noteContent.setText(content);
                new PortableContent(getContext()).showSnackBar(Type.UPDATE,"Created at: " + dateCreated, Duration.INFINITE);
            }
        }
    }

    private void widgetsInit(View view){
        Log.d(TAG, "widgetsInit: initializing widgets");
        noteTitle = view.findViewById(R.id.et_note_title);
        noteTitle.setText("");
        noteContent = view.findViewById(R.id.et_note_content);
        root = view.findViewById(R.id.simple_root);
    }

    public boolean backPressedSimple(){
        //extracting edit-texts data (place validation check)
        String title = "Save?";
        String msg = "Do you want to save the note before exit?";
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppDialogTheme);
        builder.setTitle(title);
        if (StaticFields.darkThemeSet) {
            builder.setIcon(R.drawable.dialog_warning_dark);
        }else{
            builder.setIcon(R.drawable.dialog_warning);
        }
        builder.setMessage(msg);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!noteTitle.getText().toString().isEmpty()) {
                    exit = true;
                    //save the data
                    StaticFields.noteTitle = noteTitle.getText().toString();
                    StaticFields.noteContent = noteContent.getText().toString();
                    getActivity().onBackPressed();
                }else{
                    new PortableContent(getContext()).showSnackBar(Type.WARNING,"Title can't be empty.",
                            Duration.SHORT);
                }
            }
        });
        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exit = true;
                StaticFields.noteTitle = StaticFields.noteContent = "";
                getActivity().onBackPressed();
            }
        });
        if (!exit) {
            builder.show();
        }
        return exit;
    }

    public boolean onSharing(){
        Log.d(TAG, "onSharing: sharing current note data");
        //sending data
        if (!noteTitle.getText().toString().isEmpty()){
            StaticFields.shareNoteTitle = noteTitle.getText().toString();
            StaticFields.shareNoteContent = noteContent.getText().toString();
            return true;
        }else{
            new PortableContent(getContext()).showSnackBar(Type.WARNING,"Title can't be empty.",
                    Duration.SHORT);
            return false;
        }
    }

    public EditText getEditText() {
        return noteContent;
    }
}