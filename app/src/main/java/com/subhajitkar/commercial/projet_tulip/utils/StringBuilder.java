package com.subhajitkar.commercial.projet_tulip.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.subhajitkar.commercial.projet_tulip.objects.DataModel;
import com.subhajitkar.commercial.projet_tulip.objects.ToDoObject;
import com.yalantis.beamazingtoday.interfaces.BatModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class StringBuilder {
    private static final String TAG = "StringBuilder";

    public String buildChecklistString(List<BatModel> checklist) {
        Log.d(TAG, "buildChecklistString: building special string from checklist");
        String resultString = "";
        for (BatModel item : checklist) {
            if (item.isChecked()) {
                resultString += "[x] ";
            } else {
                resultString += "[] ";
            }
            resultString += item.getText() + "\n";
        }
        return resultString;
    }

    public List<BatModel> getListFromChecklistString(String spclString){
        Log.d(TAG, "getListFromChecklistString: getting data back");
        List<BatModel> finalList = new ArrayList<>();
        //extracting data
        int i = 0;
        String[] splitItems = spclString.split("\n");
        for (String s: splitItems){
            String text;
            boolean checked = false;
            int index = 0;
            if (s.toCharArray()[1]=='x'){  //item checked
                checked = true;
                index++;
            }
            text = s.substring(3+index);
            finalList.add(new ToDoObject(text));
            finalList.get(i).setChecked(checked);
            i++;
        }
        return finalList;
    }

    public String bitmapToBase64(Bitmap bitmap){
        Log.d(TAG, "bitmapToBase64: converting bitmap to base64");
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap base64ToBitmap(String base64String){
        Log.d(TAG, "base64ToBitmap: converting base64 to bitmap");
        try {
            byte [] encodeByte=Base64.decode(base64String,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
