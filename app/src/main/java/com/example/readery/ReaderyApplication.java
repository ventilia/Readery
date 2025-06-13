package com.example.readery;

import android.app.Application;

import com.example.readery.utils.DatabaseInitializer;

public class ReaderyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseInitializer.populateDatabase(this);
    }
}