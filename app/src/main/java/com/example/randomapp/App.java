package com.example.randomapp;

import android.content.pm.ApplicationInfo;

public class App {
    private final ApplicationInfo info;
    private boolean isChecked;

    public App(ApplicationInfo info, boolean isChecked) {
        this.info = info;
        this.isChecked = isChecked;
    }

    public ApplicationInfo getInfo() {
        return info;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
