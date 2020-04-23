package com.subhajitkar.commercial.projet_tulip.utils;

import android.text.Html;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class PortableContent {
    private static final String TAG = "PortableContent";

    public void showSnackBar(View root, String msg, int duration){
        Log.d(TAG, "showSnackBar: showing snackbar");
        Snackbar.make(root, Html.fromHtml(msg), duration).show();
    }
}
