package com.subhajitkar.commercial.projet_tulip.fragments;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.IccOpenLogicalChannelResponse;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.android.graphics.CanvasView;
import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Type;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.adapters.BottomSheetRecyclerAdapter;
import com.subhajitkar.commercial.projet_tulip.objects.DataModel;
import com.subhajitkar.commercial.projet_tulip.utils.PortableContent;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;
import com.subhajitkar.commercial.projet_tulip.utils.StringBuilder;

import java.util.ArrayList;
import java.util.Random;

public class CanvasFragment extends Fragment {
    private static final String TAG = "CanvasFragment";

    private static CanvasView canvasView;
    private HorizontalScrollView horizontalScroll;
    private Paint.Style strokeStyle=null;
    private Bitmap canvasBitmap=null;
    private boolean exit = false;
    private String intentFlag, base64Content, canvasNoteTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_canvas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: gets called");
        //widgets init
        canvasView = view.findViewById(R.id.canvas);
        //default values
        StaticFields.canvasStrokeColor = Color.BLACK;
        StaticFields.canvasStrokeStyle = 0;
        StaticFields.canvasStrokeWidth = (int) canvasView.getPaintStrokeWidth();
        StaticFields.colorPickerPosition = new Point(0,0);
        StaticFields.colorHex = "#FF000000";
        Log.d(TAG, "onViewCreated: color: "+StaticFields.canvasStrokeColor+", colorHex: "+StaticFields.colorHex);
        //method calls
        getBundleDataAndInit();
        setupValues();
        manageEditElements(view);
    }

    private void manageEditElements(View view){
        Log.d(TAG, "manageEditElements: handling all canvas elements");

        final ImageView undo, redo, erase, draw, strokeWidth;
        undo = view.findViewById(R.id.iv_undo);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvasView.undo();
            }
        });
        redo = view.findViewById(R.id.iv_redo);
        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvasView.redo();
            }
        });
        erase = view.findViewById(R.id.iv_erase);
        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canvasView.setPaintStrokeColor(Color.WHITE);
                strokeStyle = canvasView.getPaintStyle();
                canvasView.setPaintStyle(Paint.Style.STROKE);
            }
        });
        draw = view.findViewById(R.id.iv_draw);
        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (strokeStyle!=null) {
                    canvasView.setPaintStyle(strokeStyle);
                }
                canvasView.setPaintStrokeColor(Color.BLACK);
            }
        });
        strokeWidth = view.findViewById(R.id.iv_stroke_width);
        strokeWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CanvasDialogFragment.display(getActivity().getSupportFragmentManager());
            }
        });
    }

    public static boolean setupValues(){
        Log.d(TAG, "setupValues: Setting up values");
        Paint.Style[] arrStrokeStyles = {Paint.Style.STROKE, Paint.Style.FILL, Paint.Style.FILL_AND_STROKE};
        //setting up default values
        canvasView.setPaintStrokeColor(StaticFields.canvasStrokeColor);
        canvasView.setPaintStyle(arrStrokeStyles[StaticFields.canvasStrokeStyle]);
        canvasView.setPaintStrokeWidth(StaticFields.canvasStrokeWidth);

        return true;
    }

    private void getBundleDataAndInit(){
        Log.d(TAG, "getBundleData: getting arguments data");
        if (getArguments()!=null){
            intentFlag = getArguments().getString(StaticFields.KEY_EDITOR_INTENTFLAG);
            if (intentFlag.equals("existing")) {
                canvasNoteTitle = getArguments().getString(StaticFields.KEY_EDITOR_TITLE);
                base64Content = getArguments().getString(StaticFields.KEY_EDITOR_CONTENT);
                //setting data to canvas
                canvasBitmap = new StringBuilder().base64ToBitmap(base64Content);
                canvasView.drawBitmap(canvasBitmap);
            }
        }
    }

    public boolean backPressedCanvas(){
        //extracting edit-texts data (place validation check)
        String title = "Save?";
        String msg = "Do you want to save the note before exit?";
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppDialogTheme);
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
                exit = true;
                //save the data
                if (intentFlag.equals("existing")){
                    StaticFields.noteTitle = canvasNoteTitle;
                }else {
                    StaticFields.noteTitle = StaticFields.canvasTitleTemplate + StaticFields.getDateTime(StaticFields.UniqueIdDateFormat);
                }
                StaticFields.noteContent = new StringBuilder().bitmapToBase64(canvasView.getBitmap());
                getActivity().onBackPressed();
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
        Log.d(TAG, "onSharing: sharing note data");
        if (intentFlag.equals("existing")){
            StaticFields.shareNoteTitle = canvasNoteTitle;
        }else {
            StaticFields.shareNoteTitle = StaticFields.canvasTitleTemplate + StaticFields.getDateTime(StaticFields.UniqueIdDateFormat);
        }
        StaticFields.shareCanvasBitmap = canvasView.getBitmap();
        return true;
    }

    @Override
    public void onPause() {
        //saving bitmap temporarily
        canvasBitmap = canvasView.getBitmap();
        super.onPause();
    }

    @Override
    public void onResume() {
        //restoring bitmap data, if available
        if (canvasBitmap!=null){
            canvasView.drawBitmap(canvasBitmap);
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (CanvasDialogFragment.invalidateData()){
            super.onDestroy();
        }
    }
}
