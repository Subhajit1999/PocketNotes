package com.subhajitkar.commercial.projet_tulip.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;

import java.util.ArrayList;

public class TagsDialogFragment extends DialogFragment {
    private static final String TAG = "TagsDialogFragment";

    private Toolbar toolbar;

    public static TagsDialogFragment display(FragmentManager fragmentManager) {
        TagsDialogFragment exampleDialog = new TagsDialogFragment();
        exampleDialog.show(fragmentManager, TAG);

        return exampleDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (StaticFields.darkThemeSet) {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.FullDialog_dark);
        }else{
            setStyle(DialogFragment.STYLE_NORMAL, R.style.FullDialog_Light);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_tags_dialog, container, false);
        //toolbar init
        toolbar = view.findViewById(R.id.toolbar);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //toolbar properties
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TagsDialogFragment.this.dismiss();
            }
        });
        toolbar.setTitle("Manage Tags");
    }
}
