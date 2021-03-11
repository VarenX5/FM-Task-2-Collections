package com.foxminded.android.task2;

import android.app.Application;

public class BenchmarkApp extends Application {

    private static BenchmarkApp instance;

    public static BenchmarkApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
