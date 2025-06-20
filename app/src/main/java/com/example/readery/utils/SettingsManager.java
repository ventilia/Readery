package com.example.readery.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SettingsManager {
    private static SettingsManager instance;
    private SharedPreferences prefs;
    private static final String PREF_NAME = "ReaderyPrefs";
    private static final String KEY_LANGUAGE = "language";

    private SettingsManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SettingsManager getInstance(Context context) {
        if (instance == null) {
            instance = new SettingsManager(context.getApplicationContext());
        }
        return instance;
    }


    public void setLanguage(String lang) {
        prefs.edit().putString(KEY_LANGUAGE, lang).apply();
    }


    public String getLanguage() {
        return prefs.getString(KEY_LANGUAGE, "en");
    }
}