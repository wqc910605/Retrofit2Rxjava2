package com.cypoem.retrofit;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.cypoem.retrofit.utils.Utils;

/**
 * Created by edianzu on 2017/4/18.
 */

public class App extends Application {
    private static App app;
    public static Handler sHandler = null;
    public static Context getAppContext() {
        return app;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        app=this;
        sHandler = new Handler(Looper.getMainLooper());
    }
}
