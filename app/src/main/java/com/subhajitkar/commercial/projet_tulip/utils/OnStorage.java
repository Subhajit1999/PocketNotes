package com.subhajitkar.commercial.projet_tulip.utils;

import android.content.Context;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.View;

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

    public static File createFile(String title, String body, View view, Context context){
        Log.d(TAG, "createFile: creating text file");
        //triming title to make it text file name
        String fileName;
        if (title.length()>10) {
            fileName = title.substring(0,10) + ".txt";
        }else{
            fileName = title+".txt";
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
                    Snackbar.make(view, Html.fromHtml("<font color=\""+context.getResources().getColor(R.color.colorAccent)
                            +"\">TextFile saved to device successfully.</font>"),Snackbar.LENGTH_SHORT).show();
                    Log.d(TAG, "createFile: file created: "+created+", Path: "+textFile.getAbsolutePath());
                }else{
                    Snackbar.make(view, Html.fromHtml("<font color=\""+context.getResources().getColor(R.color.colorAccent)
                            +"\">TextFile is already saved, overwriting content./font>"),Snackbar.LENGTH_SHORT).show();
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