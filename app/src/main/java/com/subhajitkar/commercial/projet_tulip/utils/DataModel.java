package com.subhajitkar.commercial.projet_tulip.utils;

public class DataModel {
    private static final String TAG = "ListObject";

    private String text;
    private int icon, iconDark, textColor;

    public DataModel(String text, int icon, int iconDark){
        this.text = text;
        this.icon = icon;
        this.iconDark = iconDark;
    }

    public DataModel(String text, int icon, int iconDark, int textColor){
        this.text = text;
        this.icon = icon;
        this.iconDark = iconDark;
        this.textColor = textColor;
    }

    public String getText() {
        return text;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getIcon() {

        if (StaticFields.darkThemeSet){
            return iconDark;
        }else {
            return icon;
        }
    }
}
