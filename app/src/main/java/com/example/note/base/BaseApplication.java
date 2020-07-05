package com.example.note.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class BaseApplication extends Application {


    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getBaseContext();

    }

    public static Context getContext() {
        return mContext;
    }
}
