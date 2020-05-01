package com.subhajitkar.commercial.projet_tulip.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.chootdev.csnackbar.Align;
import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Snackbar;
import com.chootdev.csnackbar.Type;
import com.subhajitkar.commercial.projet_tulip.R;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PortableContent {
    private static final String TAG = "PortableContent";
    private Context context;

    public PortableContent(Context context){
        this.context = context;
    }

    public void showSnackBar(Type type, String msg, Duration duration){
        Log.d(TAG, "showSnackBar: showing snackbar");
        Snackbar.with(context,null)
                .type(type)
                .message(msg)
                .duration(duration)
                .fillParent(true)
                .textAlign(Align.LEFT)
                .show();
    }

    public MDToast showToast(String msg, int duration, int type){
        Log.d(TAG, "showToast: Showing toast message.");
        return MDToast.makeText(context,msg,duration,type);
    }
}
