package com.example.randomapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SharePreference {
    private static final String MY_SHARE_PREFERENCE = "MY_SHARE_PREFERENCE";
    private static final String MY_DATA = "MY_DATA";
    private final Context context;

    public SharePreference(Context context) {
        this.context = context;
    }

    @SuppressLint("CommitPrefEdits")
    public void putBoonleanValue(String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARE_PREFERENCE, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARE_PREFERENCE, 0);
        return sharedPreferences.getBoolean(key, false);
    }

    @SuppressLint("CommitPrefEdits")
    public void putSwitchStatement(String key, List<String> value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_DATA, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.apply();
    }

    public List<String> getSwitchStatement(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_DATA, 0);
        String json = sharedPreferences.getString(key, null);
        Type type = new TypeToken<List<String>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }
}
