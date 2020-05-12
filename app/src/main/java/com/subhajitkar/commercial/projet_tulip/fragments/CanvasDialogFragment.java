package com.subhajitkar.commercial.projet_tulip.fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.android.graphics.CanvasView;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.skydoves.colorpickerview.AlphaTileView;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.listeners.ColorListener;
import com.skydoves.colorpickerview.preference.ColorPickerPreferenceManager;
import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;


public class CanvasDialogFragment extends DialogFragment {
    private static final String TAG = "TagsDialogFragment";

    private Toolbar toolbar;
    private View thumbView;
    private String prefColorPicker = "MyColorPicker";
    private static boolean firstTime = true;
    private static ColorPickerPreferenceManager manager;
    private static ColorPickerView colorPickerView;

    public static CanvasDialogFragment display(FragmentManager fragmentManager) {
        Log.d(TAG, "display: displaying dialog fragment");
        CanvasDialogFragment exampleDialog = new CanvasDialogFragment();
        exampleDialog.show(fragmentManager, TAG);

        return exampleDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: gets called");
        super.onCreate(savedInstanceState);
        if (StaticFields.darkThemeSet) {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.FullDialog_dark);
        }else{
            setStyle(DialogFragment.STYLE_NORMAL, R.style.FullDialog_Light);
        }
        thumbView = LayoutInflater.from(getContext()).inflate(R.layout.snippet_seekbar_custom_thumb, null, false);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart: gets called");
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
        Log.d(TAG, "onCreateView: gets called");
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_tags_dialog, container, false);
        //toolbar init
        toolbar = view.findViewById(R.id.toolbar);
        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: gets called");
        super.onViewCreated(view, savedInstanceState);
        //toolbar properties
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CanvasDialogFragment.this.dismiss();
            }
        });
        toolbar.setTitle("Canvas stroke options:");
        //setup spinner
        final MaterialSpinner spinner = view.findViewById(R.id.spinner);
        spinner.setItems(getActivity().getResources().getStringArray(R.array.spinner_canvas_stroke_style));
        spinner.setSelectedIndex(StaticFields.canvasStrokeStyle);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                StaticFields.canvasStrokeStyle = position;
            }
        });
        //stroke width setUp
        final SeekBar seekBar = view.findViewById(R.id.stroke_width_seekbar);
        seekBar.setMax(50);
        seekBar.setProgress(StaticFields.canvasStrokeWidth);
        seekBar.setThumb(getThumb(StaticFields.canvasStrokeWidth));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setThumb(getThumb(progress));
                StaticFields.canvasStrokeWidth = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //colorPicker setup
        colorPickerView = view.findViewById(R.id.colorPickerView);
        final AlphaTileView tileView = view.findViewById(R.id.alphaTileView);
        final TextView colorText = view.findViewById(R.id.tv_color_hex);
        //set previous data
        manager = ColorPickerPreferenceManager.getInstance(getContext());
        if (firstTime){
            manager.setColor(prefColorPicker,StaticFields.canvasStrokeColor);
            colorPickerView.selectCenter();
            tileView.setPaintColor(StaticFields.canvasStrokeColor);
            colorText.setText(StaticFields.colorHex);
        }else{
            manager.restoreColorPickerData(colorPickerView);
            colorText.setText(String.format("#%s", colorPickerView.getColorEnvelope().getHexCode()));
            tileView.setPaintColor(colorPickerView.getColor());
        }
        colorPickerView.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                Log.d(TAG, "onColorSelected: gets called");
                if (fromUser) {
                    StaticFields.canvasStrokeColor = envelope.getColor();
                    tileView.setPaintColor(envelope.getColor());
                    colorText.setText("#" + envelope.getHexCode());
                    manager.saveColorPickerData(colorPickerView);
                }
            }
        });
        //buttons setup
        final Button colorDefault = view.findViewById(R.id.bt_color_default);
        colorDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset color
                StaticFields.canvasStrokeColor = Color.BLACK;
                manager.setColor(prefColorPicker,StaticFields.canvasStrokeColor);
                colorPickerView.selectCenter();
                tileView.setPaintColor(StaticFields.canvasStrokeColor);
                colorText.setText(StaticFields.colorHex);
            }
        });
        Button resetAll = view.findViewById(R.id.bt_reset_all);
        resetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reset all
                StaticFields.canvasStrokeWidth = 3;
                seekBar.setProgress(StaticFields.canvasStrokeWidth);
                seekBar.setThumb(getThumb(StaticFields.canvasStrokeWidth));
                StaticFields.canvasStrokeStyle = 0;
                spinner.setSelectedIndex(StaticFields.canvasStrokeStyle);
                StaticFields.canvasStrokeColor = Color.BLACK;
                manager.setColor(prefColorPicker,StaticFields.canvasStrokeColor);
                colorPickerView.selectCenter();
                tileView.setPaintColor(StaticFields.canvasStrokeColor);
                colorText.setText(StaticFields.colorHex);
            }
        });
    }

    public Drawable getThumb(int progress) {
        Log.d(TAG, "getThumb: creating custom thumb for seekbar");
        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText(progress + "");

        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: gets called");
        if (CanvasFragment.setupValues()){
            firstTime = false;
            super.onPause();
        }
    }

    public static boolean invalidateData(){
        Log.d(TAG, "resetData: invalidating all data");
        firstTime = true;
        if (colorPickerView!=null) {
            colorPickerView.selectCenter();
            manager.clearSavedAllData();
        }
        return true;
    }
}
