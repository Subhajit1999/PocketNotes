package com.subhajitkar.commercial.projet_tulip.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.activities.NoteEditorActivity;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;

public class EmptyNotesFragment extends Fragment {
    private static final String TAG = "EmptyNotesFragment";
    private String Frag_identifier;

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
        LinearLayout buttonLayout = view.findViewById(R.id.add_button_layout);

        //getting fragment argument
        if (getArguments()!=null){
            Frag_identifier = getArguments().getString(StaticFields.KEY_INTENT_EMPTYNOTES);
        }
        if (StaticFields.getProfilesCount(Frag_identifier)<=0 && !Frag_identifier.equals("archives")) {  //if called in the mainActivity, means notes list empty
            buttonLayout.setVisibility(View.VISIBLE);
            LinearLayout button = view.findViewById(R.id.add_note);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //button click event
                    Intent i = new Intent(getContext(), NoteEditorActivity.class);
                    i.putExtra(StaticFields.KEY_INTENT_EDITORACTIVITY,"new");
                    startActivity(i);
                }
            });
        }
    }
}