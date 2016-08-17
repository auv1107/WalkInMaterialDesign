package com.example.android.materialdesigncodelab;

import android.app.Application;

/**
 * Created by lixindong on 8/8/16.
 */
public class MdApp extends Application {
    public static MdApp sInstance;
    public static MdApp getInstance() {
        return sInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
