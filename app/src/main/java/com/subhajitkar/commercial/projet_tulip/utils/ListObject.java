package com.subhajitkar.commercial.projet_tulip.utils;

public class ListObject {
    private static final String TAG = "ListObject";

    private String text;
    private int icon, iconDark;

    public ListObject(String text, int icon, int iconDark){
        this.text = text;
        this.icon = icon;
        this.iconDark = iconDark;
    }

    public String getText() {
        return text;
    }

    public int getIcon() {

        if (StaticFields.darkThemeSet){
            return iconDark;
        }else {
            return icon;
        }
    }
}
