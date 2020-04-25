package com.subhajitkar.commercial.projet_tulip.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Type;
import com.google.android.material.snackbar.Snackbar;
import com.subhajitkar.commercial.projet_tulip.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class OnStorage {
    private static final String TAG = "OnStorage";

    public boolean isExternalStorageReadable(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
        || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())){
            Log.d(TAG, "isExternalStorageReadable: Media readable");
            return true;
        }else{
            return false;
        }
    }

    private boolean isExternalStorageWritable(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            Log.d(TAG, "isExternalStorageReadable: Media writable");
            return true;
        }else{
            return false;
        }
    }

    public static File createFile(String title, String body, View view, Context context, String fileType){
        Log.d(TAG, "createFile: creating text file");
        //triming title to make it text file name
        String fileName;
        if (title.length()>10) {
            fileName = title.substring(0,10) + fileType;
        }else{
            fileName = title+fileType;
        }
        File textFile = null;
        if (new OnStorage().isExternalStorageWritable()){  //checking if external storage writable
            Log.d(TAG, "createFile: storage writable");
            File root = Environment.getExternalStorageDirectory();  //root directory
            File dir = new File(root.getAbsolutePath()+"/PocketNotes");  //creating new dir
            if (!dir.exists()){
                boolean success = dir.mkdir();
                Log.d(TAG, "createFile: Success: "+success);
            }
            textFile = new File(dir,fileName);    //creating blank file
            try {
                if (!textFile.exists()){
                    boolean created = textFile.createNewFile();
                    new PortableContent().showSnackBar(context, Type.SUCCESS,
                            "File saved to device successfully.", Duration.SHORT);
                    Log.d(TAG, "createFile: file created: "+created+", Path: "+textFile.getAbsolutePath());
                }else{
                    new PortableContent().showSnackBar(context, Type.WARNING,
                            "File is already saved, overwriting content.", Duration.SHORT);
                }
                if (textFile.isFile()) {  //if that's a file then write into it
                    FileOutputStream fos = new FileOutputStream(textFile);
                    String content = title + "\n\n" + body;
                    fos.write(content.getBytes());
                    fos.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return textFile;
    }
}
