package com.subhajitkar.commercial.projet_tulip.objects;

import com.yalantis.beamazingtoday.interfaces.BatModel;

public class ToDoObject implements BatModel {

    private String name;

    private boolean isChecked;

    public ToDoObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public String getText() {
        return getName();
    }

}
