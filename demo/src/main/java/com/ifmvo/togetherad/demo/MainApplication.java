package com.ifmvo.togetherad.demo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.ifmvo.togetherad.GroMoreManager;

public class MainApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    protected static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        GroMoreManager.getInstance(context);
    }
}
